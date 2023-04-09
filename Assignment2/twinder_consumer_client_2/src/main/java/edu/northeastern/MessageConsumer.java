package edu.northeastern;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import edu.northeastern.models.SwipeData;
import edu.northeastern.models.SwipeDetailsMessage;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Data
@Log4j2
public class MessageConsumer {

    private static final int NUM_OF_THREADS = 50;

    private final Connection connection;
    private final String queueName;

    private final Gson gson;
    private final Map<String, SwipeData> swipeDataMap;

    MessageConsumer(final Connection connection, final String queueName) {
        this.queueName = queueName;
        this.connection = connection;

        gson = new Gson();
        swipeDataMap = new ConcurrentHashMap<>();
    }

    @SneakyThrows
    public void handle() {
        log.info("Handling message...");
        final Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            executorService.submit(() -> {
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        String rawMessage = new String(body, StandardCharsets.UTF_8);
                        log.info("Received message: " + rawMessage);

                        // Parse swipe details message
                        final SwipeDetailsMessage message = gson.fromJson(rawMessage, SwipeDetailsMessage.class);
                        final String userId = message.getSwiper();

                        if (!swipeDataMap.containsKey(userId)) {
                            swipeDataMap.put(userId, new SwipeData(userId));
                        }

                        final SwipeData swipeData = swipeDataMap.get(userId);
                        swipeData.handleMessage(message);

                        printHelper(swipeData.getUserId());

                        latch.countDown();
                    }
                };

                try {
                    channel.basicConsume(queueName, true, consumer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void printHelper(final String swiperId) {
        SwipeData swipeData = swipeDataMap.getOrDefault(swiperId, new SwipeData(swiperId));
        System.out.println("Swiper : " + swiperId + "  potential matches (max 100): "
                + swipeData.getLikedSwipeeIds().stream().limit(100).collect(Collectors.toList()));
    }
}

package edu.northeastern;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import edu.northeastern.models.SwipeData;
import edu.northeastern.models.SwipeDetailsMessage;
import lombok.Data;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class MessageConsumerLikeAndDislike {

    private final Connection connection;
    private final String queueName;

    private final Gson gson;
    private final Map<String, SwipeData> swipeDataMap;
    private final Map<String, Integer> likeMap;
    private final Map<String, Integer> dislikeMap;

    MessageConsumerLikeAndDislike(final Connection connection, final String queueName) {
        this.queueName = queueName;
        this.connection = connection;

        gson = new Gson();
        swipeDataMap = new ConcurrentHashMap<>();
        likeMap = new ConcurrentHashMap<>();
        dislikeMap = new ConcurrentHashMap<>();
    }

    @SneakyThrows
    public void handle() {
        final Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String rawMessage = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message: " + rawMessage);

                // Parse swipe details message
                final SwipeDetailsMessage message = gson.fromJson(rawMessage, SwipeDetailsMessage.class);
                final String userId = message.getSwiper();
                final SwipeData swipeData = swipeDataMap.getOrDefault(userId, new SwipeData(userId));
                swipeData.handleMessage(message);
                swipeDataMap.put(userId, swipeData);
                likeMap.put(userId, swipeData.getNumLikes());
                dislikeMap.put(userId, swipeData.getNumDislikes());

                System.out.println(likeMap);
                System.out.println(dislikeMap);
                //countHelper();
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

//    private void countHelper() {
//        SwipeData data = swipeDataMap.values().stream().sorted((SwipeData a, SwipeData b) -> b.getNumLikes() - a.getNumLikes()).findFirst().get();
//
//        System.out.println(data);
//    }
}

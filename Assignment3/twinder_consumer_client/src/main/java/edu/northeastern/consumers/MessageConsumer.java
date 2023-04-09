package edu.northeastern.consumers;

import com.google.gson.Gson;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import edu.northeastern.models.SwipeDetailsMessage;
import edu.northeastern.connections.MongoDBConnection;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.northeastern.utils.Constants.MONGO_DB_COLLECTION;
import static edu.northeastern.utils.Constants.MONGO_DB_NAME;
import static edu.northeastern.utils.Constants.NUM_OF_THREADS;

@Data
@Slf4j
public class MessageConsumer {

    private final Connection connection;
    private final String queueName;

    private final Gson gson;

    public MessageConsumer(final Connection connection, final String queueName) {
        this.queueName = queueName;
        this.connection = connection;
        this.gson = new Gson();
    }

    @SneakyThrows
    public void handle() {
        log.info("Handling message...");

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            executorService.submit(() -> {
                try {
                    final Channel channel = connection.createChannel();
                    // prevent non-existing queue exception
                    channel.queueDeclare(queueName, false, false, false, null);

                    final MongoClient mongoClient = MongoDBConnection.getInstance();
                    final MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB_NAME);
                    final MongoCollection<SwipeDetailsMessage> mongoCollection = mongoDatabase.getCollection(MONGO_DB_COLLECTION, SwipeDetailsMessage.class);

                    final Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                            final String rawMessage = new String(body, StandardCharsets.UTF_8);
                            final SwipeDetailsMessage message = gson.fromJson(rawMessage, SwipeDetailsMessage.class);

                            log.info("Received and parsed message from RMQ: {}", message);

                            final ClientSession clientSession = mongoClient.startSession();
                            clientSession.withTransaction(() -> {
                                        InsertOneResult result = mongoCollection.insertOne(clientSession, message);
                                        log.info("Inserted message into database resultId: {}", result.getInsertedId());
                                        return message;
                                    },
                                    MongoDBConnection.getDefaultTransactionOptions());
                        }
                    };
                    channel.basicConsume(queueName, true, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}

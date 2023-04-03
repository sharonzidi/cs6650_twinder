package edu.northeastern;

import com.google.gson.Gson;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.*;
import edu.northeastern.models.SwipeData;
import edu.northeastern.models.SwipeDetailsMessage;
import lombok.Data;
import lombok.SneakyThrows;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class MessageConsumerLikedSwipee {

    private final Connection connection;
    private final String queueName;

    private final Gson gson;
    private final Map<String, List<String>> likedSwipeeDataMap;
    private final Map<String, SwipeData> swipeDataMap;

    MessageConsumerLikedSwipee(final Connection connection, final String queueName) {
        this.queueName = queueName;
        this.connection = connection;

        gson = new Gson();
        likedSwipeeDataMap = new ConcurrentHashMap<>();
        swipeDataMap = new ConcurrentHashMap<>();
    }

    @SneakyThrows
    public void handle() {
        String connectionString = "mongodb+srv://dbuser:EwuPYvnsU2mtLsbt@cluster0.lhvylio.mongodb.net/?retryWrites=true&w=majority";
        try (var mongoClient = MongoClients.create(connectionString)) {
            // Get a handle to the database
            MongoDatabase database = mongoClient.getDatabase("cs6650-distributed-sys");

            // Get a handle to the collection
            MongoCollection<Document> collection = database.getCollection("twinder-data");

            // Create a new document
            Document document = new Document("name", "John")
                    .append("age", 30)
                    .append("email", "john@example.com");

            // Insert the document into the collection
            collection.insertOne(document);

            // Perform a read operation to check if data was written successfully
            for (Document document2 : collection.find()) {
                System.out.println(document2.toJson());
            }

            System.out.println("Document inserted successfully.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }






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
                List<String> likedSwipeeList =  swipeData.getLikedSwipeeIds();

                List<String> list = new ArrayList<>();
                for(int i = 0; i < 100; i++) {
                    list.add(likedSwipeeList.get(i));
                }
                likedSwipeeDataMap.put(userId, list);
                System.out.println(likedSwipeeList);

                //countHelper();
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

//    private void countHelper() {
//        int count = 0;
//        for (Map.Entry<String, List<String>> entry : likedSwipeeDataMap.entrySet()) {
//            if (count == 100) {
//                break;
//            }
//            List<String> value = entry.getValue();
//            System.out.println(value);
//            count++;
//        }
//    }
}

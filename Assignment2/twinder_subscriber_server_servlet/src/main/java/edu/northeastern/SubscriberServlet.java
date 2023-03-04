package edu.northeastern;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;
import edu.northeastern.models.SwipeDetailsMessage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SubscriberServlet extends HttpServlet {

    private static final String QUEUE_NAME = "twinder_swipe_queue";
    private final Gson gson = new Gson();
    private final Map<String, SwipeDetailsMessage> swiperToMessageMap = new HashMap<>();

    private Connection connection;
    private Channel channel;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Handling SubscriberServlet request...");

        try {
            // Set up RabbitMQ connection and channel
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Replace with the hostname or IP address of your RabbitMQ server
            factory.setUsername("admin-user"); // Replace with your RabbitMQ username
            factory.setPassword("admin-password"); // Replace with your RabbitMQ password

            connection = factory.newConnection();
            channel = connection.createChannel();

            // Declare the queue if it doesn't already exist
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Create a consumer to consume messages from the queue
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String rawMessage = new String(body, StandardCharsets.UTF_8);
                    System.out.println("Received message: " + rawMessage);
                    messageHandler(gson.fromJson(rawMessage, SwipeDetailsMessage.class));
                }
            };

            // Start consuming messages from the queue
            while (true) {
                channel.basicConsume(QUEUE_NAME, true, consumer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Handling SubscriberServlet request...");
//
//        try {
//            // Set up RabbitMQ connection and channel
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost("localhost"); // Replace with the hostname or IP address of your RabbitMQ server
//            factory.setUsername("admin-user"); // Replace with your RabbitMQ username
//            factory.setPassword("admin-password"); // Replace with your RabbitMQ password
//
//            connection = factory.newConnection();
//            channel = connection.createChannel();
//
//            // Declare the queue if it doesn't already exist
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//                String rawMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
//                System.out.println("Received message: " + rawMessage);
//                messageHandler(gson.fromJson(rawMessage, SwipeDetailsMessage.class));
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//            };
//
//            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
//            });
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    public void init() {
//        System.out.println("Handling SubscriberServlet request...");
//
//        try {
//            // Set up RabbitMQ connection and channel
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost("localhost"); // Replace with the hostname or IP address of your RabbitMQ server
//            factory.setUsername("admin-user"); // Replace with your RabbitMQ username
//            factory.setPassword("admin-password"); // Replace with your RabbitMQ password
//
//            connection = factory.newConnection();
//            channel = connection.createChannel();
//
//            // Declare the queue if it doesn't already exist
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//            // Create a consumer to consume messages from the queue
//            Consumer consumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
//                    String rawMessage = new String(body, StandardCharsets.UTF_8);
//                    System.out.println("Received message: " + rawMessage);
//                    messageHandler(gson.fromJson(rawMessage, SwipeDetailsMessage.class));
//                }
//            };
//
//            // Start consuming messages from the queue
//            channel.basicConsume(QUEUE_NAME, true, consumer);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void destroy() {
        try {
            // close the channel and the connection
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void messageHandler(final SwipeDetailsMessage message) {
        System.out.println("Handling message...");
        System.out.println(message);
    }
}

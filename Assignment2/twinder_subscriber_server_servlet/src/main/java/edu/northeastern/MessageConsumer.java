//package edu.northeastern;
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.Consumer;
//import com.rabbitmq.client.DefaultConsumer;
//import com.rabbitmq.client.Envelope;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
//public class MessageConsumer {
//    public static final String QUEUE_NAME = "twinder_swipe_queue";
//
//    public static void main(String[] args) throws IOException, TimeoutException {
//        // Set up RabbitMQ connection and channel
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost"); // Replace with the hostname or IP address of your RabbitMQ server
//        factory.setUsername("admin-user"); // Replace with your RabbitMQ username
//        factory.setPassword("admin-password"); // Replace with your RabbitMQ password
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        // Declare the queue if it doesn't already exist
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//        // Create a consumer to consume messages from the queue
//        Consumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                String message = new String(body, "UTF-8");
//                System.out.println("Received message: " + message);
//            }
//        };
//
//        // Start consuming messages from the queue
//        channel.basicConsume(QUEUE_NAME, true, consumer);
//    }
//}

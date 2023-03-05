package edu.northeastern;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

    private static final String QUEUE_NAME = "twinder_swipe_queue";
    private static final String RMQ_HOST = "localhost";
    private static final String RMQ_USERNAME = "admin-user";
    private static final String RMQ_PASSWORD = "aKNlI4BwD#w74S#R9&KE";

    public static void main(String[] args) {

        // Set up connection factory
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RMQ_HOST);
        connectionFactory.setUsername(RMQ_USERNAME);
        connectionFactory.setPassword(RMQ_PASSWORD);
        connectionFactory.setPort(5672);

        // Create connection pool and acquire connection
        final RMQConnectionPool pool = new RMQConnectionPool(connectionFactory, 2);
        final Connection connection = pool.getConnection();
        final MessageConsumer consumer = new MessageConsumer(connection, QUEUE_NAME);

        try {
            // handling RMQ messages
            consumer.handle();
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
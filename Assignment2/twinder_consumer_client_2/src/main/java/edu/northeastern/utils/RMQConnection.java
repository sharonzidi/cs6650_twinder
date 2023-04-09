package edu.northeastern.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static edu.northeastern.utils.Constants.RMQ_HOST;
import static edu.northeastern.utils.Constants.RMQ_PASSWORD;
import static edu.northeastern.utils.Constants.RMQ_USERNAME;

@Log4j2
public class RMQConnection {

    private static Connection connection;

    @SneakyThrows
    public static Connection getConnection() {
        if (connection == null) {
            log.info("Creating new RMQ connection...");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RMQ_HOST);
            factory.setUsername(RMQ_USERNAME);
            factory.setPassword(RMQ_PASSWORD);
            factory.setPort(5672);
            connection = factory.newConnection();
        }
        return connection;
    }
}

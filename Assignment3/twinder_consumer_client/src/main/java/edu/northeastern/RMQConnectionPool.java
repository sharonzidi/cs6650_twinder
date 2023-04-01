package edu.northeastern;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RMQConnectionPool {

    private final ConcurrentLinkedQueue<Connection> pool;
    private final ConnectionFactory factory;
    private final int maxPoolSize;

    public RMQConnectionPool(ConnectionFactory factory, int maxPoolSize) {
        this.factory = factory;
        this.maxPoolSize = maxPoolSize;
        this.pool = new ConcurrentLinkedQueue<>();
    }

    public Connection getConnection() {
        Connection connection = pool.poll();
        if (connection == null) {
            connection = createConnection();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (pool.size() < maxPoolSize) {
            pool.offer(connection);
        } else {
            try {
                connection.close();
            } catch (Exception e) {
                // log error
            }
        }
    }

    private Connection createConnection() {
        try {
            return factory.newConnection();
        } catch (Exception e) {
            // log error
            throw new RuntimeException("Failed to create RMQ connection", e);
        }
    }
}

package edu.northeastern;

import edu.northeastern.consumers.MessageConsumer;
import edu.northeastern.connections.RMQConnection;

import static edu.northeastern.utils.Constants.RMQ_QUEUE_STATS;

public class Main {

    public static void main(String[] args) {
        final MessageConsumer consumer = new MessageConsumer(RMQConnection.getConnection(), RMQ_QUEUE_STATS);
        consumer.handle();
    }
}

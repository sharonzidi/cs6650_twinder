package edu.northeastern;

import edu.northeastern.utils.RMQConnection;

import static edu.northeastern.utils.Constants.RMQ_QUEUE_MATCHES;

public class Main {

    public static void main(String[] args) {
        final MessageConsumer consumer = new MessageConsumer(RMQConnection.getConnection(), RMQ_QUEUE_MATCHES);
        consumer.handle();
    }
}

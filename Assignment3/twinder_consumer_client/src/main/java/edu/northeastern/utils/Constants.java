package edu.northeastern.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final int NUM_OF_THREADS = 50;

    public static final String RMQ_HOST = "localhost";

    public static final String RMQ_USERNAME = "admin-user";

    public static final String RMQ_PASSWORD = "aKNlI4BwD#w74S#R9&KE";

    public static final String RMQ_QUEUE_STATS = "twinder_queue_stats";

    public static final String RMQ_QUEUE_MATCHES = "twinder_queue_matches";

    public static final String MONGO_DB_CONNECTION_STRING = "mongodb+srv://dbuser:EwuPYvnsU2mtLsbt@cluster0.lhvylio.mongodb.net/?retryWrites=true&w=majority";

    public static final String MONGO_DB_NAME = "cs6650-distributed-sys";

    public static final String MONGO_DB_COLLECTION = "twinder-data";
}

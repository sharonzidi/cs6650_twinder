package edu.northeastern.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String RMQ_HOST = "localhost";

    public static final String RMQ_USERNAME = "admin-user";

    public static final String RMQ_PASSWORD = "aKNlI4BwD#w74S#R9&KE";

    public static final String RMQ_QUEUE_STATS = "twinder_queue_stats";

    public static final String RMQ_QUEUE_MATCHES = "twinder_queue_matches";
}

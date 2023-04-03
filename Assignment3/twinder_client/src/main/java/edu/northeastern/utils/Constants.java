package edu.northeastern.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final String API_BASE_PATH = "http://localhost:9988/dev-server";

    public static final int NUM_OF_POST_SWIPE_TASKS = 20;

    public static final int NUM_OF_POST_SWIPE_CONSUMERS = 10;

    public static final int NUM_OF_GET_TASKS_PER_SECOND = 5;

    public static final int NUM_OF_GET_CONSUMERS = 1;
}

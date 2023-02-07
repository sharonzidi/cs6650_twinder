package edu.northeastern;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SwipeApi;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private static final String BASE_PATH = "http://localhost:9988";


    //    private final BlockingQueue<E> dataBuffer;
    public static void main(String[] args) {
        final ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(BASE_PATH);

        final BlockingQueue<Message> blockingQueue = new LinkedBlockingQueue<>();

        final Producer producer = new Producer(apiClient, blockingQueue);
        final Consumer consumer1 = new Consumer("consumer #1", apiClient, blockingQueue);
        final Consumer consumer2 = new Consumer("consumer #2", apiClient, blockingQueue);

        final Thread producerThread = new Thread(producer);
        final Thread consumerThread1 = new Thread(consumer1);
        final Thread consumerThread2 = new Thread(consumer2);

        // put API request in queue

        final long startTime = System.currentTimeMillis();
        System.out.println("startTime: " + startTime);

        producerThread.start();

        consumerThread1.start();
        consumerThread2.start();

        System.out.println("queue size: " + blockingQueue.size());
    }

    private static SwipeApi generateSwipeApi(ApiClient apiClient) {
        final SwipeApi swipeApi = new SwipeApi(apiClient);
        return swipeApi;
    }
}
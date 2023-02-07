package edu.northeastern;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final String identifier;
    private final ApiClient apiClient;
    private final BlockingQueue<Message> blockingQueue;
    private boolean isRunning;

    public Consumer(final String identifier, final ApiClient apiClient,
                    final BlockingQueue<Message> blockingQueue) {
        this.identifier = identifier;
        this.apiClient = apiClient;
        this.blockingQueue = blockingQueue;
        isRunning = true;
    }

    @Override
    public void run() {
        System.out.println("starting consumer");

        while (isRunning) {
            consume();
        }
    }

    public void consume() {
        if (!blockingQueue.isEmpty()) {
            System.out.println("handling message and queue size: " + blockingQueue.size());
            System.out.println("handling message with identifier: " + identifier);
            final Message message = blockingQueue.poll();

            if (message.isPill) {
                isRunning = false;
                return;
            }

            final SwipeApi swipeApi = message.swipeApi;
            try {

                final SwipeDetails swipeDetails = new SwipeDetails();
                swipeDetails.setSwiper("xiaoming");
                swipeDetails.setSwipee("xiaohong");
                swipeDetails.setComment("hello world!");

                swipeApi.swipe(swipeDetails, "left");

                final long endTime = System.currentTimeMillis();

                System.out.println("endTime  : " + endTime);

            } catch (ApiException exception) {
                exception.printStackTrace();
            }
        }
    }
}

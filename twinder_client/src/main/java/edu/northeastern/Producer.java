package edu.northeastern;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SwipeApi;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final ApiClient apiClient;
    private final BlockingQueue<Message> blockingQueue;

    public Producer(final ApiClient apiClient,
                    final BlockingQueue<Message> blockingQueue) {
        this.apiClient = apiClient;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        System.out.println("executing producer");
        produce(2000);
    }

    public void produce(final int size) {
        // put API request in queue
        for (int i = 0; i < size; i++) {
            final SwipeApi swipeApi = new SwipeApi(apiClient);
            final Message message = new Message(swipeApi);
            blockingQueue.add(message);
        }

        blockingQueue.add( new Message(true));
        blockingQueue.add( new Message(true));
    }
}

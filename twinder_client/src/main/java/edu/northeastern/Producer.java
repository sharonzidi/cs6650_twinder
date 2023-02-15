package edu.northeastern;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SwipeApi;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final ApiClient apiClient;
    private final BlockingQueue<Message> blockingQueue;
    private final int numOfTasks;
    private final int numOfPills;


    public Producer(final ApiClient apiClient,
                    final BlockingQueue<Message> blockingQueue,
                    final int numOfTasks,
                    final int numOfPills) {
        this.apiClient = apiClient;
        this.blockingQueue = blockingQueue;
        this.numOfTasks = numOfTasks;
        this.numOfPills = numOfPills;
    }

    @Override
    public void run() {
        System.out.println("executing producer");
        produce(numOfTasks);
    }

    public void produce(final int size) {
        // put API request in queue
        for (int i = 0; i < size; i++) {
            final SwipeApi swipeApi = new SwipeApi(apiClient);
            final Message message = new Message(swipeApi);
            blockingQueue.add(message);
        }


        for (int i = 0; i < numOfPills; i++) {
            blockingQueue.add(new Message(true));
        }
    }
}

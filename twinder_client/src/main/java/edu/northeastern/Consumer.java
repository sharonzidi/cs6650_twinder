package edu.northeastern;

import edu.northeastern.models.TimeEntry;
import edu.northeastern.utils.Utils;
import io.swagger.client.ApiException;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Consumer implements Runnable {

    private final String identifier;
    private final BlockingQueue<Message> blockingQueue;
    private final ConcurrentLinkedQueue<TimeEntry> resultQueue;
    private boolean isRunning;

    public Consumer(final String identifier,
                    final BlockingQueue<Message> blockingQueue,
                    final ConcurrentLinkedQueue<TimeEntry> resultQueue) {
        this.identifier = identifier;
        this.blockingQueue = blockingQueue;
        this.resultQueue = resultQueue;
        isRunning = true;
    }

    @Override
    public void run() {
        System.out.println("starting consumer #" + identifier);

        while (isRunning) {
            consume();
        }
    }

    public void consume() {
        if (!blockingQueue.isEmpty()) {
//            System.out.println("handling message and queue size: " + blockingQueue.size());
//            System.out.println("handling message with identifier: " + identifier);
            final Message message = blockingQueue.poll();

            if (message.isPill()) {
                isRunning = false;
                return;
            }

            final SwipeApi swipeApi = message.getSwipeApi();
            try {
                final long startTime = System.currentTimeMillis();

                final SwipeDetails swipeDetails = Utils.generateSwipeDetails();
                final String leftOrRight = Utils.getLeftOrRight();
                swipeApi.swipe(swipeDetails, leftOrRight);

                final long endTime = System.currentTimeMillis();
                resultQueue.add(new TimeEntry(startTime, endTime));

            } catch (ApiException exception) {
                exception.printStackTrace();
            }
        }
    }
}

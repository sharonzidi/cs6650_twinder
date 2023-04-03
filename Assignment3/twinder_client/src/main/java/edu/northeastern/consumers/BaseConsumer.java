package edu.northeastern.consumers;

import edu.northeastern.models.Message;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.models.TimeEntry;
import lombok.NonNull;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Setter
abstract public class BaseConsumer implements Runnable {

    @NonNull
    protected String identifier;
    @NonNull
    protected BlockingQueue<Message> blockingQueue;
    @NonNull
    protected ConcurrentLinkedQueue<TimeEntry> resultQueue;
    @NonNull
    protected SharedStatus sharedStatus;

    protected boolean isRunning;

    public BaseConsumer(@NonNull String identifier,
                        @NonNull BlockingQueue<Message> blockingQueue,
                        @NonNull ConcurrentLinkedQueue<TimeEntry> resultQueue,
                        @NonNull SharedStatus sharedStatus) {
        this.identifier = identifier;
        this.blockingQueue = blockingQueue;
        this.resultQueue = resultQueue;
        this.sharedStatus = sharedStatus;
        this.isRunning = true;
    }

    @Override
    public void run() {
        System.out.println("starting consumer #" + identifier);

        while (isRunning) {
            consume();
        }
    }

    public void consume() {
        isRunning = false;
    }
}

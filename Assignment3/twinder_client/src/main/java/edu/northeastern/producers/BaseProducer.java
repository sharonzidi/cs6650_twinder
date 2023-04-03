package edu.northeastern.producers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;
import lombok.NonNull;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;

@Setter
public abstract class BaseProducer implements Runnable {

    protected int numOfTasks;
    protected int numOfPills;
    @NonNull
    protected BlockingQueue<Message> blockingQueue;
    @NonNull
    protected MessageType messageType;
    @NonNull
    protected SharedStatus sharedStatus;

    protected BaseProducer(int numOfTasks,
                           int numOfPills,
                           @NonNull BlockingQueue<Message> blockingQueue,
                           @NonNull MessageType messageType,
                           @NonNull SharedStatus sharedStatus) {
        this.numOfTasks = numOfTasks;
        this.numOfPills = numOfPills;
        this.blockingQueue = blockingQueue;
        this.messageType = messageType;
        this.sharedStatus = sharedStatus;
    }

    @Override
    public void run() {
        produceTasks();
        producePills();
    }

    public void produceTasks() {
        for (int i = 0; i < numOfTasks; i++) {
            final Message message = Message.builder()
                    .messageType(messageType)
                    .build();
            blockingQueue.add(message);
        }
    }

    private void producePills() {
        for (int i = 0; i < numOfPills; i++) {
            final Message message = Message.builder()
                    .messageType(MessageType.PILL)
                    .build();
            blockingQueue.add(message);
        }
    }
}

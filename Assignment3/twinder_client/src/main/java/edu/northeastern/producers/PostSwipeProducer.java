package edu.northeastern.producers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;

import java.util.concurrent.BlockingQueue;

public class PostSwipeProducer extends BaseProducer {

    public PostSwipeProducer(int numOfTasks, int numOfPills, BlockingQueue<Message> blockingQueue, SharedStatus sharedStatus) {
        super(numOfTasks, numOfPills, blockingQueue, MessageType.SWIPE, sharedStatus);
    }

    @Override
    public void produceTasks() {
        for (int i = 0; i < numOfTasks; i++) {
            final Message message = Message.builder()
                    .messageType(MessageType.SWIPE)
                    .build();
            blockingQueue.add(message);
        }
    }
}

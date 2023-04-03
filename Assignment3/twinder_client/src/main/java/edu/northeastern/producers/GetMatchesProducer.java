package edu.northeastern.producers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;

import java.util.concurrent.BlockingQueue;

public class GetMatchesProducer extends BaseProducer {

    public GetMatchesProducer(int numOfTasks, int numOfPills, BlockingQueue<Message> blockingQueue, SharedStatus sharedStatus) {
        super(numOfTasks, numOfPills, blockingQueue, MessageType.MATCHES, sharedStatus);
    }

    @Override
    public void produceTasks() {
        for (int i = 0; i < numOfTasks; i++) {
            final Message message = Message.builder()
                    .messageType(MessageType.MATCHES)
                    .build();
            blockingQueue.add(message);
        }
    }
}

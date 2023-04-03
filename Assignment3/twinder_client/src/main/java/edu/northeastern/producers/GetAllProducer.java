package edu.northeastern.producers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import static edu.northeastern.utils.Constants.NUM_OF_POST_SWIPE_CONSUMERS;

public class GetAllProducer extends BaseProducer {

    public GetAllProducer(int numOfTasks, int numOfPills, BlockingQueue<Message> blockingQueue, SharedStatus sharedStatus) {
        super(numOfTasks, numOfPills, blockingQueue, MessageType.STATS, sharedStatus);
    }

    @Override
    public void produceTasks() {
        System.out.println("starting GetStatsProducer");

        while (sharedStatus.isRunning()) {
            final int count = sharedStatus.getCountMap().getOrDefault("PostSwipeConsumer", 0);
            if (count == NUM_OF_POST_SWIPE_CONSUMERS) {
                sharedStatus.setRunning(false);
            }
            System.out.println("GetStatsProducer -> SharedStatus: " + sharedStatus);

            for (int i = 0; i < numOfTasks; i++) {
                final Message message = Message.builder()
                        .messageType(randomSelect(MessageType.STATS, MessageType.MATCHES))
                        .build();
                blockingQueue.add(message);
            }
            try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static MessageType randomSelect(MessageType value1, MessageType value2) {
        Random random = new Random();
        final int randomIndex = random.nextInt(2);
        return randomIndex == 0 ? value1 : value2;
    }
}

package edu.northeastern.consumers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.models.TimeEntry;
import edu.northeastern.utils.Utils;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;
import lombok.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static edu.northeastern.utils.Constants.API_BASE_PATH;

public class PostSwipeConsumer extends BaseConsumer {

    @NonNull
    private final ApiClient apiClient;

    public PostSwipeConsumer(@NonNull String identifier,
                             @NonNull BlockingQueue<Message> blockingQueue,
                             @NonNull ConcurrentLinkedQueue<TimeEntry> resultQueue,
                             @NonNull SharedStatus sharedStatus) {
        super(identifier, blockingQueue, resultQueue, sharedStatus);
        apiClient = new ApiClient().setBasePath(API_BASE_PATH);
    }

    @Override
    public void consume() {
        if (!blockingQueue.isEmpty()) {
            System.out.println(identifier + " is handling message and queue size: " + blockingQueue.size());
            final Message message = blockingQueue.poll();

            if (MessageType.PILL.equals(message.getMessageType())) {
                isRunning = false;
                final String key = this.getClass().getSimpleName();
                sharedStatus.getCountMap().put(key, sharedStatus.getCountMap().getOrDefault(key, 0) + 1);
                return;
            }

            try {
                final long startTime = System.currentTimeMillis();

                // prepare data
                final SwipeApi swipeApi = new SwipeApi(apiClient);
                final SwipeDetails swipeDetails = Utils.generateSwipeDetails();
                final String leftOrRight = Utils.getLeftOrRight();

                // send request
                swipeApi.swipe(swipeDetails, leftOrRight);

                final long endTime = System.currentTimeMillis();

                // add timestamp into result
                resultQueue.add(new TimeEntry(startTime, endTime));

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

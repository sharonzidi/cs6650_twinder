package edu.northeastern.consumers;

import edu.northeastern.models.Message;
import edu.northeastern.models.MessageType;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.models.TimeEntry;
import edu.northeastern.utils.Utils;
import io.swagger.client.ApiClient;
import io.swagger.client.api.MatchesApi;
import lombok.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static edu.northeastern.utils.Constants.API_BASE_PATH;

public class GetMatchesConsumer extends BaseConsumer {

    @NonNull
    private final ApiClient apiClient;

    public GetMatchesConsumer(@NonNull String identifier,
                              @NonNull BlockingQueue<Message> blockingQueue,
                              @NonNull ConcurrentLinkedQueue<TimeEntry> resultQueue,
                              @NonNull SharedStatus sharedStatus) {
        super(identifier, blockingQueue, resultQueue, sharedStatus);
        apiClient = new ApiClient().setBasePath(API_BASE_PATH);
    }

    @Override
    public void consume() {
        if (!blockingQueue.isEmpty()) {
            System.out.println("handling message and queue size: " + blockingQueue.size());
            System.out.println("handling message with identifier: " + identifier);
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
                final MatchesApi matchesApi = new MatchesApi(apiClient);
                final String swiperUserId = Utils.generateRandomSwiperUserId();

                // send request
                matchesApi.matches(swiperUserId);

                final long endTime = System.currentTimeMillis();

                // add timestamp into result
                resultQueue.add(new TimeEntry(startTime, endTime));

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}

package edu.northeastern;

import edu.northeastern.consumers.GetAllConsumer;
import edu.northeastern.consumers.PostSwipeConsumer;
import edu.northeastern.helpers.Analytic;
import edu.northeastern.models.Message;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.models.TimeEntry;
import edu.northeastern.producers.GetAllProducer;
import edu.northeastern.producers.PostSwipeProducer;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static edu.northeastern.utils.Constants.NUM_OF_GET_CONSUMERS;
import static edu.northeastern.utils.Constants.NUM_OF_GET_TASKS_PER_SECOND;
import static edu.northeastern.utils.Constants.NUM_OF_POST_SWIPE_CONSUMERS;
import static edu.northeastern.utils.Constants.NUM_OF_POST_SWIPE_TASKS;

public class Main {

    public static void main(String[] args) {
        final SharedStatus sharedStatus = new SharedStatus();

        final int totalNumOfThreads = NUM_OF_POST_SWIPE_CONSUMERS + 1 + NUM_OF_GET_CONSUMERS + 1;
        final ExecutorService executorService = Executors.newFixedThreadPool(totalNumOfThreads);
        final List<Future<?>> futures = new LinkedList<>();

        // post swipe
        final BlockingQueue<Message> postSwipeBlockingQueue = new LinkedBlockingQueue<>();
        final ConcurrentLinkedQueue<TimeEntry> postSwipeResultQueue = new ConcurrentLinkedQueue<>();
        // add post swipe producers
        final PostSwipeProducer postSwipeProducer = new PostSwipeProducer(NUM_OF_POST_SWIPE_TASKS, NUM_OF_POST_SWIPE_CONSUMERS, postSwipeBlockingQueue, sharedStatus);
        futures.add(executorService.submit(postSwipeProducer));
        // add post swipe consumers
        for (int i = 0; i < NUM_OF_POST_SWIPE_CONSUMERS; i++) {
            final PostSwipeConsumer postSwipeConsumer = new PostSwipeConsumer("PostSwipeConsumer #" + i, postSwipeBlockingQueue, postSwipeResultQueue, sharedStatus);
            futures.add(executorService.submit(postSwipeConsumer));
        }

        // get stats
        final BlockingQueue<Message> getBlockingQueue = new LinkedBlockingQueue<>();
        final ConcurrentLinkedQueue<TimeEntry> getResultQueue = new ConcurrentLinkedQueue<>();
        // add get stats producer
        final GetAllProducer getAllProducer = new GetAllProducer(NUM_OF_GET_TASKS_PER_SECOND, NUM_OF_GET_CONSUMERS, getBlockingQueue, sharedStatus);
        futures.add(executorService.submit(getAllProducer));
        // add get stats consumer
        final GetAllConsumer getAllConsumer = new GetAllConsumer("GetAllConsumer #0", getBlockingQueue, getResultQueue, sharedStatus);
        futures.add(executorService.submit(getAllConsumer));

        // check when all threads are done processing in executor service
        while (true) {
            final int count = (int) futures.stream().filter(Future::isDone).count();
            if (count == totalNumOfThreads) {
                System.out.println("Share status:" + sharedStatus);
                executorService.shutdown();
                break;
            }
        }

        processAnalytics(new ArrayList<>(postSwipeResultQueue), NUM_OF_POST_SWIPE_CONSUMERS, "PostSwipe");
        processAnalytics(new ArrayList<>(getResultQueue), NUM_OF_GET_CONSUMERS, "Get");
    }

    @SneakyThrows
    public static void processAnalytics(List<TimeEntry> timeEntries, int numOfConsumers, String prefix) {
        // calculation
        final Analytic analytic = new Analytic(numOfConsumers, timeEntries.size(), timeEntries.size(), 0, timeEntries);
        analytic.print();

        // write to csv file
        String folderPath = "analytics/";
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = folderPath + prefix + "_" + numOfConsumers + "_threads_" + "results.csv";
        System.out.println("Generating csv file for " + fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("start_time,end_time,latency,request_type,response_code\n");

        for (final TimeEntry result : timeEntries) {
            final String row = String.join(",", String.valueOf(result.getStartTime()),
                    String.valueOf(result.getEndTime()),
                    String.valueOf(result.getEndTime() - result.getStartTime()),
                    "POST",
                    "201");
            writer.write(row + "\n");
        }
        writer.close();
    }

}

package edu.northeastern;

import edu.northeastern.models.TimeEntry;
import io.swagger.client.ApiClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private static final String BASE_PATH = "http://localhost:9988/cs6650_server_war";

    private static final int NUM_OF_TASKS = 100;

    private static final int NUM_OF_CONSUMER_THREADS = 10;

    public static void main(String[] args) throws IOException {
        final ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(BASE_PATH);

        final BlockingQueue<Message> blockingQueue = new LinkedBlockingQueue<>();
        final ConcurrentLinkedQueue<TimeEntry> resultQueue = new ConcurrentLinkedQueue<>();

        final Producer producer = new Producer(apiClient, blockingQueue, NUM_OF_TASKS, NUM_OF_CONSUMER_THREADS);
        final Thread producerThread = new Thread(producer);

        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_CONSUMER_THREADS);
        final List<Future<?>> futures = new ArrayList<>();

        // start threads
        producerThread.start();
        for (int i = 0; i < NUM_OF_CONSUMER_THREADS; i++) {
            futures.add(executorService.submit(new Consumer("consumer #" + (i + 1), blockingQueue, resultQueue)));
        }

        while (true) {
            final int count = (int) futures.stream().filter(Future::isDone).count();
            if (count == NUM_OF_CONSUMER_THREADS) {
                executorService.shutdown();
                break;
            }
        }

        // calculation
        final List<TimeEntry> timeEntries = new ArrayList<>(resultQueue);
        final Analytic analytic = new Analytic(NUM_OF_CONSUMER_THREADS, NUM_OF_TASKS, NUM_OF_TASKS, 0, timeEntries);
        analytic.print();

        // write to csv file
        String fileName = NUM_OF_CONSUMER_THREADS + "_threads_" + "results.csv";
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

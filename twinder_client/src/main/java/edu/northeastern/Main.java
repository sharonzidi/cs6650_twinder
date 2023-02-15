package edu.northeastern;

import edu.northeastern.models.TimeEntry;
import io.swagger.client.ApiClient;

import javax.sound.midi.Soundbank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String BASE_PATH = "http://localhost:9988";

    private static final int NUM_OF_TASKS = 10000;

    private static final int NUM_OF_CONSUMER_THREADS = 100;

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

//        System.out.println("the total run time (wall time) for all threads to complete : " + diff);

//        Thread.sleep(3000);
        while (true) {
            final int count = (int) futures.stream().filter(Future::isDone).count();

            if (count == NUM_OF_CONSUMER_THREADS) {
//                for (Future<?> future : futures) {
//                    System.out.println(future.isDone());
//                }
                System.out.println("Shutdown");
                executorService.shutdown();
                break;
            }
        }

        // calculation
        final List<TimeEntry> results = new ArrayList<>(resultQueue);

        // get initial time
        final long initialTime = results.stream()
                .min((a, b) -> Math.toIntExact(a.getStartTime() - b.getStartTime()))
                .map(TimeEntry::getStartTime).orElse(0L);

        final long lastTime = results.stream()
                .max((a, b) -> Math.toIntExact(a.getEndTime() - b.getEndTime()))
                .map(TimeEntry::getStartTime).orElse(0L);

        System.out.println("total number of threads: " + NUM_OF_CONSUMER_THREADS + ", total number of tasks: " + NUM_OF_TASKS);
        System.out.println("total wall time: " + (lastTime - initialTime));

        // total request # / diff -> (res/ms)*1000 -> throughput / s
        // little's law
        // print
        // csv

        String fileName = NUM_OF_CONSUMER_THREADS + "_threads_" + "results.csv";
        System.out.println("Generating csv file for " + fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("start_time,end_time,latency,request_type,response_code\n");

        for (final TimeEntry result : results) {
            final String row = String.join(",", String.valueOf(result.getStartTime()),
                    String.valueOf(result.getEndTime()),
                    String.valueOf(result.getEndTime() - result.getStartTime()),
                    "POST",
                    "201");
            System.out.println(row);
            writer.write(row + "\n");
        }
        writer.close();
    }
}

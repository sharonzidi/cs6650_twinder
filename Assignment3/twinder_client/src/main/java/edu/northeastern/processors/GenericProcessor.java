package edu.northeastern.processors;

import edu.northeastern.consumers.BaseConsumer;
import edu.northeastern.helpers.Analytic;
import edu.northeastern.models.Message;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.models.TimeEntry;
import edu.northeastern.producers.BaseProducer;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class GenericProcessor<T extends BaseProducer, U extends BaseConsumer> {

    private final int numOfTasks;

    private final int numOfConsumers;

    private final Function<String, T> producerFactory;

    private final Function<String, U> consumerFactory;

    private final SharedStatus sharedStatus;

    public GenericProcessor(int numOfTasks, int numOfConsumers, Function<String, T> producerFactory, Function<String, U> consumerFactory, SharedStatus sharedStatus) {
        this.numOfTasks = numOfTasks;
        this.numOfConsumers = numOfConsumers;
        this.producerFactory = producerFactory;
        this.consumerFactory = consumerFactory;
        this.sharedStatus = sharedStatus;

        processAll();
    }

    public void processAll() {
        final List<TimeEntry> timeEntries = processRequests();
        processAnalytics(timeEntries);
    }

    public List<TimeEntry> processRequests() {
        final BlockingQueue<Message> blockingQueue = new LinkedBlockingQueue<>();
        final ConcurrentLinkedQueue<TimeEntry> resultQueue = new ConcurrentLinkedQueue<>();

        T producer = producerFactory.apply("main producer");
        producer.setBlockingQueue(blockingQueue);
        producer.setNumOfTasks(numOfTasks);
        producer.setSharedStatus(sharedStatus);

        // producer threads
        final Thread producerThread = new Thread(producer);

        // consumer threads
        final ExecutorService executorService = Executors.newFixedThreadPool(numOfConsumers);
        final List<Future<?>> futures = new ArrayList<>();

        // start threads
        producerThread.start();
        for (int i = 0; i < numOfConsumers; i++) {
            U consumer = consumerFactory.apply("consumer #" + (i + 1));
            consumer.setIdentifier("consumer #" + (i + 1));
            consumer.setBlockingQueue(blockingQueue);
            consumer.setResultQueue(resultQueue);

            futures.add(executorService.submit(consumer));
        }

        // check when all threads are done processing in executor service
        while (true) {
            final int count = (int) futures.stream().filter(Future::isDone).count();
            if (count == numOfConsumers) {
                executorService.shutdown();
                break;
            }
        }

        return new ArrayList<>(resultQueue);
    }

    @SneakyThrows
    public void processAnalytics(List<TimeEntry> timeEntries) {
        // calculation
        final Analytic analytic = new Analytic(numOfConsumers, numOfTasks, numOfTasks, 0, timeEntries);
        analytic.print();

        // write to csv file
        String folderPath = "analytics/";
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        U tempInstance = consumerFactory.apply("");
        String fileName = folderPath + tempInstance.getClass().getSimpleName() + "_" + numOfConsumers + "_threads_" + "results.csv";
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

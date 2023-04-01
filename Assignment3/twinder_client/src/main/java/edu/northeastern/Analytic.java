package edu.northeastern;

import edu.northeastern.models.TimeEntry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Analytic {
    private int numberOfThreads;
    private int numberOfRequests;
    private int numberOfSuccessfulRequests;
    private int numberOfUnsuccessfulRequests;
    private List<TimeEntry> timeEntries;

    private double wallTime;
    private List<Long> latencies;
    private double meanLatency;
    private double medianLatency;
    private double throughput; //in requests per second (total number of requests/wall time)
    private long p99Latency;
    private long minLatency;
    private long maxLatency;

    public Analytic(int numberOfThreads,
                    int numberOfRequests,
                    int numberOfSuccessfulRequests,
                    int numberOfUnsuccessfulRequests,
                    List<TimeEntry> timeEntries) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfRequests = numberOfRequests;
        this.numberOfSuccessfulRequests = numberOfSuccessfulRequests;
        this.numberOfUnsuccessfulRequests = numberOfUnsuccessfulRequests;
        this.timeEntries = timeEntries;

        helper();
    }

    private void helper() {
        final long initialTime = timeEntries.stream()
                .min((a, b) -> Math.toIntExact(a.getStartTime() - b.getStartTime()))
                .map(TimeEntry::getStartTime).orElse(0L);

        final long lastTime = timeEntries.stream()
                .max((a, b) -> Math.toIntExact(a.getEndTime() - b.getEndTime()))
                .map(TimeEntry::getStartTime).orElse(0L);

        wallTime = lastTime - initialTime;
        latencies = timeEntries.stream()
                .map(e -> e.getEndTime() - e.getStartTime())
                .sorted()
                .collect(Collectors.toList());

        // calculate mean
        meanLatency = timeEntries.stream()
                .map(entry -> entry.getEndTime() - entry.getStartTime())
                .mapToLong(e -> e)
                .average()
                .orElse(-1);

        // calculate median
        final DoubleStream diffStream = timeEntries.stream()
                .map(entry -> entry.getEndTime() - entry.getStartTime())
                .mapToDouble(e -> e)
                .sorted();
        medianLatency = timeEntries.size() % 2 == 0 ?
                diffStream.skip(timeEntries.size() / 2 - 1).limit(2).average().orElse(-1) :
                diffStream.skip(timeEntries.size() / 2).findFirst().orElse(-1);

        // calculate throughput
        throughput = (double) numberOfSuccessfulRequests / ((double) wallTime / 1000);

        // calculate p99
        p99Latency = latencies.get((int) Math.ceil(99.0 / 100.0 * numberOfSuccessfulRequests) - 1);

        // calculate min and max
        minLatency = latencies.get(0);
        maxLatency = latencies.get(latencies.size() - 1);

    }

    public void print() {
        System.out.println("total number of threads: " + numberOfThreads + ", total number of tasks: " + numberOfRequests);
        System.out.println();
        System.out.println("number of successful requests sent: " + numberOfSuccessfulRequests);
        System.out.println("number of unsuccessful requests: " + numberOfUnsuccessfulRequests);
        System.out.println("the total run time (wall time) for all threads to complete: " + wallTime);
        System.out.println("total throughput in requests per second: " + throughput);

        System.out.println("mean: " + meanLatency);
        System.out.println("median: " + medianLatency);
        System.out.println("throughput: " + throughput + " (requests/second)");
        System.out.println("p99: " + p99Latency);
        System.out.println("min: " + minLatency);
        System.out.println("max: " + maxLatency);
        System.out.println();
    }

    // total request # / diff -> (res/ms)*1000 -> throughput / s
    // little's law
}

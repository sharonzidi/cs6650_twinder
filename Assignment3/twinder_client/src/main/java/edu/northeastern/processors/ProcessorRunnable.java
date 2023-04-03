package edu.northeastern.processors;

import edu.northeastern.consumers.BaseConsumer;
import edu.northeastern.models.SharedStatus;
import edu.northeastern.producers.BaseProducer;

import java.util.function.Function;

public class ProcessorRunnable<T extends BaseProducer, U extends BaseConsumer> implements Runnable {

    private final GenericProcessor<T, U> processor;

    public ProcessorRunnable(int numOfTasks, int numOfConsumers, Function<String, T> producerFactory, Function<String, U> consumerFactory, SharedStatus sharedStatus) {
        this.processor = new GenericProcessor<>(numOfTasks, numOfConsumers, producerFactory, consumerFactory, sharedStatus);
    }

    @Override
    public void run() {
        processor.processAll();
    }
}

package org.lqk.kafka.consumer;


import java.util.concurrent.ExecutorService;


public interface KafkaConsumerListener {

    void onMessages(String key, String value);

    void onMessages(String value);

    int getProcessThreads();

    ExecutorService getExecutor();

    String getGroupId();

    String getTopic();
}

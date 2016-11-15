package org.lqk.kafka.producer;

import kafka.javaapi.producer.Producer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class KafkaProducerBaseTemplate implements InitializingBean, DisposableBean {

    /*
        key通常用作分区规则
        value为要发送的消息
     */
    protected Producer<String, String> producer = null;

    private int processThreads = -1;

    protected ExecutorService executor;

    private KafkaProducerConfig kafkaProducerConfig;


    /**
     * �첽����
     *  @param key
     * @param value
     */
    public abstract void sendAsync(String key, String value);

    /**
     * �첽����
     *
     * @param value
     */
    public abstract void sendAsync(String value);

    /**
     * ͬ������
     *  @param key
     * @param value
     */
    public abstract void sendSync(String key, String value);

    /**
     * ͬ������
     *
     * @param value
     */
    public abstract void sendSync(String value);

    public int getProcessThreads() {
        return processThreads;
    }


    public ExecutorService getExecutor() {
        return executor;
    }


    public void setProcessThreads(int processThreads) {
        if (processThreads <= 0) {
            throw new IllegalArgumentException("Invalid processThreads value:" + processThreads);
        }
        this.processThreads = processThreads;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.processThreads > 0) {
            this.executor = Executors.newFixedThreadPool(this.processThreads);
        }
        if (producer == null) {
            producer = new Producer<String, String>(kafkaProducerConfig.getProducerConfig());
        }

    }

    @Override
    public void destroy() throws Exception {
        if (producer != null) {
            producer.close();
        }
        if (this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
    }

    public KafkaProducerConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    public void setKafkaProducerConfig(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }
}

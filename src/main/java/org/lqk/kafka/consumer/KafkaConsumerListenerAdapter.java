package org.lqk.kafka.consumer;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsumerListenerAdapter implements KafkaConsumerListener, InitializingBean, DisposableBean {


	private int processThreads = -1;

	protected ExecutorService executor;

	private String groupId;

    private String topic;

	private ConsumerListener consumerListener;

	public int getProcessThreads() {
		return processThreads;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setProcessThreads(int processThreads) {
		if (processThreads < 0) {
			throw new IllegalArgumentException("Invalid processThreads value:" + processThreads);
		}
		this.processThreads = processThreads;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
        if (null == groupId || "".equals(groupId)) {
            throw new IllegalArgumentException("Invalid groupId value:" + groupId);
        }
		this.groupId = groupId;
	}


    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic){
        if (null == topic || "".equals(topic)) {
            throw new IllegalArgumentException("Invalid topic value:" + topic);
        }
        this.topic = topic;
    }

	@Override
	public void onMessages(String key, String value) {
		consumerListener.onReceiveMessage(key,value);
	}


	@Override
	public void onMessages(String value) {
		consumerListener.onReceiveMessage(value);
	}

	public ConsumerListener getConsumerListener() {
		return consumerListener;
	}

	public void setConsumerListener(ConsumerListener consumerListener) {
		this.consumerListener = consumerListener;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.processThreads > 0) {
			this.executor = Executors.newFixedThreadPool(this.processThreads);
		}
	}

	@Override
	public void destroy() throws Exception {
		if (this.executor != null) {
			this.executor.shutdown();
			this.executor = null;
		}
	}

}

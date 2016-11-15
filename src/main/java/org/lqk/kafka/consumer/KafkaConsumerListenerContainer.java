package org.lqk.kafka.consumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;

public class KafkaConsumerListenerContainer implements ListenerContainer, InitializingBean, DisposableBean {


	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerListenerContainer.class);
	private List<KafkaConsumerListener> consumerListeners = new ArrayList<KafkaConsumerListener>();
	private KafkaConsumerConfig kafkaConsumerConfig = null;
	/*
		暂存consumer对象,以备销毁时释放
	 */
	private final CopyOnWriteArraySet<ConsumerConnector> consumers = new CopyOnWriteArraySet<ConsumerConnector>();

	@Override
	public void register(final KafkaConsumerListener listener) {
		this.addListener(listener);
		this.consumerListeners.add(listener);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (KafkaConsumerListener listener : this.consumerListeners) {
			this.addListener(listener);
		}
	}

	private void addListener(KafkaConsumerListener listener) {
		ConsumerConnector consumer = createConnector(listener);
		this.consumers.add(consumer);
		this.poll(listener, consumer);
	}

	private ConsumerConnector createConnector(KafkaConsumerListener listener) {
		Properties properties = kafkaConsumerConfig.getProperties();
		properties.put("group.id", listener.getGroupId());
		return Consumer.createJavaConsumerConnector(kafkaConsumerConfig.getConsumerConfig());
	}
	/*
		轮询consumer,拿到数据,交给listener处理
	 */
	private void poll(final KafkaConsumerListener listener, ConsumerConnector consumer) {
		int threadCount = listener.getProcessThreads();
		String topic = listener.getTopic();
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, threadCount);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		final List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
		ExecutorService threadPool = listener.getExecutor();
		for (final KafkaStream stream : streams) {
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while (it.hasNext()) {
						try {
							MessageAndMetadata<byte[], byte[]> message = it.next();
							String key = null;
							String value = null;
							byte[] byteKey = message.key();
							if (null != byteKey) {
								key = new String(byteKey, "UTF-8");
							}
							value = new String(message.message(), "UTF-8");
							if (null != key) {
								LOG.debug("kafka poll message key=" + key + ",value=" + value);
								listener.onMessages(key, value);
							}else{
								LOG.debug("kafka poll message value=" + value);
								listener.onMessages(value);
							}
						} catch (UnsupportedEncodingException e) {
							LOG.debug("kafka fetch message error", e);
						}
					}
				}
			});
		}
	}


	public List<KafkaConsumerListener> getConsumerListeners() {
		return consumerListeners;
	}

	public void setConsumerListeners(List<KafkaConsumerListener> consumerListeners) {
		this.consumerListeners = consumerListeners;
	}

	@Override
	public void destroy() throws Exception {
		for (ConsumerConnector consumer : consumers) {
			consumer.shutdown();
			consumer = null;
		}
		this.consumers.clear();
		this.consumerListeners.clear();
	}


	public KafkaConsumerConfig getKafkaConsumerConfig() {
		return kafkaConsumerConfig;
	}

	public void setKafkaConsumerConfig(KafkaConsumerConfig kafkaConsumerConfig) {
		this.kafkaConsumerConfig = kafkaConsumerConfig;
	}
}

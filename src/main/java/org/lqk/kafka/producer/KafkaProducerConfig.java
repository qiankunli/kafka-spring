package org.lqk.kafka.producer;

import kafka.producer.ProducerConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Properties;

public class KafkaProducerConfig {

	private String brokers;

	private String serializerClass;

	private String ack;

	private Map<Object,Object> attributes;

	public String getBrokers() {
		return brokers;
	}

	public void setBrokers(String brokers) {
		this.brokers = brokers;
	}

	public String getSerializerClass() {
		return serializerClass;
	}

	public void setSerializerClass(String serializerClass) {
		this.serializerClass = serializerClass;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public Map<Object, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Object, Object> attributes) {
		this.attributes = attributes;
	}

	public ProducerConfig getProducerConfig() {
		if (StringUtils.isBlank(brokers)) {
			throw new IllegalArgumentException("Blank brokers");
		}
		if (StringUtils.isBlank(serializerClass)) {
			throw new IllegalArgumentException("Blank serializerClass");
		}
		if (StringUtils.isBlank(ack)) {
			throw new IllegalArgumentException("Blank ack");
		}
		Properties props = new Properties();
		props.put("metadata.broker.list", this.brokers);
		props.put("serializer.class", this.serializerClass);
		props.put("request.required.acks", this.ack);

		if(null != attributes && attributes.size() > 0){
			props.putAll(attributes);
		}
		return new ProducerConfig(props);
	}
}

package org.lqk.kafka.producer;

import kafka.producer.KeyedMessage;

/**
 *
 * 所谓异步发送后台,就是专门创建线程并发送
 */
public class KafkaProducerTemplate extends KafkaProducerBaseTemplate{

    private String topic;

    @Override
	public void sendAsync(String value) {
        final KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic,value);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                producer.send(data);
            }
        });	
	}
   
    @Override
    public void sendAsync(String key, String value){
        final KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, value);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                producer.send(data);
            }
        });
    }

	@Override
	public void sendSync(String value) {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, value);
		producer.send(data);
	}

	@Override
	public void sendSync(String key, String value) {
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic,key,value);
        producer.send(data);
	}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

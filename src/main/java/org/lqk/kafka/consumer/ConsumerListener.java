package org.lqk.kafka.consumer;

public interface ConsumerListener {

	void onReceiveMessage(String key, String message) ;

	void onReceiveMessage(String message);

}

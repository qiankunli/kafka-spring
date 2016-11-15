package org.lqk.kafka;

import org.lqk.kafka.consumer.ConsumerListener;

/**
 * Created by bert on 16/10/18.
 */
public class MockConsumerListener implements ConsumerListener {
    @Override
    public void onReceiveMessage(String key, String message) {
        System.out.println(key);
        System.out.println(message);
    }

    @Override
    public void onReceiveMessage(String message) {
        System.out.println(message);
    }
}

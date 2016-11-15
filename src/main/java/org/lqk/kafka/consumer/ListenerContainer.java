package org.lqk.kafka.consumer;

import java.io.Serializable;

public interface ListenerContainer extends Serializable {

    void register(KafkaConsumerListener listener);
}

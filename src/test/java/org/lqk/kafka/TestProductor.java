package org.lqk.kafka;

import org.lqk.kafka.producer.KafkaProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bert on 16/10/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-kafka-producer-context.xml"})
public class TestProductor {
    @Autowired
    private KafkaProducerTemplate producerTemplate;

    @Test
    public void kafka() throws Exception {


        producerTemplate.sendSync("abc");

    }
}

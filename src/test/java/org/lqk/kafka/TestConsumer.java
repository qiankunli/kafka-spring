package org.lqk.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bert on 16/10/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-kafka-consumer-context.xml"})
public class TestConsumer {
    @Test
    public void test() throws InterruptedException {
        while(true){
            Thread.sleep(1000);
        }
    }
}

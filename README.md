## 简介

像使用spring-rabbitmq一样使用kafka

## 使用

1. 引入jar
2. 配置文件和类参见`src/test/java,src/test/resources`


## 结构

consumer 抽象

1. ConsumerListener,留给客户端实现的接口
2. KafkaConsumerListener,消息监听器,或者消息处理器,自带线程池
3. KafkaConsumerListenerAdapter,将ConsumerListener包装成KafkaConsumerListener
4. KafkaConsumerListenerContainer,根据consumer config创建consumer connector,并将其与listener绑定


## 其它

代码原始思路来自`https://github.com/huangruisheng/kafka-spring.git`,对其做了类似spring-rabbit的改造。

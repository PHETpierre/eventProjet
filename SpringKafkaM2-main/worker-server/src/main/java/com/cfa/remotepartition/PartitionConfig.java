package com.cfa.remotepartition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

//@Configuration
@Slf4j
public class PartitionConfig {
    public static String TOPIC = "step-execution-events";
    public static String GROUP_ID = "stepresponse_partition";
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel requestsOut() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(PartitionConfig.TOPIC);
        containerProps.setGroupId(PartitionConfig.GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(PartitionConfig.TOPIC));
        return IntegrationFlows
                .from(requestsOut())
                .handle(kafkaMessageHandler)
                .get();
    }
}

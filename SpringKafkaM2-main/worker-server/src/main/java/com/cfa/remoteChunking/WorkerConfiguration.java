package com.cfa.remoteChunking;

import com.cfa.objects.lettre.Lettre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@Slf4j
public class WorkerConfiguration {
    public static String TOPIC_SEND = "step-chunk-send";
    public static String TOPIC_RECEIV = "step-chunk-receive";
    public static String GROUP_ID = "stepresponse_chunking";
    @Autowired
    private RemoteChunkingWorkerBuilder workerBuilder;
    @Autowired
    private ConsumerFactory kafkaFactory;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Bean
    public IntegrationFlow workerFlow(){
        return this.workerBuilder
                .itemWriter(writer())
                .inputChannel(requests())
                .outputChannel(replies())
                .build();
    }

    @Bean
    public LettreWriter writer() {
        return new LettreWriter();
    }

    @Bean
    public DirectChannel requests(){
        return new DirectChannel();
    }

    @Bean
    QueueChannel replies(){
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(WorkerConfiguration.TOPIC_RECEIV));
        return IntegrationFlows
                .from(replies())
                .handle(kafkaMessageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(WorkerConfiguration.TOPIC_SEND);
        containerProps.setGroupId(WorkerConfiguration.GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();
    }
}

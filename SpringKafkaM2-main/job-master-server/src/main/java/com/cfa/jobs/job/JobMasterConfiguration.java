package com.cfa.jobs.job;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilder;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
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
@EnableIntegration
@Slf4j
public class JobMasterConfiguration {

    public static String TOPIC_SEND = "step-chunk-send";
    public static String TOPIC_RECEIV = "step-chunk-receive";

    public static String GROUP_ID = "stepresponse_chunking";
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    private LettreReader lettreReader;
    @Autowired
    private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;
    @Autowired
    private ConsumerFactory kafkaFactory;
    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Bean
    public Job remoteJobLettre(){
        return jobBuilderFactory.get("remoteJobLettre")
                .start(managerStep())
                .build();
    }

    @Bean
    public TaskletStep managerStep() {
        log.info("in remote step");
        return this.managerStepBuilderFactory.get("masterStep")
                .chunk(3)
                .reader(lettreReader.reader())
                .outputChannel(requests())
                .inputChannel(replies())
                .build();
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
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(JobMasterConfiguration.TOPIC_SEND));
        return IntegrationFlows
                .from(requests())
                .handle(kafkaMessageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow inboundFlow() {
        final ContainerProperties containerProps = new ContainerProperties(JobMasterConfiguration.TOPIC_RECEIV);
        containerProps.setGroupId(JobMasterConfiguration.GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);
        System.out.println("result");

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(replies())
                .get();
    }
}

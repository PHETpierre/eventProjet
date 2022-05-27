package com.cfa.jobs.jobexample;

import com.hazelcast.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.w3c.dom.ls.LSOutput;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * creation of the tasklet
 *
 */
@Slf4j
@RequiredArgsConstructor
@EnableBinding({Source.class})
public class SimpleTaskletSource implements Tasklet, StepExecutionListener {
    private final Source source;

    @Override
    public void beforeStep(final StepExecution parStepExecution) {
        log.debug("BeforeStep Asset tasket initialized");
    }

    @Override
    public RepeatStatus execute(final StepContribution parStepContribution, final ChunkContext parChunkContext) {
        final Map<String, Object> locParameters =
                parStepContribution.getStepExecution().getJobParameters().getParameters()
                                .entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));

        // creating the message to send
        final String locPayload = (String) locParameters.get("value");
        final Message<String> locPartitionKey = MessageBuilder.withPayload(locPayload)
                                                             .setHeader("custom_info", "start")
                                                             .build();
        log.info("Message to send : " + locPayload);
        source.output().send(locPartitionKey);

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution parStepExecution) {
        log.debug("AfterStep executing tasklet");
        return ExitStatus.UNKNOWN;
    }

}
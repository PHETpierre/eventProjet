package com.cfa.customjobservice;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Processor.input : to listen msg from job-master-server
 * Processor.output : to send msg to job-master-server
 */
@EnableBinding(Processor.class)
@Slf4j
@RequiredArgsConstructor
public class JobProcessor {
  private final Processor _processor;

  @StreamListener(target = Processor.INPUT, condition = "headers['custom_info']=='start'")
  public void listenStart(final Message<String> parMsg) {
    final String locPayload = parMsg.getPayload();

    final Message<String> locMessageEnd = MessageBuilder.withPayload(locPayload)
      .setHeader("custom_info", "end")
      .build();

    log.info("[Worker START END] sending message to MASTER SERVER");
    // _processor.output().send(locMessageEnd);
  }

}
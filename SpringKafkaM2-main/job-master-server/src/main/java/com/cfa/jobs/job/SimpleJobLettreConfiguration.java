package com.cfa.jobs.job;

import com.cfa.objects.lettre.Lettre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SimpleJobLettreConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private LettreReader lettreReader;
    @Autowired
    private LettreWriter lettreWriter;

    @Bean
    public Job simpleJobLettre(){
        return jobBuilderFactory.get("jobLettre")
                .start(stepLettre())
                .build();
    }

    @Bean
    public Step stepLettre(){
        Step step = null;
        try{
            step = stepBuilderFactory.get("stepLettre")
                    .<Lettre, Lettre>chunk(10)
                    .reader(lettreReader.reader())
                    .writer(writer())
                    .build();
        }catch(Exception e){
            System.out.println(e);;
        }
        return step;
    }

    @Bean
    public Step simpleStep01(){
        return stepBuilderFactory.get("simpleStep01")
                .tasklet(new SimpleTaskletLettre())
                .build();
    }

    @Bean
    public LettreWriter writer()
    {
        return new LettreWriter();
    }
}

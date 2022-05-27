package com.cfa.jobs.job;

import com.cfa.objects.lettre.Lettre;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@AllArgsConstructor
@EnableBinding({Source.class})
public class LettreReader implements ItemReader {
    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    public static FlatFileItemReader<Lettre> reader()
    {
        FlatFileItemReader<Lettre> reader = new FlatFileItemReader<Lettre>();
        reader.setResource(new FileSystemResource("/home/pierre/Documents/ADO2021/introEvent/SpringKafkaM2-main/dataMsg.csv"));
        reader.setLineMapper(new DefaultLineMapper() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "message", "creationDate", "treatmentDate" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Lettre>() {
                    {
                        setTargetType(Lettre.class);
                    }
                });
            }
        });
        return reader;
    }
}

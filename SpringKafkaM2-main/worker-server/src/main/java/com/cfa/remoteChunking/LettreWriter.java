package com.cfa.remoteChunking;

import com.cfa.objects.lettre.Lettre;
import com.cfa.objects.lettre.LettreController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@EnableBinding({Source.class})
public class LettreWriter implements ItemWriter<Lettre> {
    @Autowired
    private LettreController lettreController;

    @Override
    public void write(List<? extends Lettre> list) throws Exception {
        System.out.println("in writer");
        System.out.println(list.get(0));
        final RestTemplate restTemplate = new RestTemplate();
        try {
            Thread.sleep(10000);
            for (Lettre item : list) {
                restTemplate.postForObject("http://localhost:9623/lettre/addLettre", item, com.cfa.objects.lettre.Lettre.class);
//                lettreController.addLettre((com.cfa.objects.lettre.Lettre) item);
            }
        } catch(InterruptedException e) {
            System.out.println("An Excetion occured: " + e);
        }
    }
}

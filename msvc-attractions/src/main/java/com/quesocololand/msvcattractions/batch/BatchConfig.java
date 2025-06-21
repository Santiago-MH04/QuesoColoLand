package com.quesocololand.msvcattractions.batch;


import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemProcessor;
import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemReader;
import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final VisitorCountItemReader visitorCountItemReader;
    private final VisitorCountItemProcessor visitorCountItemProcessor;
    private final MongoTemplate mongoTemplate;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Bean
    public MongoItemWriter<VisitorCount> visitorCountItemWriter() {
        return new MongoItemWriterBuilder<VisitorCount>()
                .template(mongoTemplate)
                .collection("visitor_counts")
                .build();
    }

    @Bean
    public Job importVisitorCounts() {
        return new JobBuilder("importVisitorCounts", this.jobRepository)
                .start(this.readFile()) //We count only with one step
                .build();
    }

    //Beans of BatchConfig
    @Bean
    public Step readFile(){
        return new StepBuilder("CSVImportToDatabase", this.jobRepository)
                    //Input object, output object
                .<VisitorCount, VisitorCount>chunk(10, this.transactionManager)
                .reader(this.visitorCountItemReader)
                .writer(this.visitorCountItemWriter())
                .processor(this.visitorCountItemProcessor)
                .faultTolerant()
                .build();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository){
        TaskExecutorJobLauncher taskExecutorJobLauncher = new TaskExecutorJobLauncher();
        taskExecutorJobLauncher.setJobRepository(jobRepository);

        return taskExecutorJobLauncher;
    }

    @Bean
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("importVisitorCounts", jobRepository)
                .start(this.readFile()) //We count only with one step
                .build();
    }
}

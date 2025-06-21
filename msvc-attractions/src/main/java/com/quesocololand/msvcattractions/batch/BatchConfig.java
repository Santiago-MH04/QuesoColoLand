package com.quesocololand.msvcattractions.batch;


import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemProcessor;
import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemReader;
import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemWriter;
import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
        //Fields of BatchConfig
    private final VisitorCountRepository visitorCountRepo;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

        //Constructors of BatchConfig
    public BatchConfig(VisitorCountRepository visitorCountRepo, @Qualifier("mongoJobRepository") JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.visitorCountRepo = visitorCountRepo;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    //Field setters of BatchConfig (setters)
        //Bean getters of BatchConfig (getters)
    @Bean
    public VisitorCountItemReader getItemReader(){
        return new VisitorCountItemReader();
    }
    @Bean
    public VisitorCountItemWriter getItemWriter(){
        return new VisitorCountItemWriter(this.visitorCountRepo);
    }
    @Bean
    public VisitorCountItemProcessor getItemProcessor(){
        return new VisitorCountItemProcessor();
    }
        //Beans of BatchConfig
    @Bean
    public Step readFile(){
        return new StepBuilder("CSVImportToDatabase", this.jobRepository)
                    //Input object, output object
                .<VisitorCount, VisitorCount>chunk(10, this.platformTransactionManager)
                .reader(this.getItemReader())
                .writer(this.getItemWriter())
                .processor(this.getItemProcessor())
                .faultTolerant()
                .build();
    }

    @Bean
    public JobLauncher mongoJobLauncher(){
        TaskExecutorJobLauncher taskExecutorJobLauncher = new TaskExecutorJobLauncher();
            taskExecutorJobLauncher.setJobRepository(this.jobRepository);

        return taskExecutorJobLauncher;
    }

    @Bean
    public Job registerAttractionUsageJob() {
        return new JobBuilder("importVisitorCounts", this.jobRepository)
                .start(this.readFile()) //We count only with one step
                .build();
    }
}

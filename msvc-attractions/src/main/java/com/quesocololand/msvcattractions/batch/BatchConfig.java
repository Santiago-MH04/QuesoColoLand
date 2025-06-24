package com.quesocololand.msvcattractions.batch;


import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemProcessor;
import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemReader;
import com.quesocololand.msvcattractions.batch.steps.VisitorCountItemWriter;
import com.quesocololand.msvcattractions.models.VisitorCount;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
        //Fields of BatchConfig
    private final VisitorCountItemReader visitorCountItemReader;
    private final VisitorCountItemWriter visitorCountItemWriter;

    private final VisitorCountItemProcessor visitorCountItemProcessor;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

        //Beans of BatchConfig
    @Bean
    @SessionScope
    public Step readFile(){
        return new StepBuilder("CSVImportToDatabase", this.jobRepository)
                //Input object, output object
            .<VisitorCount, VisitorCount>chunk(10, this.transactionManager)
            .reader(this.visitorCountItemReader)
            .writer(this.visitorCountItemWriter)
            .processor(this.visitorCountItemProcessor)
            /*.faultTolerant()*/
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

package com.quesocololand.msvcattractions.services.implementations;

import com.quesocololand.msvcattractions.services.abstractions.VisitorCountBatchImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class VisitorCountBatchImportServiceImpl implements VisitorCountBatchImportService {

    private final Job job;
    private final JobLauncher jobLauncher;

    @Override
    public void batchImport(Path fileResourcePath) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", fileResourcePath.toAbsolutePath().toString())
                .toJobParameters();

                this.jobLauncher.run(this.job, jobParameters);
    }

}

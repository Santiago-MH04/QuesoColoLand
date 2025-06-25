package com.quesocololand.msvcattractions.services.abstractions;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.nio.file.Path;

public interface VisitorCountBatchImportService {
    //Methods of VisitorCountBatchImportService
    void batchImport(Path fileResourcePath) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;
}

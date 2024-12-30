package com.sps.batchconfig;

import org.springframework.batch.core.JobExecution;

public interface IBatchProcessingService {

	JobExecution performBatchProcessing(String filePath);

}

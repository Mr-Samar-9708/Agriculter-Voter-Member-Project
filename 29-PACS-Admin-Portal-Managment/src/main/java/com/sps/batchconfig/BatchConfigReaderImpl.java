package com.sps.batchconfig;

import java.io.FileNotFoundException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.sps.entity.PacsMemEntity;
import com.sps.exception.handling.PacsAppException;
import com.sps.listener.PacsMemListener;
import com.sps.processor.PacsMemProcessor;
import com.sps.repository.IPacsMangRepository;

@Component // Marks this class as a Spring bean.
public class BatchConfigReaderImpl implements IBatchProcessingService{

    // Path of the input file, made static to share across methods.
	/*private static String path = null;*/

	//Chuck for process the data at a time based on number.
	private int chunkSize = 0;
	
	private static final int FILE_LOAD_DELAY_MS = 5;
	
    // Repository used to save data to the database.
	private final IPacsMangRepository pacsRepo;
	
    // Listener to log or track the batch job's progress or errors.
	private final PacsMemListener pacsListener;

    // Processor to process or modify data during the batch process.
	private final PacsMemProcessor pacsProcessor;

    // Repository for job metadata, used by Spring Batch.
	private final JobRepository jobRepo;

    // Manages database transactions, ensuring data integrity.
	private final PlatformTransactionManager txManager;

    // Launches the batch job to execute it.
	private final JobLauncher jobLauncher;

    // Constructor for dependency injection (assigning values to static fields).
	public BatchConfigReaderImpl(IPacsMangRepository pacsRepo, PacsMemProcessor pacsProcessor,
			PacsMemListener pacsListener, JobRepository jobRepo, PlatformTransactionManager txManager,
			JobLauncher jobLauncher) {
		this.pacsRepo = pacsRepo;
		this.pacsListener = pacsListener;
		this.pacsProcessor = pacsProcessor;
		this.jobRepo = jobRepo;
		this.txManager = txManager;
		this.jobLauncher = jobLauncher;
	}
	
    // Method to read data from a CSV file.
	/*public FlatFileItemReader<PacsMemEntity> readExcelFile(@Value("#{jobParameters['fileName']}") String userFile) {
		return new FlatFileItemReaderBuilder<PacsMemEntity>()
	            .name("file-reader") // Name the reader for debugging.
	            .resource(new FileSystemResource(userFile)) // Read the file using the dynamic path.
	            .delimited() // Indicates the file uses comma-separated values (CSV format).
	            .names("serialNo", "name", "fatherOrHushband", "villageNam", "wordNo", "headOfFamily")
	            .targetType(PacsMemEntity.class) // Maps each row to a PacsMemEntity object.
	            .build();
	}*/

	public FlatFileItemReader<PacsMemEntity> createReader(String userFile) {
		return new FlatFileItemReaderBuilder<PacsMemEntity>()
                .name("file-reader") // Name the reader for debugging.
                .resource(new ClassPathResource(userFile)) // Read the file using the dynamic path.
                .delimited() // Indicates the file uses comma-separated values (CSV format).
                .names("serialNo", "name", "fatherOrHushband", "villageNam", "wordNo", "headOfFamily")
                .targetType(PacsMemEntity.class) // Maps each row to a PacsMemEntity object.
                .build();
    }

    // Method to write processed data to the database.
    public  RepositoryItemWriter<PacsMemEntity> writerInDb() {
        return new RepositoryItemWriterBuilder<PacsMemEntity>()
                .repository(this.pacsRepo) // Use the injected repository.
                .methodName("save") // Calls the save() method to insert data into the database.
                .build();
    }

    // Define a step that combines reading, processing, and writing.
    public  Step createStep(String userFile) {
        return new StepBuilder("step1", this.jobRepo) // Name the step and link the job repository.
                .<PacsMemEntity, PacsMemEntity>chunk(this.chunkSize, this.txManager) // Process 10 records at a time.
                .reader(createReader(userFile)) // Use the file reader for input.
                .processor(this.pacsProcessor) // Use the processor for transformations.
                .writer(this.writerInDb()) // Write the transformed data to the database.
                .listener(this.pacsListener) // Add a listener for logging events.
                .build();
    }

    // Define the batch job that executes the defined step.
    public  Job createJob(String userFile) {
        return new JobBuilder("job1", this.jobRepo) // Name the job and link the job repository.
                .incrementer(new RunIdIncrementer()) // Increment the job ID for unique identification of runs.
                .listener(this.pacsListener) // Add a listener to the job for logging.
                .start(this.createStep(userFile)) // Set the starting step.
                .build();
    }

	@Override // Method to dynamically execute the batch job using a specific file path.
	public JobExecution performBatchProcessing(String filePath) {
		/*BatchConfigReaderImpl.path = filePath; // Dynamically set the file path.
		BatchConfigReaderImpl.chunkNum = calculateDynamicChunkSize(filePath);// Calculating chunk dynamically
		*/
	
		try {
			
			if (!isFileCorrect(filePath)) {
				throw new FileNotFoundException("Wrong path or file not exist...!");
			}
			
			Thread.sleep(FILE_LOAD_DELAY_MS); // I am specifying for load file appropriately.

			Job job = createJob(filePath); // Create the job instance.
			
			JobParametersBuilder builder = new JobParametersBuilder();
			//builder.addString("fileName", filePath); // we can pass file path through JobParameters also
			builder.addLong("run.id", System.currentTimeMillis());// Add a unique parameter to ensure a fresh run.

			JobParameters jobParams = builder.toJobParameters();
			
			JobExecution execution = jobLauncher.run(job, // Launch the job.
					                                                                              jobParams);
			return execution;
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Reset the thread's interrupted status.
			throw new PacsAppException("Batch processing interrupted" + e.getMessage());
		} catch (Exception e) {
			throw new PacsAppException("Error occurred during batch processing for file: " + e);
		}
		
	}
	
	private boolean isFileCorrect(String filePath) {
		// Here I have added detail how we will check.
		/*1 byte = 8 bits, 1 kilobyte (KB) = 1000 bytes, and 1 megabyte (MB) = 1,000,000 bytes. */

		boolean isExist = false;

		if (filePath == null || filePath.isEmpty()) {
			throw new IllegalArgumentException("File-path is not specified or is empty.");
		}

		try {
			/*Here I am taking "FileSystemResource" instead of ClassPathResource because I
			 want to read fill from System location here I can also simple "File class".*/
			Resource resource = new ClassPathResource(filePath);

			// Check if the file exists
			if (!resource.exists()) {
				throw new FileNotFoundException("File not found in classpath: " + filePath);
			} else {
				isExist = true;
			}

			// Get file size (the resource can be treated as an InputStream)
			long fileSize = resource.contentLength(); // Get the size of the file.

			/*chunk size logic based on file size, if file larger than 1 MB then chunk size will be 50.*/
			this.chunkSize = fileSize > 1000000 ? 50 : 10;

		} catch (Exception e) {
			throw new PacsAppException("Error validating file: " + filePath, e); // Print stack trace for debugging
		}
		
		return isExist;
	}

}

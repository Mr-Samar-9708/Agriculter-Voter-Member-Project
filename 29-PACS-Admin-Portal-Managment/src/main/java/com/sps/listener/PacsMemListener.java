package com.sps.listener;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class PacsMemListener implements JobExecutionListener {

	private Long startTime, endTime;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = System.currentTimeMillis();
		LocalDateTime ldt = jobExecution.getStartTime();
		System.out.println("Batch Proccessing startTime :: " + ldt.getHour() + "hr : " + ldt.getMinute() + "min : "
				+ ldt.getSecond() + "sec, Status ::" + jobExecution.getStatus());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		endTime = System.currentTimeMillis();
		Long takenTime = (endTime - startTime);
		System.out.println("Total taken time in execution ::" + takenTime + ", Status ::" + jobExecution.getStatus());
	}

}

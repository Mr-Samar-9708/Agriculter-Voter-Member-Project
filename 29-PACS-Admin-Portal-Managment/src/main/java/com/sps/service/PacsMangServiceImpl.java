package com.sps.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.sps.batchconfig.IBatchProcessingService;
import com.sps.binding.SearchInput;
import com.sps.binding.SearchOutput;
import com.sps.entity.PacsMemEntity;
import com.sps.exception.handling.PacsAppException;
import com.sps.exception.handling.PacsMemberNoRecordFound;
import com.sps.repository.IPacsMangRepository;

@Service
public class PacsMangServiceImpl implements IPacsMangService {

	private IBatchProcessingService batchService;
	private IPacsMangRepository repository;

	public PacsMangServiceImpl(IBatchProcessingService batchService, IPacsMangRepository repository) {
		this.batchService = batchService;
		this.repository = repository;
	}

	@Override
	public String performBatchProcessing(String filePath) {
		JobExecution execution = batchService.performBatchProcessing(filePath);

		if (execution.isRunning()) {
			System.out.println("Batch processing is running..!");
		} else {
			throw new PacsAppException("Getting problem in batch processing and it get stuked..!");
		}

		if (!execution.getStatus().isUnsuccessful()) {
			return "File uploaded successfully..!";
		} else {
			throw new PacsAppException("Excel file path is incorrect.");
		}
	}

	@Override
	public List<SearchOutput> searchMember(SearchInput input) {
		PacsMemEntity entity = new PacsMemEntity();
		BeanUtils.copyProperties(input, entity);

		Example<PacsMemEntity> example = Example.of(entity);
		List<PacsMemEntity> listOfMem = repository.findAll(example);
		if (!listOfMem.isEmpty()) {
			List<SearchOutput> outputs = new ArrayList<>();

			listOfMem.forEach(mem -> {
				SearchOutput output = new SearchOutput();
				BeanUtils.copyProperties(mem, output);
				outputs.add(output);
			});
			return outputs;
		} else {
			throw new PacsMemberNoRecordFound("Voter not found..!");
		}
	}

}

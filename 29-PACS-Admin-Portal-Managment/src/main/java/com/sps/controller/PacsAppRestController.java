package com.sps.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sps.binding.SearchInput;
import com.sps.binding.SearchOutput;
import com.sps.service.IPacsMangService;

@RestController
@RequestMapping("/pacs/member")
public class PacsAppRestController {

	private IPacsMangService service;

	public PacsAppRestController(IPacsMangService serv) {
		this.service = serv;
	}

	@GetMapping("/loadingFile/{path}")
	public ResponseEntity<String> performBatchProcess(@PathVariable String path) {
		String msg = service.performBatchProcessing(path);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@PostMapping("/searchMember")
	public ResponseEntity<?> searchMember(@RequestBody SearchInput input) {
		List<SearchOutput> outputs = service.searchMember(input);
		final String msg = "Record not found, try to fill-up search box correctly..!";
		
		if (!outputs.isEmpty()) {
			return new ResponseEntity<>(outputs, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
		}
	}
}

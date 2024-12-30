package com.sps.service;

import java.util.List;

import com.sps.binding.SearchInput;
import com.sps.binding.SearchOutput;

public interface IPacsMangService {

	String performBatchProcessing(String filePath) ;
	
	List<SearchOutput> searchMember(SearchInput input) ;
}

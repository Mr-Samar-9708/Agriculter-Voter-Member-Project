package com.sps.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchOutput {

	private Integer serialNo;
	private String name;
	private String fatherOrHushband;
	private String villageNam;
	private Integer wordNo;
	private String headOfFamily;
}

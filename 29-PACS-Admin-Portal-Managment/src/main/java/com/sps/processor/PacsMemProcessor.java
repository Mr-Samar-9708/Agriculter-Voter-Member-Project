package com.sps.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.sps.entity.PacsMemEntity;

@Component
public class PacsMemProcessor implements ItemProcessor<PacsMemEntity, PacsMemEntity> {

	@Override
	public PacsMemEntity process(PacsMemEntity mem) throws Exception {
		if (mem.getSerialNo() > 0) {
		    return mem;
		} else {
		    throw new IllegalArgumentException("Serial number must be greater than 0");
		}
	}

}

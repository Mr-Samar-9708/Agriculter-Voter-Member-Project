package com.sps.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "PACS_Voter_Detail")
public class PacsMemEntity {

	@Id
	@SequenceGenerator(name = "gen1", sequenceName = "serialNo_sql", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "gen1", strategy = GenerationType.SEQUENCE)
	private Integer serialNo;
	@Column(length = 35)
	private String name;
	@Column(length = 35)
	private String fatherOrHushband;
	@Column(length = 35)
	private String villageNam;
	private Integer wordNo;
	@Column(length = 35)
	private String headOfFamily;

	// Meta-Data
	@Column(updatable = false, insertable = false)// updatable false and insertable also it mean, it will not be include in 
	@CreationTimestamp                                                                                                 // update query or insert query.
	private LocalDateTime createdTime;
	@Column(updatable = true, insertable = false) // updatable true means it will be only include in update query.
	@CreationTimestamp
	private LocalDateTime updateTime;
	@Column(length = 35)
	private String createdBy;
	@Column(length = 35)
	private String updatedBy;
}

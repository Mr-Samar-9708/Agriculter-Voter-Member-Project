package com.sps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sps.entity.PacsMemEntity;

public interface IPacsMangRepository extends JpaRepository<PacsMemEntity, Integer> {

}

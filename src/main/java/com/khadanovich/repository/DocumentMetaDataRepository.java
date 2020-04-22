package com.khadanovich.repository;

import org.springframework.data.repository.CrudRepository;

import com.khadanovich.domain.DocumentMetaData;

public interface DocumentMetaDataRepository extends CrudRepository<DocumentMetaData, Long> {

}

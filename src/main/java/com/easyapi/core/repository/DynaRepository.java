package com.easyapi.core.repository;

import com.easyapi.core.model.DynaModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DynaRepository extends MongoRepository<DynaModel, Long> {
    public List<DynaModel> findByResource(String resource);
    public DynaModel findByResourceAndId(String resource, Long id);
    public Integer countByResource(String resource);
    public void deleteByResource(String resource);
}

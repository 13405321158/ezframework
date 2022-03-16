package com.leesky.ezframework.demo.es.repo;


import com.leesky.ezframework.demo.es.model.Demo01Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface Idem01Repo extends ElasticsearchRepository<Demo01Model, String> {
}

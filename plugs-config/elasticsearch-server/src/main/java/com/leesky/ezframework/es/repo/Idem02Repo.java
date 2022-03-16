package com.leesky.ezframework.es.repo;

import com.leesky.ezframework.es.model.Demo02Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface Idem02Repo extends ElasticsearchRepository<Demo02Model, String> {
}

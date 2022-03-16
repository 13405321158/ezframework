package com.leesky.ezframework.es.repo;

import com.leesky.ezframework.es.model.Demo01Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface Idem01Repo extends ElasticsearchRepository<Demo01Model, String> {
}

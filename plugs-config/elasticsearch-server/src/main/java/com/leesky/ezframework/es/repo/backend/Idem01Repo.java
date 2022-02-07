package com.leesky.ezframework.es.repo.backend;

import com.leesky.ezframework.es.model.backend.Demo01Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface Idem01Repo extends ElasticsearchRepository<Demo01Model, String> {
}

package com.lang.search.dao;

import com.lang.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {

}

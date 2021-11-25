package com.lang.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.lang.search.dao.SkuEsMapper;
import com.lang.entity.Result;
import com.lang.search.pojo.SkuInfo;
import com.lang.sellergoods.feign.ItemFeign;
import com.lang.sellergoods.pojo.Item;
import com.lang.search.service.SkuService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ItemFeign itemFeign;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;//spring-data-es 提供的一个模板工具类对象


    @Override
    public void importSku() {

        //调用商品微服务，获取sku商品数据
        Result<List<Item>> result = itemFeign.findByStatus("1");
        //把数据转换为搜索实体类数据
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(result.getData()), SkuInfo.class);

        //遍历sku集合
        for (SkuInfo skuInfo : skuInfoList) {
            //获取规格
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            //关联设置到specMap
            skuInfo.setSpecMap(specMap);
        }

        //保存sku集合数据到es
        skuEsMapper.saveAll(skuInfoList);
    }

    @Override
    public Map search(Map<String, String> searchMap) {
        //1.获取关键字的值
        String keywords = searchMap.get("keywords");

        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";//赋值给一个默认的值
        }
        //2.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

// ****** 设置分组的条件  terms后表示分组查询后的列名
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("category"));

        //设置分组条件  商品品牌
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brand").size(50));

        //设置分组条件  商品的规格
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100));


        //3.设置查询的条件
        //使用：QueryBuilders.matchQuery("title", keywords) ，搜索华为 ---> 华    为 二字可以拆分查询，
        //使用：QueryBuilders.matchPhraseQuery("title", keywords) 华为二字不拆分查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));


        //4.构建查询对象
        NativeSearchQuery build = nativeSearchQueryBuilder.build();


        //5.执行查询
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(build, SkuInfo.class);
        //对搜索searchHits集合进行分页封装
        SearchPage<SkuInfo> searchHits1 = SearchHitSupport.searchPageFor(searchHits, build.getPageable());//Pageable 0,10


//******** 获取分组结果
        // 商品分类
        Terms termsCategory = searchHits.getAggregations().get("skuCategorygroup");
        //商品品牌
        Terms termsBrand = searchHits.getAggregations().get("skuBrandgroup");
        //商品规格
        Terms termsSpec = searchHits.getAggregations().get("skuBrandgroup");

        //获取分类名称集合
        List<String> categoryList = getStringsCategoryList(termsCategory);
        //获取品牌名称的集合
        List<String> brandList = getStringsCategoryList(termsBrand);

        //获取品牌规格的集合
        Map<String, Set<String>> specMap = getStringSetMap(termsSpec);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置规格过滤查询
        if (searchMap != null) {
            for (String key : searchMap.keySet()) {//{ brand:"",category:"",spec_网络:"电信4G"}
                if (key.startsWith("spec_")) {
                    //截取规格的名称
                    boolQueryBuilder.filter(
                            QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key))
                    );
                }
            }
        }

        //遍历取出查询的商品信息
        List<SkuInfo> list = new ArrayList<>();

        for (SearchHit<SkuInfo> searchHit : searchHits1.getContent()) {// 获取搜索到的数据
            SkuInfo content = searchHit.getContent();
            list.add(content);
        }

        //6.返回结果
        Map resultMap = new HashMap<>();

        resultMap.put("categoryList", categoryList);
        resultMap.put("brandList", brandList);
        resultMap.put("rows", list);//获取所需SkuInfo集合数据内容（当前页数据集合）
        resultMap.put("total", searchHits.getSearchHits());//总记录数
        resultMap.put("totalPages", searchHits1.getTotalPages());//总页数

        return resultMap;
    }

    /**
     * 获取分类列表数据
     *
     * @param terms
     * @return
     **/
    private List<String> getStringsCategoryList(Terms terms) {
        List<String> categoryList = new ArrayList<>();

        if (terms != null) {
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();// 分组的值（分类名称）
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }

    /**
     * 获取规格列表数据
     * String：规格的名称
     * Set<String> ： 规格选项
     *
     * @param termsSpec
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(Terms termsSpec) {
        Map<String, Set<String>> specMap = new HashMap<>();

        //所有不同的spec
        Set<String> specList = new HashSet<>();
        if (termsSpec != null) {
            for (Terms.Bucket bucket : termsSpec.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
        }
        for (String specjson : specList) {
            Map<String, String> map = JSON.parseObject(specjson, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();        //规格名字
                String value = entry.getValue(); //规格选项值
                //获取当前规格名字对应的规格数据
                Set<String> specValues = specMap.get(key);
                if (specValues == null) {
                    specValues = new HashSet<>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                //将数据存入到specMap中
                specMap.put(key, specValues);
            }
        }
        return specMap;
    }

    @Override
    public void clearAll() {
        skuEsMapper.deleteAll();
    }

}

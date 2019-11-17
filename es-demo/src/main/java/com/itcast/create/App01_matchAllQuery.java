package com.itcast.create;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.rmi.transport.Transport;

import java.net.InetAddress;

/**
 * @Author YY
 * @Date 2019/11/17 15:56
 * @Version 1.0
 * ES搜索 - 查询所有文档
 */
public class App01_matchAllQuery {
    private TransportClient client;

    @Test
    public void test() {
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("java web开发");
        /**
         * 参数一: Field的名称
         * 参数二：词条的名称
         */
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("bookname","java");
        /**
         * 参数一: 需要查询的id值（多个）
         */
//        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("5","3");
        /**
         * rangeQuery: 指定需要搜索的Field
         *
         * 常用的比较方法：
         *   gt(): 大于
         *   lt(): 小于
         *   gte(): 大于等于
         *   lte(): 小于等于
         */
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("price").gt(80).lt(100);
        search(queryBuilder);
    }

    /**
     * 查询所有文档
     */
    public void search(QueryBuilder queryBuilder) {
        //执行查询，返回结果
        /**
         * prepareSearch(索引)：设置需要查询的索引（相当于数据库）
         * setType(类型)设置需要查询的类型（相当于表）
         * setQuery（查询条件）设置查询条件（不同的查询会着这里变化
         *
         * QueryBuilders.matchAllQuery查询所有文档
         */
        SearchResponse response = client.prepareSearch("yy_index")
                .setTypes("book")
                .setQuery(queryBuilder)
                .get();
        //遍历结果SearchHits: 查询到的词条列表
        SearchHits hits = response.getHits();
        //获取命中的文档数量
        System.out.println("命中的文档数量" + hits.totalHits);
        SearchHit[] searchHits = hits.getHits();
        //遍历所有词条列表数据
        for (SearchHit searchHit : searchHits) {
            System.out.println("=============================");
            System.out.println(searchHit.getSourceAsMap().get("id"));
            System.out.println(searchHit.getSourceAsMap().get("bookname"));
            System.out.println(searchHit.getSourceAsMap().get("price"));
            System.out.println(searchHit.getSourceAsMap().get("pic"));
            System.out.println(searchHit.getSourceAsMap().get("bookdesc"));

        }
    }

    /**
     * 初始化工作
     */
    @Before
    public void init() throws Exception {
        //1、创建一个Settings对象，相当于一个配置信息。主要配置集群的名称。
        /**
         * 参数一：集群key，固定不变的
         * 参数二：集群环境的名称。默认的ES的集群环境名称为 "elasticsearch"
         */
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建一个客户端client对象
        //传入setting参数
        client = new PreBuiltTransportClient(settings);
        //2.1 设置连接参数
        /**
         * 参数一：ES的连接主机
         * 参数二：ES的连接端口。注意：ES和Java通讯端口为9300
         */
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    /**
     * 释放资源
     */
    @After
    public void clear() {
        client.close();
    }
}


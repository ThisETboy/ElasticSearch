package com.itcast.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.pojo.Book;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @Author YY
 * @Date 2019/11/16 20:31
 * @Version 1.0
 * 演示在ES中更新文档
 * 注意：在操作文档的时候指定类型
 */
public class App03_UpdateDocument {
    private TransportClient client;

    /**
     * 使用preparedUpdate方法进行修改
     */
    @Test
    public void test1() throws Exception {
        //准备好要修改的文档数据
        Book book = new Book();
        book.setId(5);
        book.setBookname("SpringBoot");
        book.setPrice(75L);
        book.setPic("123.jpg");
        book.setBookdesc("精通了");

        //JavaBean转换为Json
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(book);

        //调用prpearedUpdate方法修改文档
        client.prepareUpdate("yy_index", "book", book.getId() + "")
                .setDoc(jsonData, XContentType.JSON)
                .get();

    }

    /**
     * 初始化工作
     */
    @Before
    public void init() throws Exception {
        //创建一个Settings相当于一个配置信息，主要配置集群的名称
        /**
         * 参数一：集群key，固定不变
         * 参数二：集群环境的名称。默认的ES的集群环境名称为"elasticsearch"
         */
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建一个客户端client对象
        //传入setting参数
        client = new PreBuiltTransportClient(settings);
        //设置连接参数
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

    /**
     * 使用preparedDelete方法进行修改
     */
    @Test
    public void testDelete() throws Exception {
        client.prepareDelete("yy_index", "book", "5").get();
    }

    /**
     * 根据条件删除文档
     */
    @Test
    public void deleteBy() throws Exception {
        //构建删除条件(链式写法)
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client)
                .filter(QueryBuilders.termQuery("bookname", "java"))
                .source("yy_index")
                .get();
        //返回删除的记录数
        long deleted = response.getDeleted();
        System.out.println("删除了" + deleted + "条");

        //非链式写法
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = DeleteByQueryAction.INSTANCE.newRequestBuilder(client);
        deleteByQueryRequestBuilder = deleteByQueryRequestBuilder.filter(QueryBuilders.termQuery("bookname", "java"));
        deleteByQueryRequestBuilder = deleteByQueryRequestBuilder.source("xiaofeifei_index");
        BulkByScrollResponse response1 = deleteByQueryRequestBuilder.get();
        long deleted1 = response1.getDeleted();
        System.out.println(deleted);
    }

}

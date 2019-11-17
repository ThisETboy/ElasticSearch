package com.itcast.create;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @Author YY
 * @Date 2019/11/17 15:43
 * @Version 1.0
 * 创建映射
 */
public class App06_create_mapping {
    //操作es的客户端
    private TransportClient client;

    @Test
    public void test() throws Exception {
        String mappingJson = "{\"book\":{\"properties\":{\"price\":{\"type\":\"long\",\"store\":true},\"bookdesc\":{\"type\":\"text\",\"index\":true,\"store\":false,\"analyzer\":\"ik_max_word\"},\"id\":{\"type\":\"long\",\"index\":true,\"store\":true},\"pic\":{\"type\":\"text\",\"index\":false,\"store\":true},\"bookname\":{\"type\":\"text\",\"index\":true,\"store\":true,\"analyzer\":\"ik_max_word\"}}}}";
        //创建映射
        client.admin()
                .indices()
                .preparePutMapping("yy_index").setType("book")
                .setSource(mappingJson, XContentType.JSON)
                .get();
    }

    //给客户端赋值
    @Before
    public void init() throws Exception {
        //创建一个settings对象，相当于一个配置信息，主要配置集群的名称
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        client = new PreBuiltTransportClient(settings);
        //使用client对象创建一个索引库
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

    }

    @After
    public void close() {
        client.close();
    }

}

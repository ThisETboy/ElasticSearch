package com.itcast.create;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @Author YY
 * @Date 2019/11/17 15:11
 * @Version 1.0
 */
public class App06_Mapping {
    private TransportClient client;


    @Test
    public void test() throws Exception {
        /**
         * field("store",true):该Field进行存储
         * field("store",false)：该Field不进行存储
         * field("index",true): 该Field进行索引
         * field("index",false): 该Field不进行索引
         */

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("book")
                .startObject("properties")
                .startObject("id").field("type", "long").field("store", true).endObject()
                .startObject("bookname").field("type", "text").field("store", true).endObject()
                .startObject("price").field("type", "float").field("store", true).endObject()
                .startObject("pic").field("type", "text").field("store", true).field("index", false).endObject()
                .startObject("bookdesc").field("type", "text").field("store", false).endObject()
                .endObject()
                .endObject()
                .endObject();

        //使用client对象修改Mapping的信息到索引中
        /**
         * preparePutMapping("索引"):设置映射放在哪个索引
         * setType("类型")设置映射放在哪个类型上面
         */
        client.admin()
                .indices()
                .preparePutMapping("yy_index")
                .setType("book")
                .setSource(contentBuilder)
                .get();
    }

    /**
     * 初始化工作
     */
    @Before
    public void init() throws Exception {
        //创建一个Settings对象。相当于一个配置信息。主要配置集群的名称
        /**
         * 参数一：集群key，固定不变的
         * 参数二：集群环境的名称，默认的ES的集群名称为"elasticsearch"
         */
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建一个客户端client对象
        client = new PreBuiltTransportClient(settings);
        //设置连接参数
        /**
         * 参数一：ES的连接主机
         * 参数二：ES的连接端口，9300
         */
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @After
    public void clear() {
        client.close();
    }
}

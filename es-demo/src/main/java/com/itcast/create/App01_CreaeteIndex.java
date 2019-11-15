package com.itcast.create;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @Author YY
 * @Date 2019/11/15 20:57
 * @Version 1.0
 * 演示在ES中创建新的索引（类似于mysql的数据库）
 */
public class App01_CreaeteIndex {

    @Test
    public void test1() throws Exception {
        //创建一个Setting对象，相当于一个配置信息，主要配置集群的名称
        /**
         * 参数一：集群key，固定不变
         * 参数二：集群环境的名称。默认的ES的集群环境名称为"elasticsearch"
         */
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();

        //创建一个客户端client对象
        //传入setting参数
        TransportClient client = new PreBuiltTransportClient(settings);
        //设置连接参数
        /**
         * 参数一：ES的连接主机
         * 参数二：ES的连接端口。注意：ES和java通讯端口为9300
         */
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //使用client对象创建一个索引库
        //prepareCreate:创建索引的方法
        //get():执行前面的动作
        client.admin().indices().prepareCreate("yy_index").get();
        //关闭client对象
        client.close();
    }
}

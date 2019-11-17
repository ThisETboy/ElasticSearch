package com.itcast.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.dao.BookImpl;
import com.itcast.pojo.Book;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;

/**
 * @Author YY
 * @Date 2019/11/16 8:59
 * @Version 1.0
 * 创建文档对象
 */
public class App02_create_Document {
    //操作es的客户端
    private TransportClient client;

    //创建对象方式三:就是json字符串:把JavaBean转成字符串
    @Test
    public void test3() throws Exception {
        //由JavaBean转成这个字符串就可以了
        List<Book> bookList = new BookImpl().findAllBooks();

        //遍历对象，把每个对象转成json字符
        for (Book book : bookList) {
            //把对象转成json字符串
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(book);
            //执行//第三个参数是文档的id，一般与数据的id保持一致
            client.prepareIndex("yy_index", "book", book.getId() + "")
                    .setSource(jsonData, XContentType.JSON)
                    .get();//执行前面所有操作
        }
    }

    //给客户端赋值
    @Before
    public void init() throws Exception {
        //创建一个Settings对象，相当于一个配置信息，主要配置集群的名称
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建一个客户端client对象
        client = new PreBuiltTransportClient(settings);
        //使用client对象创建一个索引库
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    //释放资源
    @After
    public void clear() {
        //关闭client对象
        client.close();
    }

}

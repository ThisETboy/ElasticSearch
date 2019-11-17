package com.itcast.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcast.pojo.Book;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author YY
 * @Date 2019/11/15 21:14
 * @Version 1.0
 * 演示在ES中创建文档（类似于mysql的一行行记录）
 * 注意：在操作文档的时候指定类型（type）
 */
public class App_CreateDocument {
    private TransportClient client;

    //保存文档格式{"id":1,"bookname":"java web开发详解","price":56.5,"pic":"1.jpg":"bookdesc":"xxxxxxx"}

    /**
     * 方式一:使用Json工厂类进行添加文档
     */
    @Test
    public void test() throws IOException {
        /**
         * 创建一个文档
         */
        //XContentFactory: 用于构建json数据的工厂
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                .startObject()// json对象的开始 ：{
                .field("id", 1)
                .field("bookname", "java web 详解")
                .field("price", 566)
                .field("pic", "1.jpg")
                .field("bookdesc", "《Java从入门到精通》是人民邮电出版社于 2010年出版的图书，由国家863中部软件孵化器主编。" +
                        "以零基础讲解为宗旨，深入浅出地讲解Java的各项技术及实战技能。本书从初学者角度出发，通过通俗易懂的语言、丰富多彩" +
                        "的实例，详细介绍了使用Java语言进行程序开发应该掌握的各方面技术。全书共分28章，包括：初识Java，熟悉Eclipse开发" +
                        "工具，Java 语言基础，流程控制，字符串，数组，类和对象，包装类，数字处理类，接口、继承与多态，类的高级特性，异常" +
                        "处理，Swing程序设计，集合类，I/O输入输出，反射，枚举类型与泛型，多线程，网络通信，数据库操作，Swing表格组件，" +
                        "Swing树组件，Swing其他高级组件，高级布局管理器，高级事件处理，AWT绘图与音频播放，打印技术和企业进销存管理系统等" +
                        "。所有知识都结合具体实例进行介绍，涉及的程序代码给出了详细的注释，可以使读者轻松领会Java程序开发的精髓，快速提高" +
                        "开发技能。.")
                .endObject();
        //把文档添加到索引下面(需要指定类型)
        /**
         * 参数1：指定索引名称
         * 参数2：指定类型的名称
         * 参数3：指定文档的ID值
         */
        //setSource：设置文档内容
        client.prepareIndex("yy_index", "book", 1 + "")
                .setSource(contentBuilder)
                .get();
    }

    /**
     * 初始化工作
     */
    @Before
    public void init() throws Exception {
        //创建一个Setting对象，相当于一个配置信息，主要配置集群信息
        /**
         * 参数一：集群key，固定不变
         * 参数二：集群环境的名称，默认的ES的集群环境名称为"elasticsearch"
         */
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建一个客户端client对象(非常重要的)
        //传入setting参数
        client = new PreBuiltTransportClient(settings);
        /**
         * 设置连接参数
         * 参数一：ES的连接主机
         * 参数二：ES的连接端口。注意ES和java通讯端口为9300
         */
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

    }

    /**
     * 释放资源
     */
    @After
    public void release() {
        client.close();
    }

    /**
     * 方式二：使用Map集合封装数据进行添加文档
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        /**
         * 创建一个文档
         */
        //1.创建一个文档
        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("bookname", "lucene从入门到精通");
        map.put("price", 76.5);
        map.put("pic", "2.jpg");
        map.put("bookdesc", "本书总结搜索引擎相关理论与实际解决方案，并给出了 Java 实现，其中利用了流行的开源项目Lucene和Solr，" +
                "而且还包括原创的实现。本书主要包括总体介绍部分、爬虫部分、自然语言处理部分、全文检索部分以及相关案例分析。爬虫部分" +
                "介绍了网页遍历方法和如何实现增量抓取，并介绍了从网页等各种格式的文档中提取主要内容的方法。自然语言处理部分从统计机" +
                "器学习的原理出发，包括了中文分词与词性标注的理论与实现以及在搜索引擎中的实用等细节，同时对文档排重、文本分类、自动" +
                "聚类、句法分析树、拼写检查等自然语言处理领域的经典问题进行了深入浅出的介绍并总结了实现方法。在全文检索部分，结合" +
                "Lucene 3.0介绍了搜索引擎的原理与进展。用简单的例子介绍了Lucene的最新应用方法。本书包括完整的搜索实现过程：从完成" +
                "索引到搜索用户界面的实现。本书还进一步介绍了实现准实时搜索的方法，展示了Solr 1.4版本的用法以及实现分布式搜索服务" +
                "集群的方法。最后介绍了在地理信息系统领域和户外活动搜索领域的应用。");

        //2.把文档添加到索引下面（需要指定类型）
        /**
         * 参数一：指定索引名称
         * 参数二：指定类型的名称
         * 参数三：指定文档的ID值
         */
        //setSource: 设置文档内容
        client.prepareIndex("yy_index", "book", 2 + "")
                .setSource(map)
                .get();
    }

    /**
     * 方式三:使用JavaBean封装数据进行添加文档(推荐方式)
     */
    @Test
    public void test3() throws Exception {
        /**
         * 创建一个文档对象
         */
        Book book = new Book();
        book.setId(3);
        book.setBookname("疯人院");
        book.setPrice(1280L);
        book.setPic("2.jpg");
        book.setBookdesc("好听");
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(book);
        /**
         * 把文档添加到索引下面
         * 参数一：指定索引名称
         * 参数二：指定类型的名称
         * 参数三：指定文档的ID值
         */
        //setSource设置文档的内容
        client.prepareIndex("yy_index", "book", 3 + "")
                .setSource(jsonData, XContentType.JSON)
                .get();

    }
}

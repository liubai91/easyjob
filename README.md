easyjob是一款基于quartz简单的线上任务调度管理工具，支持通过web页面定制调度计划，设定调度参数，查看执行日志

## 快速开始

1. 添加maven依赖
```
<dependency>
    <groupId>com.easyjob</groupId>
    <artifactId>easyjob</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. 配置管理页面
```
@Configuration
public class EasyConfig implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Bean
    public ServletRegistrationBean easyJobViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new JobViewServlet(beanFactory), "/easyjob/*");
        return bean;
    }
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
```
注意servlet映射不可自定义必须为"/easyjob/*"

3. 执行数据库初始化脚本

本工具使用数据库来存储任务元信息，任务日志，所有使用该工具的工程必须使用数据库，并且数据库中有如下所述的三张表

下载地址：[https://github.com/liubai91/easyjob/blob/main/src/main/resources/sql/intitialsql.sql](https://github.com/liubai91/easyjob/blob/main/src/main/resources/sql/intitialsql.sql)
在工程连接到数据库中执行该数据库脚本
该脚本会创建3个表如下图
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698125063466-0b27e7d4-3289-4cf1-bfbd-3e0f844d09aa.png#averageHue=%23f5f4f4&clientId=uae5472e7-b265-4&from=paste&height=108&id=u0a293fa3&originHeight=215&originWidth=253&originalType=binary&ratio=2&rotation=0&showTitle=false&size=18119&status=done&style=none&taskId=u4774485a-08da-4abe-b70e-8bafa7dd4ff&title=&width=126.5)

4. 编写第一个定时任务
```
@TaskGroup(decription = "测试任务组")
public class TestTask {


    @Task(value = "测试任务一", description = "该任务用于测试")
    public void test(@TaskParam("姓名") String name) {
        System.out.println("hello");
    }
}
```
@TaskGroup是自定义@Component的一个特殊化版本，注解了该注解的类也会被扫描并注册到spring容器中，
@Task注解作用在方法上，用于标注一个定时任务，在注解中添加相应属性，可以便于web端的配置管理
@TaskParam("姓名") 注解作用在参数上，用于标注任务参数，在注解中添加相应属性，可以便于web端的配置管理
如上一个定时任务，会工具探测到，在管理页面的效果如下
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698126168253-795a52c4-44d4-44ba-b643-9ccc15c9b4ab.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=613&id=uf5853366&originHeight=1226&originWidth=2542&originalType=binary&ratio=2&rotation=0&showTitle=false&size=141389&status=done&style=none&taskId=u03b05a91-199a-4e0a-bdb1-1bad8486be8&title=&width=1271)

5. 设定调度计划，设定调度参数

![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121697373-e2f15125-629f-4d5f-b4d4-a07116733655.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=526&id=ue0805a34&originHeight=1052&originWidth=2674&originalType=binary&ratio=2&rotation=0&showTitle=false&size=200887&status=done&style=none&taskId=u13374375-e354-41bd-8a6c-9325b32eeed&title=&width=1337)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121762248-2de43de4-2d9f-4393-a7ce-151c066ed9bb.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=651&id=u41c8a524&originHeight=1302&originWidth=2590&originalType=binary&ratio=2&rotation=0&showTitle=false&size=177472&status=done&style=none&taskId=u6fb50433-1685-4276-a0ad-00a90bb8bf3&title=&width=1295)

6. 开始调度

![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121907576-be8e9843-e4d7-4dac-b480-315bb2abcd94.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=500&id=u38c94b27&originHeight=1000&originWidth=2658&originalType=binary&ratio=2&rotation=0&showTitle=false&size=134342&status=done&style=none&taskId=u454d1dd6-c386-4c4d-baf1-9416bfb223c&title=&width=1329)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698126413931-87d79f1d-73a8-45ea-a3b2-69aa089444d1.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=367&id=ue272be2e&originHeight=734&originWidth=2624&originalType=binary&ratio=2&rotation=0&showTitle=false&size=139212&status=done&style=none&taskId=uc8ba174b-b942-47fd-979c-44a525ca9eb&title=&width=1312)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698126381533-86e293fb-f9d4-434e-824b-cac107780991.png#averageHue=%23fdfdfd&clientId=uae5472e7-b265-4&from=paste&height=338&id=ue72f6685&originHeight=676&originWidth=1732&originalType=binary&ratio=2&rotation=0&showTitle=false&size=67205&status=done&style=none&taskId=ue216ab0a-166a-4aed-8ffb-9b9f62538f6&title=&width=866)
先在调度中心启动调度器
然后在任务管理中调度该任务
最后就能在调度中心看到任务正在执行，执行完毕后能在任务日志中看此次执行的相关信息

7. 查看执行日志

![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121808224-14405ac7-91bd-47cf-b305-310df61e05ee.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=624&id=ufc28a4d1&originHeight=1248&originWidth=2844&originalType=binary&ratio=2&rotation=0&showTitle=false&size=246656&status=done&style=none&taskId=ub13765ea-a547-4714-bae1-343d9b903c0&title=&width=1422)


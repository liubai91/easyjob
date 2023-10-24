easyjob是一款基于quartz简单的线上任务调度管理工具，支持通过web页面定制调度计划，设定调度参数，查看执行日志

## 快速开始

1. 添加maven依赖
```
<dependency>
    <groupId>com.easyJob</groupId>
    <artifactId>easyJob</artifactId>
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

4. 编写第一个定时任务
5. 设定调度计划，设定调度参数

![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121697373-e2f15125-629f-4d5f-b4d4-a07116733655.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=526&id=ue0805a34&originHeight=1052&originWidth=2674&originalType=binary&ratio=2&rotation=0&showTitle=false&size=200887&status=done&style=none&taskId=u13374375-e354-41bd-8a6c-9325b32eeed&title=&width=1337)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121762248-2de43de4-2d9f-4393-a7ce-151c066ed9bb.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=651&id=u41c8a524&originHeight=1302&originWidth=2590&originalType=binary&ratio=2&rotation=0&showTitle=false&size=177472&status=done&style=none&taskId=u6fb50433-1685-4276-a0ad-00a90bb8bf3&title=&width=1295)

6. 开始调度

7. 查看执行日志

![image.png](https://cdn.nlark.com/yuque/0/2023/png/21567582/1698121808224-14405ac7-91bd-47cf-b305-310df61e05ee.png#averageHue=%23fefefe&clientId=uae5472e7-b265-4&from=paste&height=624&id=ufc28a4d1&originHeight=1248&originWidth=2844&originalType=binary&ratio=2&rotation=0&showTitle=false&size=246656&status=done&style=none&taskId=ub13765ea-a547-4714-bae1-343d9b903c0&title=&width=1422)


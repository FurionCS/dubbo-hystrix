### hystrix 

#### 了解hystrix 熔断的流程图

[流程图](img/hystrix-command-flow-chart-640.png)
在dubbo中对于消费者的保护提供了actives进行并发控制保护，但是功能相对薄弱，下面我们探讨下如何使用Netflix提供的服务容错组件Hystrix对dubo消费者提供线程隔离保护
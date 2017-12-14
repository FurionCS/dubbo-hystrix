package com.netease.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.*;

import java.util.Map;

public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;
    private Invoker<?>       invoker;
    private Invocation       invocation;


    public DubboHystrixCommand(Invoker<?> invoker,Invocation invocation){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(invocation.getMethodName()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//10秒钟内至少19此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(30000)//熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false)
                )//使用dubbo的超时，禁用这里的超时
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(DEFAULT_THREADPOOL_CORE_SIZE)
                )
        );//线程池为30
        this.invoker=invoker;
        this.invocation=invocation;
    }
    @Override
    protected Result run() throws Exception{
        return invoker.invoke(invocation);
    }

    @Override
    protected Result getFallback() {
        return new DubboHystrixCommand.customResult();
    }

    class customResult implements Result {
        customResult() {
        }


        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Throwable getException() {
            return null;
        }

        @Override
        public boolean hasException() {
            return false;
        }

        @Override
        public Object recreate() throws Throwable {
            return null;
        }

        @Override
        public Object getResult() {
            return null;
        }

        @Override
        public Map<String, String> getAttachments() {
            return null;
        }

        @Override
        public String getAttachment(String s) {
            return null;
        }

        @Override
        public String getAttachment(String s, String s1) {
            return null;
        }
    }
}

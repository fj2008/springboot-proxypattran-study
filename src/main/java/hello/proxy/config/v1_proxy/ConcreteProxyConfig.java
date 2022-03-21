package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderContorllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerconcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderContorllerV2 orderContorllerV2(LogTrace logTrace){
        OrderContorllerV2 controllerImpl = new OrderContorllerV2(orderServiceV2(logTrace));
        return new OrderControllerconcreteProxy(controllerImpl, logTrace);
    }


    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace){
        OrderServiceV2 serviceImpl= new OrderServiceV2(orderRepositoryV2(logTrace));
        return new OrderServiceConcreteProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();

        return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);
    }
}

package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA(){
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        // Proxy.newProxyInstance(로드할 클래스,어떤인터페이스기반으로 프록시사용할지, 프록시가 사용해야하는 로직)
        // 위와같이 세개의 인수를 넣어준다.
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        proxy.call();
        log.info("targetClass ={}",target.getClass());
        log.info("targetClass ={}",proxy.getClass());

    }

    @Test
    void dynamicB(){
        BInterface target = new BImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        // Proxy.newProxyInstance(로드할 클래스,어떤인터페이스기반으로 프록시사용할지, 프록시가 사용해야하는 로직)
        // 위와같이 세개의 인수를 넣어준다.
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);
        proxy.call();
        log.info("targetClass ={}",target.getClass());
        log.info("targetClass ={}",proxy.getClass());

    }
}

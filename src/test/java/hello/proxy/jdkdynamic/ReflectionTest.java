package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() throws NoSuchMethodException, ClassNotFoundException {
        Hello target = new Hello();

        //공통 로직 1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("result={}", result1);
        //공통 로직 2 시작
        String result2 = target.callB();
        log.info("result={}", result2);
        // 공통로직 1과 2는 내용 이 같지만 호출내용이 다르기때문에 메서드로 뽑아내거나 하기가 쉽지 않다
        // 이럴땐 리플랙션을 사용하면 해결할 수 있다.

    }
    @Test
    void reflection() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //클래스 정보 가져오기
        Class classHello =  Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 메서드정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 =methodCallA.invoke(target);
        log.info("result1={}",result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 =methodCallA.invoke(target);
        log.info("result2={}",result2);

    }

    @Test
    void reflection2() throws Exception{

        Class classHello =  Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 메서드정보
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);

    }
    private void dynamicCall(Method method,Object target)throws Exception{
        log.info("start");

        Object result1 =method.invoke(target);
        log.info("result={}",result1);
    }

    @Slf4j
    static class Hello{
        public  String callA(){
            log.info("callA");
            return "A";
        }

        public  String callB(){
            log.info("callB");
            return "B";
        }
    }
}

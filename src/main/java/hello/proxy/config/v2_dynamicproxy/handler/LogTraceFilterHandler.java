package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final  String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터
        String methodName = method.getName();
        //save, request,req* ,*est와 같이 할 수 있다.
        if(!PatternMatchUtils.simpleMatch(patterns,methodName)){
            //특정 메서드 이름이 메칭되는겨우에만 실행하도록 한다.
            //메칭안된다고 그냥 이상한거 리턴하면 심각한 오류가 발생할 수 있기때문에 잘 리턴해준다.
            return method.invoke(target,args);
        }

        TraceStatus status = null;
        try{
            String message = method.getDeclaringClass().getSimpleName() +"."+ method.getName()+"()";
            status =  logTrace.begin(message);
            //로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }
}

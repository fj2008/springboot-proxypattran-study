package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderContorllerV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerconcreteProxy  extends OrderContorllerV2 {

    private final OrderContorllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerconcreteProxy( OrderContorllerV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status =  logTrace.begin("OrderController.OderItem()");
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}

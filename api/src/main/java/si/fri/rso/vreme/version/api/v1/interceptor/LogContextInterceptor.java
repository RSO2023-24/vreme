package si.fri.rso.vreme.version.api.v1.interceptor;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.UUID;

@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogContextInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogContextInterceptor.class);

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        try {
            logger.info("LogContextInterceptor activated");

            HashMap<String, String> settings = new HashMap<>();

            settings.put("environmentType", EeConfig.getInstance().getEnv().getName());
            settings.put("applicationName", EeConfig.getInstance().getName());
            settings.put("applicationVersion", EeConfig.getInstance().getVersion());
            settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

            settings.put("uniqueRequestId", UUID.randomUUID().toString());
            settings.put("serviceName", "vreme"); // replace with your service name
            settings.put("methodName", context.getMethod().getName());

            MDC.setContextMap(settings);

            try {
                long startTime = System.nanoTime();
                Object result = context.proceed();
                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;
                MDC.put("executionTime", String.valueOf(executionTime));

                return result;
            } finally {
                MDC.clear();
            }
        } catch (Exception e) {
            logger.error("Error in LogContextInterceptor", e);
            throw e; // rethrow the exception so it can be handled elsewhere
        }
    }
}
package org.nikth.aop;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

public class MyPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable 
	{
		String name = createInvocationTraceName(invocation);
		
        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        }
        finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            Object[] args = invocation.getArguments();
            logger.info("Method "+name+ "args:"+Arrays.asList(args)+" execution lasted:"+time+" ms");
        }
	}

}

package org.nikth.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopConfiguration 
{
	@Pointcut(
			"within(org.nikth..*)"
			//"execution(public String org.nikth.HelloRest.testProperties(..))"
			)
	public void monitor() { }

	@Bean
	public MyPerformanceMonitorInterceptor performanceMonitorInterceptor() {
		//return new PerformanceMonitorInterceptor(false);
		return new MyPerformanceMonitorInterceptor();
	}

	@Bean
	public Advisor performanceMonitorAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("org.nikth.aop.AopConfiguration.monitor()");
		return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
	}
}

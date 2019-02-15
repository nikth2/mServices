package org.nikth;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@SpringBootApplication
public class MServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MServicesApplication.class, args);
		ArrayList<String> list = new ArrayList<String>();
		list.add("nikos");
		list.add("kostas");
		list.forEach(str->System.out.println(str));
	}
	
	@Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("StravaSegment-");
        executor.initialize();
        return executor;
    }

}


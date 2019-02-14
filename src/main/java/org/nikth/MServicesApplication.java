package org.nikth;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MServicesApplication.class, args);
		ArrayList<String> list = new ArrayList<String>();
		list.add("nikos");
		list.add("kostas");
		list.forEach(str->System.out.println(str));
	}

}


package org.nikth;


import javax.inject.Inject;

import org.nikth.data.DataRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRest 
{
	@Inject DataRepository dr;
	
	@RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
	
	@RequestMapping("/user/{id}")
    public String getUserById(@PathVariable("id")int id) 
	{
        return "userId:"+id +" name:"+dr.getUserById(id).getName();
    }
	
	
	public void getSegments()
	{
	}
}

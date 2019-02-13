package org.nikth;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.nikth.data.DataRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.client.ApiClient;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.auth.Authentication;
import io.swagger.client.model.DetailedAthlete;
import io.swagger.client.model.SummaryActivity;


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
	
	@RequestMapping(path="/test",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String,String> test()
	{
		ApiClient client = new ApiClient();
		client.setAccessToken("4764f9b5a7f0bb3e707f197b57656bb4dbf24c22");
	    io.swagger.client.api.ActivitiesApi api = new ActivitiesApi(client);
	    try {
	    	AthletesApi athApi = new AthletesApi(client);
	    	Map<String, Authentication> m = client.getAuthentications();
	    	Authentication auth = m.get("strava_oauth");
	    	//System.out.println(auth.);
	    	DetailedAthlete result = athApi.getLoggedInAthlete();
	    	System.out.println(result);
			//DetailedActivity da = api.getActivityById(new Long(2136435252),Boolean.TRUE);
	    	List<SummaryActivity> activities = api.getLoggedInAthleteActivities(1549947860, 1541917515, 10, 1);
	    	//api.get
			System.out.println(activities);
			//da.getSegmentEfforts().stream();
			return Collections.singletonMap("name",activities.get(0).getName());
			
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.singletonMap("error",e.getMessage());
		}
	}
}

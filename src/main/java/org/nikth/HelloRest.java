package org.nikth;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.nikth.callback.ActivityCallback;
import org.nikth.data.Activity;
import org.nikth.data.DBAdapter;
import org.nikth.data.DataRepository;
import org.nikth.data.Segment;
import org.nikth.data.User;
import org.nikth.properties.SQLProperties;
import org.nikth.redis.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.auth.Authentication;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.DetailedAthlete;
import io.swagger.client.model.SummaryActivity;
import nikth.services.MySegment;
import nikth.services.Util;


@RestController
public class HelloRest 
{
	@Autowired
	DataRepository dr;
	
	@Autowired
	DBAdapter db;
	
	@Autowired
	SQLProperties properties;
	
	@Autowired
    private RedisTemplate<String, String> redis;
	
	@Autowired
	RedisRepository redisRepo;
	
	@Value("${strava.activity.id}")
	private String activityIdFromYml;
	
	@Value("${strava.access.token}")
	private String accessToken;
	
	@RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
	
	@RequestMapping("/user/{id}")
    public String getUserById(@PathVariable("id")int id) 
	{
        return "userId:"+id +" name:"+dr.getUserById(id).getName();
    }
	
	@RequestMapping("/activity/{id}")
    public String getActivityById(@PathVariable("id")int id) 
	{
        return "activity id:"+id +" name:"+dr.getActivityById(id).getName();
    }
	
	@RequestMapping("/redis/put")
    public String testRedisSet(@RequestParam("name")String name) 
	{
        //redis.opsForValue().set("name", name);
        User user = new User();
        user.setName(name);
        user.setId(System.currentTimeMillis());
        user.setUpperName(name.toUpperCase());
        redisRepo.addUser(user);
        
        return "added "+name;
    }
	
	@RequestMapping("/redis/get/{name}")
    public String testRedisGet(@PathVariable("name")String name) 
	{
        //return redis.opsForValue().get(name);
		Gson gson = new GsonBuilder().create();
	    return  gson.toJson(redisRepo.getUser(name));
    }
	
	@RequestMapping(path="/detailed_activity/{id}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getDetailedActivityById(@PathVariable("id")long id) throws ApiException
	{
		ApiClient client = new ApiClient();
		client.setAccessToken(accessToken);
	    io.swagger.client.api.ActivitiesApi api = new ActivitiesApi(client);
	    
	    
	    //ApiCallback<DetailedActivity> callback  = new ActivityCallback();
	    DetailedActivity activity = api.getActivityById(id, Boolean.TRUE);
	    //Call activity = api.getActivityByIdAsync(id, Boolean.TRUE,callback);
	    Gson gson = new GsonBuilder().create();
	    return  gson.toJson(activity);
    }
	
	@RequestMapping(path="/test",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String,String> test()
	{
		ApiClient client = new ApiClient();
		client.setAccessToken(accessToken);
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
	
	@Transactional
	@RequestMapping(path="/load",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public long load()
	{
		long start = System.currentTimeMillis();
		ApiClient client = new ApiClient();
		client.setAccessToken(accessToken);
		ActivitiesApi api = new ActivitiesApi(client);
		Util util = new Util();
		long[] activityId = new long[] {2136435252,2107079241,2090662526};
		
		//ArrayList<MySegment> segmentList = new ArrayList<MySegment>();
		for (int i = 0; i < activityId.length; i++) 
		{
			try 
			{
				System.out.println("HelloRest.load "+activityId[i]);
				DetailedActivity activity = api.getActivityById(activityId[i], Boolean.TRUE);
				
				Activity myActivity = new Activity();
				myActivity.setName(activity.getName());
				myActivity.setActivityId(activityId[i]);
				
				System.out.println(activity.getName());
				//segmentList.addAll(util.getSegmentsForActivity(client, activityId[i]));
				
				Stream<Segment> dbStream =  util.getSegmentsForActivity(client, activityId[i]).stream().map(stravaSegment->{
					Segment dbs = new Segment();
					dbs.setActivity(myActivity);
					dbs.setDistance(stravaSegment.getPathLength());
					dbs.setElevation(stravaSegment.getGainedAltitude());
					dbs.setTime(stravaSegment.getSegmentTime());
					
					return dbs;
					});
				/*
				 * Set<Segment> segmentsSet = dbStream.collect(Collectors.toSet());
				 * myActivity.setSegments(segmentsSet); System.out.println(myActivity);
				 */
				dr.saveActivity(myActivity);
				dbStream.forEach(s->dr.saveSegment(s));
			} 
			catch (ApiException e) {
				e.printStackTrace();
			}
		}
		
		return System.currentTimeMillis() - start;
	}
	
	
	@Transactional
	@RequestMapping(path="/loadAsync",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public long loadAsync()
	{
		long start = System.currentTimeMillis();
		ApiClient client = new ApiClient();
		client.setAccessToken(accessToken);
		ActivitiesApi api = new ActivitiesApi(client);
		long[] activityId = new long[] {2136435252,2107079241,2090662526};
		
		
		for (int i = 0; i < activityId.length; i++) 
		{
			long id = activityId[i];
			CompletableFuture.runAsync(()-> loadAsync(client, api,id ));
		}
		
		return System.currentTimeMillis() - start;
	}
	
	@Transactional
	@RequestMapping(path="/loadAsync2",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public long loadAsync2()
	{
		long start = System.currentTimeMillis();
		ApiClient client = new ApiClient();
		client.setAccessToken(accessToken);
		ActivitiesApi api = new ActivitiesApi(client);
		long[] activityId = new long[] {2136435252,2107079241,2090662526};
		
		List<CompletableFuture<HashMap<Activity, Stream<Segment>>>> futures = new ArrayList<CompletableFuture<HashMap<Activity, Stream<Segment>>>>();
		
		for (int i = 0; i < activityId.length; i++) 
		{
			long id = activityId[i];
			CompletableFuture<HashMap<Activity, Stream<Segment>>> f = CompletableFuture.supplyAsync(()-> loadAsync2(client, api,id ));
			futures.add(f);
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).join();
		
		futures.forEach(f->{
			try 
			{
				f.get().keySet().forEach(activity->dr.saveActivity(activity));
				f.get().values().forEach(dbs->dbs.forEach(s->dr.saveSegment(s)));
				System.out.println("Saved");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		return System.currentTimeMillis() - start;
	}
	
	@RequestMapping(path="/testDB")
	public void testDB()
	{
		//db.getAllSegments();
		db.getSegmentsForActivityWithProcedure(1943);
	}
	
	@RequestMapping(path="/properties")
	public String testProperties()
	{
		System.out.println("MServicesApplication.main:"+properties.getTest());
		return properties.getSelect().getGetSegmentsByActivity();
	}
	
	
	
	
	@Async
	private CompletableFuture<ArrayList<MySegment>> getSegmentsAsync(ApiClient client,long activityId) throws ApiException
	{
		Util util = new Util();
		return CompletableFuture.completedFuture(util.getSegmentsForActivity(client, activityId));
	}
	
	@Async
	@Transactional
	private void loadAsync(ApiClient client, ActivitiesApi api,long activityId)
	{
		try
		{
			System.out.println("HelloRest.loadAsync "+activityId);
			Util util = new Util();
			DetailedActivity activity = api.getActivityById(activityId, Boolean.TRUE);
			
			Activity myActivity = new Activity();
			myActivity.setName(activity.getName());
			myActivity.setActivityId(activityId);
			
			System.out.println(activity.getName());
			
			Stream<Segment> dbStream =  util.getSegmentsForActivity(client, activityId).stream().map(stravaSegment->{
				Segment dbs = new Segment();
				dbs.setActivity(myActivity);
				dbs.setDistance(stravaSegment.getPathLength());
				dbs.setElevation(stravaSegment.getGainedAltitude());
				dbs.setTime(stravaSegment.getSegmentTime());
				
				return dbs;
				});
			
			  dr.saveActivity(myActivity);
			  dbStream.forEach(s->dr.saveSegment(s));
			 
			
			System.out.println("Finished "+Thread.currentThread().getName() );
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Async
	@Transactional
	private HashMap<Activity, Stream<Segment>> loadAsync2(ApiClient client, ActivitiesApi api,long activityId)
	{
		try
		{
			System.out.println("HelloRest.loadAsync "+activityId);
			Util util = new Util();
			DetailedActivity activity = api.getActivityById(activityId, Boolean.TRUE);
			
			Activity myActivity = new Activity();
			myActivity.setName(activity.getName());
			myActivity.setActivityId(activityId);
			
			System.out.println(activity.getName());
			
			Stream<Segment> dbStream =  util.getSegmentsForActivity(client, activityId).stream().map(stravaSegment->{
				Segment dbs = new Segment();
				dbs.setActivity(myActivity);
				dbs.setDistance(stravaSegment.getPathLength());
				dbs.setElevation(stravaSegment.getGainedAltitude());
				dbs.setTime(stravaSegment.getSegmentTime());
				
				return dbs;
				});
			
			  //Set<Segment> segmentsSet = dbStream.collect(Collectors.toSet());
			  ///myActivity.setSegments(segmentsSet); 
			  //System.out.println(myActivity);
			  
			 
			HashMap<Activity, Stream<Segment>> map = new HashMap<Activity, Stream<Segment>>();
			map.put(myActivity,dbStream);
			
			System.out.println("Finished "+Thread.currentThread().getName() );
			return map;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
}

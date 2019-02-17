package org.nikth.data;

import java.util.ArrayList;
import java.util.Map;

import org.nikth.properties.SQLProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Component
public class DBAdapter 
{
	@Autowired
	JdbcTemplate jdbc;
	
	public void getAllSegments()
	{
		jdbc.query("select * from segment", (rs,rownum)->rs.getString("activity_id")).forEach(x->System.out.println(x));
	}
	
	public void getSegmentsForActivityWithProcedure(long activityId)
	{
		SQLProperties properties = new SQLProperties();
		
		SimpleJdbcCall call = new SimpleJdbcCall(jdbc).withProcedureName("get_segments_by_activity");
		
		
		Map<String, Object> simpleJdbcCallResult = call.execute(activityId);
		
		Object obj = simpleJdbcCallResult.get("#result-set-1");
		ArrayList<LinkedCaseInsensitiveMap<?>> resultList = (ArrayList<LinkedCaseInsensitiveMap<?>>)obj;
		
		resultList.forEach(rs->{
			Segment s = new Segment();
			s.setId((Long)rs.get("id"));
			s.setDistance((Long)rs.get("distance"));
			System.out.println(s);
		});
		
		
		
		
		//System.out.println("DBAdapter.getSegmentsForActivityWithProcedure "+properties.getSecurity().getGetSegmentsByActivity());
		//System.out.println("DBAdapter.getSegmentsForActivityWithProcedure "+properties.getTest());
	}
}

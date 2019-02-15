package org.nikth.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBAdapter 
{
	@Autowired
	JdbcTemplate jdbc;
	
	public void getAllSegments()
	{
		jdbc.query("select * from segment", (rs,rownum)->rs.getString("activity_id")).forEach(x->System.out.println(x));
	}
}

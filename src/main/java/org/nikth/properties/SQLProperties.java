package org.nikth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="sql")
public class SQLProperties 
{
	private final Select select = new Select();
	
	private String test;
	

	

	public Select getSelect() {
		return select;
	}



	public String getTest() {
		return test;
	}



	public void setTest(String test) {
		this.test = test;
	}



	public static class Select
	{
		private String getSegmentsByActivity;

		public String getGetSegmentsByActivity() {
			return getSegmentsByActivity;
		}

		public void setGetSegmentsByActivity(String getSegmentsByActivity) {
			this.getSegmentsByActivity = getSegmentsByActivity;
		}
		
		
	}
}

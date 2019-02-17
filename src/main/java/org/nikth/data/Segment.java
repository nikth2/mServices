package org.nikth.data;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Segment 
{	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="activityId")
	private Activity activity;
	
	@Column(name="elevation")
	private long elevation;
	
	@Column
	private long time;
	
	@Column
	private long distance; 

	public Segment() 
	{
	}
	
	

	/*
	 * public Segment(long elevation, long time, long distance, Activity activity) {
	 * this.elevation = elevation; this.time = time; this.distance = distance;
	 * this.activity = activity; }
	 */



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public long getElevation() {
		return elevation;
	}

	public void setElevation(long elevation) {
		this.elevation = elevation;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Segment [id=" + id + ", activity=" + Optional.ofNullable(activity).map(Activity::getName) + ", elevation=" + elevation + ", time=" + time
				+ ", distance=" + distance + "]";
	}

	
	
	
}

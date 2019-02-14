package org.nikth.data;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Activity 
{
	@Id
	private long id;
	
	@Column
	private String name;
	
	
	@OneToMany(mappedBy="activity", fetch=FetchType.LAZY)
	private Set<Segment> segments;
	
	public Activity()
	{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Segment> getSegments() {
		return segments;
	}

	public void setSegments(Set<Segment> segments) {
		this.segments = segments;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", segments=" + segments + "]";
	}


	

}

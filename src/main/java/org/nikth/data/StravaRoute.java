package org.nikth.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StravaRoute 
{
	@Id
	private int id;
	
	@Column
	private String name;
	
	public StravaRoute()
	{
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "StravaRoute [id=" + id + ", name=" + name + "]";
	}
	
	
}

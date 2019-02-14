package org.nikth.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class DataRepository 
{
	@PersistenceContext
    private EntityManager em;
	
	public User getUserById(int id)
	{
		return em.find(User.class, id);
	}
	
	public void saveActivity(Activity activity)
	{
		em.persist(activity);
	}
	
	public void saveSegment(Segment s)
	{
		em.persist(s);
	}
}

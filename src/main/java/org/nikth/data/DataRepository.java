package org.nikth.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
	
	public Activity getActivityById(long id)
	{
		return em.find(Activity.class, id);
	}
	
	@Transactional
	public void saveActivity(Activity activity)
	{
		em.persist(activity);
	}
	
	@Transactional
	public void saveSegment(Segment s)
	{
		em.persist(s);
	}
}

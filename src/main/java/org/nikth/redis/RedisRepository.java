package org.nikth.redis;


import java.util.Map;

import org.nikth.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public class RedisRepository 
{
	@Autowired
	RedisTemplate<String,Object> myRedis;
	
	@Autowired
	JedisConnectionFactory jedisConnectionFactory;
	
	//@Autowired
	//StringRedisTemplate strRedis;
	
	public static final String KEY="USERS";
	private BoundHashOperations<String, String, Object> hashOps;
	
	//private RedisList<String> users;
	
	//Jackson2HashMapper mapper = new Jackson2HashMapper(true);
	HashMapper<Object, String, Object> mapper =  new Jackson2HashMapper(true);
	
	public RedisRepository()
	{
		//hashOps = myRedis.boundHashOps(KEY);
		//this.users = new DefaultRedisList<String>(RedisRepository.KEY,strRedis);
	}
	
	/*@Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());                                           
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }*/
	
	public void addUser(User user)
	{
		myRedis.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		//myRedis.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		hashOps = myRedis.boundHashOps(user.getName());
		//hashOps = redisTemplate().boundHashOps(KEY);
		//hashOps.put(user.getName(), user);
		Map<String,Object> mh =  mapper.toHash(user);
		hashOps.putAll(mh);
	}
	
	public User getUser(String name)
	{
		myRedis.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		hashOps = myRedis.boundHashOps(name);
		return (User)mapper.fromHash( hashOps.entries());
	}
	
	

}


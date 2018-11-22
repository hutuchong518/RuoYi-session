package com.ruoyi.framework.shiro.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 使用redis实现Cache缓存
 * 
 * @author ruoyi
 */
@SuppressWarnings("unchecked")
public class RedisCache<K, V> implements Cache<K, V>
{
    private long expireTime;// 缓存的超时时间，单位为s

    /**
     * 用于shiro的cache的名字
     */
    private String cacheName = "shiro_redis_session";

    private RedisTemplate<K, V> redisTemplate; // 通过构造方法注入该对象

    public RedisCache()
    {
        super();
    }

    public RedisCache(long expireTime, RedisTemplate<K, V> redisTemplate)
    {
        super();
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    public String cacheKey(String cacheName, K key)
    {
        return keyPrefix(cacheName) + key;
    }

    public String keyPrefix(String cacheName)
    {
        return cacheName + ":";
    }

    /**
     * 通过key来获取对应的缓存对象
     */
    @Override
    public V get(K key) throws CacheException
    {
        return redisTemplate.opsForValue().get(cacheKey(cacheName, key));
    }

    /**
     * 将权限信息加入缓存中
     */
    @Override
    public V put(K key, V value) throws CacheException
    {
        if ("loginRecordCache".equals(cacheName))
        {
            redisTemplate.opsForValue().set((K) cacheKey(cacheName, key), value, 600, TimeUnit.SECONDS);
        }
        else
        {
            redisTemplate.opsForValue().set((K) cacheKey(cacheName, key), value, expireTime, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 将权限信息从缓存中删除
     */
    @Override
    public V remove(K key) throws CacheException
    {
        V v = redisTemplate.opsForValue().get(cacheKey(cacheName, key));
        redisTemplate.opsForValue().getOperations().delete((K) cacheKey(cacheName, key));
        return v;
    }

    @Override
    public void clear() throws CacheException
    {

    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public Set<K> keys()
    {
        return null;
    }

    @Override
    public Collection<V> values()
    {
        return null;
    }

}

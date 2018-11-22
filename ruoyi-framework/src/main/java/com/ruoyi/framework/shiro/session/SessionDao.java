package com.ruoyi.framework.shiro.session;

import java.io.Serializable;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SessionDao 工厂类
 * 
 * @author ruoyi
 */
@Service
public class SessionDao
{
    @Value("${spring.redis.enabled}")
    private boolean redisEnabled = false;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    /**
     * 根据CustomBusiType实例各类Model
     * 
     * @param sessinId 会话ID
     * @return 会话信息
     */
    public Session readSession(Serializable sessinId)
    {
        Session session = null;

        if (redisEnabled)
        {
            session = redisSessionDAO.readSession(sessinId);
        }
        else
        {
            session = onlineSessionDAO.readSession(sessinId);
        }
        return session;
    }

    /**
     * 同步会话到DB
     * 
     * @param onlineSession 会话信息
     */
    public void syncToDb(OnlineSession onlineSession)
    {
        if (redisEnabled)
        {
            redisSessionDAO.syncToDb(onlineSession);
        }
        else
        {
            onlineSessionDAO.syncToDb(onlineSession);
        }
    }

}

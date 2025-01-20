package com.graphode.journalapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphode.journalapp.api.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    public <T> T getValue(String key, Class<T> weatherResponseClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(o.toString(), weatherResponseClass);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error occured in getting value from cache. " + e);
            return null;
        }
    }

    public void setValue(String key, Object o, Long ttl) {
        try {
            redisTemplate.opsForValue().set(key, o.toString(), ttl);
        } catch (Exception e) {
            log.error("Error occured in setting value in cache. " + e);
        }
    }
}

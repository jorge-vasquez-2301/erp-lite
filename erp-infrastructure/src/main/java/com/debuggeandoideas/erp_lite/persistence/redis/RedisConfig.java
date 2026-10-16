package com.debuggeandoideas.erp_lite.persistence.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.debuggeandoideas.erp_lite.constants.CacheConstants.*;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final Duration REDIS_CACHE_TTL = Duration.ofHours(24);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .typePropertyName("_type")
                .enableUnsafeDefaultTyping()
                .build();

        RedisCacheConfiguration configuration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(REDIS_CACHE_TTL)
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));


        Map<String, RedisCacheConfiguration> configsMap = new HashMap<>();

        configsMap.put(CACHE_PRODUCTS_BY_ID, configuration);
        configsMap.put(CACHE_PRODUCTS_BY_SKU, configuration);
        configsMap.put(CACHE_PRODUCTS_BY_CATEGORY, configuration);
        configsMap.put(CACHE_PRODUCTS_ACTIVE, configuration);

        configsMap.put(CACHE_CATALOGS_BY_TYPE, configuration);
        configsMap.put(CACHE_CATALOGS_ITEMS, configuration);

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(configuration)
                .withInitialCacheConfigurations(configsMap)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .typePropertyName("_type")
                .enableUnsafeDefaultTyping()
                .build();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // cache manger
        redisTemplate.setValueSerializer(serializer); // cache manger
        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); // redis template
        redisTemplate.setHashValueSerializer(serializer);  // redis template

        return redisTemplate;
    }
}
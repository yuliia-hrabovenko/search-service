package net.guzari.search.domain.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static java.time.Duration.ofDays;
import static net.guzari.search.domain.report.CacheConstants.CACHE_BASE;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisBuilderCustomizer(ObjectMapper objectMapper) {
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper.activateDefaultTyping(cacheObjectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, PROPERTY)
                .registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer defaultSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        Jackson2JsonRedisSerializer<Report> reportJackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Report.class);
        reportJackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return builder -> builder.cacheDefaults(defaultCacheConfig()
                        .serializeValuesWith(fromSerializer(defaultSerializer))
                        .entryTtl(ofDays(1)))
                .withCacheConfiguration(CACHE_BASE, defaultCacheConfig()
                        .serializeValuesWith(fromSerializer(reportJackson2JsonRedisSerializer))
                        .entryTtl(ofDays(2)));
    }

}

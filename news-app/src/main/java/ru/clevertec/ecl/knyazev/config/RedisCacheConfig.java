package ru.clevertec.ecl.knyazev.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.mapper.NewsMapper;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.impl.CommentRepositoryRedisCacheProxy;
import ru.clevertec.ecl.knyazev.repository.proxy.impl.NewsRepositoryRedisCacheProxy;
import ru.clevertec.ecl.knyazev.service.CommentService;
import ru.clevertec.ecl.knyazev.service.NewsService;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.impl.CommentServiceImpl;
import ru.clevertec.ecl.knyazev.service.impl.NewsServiceImpl;

import java.lang.reflect.Proxy;
import java.util.UUID;

@ConditionalOnExpression(
        "${cache.enabled:true} and '${cache.type}'.equals('redis')"
)
@Configuration
public class RedisCacheConfig {

    @Bean
    RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory,
                                                ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(UUID.class));
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    NewsRepositoryRedisCacheProxy newsRepositoryRedisCacheProxy(NewsRepository newsRepository,
                                                                RedisTemplate<String, Object> redisTemplate,
                                                                NewsMapper newsMapperImpl) {
        return new NewsRepositoryRedisCacheProxy(newsRepository,
                redisTemplate,
                newsMapperImpl);
    }

    @Bean
    CommentRepositoryRedisCacheProxy commentRepositoryRedisCacheProxy(CommentRepository commentRepository,
                                                                      RedisTemplate<String, Object> redisTemplate,
                                                                      CommentMapper commentMapperImpl) {
        return new CommentRepositoryRedisCacheProxy(commentRepository,
                redisTemplate,
                commentMapperImpl);
    }

    @Bean
    NewsRepository newsRepositoryProxy(NewsRepositoryRedisCacheProxy newsRepositoryRedisCacheProxy) {
        return (NewsRepository) Proxy.newProxyInstance(NewsRepository.class.getClassLoader(),
                new Class[]{NewsRepository.class}, newsRepositoryRedisCacheProxy);
    }

    @Bean
    CommentRepository commentRepositoryProxy(CommentRepositoryRedisCacheProxy commentRepositoryRedisCacheProxy) {
        return (CommentRepository) Proxy.newProxyInstance(CommentRepository.class.getClassLoader(),
                new Class[]{CommentRepository.class}, commentRepositoryRedisCacheProxy);
    }

    @Bean
    NewsService newsServiceImpl(NewsRepository newsRepositoryProxy,
                                UserService userServiceImpl,
                                NewsMapper newsMapperImpl,
                                CommentMapper commentMapperImpl) {

        return new NewsServiceImpl(newsRepositoryProxy,
                userServiceImpl,
                newsMapperImpl,
                commentMapperImpl);
    }

    @Bean
    CommentService commentServiceImpl(CommentRepository commentRepositoryProxy,
                                      NewsService newsServiceImpl,
                                      UserService userServiceImpl,
                                      CommentMapper commentMapperImpl) {

        return new CommentServiceImpl(commentRepositoryProxy,
                newsServiceImpl,
                userServiceImpl,
                commentMapperImpl);
    }
}

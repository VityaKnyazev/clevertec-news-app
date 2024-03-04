package ru.clevertec.ecl.knyazev.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.knyazev.cache.factory.AbstractCacheFactory;
import ru.clevertec.ecl.knyazev.cache.factory.impl.ConcurrentCacheFactory;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;
import ru.clevertec.ecl.knyazev.cache.operator.impl.CommentCacheOperator;
import ru.clevertec.ecl.knyazev.cache.operator.impl.NewsCacheOperator;
import ru.clevertec.ecl.knyazev.config.properties.CacheProperties;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.impl.CommentRepositoryCacheProxy;
import ru.clevertec.ecl.knyazev.repository.proxy.impl.NewsRepositoryCacheProxy;

import java.lang.reflect.Proxy;
import java.util.UUID;

@ConditionalOnProperty(prefix = "cache",
        name = "enabled",
        havingValue = "true")
@Configuration
public class CacheConfig {

    @Bean
    AbstractCacheFactory concurrentCacheFactory(CacheProperties cacheProperties) {
        return new ConcurrentCacheFactory(cacheProperties.algorithm(), cacheProperties.size());
    }

    @Bean
    AbstractCacheOperator<UUID, News> newsCacheOperator(AbstractCacheFactory concurrentCacheFactory) {
        return new NewsCacheOperator(concurrentCacheFactory.initCache());
    }

    @Bean
    AbstractCacheOperator<UUID, Comment> commentCacheOperator(AbstractCacheFactory concurrentCacheFactory) {
        return new CommentCacheOperator(concurrentCacheFactory.initCache());
    }

    @Bean
    NewsRepositoryCacheProxy houseRepositoryCacheProxy(NewsRepository newsRepository,
                                                       AbstractCacheOperator<UUID, News> newsCacheOperator) {
        return new NewsRepositoryCacheProxy(newsRepository,
                newsCacheOperator);
    }

    @Bean
    CommentRepositoryCacheProxy commentRepositoryCacheProxy(CommentRepository commentRepository,
                                                            AbstractCacheOperator<UUID, Comment> commentCacheOperator) {
        return new CommentRepositoryCacheProxy(commentRepository,
                commentCacheOperator);
    }

    @Bean
    NewsRepository newsRepositoryProxy(NewsRepositoryCacheProxy newsRepositoryCacheProxy) {
        return (NewsRepository) Proxy.newProxyInstance(NewsRepository.class.getClassLoader(),
                new Class[]{NewsRepository.class}, newsRepositoryCacheProxy);
    }

    @Bean
    CommentRepository commentRepositoryProxy(CommentRepositoryCacheProxy commentRepositoryCacheProxy) {
        return (CommentRepository) Proxy.newProxyInstance(CommentRepository.class.getClassLoader(),
                new Class[]{CommentRepository.class}, commentRepositoryCacheProxy);
    }

    //TODO add Services beans with proxy-repositories in feature/service
}

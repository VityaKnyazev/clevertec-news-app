package ru.clevertec.ecl.knyazev.repository.proxy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.NewsMapper;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.RepositoryCacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class NewsRepositoryRedisCacheProxy extends RepositoryCacheProxy<UUID, News> implements InvocationHandler {

    private static final String NEWS_HASH_KEY = "news";

    private final NewsRepository newsRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final NewsMapper newsMapperImpl;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.executeProxyMethod(newsRepository,
                method,
                args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<News> whenFindByUuid(UUID uuid) {
        Optional<News> dbNews;

        HashOperations<String, UUID, News> hashOperations = redisTemplate.opsForHash();

        if (hashOperations.hasKey(NEWS_HASH_KEY, uuid)) {
            dbNews = Optional.ofNullable(hashOperations.get(NEWS_HASH_KEY, uuid));
        } else {
            dbNews = newsRepository.findByUuid(uuid);
            dbNews.ifPresent(news -> hashOperations.put(NEWS_HASH_KEY,
                    uuid,
                    newsMapperImpl.toNewsWithoutComments(news)));
        }

        return dbNews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected News whenSaveOrUpdate(News savingOrUpdatingNews) throws DataAccessException {
        HashOperations<String, UUID, News> hashOperations = redisTemplate.opsForHash();

        News savedOrUpdatedNews = newsRepository.save(savingOrUpdatingNews);

        hashOperations.put(NEWS_HASH_KEY,
                savedOrUpdatedNews.getUuid(),
                newsMapperImpl.toNewsWithoutComments(savedOrUpdatedNews));

        return savedOrUpdatedNews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void whenDelete(UUID deletingNewsUUID) throws DataAccessException {
        HashOperations<String, UUID, News> hashOperations = redisTemplate.opsForHash();

        newsRepository.deleteByUuid(deletingNewsUUID);
        hashOperations.delete(NEWS_HASH_KEY, deletingNewsUUID);
    }
}

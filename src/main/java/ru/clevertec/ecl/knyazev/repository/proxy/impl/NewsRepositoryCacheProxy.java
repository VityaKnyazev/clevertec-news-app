package ru.clevertec.ecl.knyazev.repository.proxy.impl;

import org.springframework.dao.DataAccessException;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.RepositoryCacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

public class NewsRepositoryCacheProxy extends RepositoryCacheProxy<UUID, News> implements InvocationHandler {

    private final NewsRepository newsRepository;

    public NewsRepositoryCacheProxy(NewsRepository newsRepository,
                                    AbstractCacheOperator<UUID, News> cacheOperator) {
        super(cacheOperator);

        this.newsRepository = newsRepository;
    }

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
        return cacheOperator.find(uuid)
                .or(() -> {
                    Optional<News> dbNews = newsRepository.findByUuid(uuid);
                    dbNews.ifPresent(newsDB -> cacheOperator.add(newsDB.getUuid(), newsDB));
                    return dbNews;
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected News whenSaveOrUpdate(News savingOrUpdatingNews) throws DataAccessException {
        News savedOrUpdatedNews = newsRepository.save(savingOrUpdatingNews);

        cacheOperator.add(savedOrUpdatedNews.getUuid(), savedOrUpdatedNews);
        return savedOrUpdatedNews;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void whenDelete(UUID deletingNewsUUID) throws DataAccessException {
        newsRepository.deleteByUuid(deletingNewsUUID);
        cacheOperator.delete(deletingNewsUUID);
    }

}

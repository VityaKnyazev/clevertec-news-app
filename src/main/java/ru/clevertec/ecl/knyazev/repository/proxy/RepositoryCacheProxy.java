package ru.clevertec.ecl.knyazev.repository.proxy;

import lombok.RequiredArgsConstructor;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Represents abstract cache proxy for using with standard Data JPA
 * Repository
 *
 * @param <K> cache key type
 * @param <V> cache value type
 */
@RequiredArgsConstructor
public abstract class RepositoryCacheProxy<K, V> {

    protected final AbstractCacheOperator<K, V> cacheOperator;

    /**
     * Execute proxy method depends on method name from standard Data JPA
     * repository
     *
     * @param realRepositoryInstance real repository instance
     * @param method method type
     * @param args method args
     * @return result of executed proxy method from standard Data JPA
     * repository
     * @throws Throwable if exception was thrown during execution
     */
    @SuppressWarnings("unchecked")
    protected Object executeProxyMethod(Object realRepositoryInstance, Method method, Object[] args) throws Throwable {
        RepositoryMethod repositoryMethod = RepositoryMethod.findByName(method.getName());

        return switch (repositoryMethod) {
            case findByUuid -> whenFindByUuid((K) args[0]);
            case findAll, undefined -> method.invoke(realRepositoryInstance, args);
            case save -> whenSaveOrUpdate((V) args[0]);
            case deleteByUuid -> {
                whenDelete((K) args[0]);
                yield null;
            }
        };
    }

    /**
     * Capture findByUuid method of Repository and get from cache V object
     *
     * @param cacheKey cache key
     * @return optional V object from cache or repository
     */
    abstract protected Optional<V> whenFindByUuid(K cacheKey);

    /**
     * Capture save method of Repository and Add to cache saved or updated V object
     *
     * @param cacheValue V object to add to cache and Repository
     * @return V added to cache nd repository object
     */
    abstract protected V whenSaveOrUpdate(V cacheValue);

    /**
     * Remove object with given cache key from data source and cache
     *
     * @param cacheKey cache key
     */
    abstract protected void whenDelete(K cacheKey);

    /**
     * Represents method names of standard DATA JPA Repository class
     */
    protected enum RepositoryMethod {
        findByUuid, findAll, save, deleteByUuid, undefined;

        public static RepositoryMethod findByName(String name) {

            RepositoryMethod result = null;
            for (RepositoryMethod repositoryMethod : values()) {

                if (repositoryMethod.name().equalsIgnoreCase(name)) {
                    result = repositoryMethod;
                    break;
                }

            }

            return result != null ? result : undefined;
        }
    }
}

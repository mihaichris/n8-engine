package com.example.n8engine.config;

import com.example.n8engine.dto.EntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CachingManager {

    private PersistentCacheManager persistentCacheManager;
    private Cache<String, EntityResponse> entityCache;

    public CachingManager() {
        persistentCacheManager =
                CacheManagerBuilder.newCacheManagerBuilder()
                        .with(CacheManagerBuilder.persistence("src/main/resources/database"
                                + File.separator
                                + "entity"))
                        .withCache("entity", CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(String.class, EntityResponse.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                                .heap(1000, EntryUnit.ENTRIES)
                                                .disk(5, MemoryUnit.GB, true))
                        )
                        .build(true);

       entityCache = persistentCacheManager.getCache("entity", String.class, EntityResponse.class);
    }

    public Cache<String, EntityResponse> getEntityCache() {
        return entityCache;
    }

    public void stopPersistentCacheManager() {
        persistentCacheManager.close();
    }

    public void initPersistentCacheManager() {
        persistentCacheManager.init();
    }

    public PersistentCacheManager getPersistentCacheManager() {
        return persistentCacheManager;
    }
}
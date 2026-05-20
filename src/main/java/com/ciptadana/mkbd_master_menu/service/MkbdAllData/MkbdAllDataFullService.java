package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

import com.ciptadana.mkbd_master_menu.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MkbdAllDataFullService {

    private final SqlScriptRegistry registry;
    private final SqlScriptExecutor scriptExecutor;

//    @Cacheable(value = CacheConfig.MKBD_ALL_DATA_FULL_CACHE, key = "#genDate")
    public Map<String, Object> getAllData(String genDate) {
        long start = System.currentTimeMillis();
        log.info("Fetching MKBD all-data-full for genDate={}", genDate);

        List<SqlScript> scripts = registry.getScripts();

        Map<String, CompletableFuture<List<Map<String, Object>>>> futures = new LinkedHashMap<>();
        for (SqlScript s : scripts) {
            futures.put(s.key(), scriptExecutor.runAsync(s, genDate));
        }
        CompletableFuture.allOf(futures.values().toArray(CompletableFuture[]::new)).join();

        Map<String, Object> detail = new LinkedHashMap<>();
        Map<String, Object> summary = new LinkedHashMap<>();

        for (SqlScript s : scripts) {
            List<Map<String, Object>> rows = futures.get(s.key()).join();
            Map<String, Object> bucket = "detail".equals(s.type()) ? detail : summary;

            if (s.baris() == null) {
                bucket.put(s.vd(), rows);
            } else {
                @SuppressWarnings("unchecked")
                Map<String, Object> vdMap = (Map<String, Object>) bucket.computeIfAbsent(
                        s.vd(), k -> new LinkedHashMap<>());
                vdMap.put(s.baris(), rows);
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("genDate", genDate);
        response.put("detail", detail);
        response.put("summary", summary);

        log.info("MKBD all-data-full fetched in {} ms ({} queries)",
                System.currentTimeMillis() - start, scripts.size());
        return response;
    }
}

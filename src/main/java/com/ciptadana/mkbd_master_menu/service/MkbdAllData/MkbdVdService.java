package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MkbdVdService {

    private final SqlScriptRegistry registry;
    private final SqlScriptExecutor scriptExecutor;

    public Map<String, Object> getByType(String type, String vd, String genDate) {
        long start = System.currentTimeMillis();

        List<SqlScript> matched = registry.getScripts().stream()
                .filter(s -> type.equals(s.type()))
                .filter(s -> vd.equals(s.vd()))
                .toList();

        if (matched.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No " + type + " scripts found for vd=" + vd);
        }

        List<SqlScript> ordered = matched.stream()
                .sorted(Comparator.comparing(SqlScript::leadingLine,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        Map<String, CompletableFuture<List<Map<String, Object>>>> futures = new LinkedHashMap<>();
        for (SqlScript s : ordered) {
            futures.put(s.key(), scriptExecutor.runAsync(s, genDate));
        }
        CompletableFuture.allOf(futures.values().toArray(CompletableFuture[]::new)).join();

        Map<String, Object> response = new LinkedHashMap<>();
        for (SqlScript s : ordered) {
            String key = s.baris() == null ? s.vd() : s.baris();
            response.put(key, futures.get(s.key()).join());
        }

        log.info("vd={} {} all-baris fetched in {} ms ({} queries)",
                vd, type, System.currentTimeMillis() - start, ordered.size());
        return response;
    }
}

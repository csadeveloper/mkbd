package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

import com.ciptadana.mkbd_master_menu.config.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class SqlScriptExecutor {

    private final NamedParameterJdbcTemplate jdbc;
    private final Executor executor;

    public SqlScriptExecutor(
            @Qualifier("oracleDataSource") DataSource dataSource,
            @Qualifier(AsyncConfig.MKBD_QUERY_EXECUTOR) Executor executor
    ) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.executor = executor;
    }

    public CompletableFuture<List<Map<String, Object>>> runAsync(SqlScript script, String genDate) {
        return CompletableFuture.supplyAsync(() -> run(script, genDate), executor);
    }

    public List<Map<String, Object>> run(SqlScript script, String genDate) {
        long t0 = System.currentTimeMillis();
        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                    script.sql(),
                    new MapSqlParameterSource("genDate", genDate));
            List<Map<String, Object>> camelCased = rows.stream()
                    .map(SqlScriptExecutor::toCamelCase)
                    .toList();
            log.debug("Query [{}] {} rows in {} ms",
                    script.key(), camelCased.size(), System.currentTimeMillis() - t0);
            return camelCased;
        } catch (RuntimeException e) {
            log.error("Query [{}] failed after {} ms: {}",
                    script.key(), System.currentTimeMillis() - t0, e.getMessage());
            throw e;
        }
    }

    private static Map<String, Object> toCamelCase(Map<String, Object> row) {
        Map<String, Object> out = new LinkedHashMap<>(row.size());
        for (Map.Entry<String, Object> e : row.entrySet()) {
            out.put(snakeToCamel(e.getKey()), e.getValue());
        }
        return out;
    }

    private static String snakeToCamel(String name) {
        if (name == null || name.isEmpty()) return name;
        StringBuilder sb = new StringBuilder(name.length());
        boolean upperNext = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}

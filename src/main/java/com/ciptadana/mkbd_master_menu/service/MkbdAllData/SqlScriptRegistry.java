package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SqlScriptRegistry {

    private List<SqlScript> scripts = Collections.emptyList();

    @PostConstruct
    public void load() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<SqlScript> list = new ArrayList<>();

        for (String type : new String[]{"detail", "summary"}) {
            String marker = "sql/" + type + "/";
            Resource[] resources = resolver.getResources("classpath:" + marker + "**/*.sql");

            for (Resource r : resources) {
                String url = r.getURL().toString();
                int idx = url.indexOf(marker);
                if (idx < 0) {
                    log.warn("Skipped resource (marker not found): {}", url);
                    continue;
                }
                String rel = url.substring(idx + marker.length());
                String[] parts = rel.split("/");

                String vd;
                String baris;
                if (parts.length == 1) {
                    vd = stripExt(parts[0]);
                    baris = null;
                } else if (parts.length == 2) {
                    vd = parts[0];
                    baris = stripExt(parts[1]);
                } else {
                    log.warn("Skipped resource (unexpected depth): {}", rel);
                    continue;
                }

                String sql = StreamUtils.copyToString(r.getInputStream(), StandardCharsets.UTF_8).strip();
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1).strip();
                }

                list.add(new SqlScript(type, vd, baris, sql));
            }
        }

        this.scripts = List.copyOf(list);
        log.info("Loaded {} SQL scripts ({} detail, {} summary)",
                scripts.size(),
                scripts.stream().filter(s -> "detail".equals(s.type())).count(),
                scripts.stream().filter(s -> "summary".equals(s.type())).count());
    }

    public List<SqlScript> getScripts() {
        return scripts;
    }

    private static String stripExt(String name) {
        int i = name.lastIndexOf('.');
        return i < 0 ? name : name.substring(0, i);
    }
}

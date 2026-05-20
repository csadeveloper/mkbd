package com.ciptadana.mkbd_master_menu.service.MkbdAllData;

import com.ciptadana.mkbd_master_menu.config.AsyncConfig;
import com.ciptadana.mkbd_master_menu.config.CacheConfig;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.MkbdAllDataResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.MkbdDetailData;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.MkbdSummaryData;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.Vd51DetailData;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData.Vd51SummaryData;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MkbdAllData.MkbdAllDataJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.CoaDetailRow;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.Vd51Baris30DetailRow;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.Vd51Baris30SummaryRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Slf4j
@Service
public class MkbdAllDataService {

    private final MkbdAllDataJpaRepository repository;
    private final Executor executor;

    public MkbdAllDataService(
            MkbdAllDataJpaRepository repository,
            @Qualifier(AsyncConfig.MKBD_QUERY_EXECUTOR) Executor executor
    ) {
        this.repository = repository;
        this.executor = executor;
    }

    @Cacheable(value = CacheConfig.MKBD_ALL_DATA_CACHE, key = "#genDate")
    public MkbdAllDataResponse getAllData(String genDate) {
        long start = System.currentTimeMillis();
        log.info("Fetching MKBD all-data for genDate={}", genDate);

        CompletableFuture<List<CoaDetailRow>> coaDetailF =
                runAsync(() -> repository.findCoaDetail(genDate), "coaDetail");

        CompletableFuture<List<Vd51Baris30DetailRow>> vd51B30DetailF =
                runAsync(() -> repository.findVd51Baris30Detail(genDate), "vd51Baris30Detail");

        CompletableFuture<List<Vd51Baris30SummaryRow>> vd51B30SummaryF =
                runAsync(() -> repository.findVd51Baris30Summary(genDate), "vd51Baris30Summary");

        CompletableFuture.allOf(coaDetailF, vd51B30DetailF, vd51B30SummaryF).join();
        MkbdAllDataResponse response = MkbdAllDataResponse.builder()
                .genDate(genDate)
                .detail(MkbdDetailData.builder()
                        .coa(coaDetailF.join())
                        .vd51(Vd51DetailData.builder()
                                .baris30(vd51B30DetailF.join())
                                .build())
                        .build())
                .summary(MkbdSummaryData.builder()
                        .vd51(Vd51SummaryData.builder()
                                .baris30(vd51B30SummaryF.join())
                                .build())
                        .build())
                .build();

        log.info("MKBD all-data fetched in {} ms for genDate={}",
                System.currentTimeMillis() - start, genDate);
        return response;
    }

    private <T> CompletableFuture<T> runAsync(Supplier<T> supplier, String label) {
        return CompletableFuture.supplyAsync(() -> {
            long t0 = System.currentTimeMillis();
            try {
                T result = supplier.get();
                log.debug("Query [{}] done in {} ms", label, System.currentTimeMillis() - t0);
                return result;
            } catch (RuntimeException e) {
                log.error("Query [{}] failed after {} ms: {}",
                        label, System.currentTimeMillis() - t0, e.getMessage());
                throw e;
            }
        }, executor);
    }
}

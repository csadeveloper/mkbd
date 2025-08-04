package com.ciptadana.mkbd_gen.service;

import com.ciptadana.mkbd_gen.database.oracle.repository.DataTransactionLedgerRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncDataLedger {

    private final DataTransactionLedgerRepository repository;

    public AsyncDataLedger(DataTransactionLedgerRepository repository) {
        this.repository = repository;
    }


    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerKpeiAsync(String pDate) {
        repository.callLedgerKpei(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerHutangPiutangAsync(String pDate) {
        repository.callLedgerHutangPiutang(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerOtherAccountRgAsync(String pDate) {
        repository.callLedgerOthersAccountRg(pDate);
        return CompletableFuture.completedFuture(null);
    }

//    @Async("asyncExecutor")
//    public CompletableFuture<Void> callLedgerOtherAccountNonRgAsync(String pDate) {
//        repository.callLedgerOthersAccountNonRg(pDate);
//        return CompletableFuture.completedFuture(null);
//    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerProcGeneralLedgerAsync(String pDate) {
        repository.callLedgerGeneralLedger(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerProcSuspendAsync(String pDate) {
        repository.callLedgerProcSuspend(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerProcUnsuspendAsync(String pDate) {
        repository.callLedgerProcUnSuspend(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callLedgerCustodyAvgOblAsync(String pDate) {
        repository.callLedgerCustodyAvgObl(pDate);
        return CompletableFuture.completedFuture(null);
    }

}

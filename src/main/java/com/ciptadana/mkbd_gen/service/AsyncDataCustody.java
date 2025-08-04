package com.ciptadana.mkbd_gen.service;

import com.ciptadana.mkbd_gen.database.oracle.repository.DataTransactionCustodyRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncDataCustody {

    private final DataTransactionCustodyRepository repository;

    public AsyncDataCustody(DataTransactionCustodyRepository repository) {
        this.repository = repository;
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callTrialBalanceAsync(String pDate) {
        repository.callTrialBalance(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> truncateCustodyAsync() {
        repository.truncateCustody();
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> truncateAgingRecvAsync() {
        repository.truncateAgingRecv();
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> truncateLedgerAsync() {
        repository.truncateLedger();
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callCustodyKseiAsync(String pDate) {
        repository.callCustodyKsei(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callHutangPiutangAsync(String pDate) {
        try {
            repository.callCustodyHutangPiutang(pDate);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
//    @Async("asyncExecutor")
//    public CompletableFuture<Void> callHutangPiutangAsync(String pDate) {
//        repository.callCustodyHutangPiutang(pDate);
//        return CompletableFuture.completedFuture(null);
//    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callPortfolioEfekAsync(String pDate) {
        repository.callCustodyPortfolioEfek(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callHargaPerolehan1Async(String pDate) {
        try {
            repository.callCustodyHargaPerolehan1(pDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CompletableFuture.completedFuture(null);
    }

//    @Async("asyncExecutor")
//    public CompletableFuture<Void> callHargaPerolehan2Async(String pDate) {
//        repository.callCustodyHargaPerolehan2(pDate);
//        return CompletableFuture.completedFuture(null);
//    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callRekeningEfekAsync(String pDate) {
        repository.callCustodyRekeningEfek(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callCustodianAsync(String pDate) {
        repository.callCustodyCustodian(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callOthersAsync(String pDate) {
        repository.callCustodyOthers(pDate);
        return CompletableFuture.completedFuture(null);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> callClosingPriceAsync(String pDate) {
        repository.callCustodyClosingPrice(pDate);
        return CompletableFuture.completedFuture(null);
    }
}

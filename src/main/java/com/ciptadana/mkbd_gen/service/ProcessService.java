package com.ciptadana.mkbd_gen.service;

import com.ciptadana.mkbd_gen.database.oracle.repository.DataTransactionCustodyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProcessService {

    private final AsyncDataCustody asyncDataCustody;
    private final AsyncDataLedger asyncDataLedger;

    private final DataTransactionCustodyRepository repository;

    public ProcessService(AsyncDataCustody asyncTasks, AsyncDataLedger asyncDataLedger, DataTransactionCustodyRepository repository) {
        this.asyncDataCustody = asyncTasks;
        this.asyncDataLedger = asyncDataLedger;
        this.repository = repository;
    }

    public void startPreProcessCustody(){

        CompletableFuture<Void> truncateCustodyFuture = asyncDataCustody.truncateCustodyAsync();
        CompletableFuture<Void> truncateAgingFuture = asyncDataCustody.truncateAgingRecvAsync();
        CompletableFuture<Void> truncateLedgerFuture = asyncDataCustody.truncateLedgerAsync();

        CompletableFuture<Void> allTask  = CompletableFuture.allOf(truncateCustodyFuture, truncateAgingFuture, truncateLedgerFuture);
        try {
            allTask.join();
            log.info("All pre processes successfully");
        } catch (Exception e) {
            log.error("One or more pre processes failed", e);
        }
    }

    public boolean startProcessCustody(String pDate){

        boolean result = false;
//        CompletableFuture<Void> trialFuture = asyncDataCustody.callTrialBalanceAsync(pDate);
//        CompletableFuture<Void> kseiFuture = asyncDataCustody.callCustodyKseiAsync(pDate);
//        CompletableFuture<Void> hutangPiutangFuture = asyncDataCustody.callHutangPiutangAsync(pDate);
//        CompletableFuture<Void> portfolioEfekFuture = asyncDataCustody.callPortfolioEfekAsync(pDate);
//        CompletableFuture<Void> hpp1Future = asyncDataCustody.callHargaPerolehan1Async(pDate);
////        CompletableFuture<Void> hpp2Future = asyncDataCustody.callHargaPerolehan2Async(pDate);
//        CompletableFuture<Void> rekeningEfekFuture = asyncDataCustody.callRekeningEfekAsync(pDate);
//        CompletableFuture<Void> custodianFuture = asyncDataCustody.callCustodianAsync(pDate);
//        CompletableFuture<Void> othersFuture = asyncDataCustody.callOthersAsync(pDate);
//        CompletableFuture<Void> closingPriceFuture = asyncDataCustody.callClosingPriceAsync(pDate);

        // Wait for all to complete
//        CompletableFuture<Void> allTask  = CompletableFuture.allOf(trialFuture); // , kseiFuture, hutangPiutangFuture, portfolioEfekFuture, hpp1Future, rekeningEfekFuture, custodianFuture, othersFuture, closingPriceFuture);

        try {
//            allTask.join();
            result = repository.callTrialBalance(pDate);
            if(result)
                result = repository.callCustodyKsei(pDate);
            if(result)
                result = repository.callCustodyHutangPiutang(pDate);
            if(result)
                result = repository.callCustodyPortfolioEfek(pDate);
            if(result)
                result = repository.callCustodyHargaPerolehan1(pDate);
            if(result)
                result = repository.callCustodyHargaPerolehan2(pDate);
            if(result)
                result = repository.callCustodyRekeningEfek(pDate);
            if(result)
                result = repository.callCustodyCustodian(pDate);
            if(result)
                result = repository.callCustodyOthers(pDate);
            if(result)
                result = repository.callCustodyClosingPrice(pDate);

            if(result)
                log.info("All custody processes completed successfully");
            else
                log.error("One or more custody processes failed");

        } catch (Exception e) {
            log.error("One or more custody processes failed", e);
        }

        return result;
    }

    public boolean startProcessLedger(String pDate){

        boolean result = false;
        CompletableFuture<Void> kpeiFuture = asyncDataLedger.callLedgerKpeiAsync(pDate);
        CompletableFuture<Void> lhutangPiutangFuture = asyncDataLedger.callLedgerHutangPiutangAsync(pDate);
        CompletableFuture<Void> otherAccRgFuture = asyncDataLedger.callLedgerOtherAccountRgAsync(pDate);
//        CompletableFuture<Void> otherAccNonRgFuture = asyncDataLedger.callLedgerOtherAccountNonRgAsync(pDate);
        CompletableFuture<Void> procGLFuture = asyncDataLedger.callLedgerProcGeneralLedgerAsync(pDate);
        CompletableFuture<Void> procSuspendFuture = asyncDataLedger.callLedgerProcSuspendAsync(pDate);
        CompletableFuture<Void> procUnSuspendFuture = asyncDataLedger.callLedgerProcUnsuspendAsync(pDate);
        CompletableFuture<Void> avgOblFuture = asyncDataLedger.callLedgerCustodyAvgOblAsync(pDate);

        // Wait for all to complete
        CompletableFuture<Void> allTask  = CompletableFuture.allOf(kpeiFuture, lhutangPiutangFuture, otherAccRgFuture, procGLFuture, procSuspendFuture, procUnSuspendFuture, avgOblFuture);
        try {
            allTask.join();
            result = true;
            log.info("All ledger processes completed successfully");
        } catch (Exception e) {
            log.error("One or more ledger processes failed", e);
        }

        return result;
    }

}

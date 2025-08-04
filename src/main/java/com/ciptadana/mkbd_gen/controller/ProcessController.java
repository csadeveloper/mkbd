package com.ciptadana.mkbd_gen.controller;

import com.ciptadana.mkbd_gen.service.MasterService;
import com.ciptadana.mkbd_gen.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("mkbd_suspended")
public class ProcessController {

    private final MasterService masterService;
    private final ProcessService processService;
    private final Semaphore lock = new Semaphore(1);

    @GetMapping("/process")
    public String runningProcess(
            @RequestParam("date") String date
            ){

        try {

            boolean acquire =  lock.tryAcquire();

            if(!acquire)
                return "PROCESS ALLREADY RUNNING";

            if(date.length() < 8)
                return "Please input Date for Closing MKBD";

            log.info("MKBD Pre Process ");
            processService.startPreProcessCustody();
            log.info("MKBD Process Start ");
            boolean resultCustody =  processService.startProcessCustody(date);
            boolean resultLedger = processService.startProcessLedger(date);

            if(!resultCustody)
                log.info("CUSTODY Process FAILED ");
            if(!resultLedger)
                log.info("LEDGER Process FAILED ");

            if(!resultCustody || !resultLedger){
                log.info("MKBD Process FAILED ");
                return "MKBD Process FAILED ";
            }else{
                log.info("MKBD Process COMPLETED ");
                return "MKBD Process COMPLETED ";
            }

        }catch (Exception e){
            log.error(e.getMessage());
            return ("ERROR EXECUTE MKBD : "+ e.getMessage());
        }finally {
            lock.release();
        }


    }

}

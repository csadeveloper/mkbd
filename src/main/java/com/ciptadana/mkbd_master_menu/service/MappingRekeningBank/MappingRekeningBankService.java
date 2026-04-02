package com.ciptadana.mkbd_master_menu.service.MappingRekeningBank;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MappingRekeningBank.MapBankInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MappingRekeningBank.MappingRekeningBankJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.AutomatedAccountResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.F1Response;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankAutomatedResponse;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MappingRekeningBank.MappingRekeningBankListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MappingRekeningBankService {

    private final MappingRekeningBankJpaRepository mappingRekeningBankJpaRepository;

    public List<MappingRekeningBankListResponse> getMappingRekeningBankList(String type, String nclient) {
        if(type.equals("1")){
            return mappingRekeningBankJpaRepository.findMappingRekeningBankList();
        } else if(type.equals("2")){
            return mappingRekeningBankJpaRepository.findMappingRekeningBankListWithCondition(nclient);
        }
        return Collections.emptyList();
    }

    public MappingRekeningBankAutomatedResponse getMappingRekeningBankAutomatedResponse(String code) {
        return mappingRekeningBankJpaRepository.findMappingRekeningBankAutomatedResponse(code);
    }

    public AutomatedAccountResponse getAutomatedAccountResponse(String code) {
        return mappingRekeningBankJpaRepository.findAutomatedAccountResponse(code);
    }

    public List<F1Response> getF1Response(String prefix) {
        return mappingRekeningBankJpaRepository.findF1Response(prefix);
    }

    public void deleteMapBank(String nclient) {
        mappingRekeningBankJpaRepository.deleteMapBankByNclient(nclient);
    }

    @Transactional
    public void insertMapBank(List<MapBankInsertRequest> requests) {
        for (MapBankInsertRequest request : requests) {
            mappingRekeningBankJpaRepository.insertMapBank(
                    request.getNclient(),
                    request.getBankAccountNo(),
                    request.getNotes(),
                    request.getBankName(),
                    request.getIsclient(),
                    request.getCurrency()
            );
        }
    }
}
package com.ciptadana.mkbd_master_menu.service.MasterIsinCode;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.IsinCodeUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinInsertRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MasterIsinCode.XdmMasterIsinUpdateRequest;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.MasterIsinCode.MasterIsinJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MasterIsinCode.IsinReksadanaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterIsinService {

    private final MasterIsinJpaRepository masterIsinJpaRepository;

    public List<IsinReksadanaResponse> getIsinReksadana(String type, String prefix) {
        if(type.equals("1")){
            return masterIsinJpaRepository.findIsinReksadana();
        }else if(type.equals("2")){
            return masterIsinJpaRepository.findIsinReksadanaByCondition(prefix);
        }
        return Collections.emptyList();
    }

    public void deleteIsinCode(String type, String isincode) {
        masterIsinJpaRepository.deleteIsinCode(type, isincode);
    }

    @Transactional
    public void insertIsinCode(List<IsinCodeInsertRequest> requests) {
        for (IsinCodeInsertRequest request : requests) {
            masterIsinJpaRepository.insertIsinCode(
                    request.getType(),
                    request.getShortcode(),
                    request.getIssuer(),
                    request.getName(),
                    request.getIsincode(),
                    request.getStatus()
            );
        }
    }

    public void deleteXdmMasterIsin(String seccode, String isinCode) {
        masterIsinJpaRepository.deleteXdmMasterIsin(seccode, isinCode);
    }

    public void updateIsinCode(IsinCodeUpdateRequest request) {
        masterIsinJpaRepository.updateIsinCode(
                request.getType(),
                request.getShortcode(),
                request.getIssuer(),
                request.getName(),
                request.getIsincode(),
                request.getStatus()
        );
    }

    public void updateXdmMasterIsin(XdmMasterIsinUpdateRequest request) {
        masterIsinJpaRepository.updateXdmMasterIsin(
                request.getSeccode(),
                request.getSecname(),
                request.getSectype(),
                request.getIssuer(),
                request.getRegistrar(),
                request.getIsinCode(),
                request.getIsinStatus(),
                request.getListingDate(),
                request.getNoOfSec(),
                request.getStockExch(),
                request.getStatus(),
                request.getNominal(),
                request.getSecNum(),
                request.getExpDate(),
                request.getInterest(),
                request.getIntType(),
                request.getIntFreq(),
                request.getDaycount(),
                request.getCurr(),
                request.getSecForm(),
                request.getEffIsinDate(),
                request.getMatDate(),
                request.getSecSector()
        );
    }

    @Transactional
    public void insertXdmMasterIsin(List<XdmMasterIsinInsertRequest> requests) {
        for (XdmMasterIsinInsertRequest request : requests) {
            masterIsinJpaRepository.insertXdmMasterIsin(
                    request.getSeccode(),
                    request.getSecname(),
                    request.getSectype(),
                    request.getIssuer(),
                    request.getRegistrar(),
                    request.getIsinCode(),
                    request.getIsinStatus(),
                    request.getListingDate(),
                    request.getNoOfSec(),
                    request.getStockExch(),
                    request.getStatus(),
                    request.getNominal(),
                    request.getSecNum(),
                    request.getExpDate(),
                    request.getInterest(),
                    request.getIntType(),
                    request.getIntFreq(),
                    request.getDaycount(),
                    request.getCurr(),
                    request.getSecForm(),
                    request.getEffIsinDate(),
                    request.getMatDate(),
                    request.getSecSector()
            );
        }
    }

}
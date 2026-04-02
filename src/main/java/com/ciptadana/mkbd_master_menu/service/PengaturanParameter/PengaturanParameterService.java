package com.ciptadana.mkbd_master_menu.service.PengaturanParameter;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.jpa.PengaturanParameter.PengaturanParameterJpaRepository;
import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.PengaturanParameter.ApplicationParameterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PengaturanParameterService {

    private final PengaturanParameterJpaRepository pengaturanParameterJpaRepository;

    public List<ApplicationParameterResponse> getParameterList() {
        return pengaturanParameterJpaRepository.findApplicationParameter();
    }

    public void updateParameter(String code, String value) {
        pengaturanParameterJpaRepository.updateParameter(code, value);
    }

}

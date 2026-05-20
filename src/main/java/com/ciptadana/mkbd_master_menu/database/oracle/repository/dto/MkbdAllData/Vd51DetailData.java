package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.Vd51Baris30DetailRow;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Vd51DetailData {
    private List<Vd51Baris30DetailRow> baris30;
}

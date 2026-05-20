package com.ciptadana.mkbd_master_menu.database.oracle.repository.dto.MkbdAllData;

import com.ciptadana.mkbd_master_menu.database.oracle.repository.projection.MkbdAllData.CoaDetailRow;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MkbdDetailData {
    private List<CoaDetailRow> coa;
    private Vd51DetailData vd51;
}

package nccc.btp.service;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.entity.NcccCommitteeGroup;
import nccc.btp.entity.NcccCommitteeList;
import nccc.btp.entity.NcccFinancialInstitution;
import nccc.btp.vo.CommitteeVo;

import java.util.List;

public interface NcccCommitteeListService {

    //取得列表
    List<CommitteeVo> findAll();

    //建立
    NcccCommitteeList add(CommitteeVo vo);

    //編輯
    NcccCommitteeList update(CommitteeVo vo);

    //刪除
    String delete(String id);

    // 付款方式
    List<PayMethodSelectOption> getPayMethodSelectOptions();

    //金融機構代碼
    List<NcccFinancialInstitution> getNcccFinancialInstitutionList();

    //研發委員組別
    List<NcccCommitteeGroup> getNcccCommitteeGroupList();
}

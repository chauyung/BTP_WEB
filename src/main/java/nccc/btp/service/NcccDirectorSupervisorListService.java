package nccc.btp.service;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.entity.NcccDirectorSupervisorList;
import nccc.btp.vo.DirectorSupervisorVo;

import java.util.List;

public interface NcccDirectorSupervisorListService {

    //取得列表
    List<DirectorSupervisorVo> findAll();

    //建立
    NcccDirectorSupervisorList add(DirectorSupervisorVo vo);

    //編輯
    NcccDirectorSupervisorList update(DirectorSupervisorVo vo);

    //刪除
    String delete(String id);

    List<PayMethodSelectOption> getPayMethodSelectOptions();
}

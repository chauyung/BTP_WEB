package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccDepositBank;
import nccc.btp.entity.NcccPendingRemittance;
import nccc.btp.vo.PendingRemittanceQueryVo;
import nccc.btp.vo.PendingRemittanceVo;

public interface PendingRemittanceService {

  List<NcccDepositBank> getBankNo();

  List<PendingRemittanceVo> search(PendingRemittanceQueryVo pendingRemittanceQueryVo);

  NcccPendingRemittance add(NcccPendingRemittance ncccPendingRemittance);

  NcccPendingRemittance update(NcccPendingRemittance ncccPendingRemittance);

  String delete(Long id);

  String checkout(PendingRemittanceQueryVo pendingRemittanceQueryVo);

  List<PendingRemittanceVo> toSAP(List<NcccPendingRemittance> list);
}

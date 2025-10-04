package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccAccountingList;
import nccc.btp.entity.NcccNssfvoueHeaderBatch;
import nccc.btp.entity.NcccNssfvoueRecData;
import nccc.btp.vo.ReceivablesManagementQueryVo;
import nccc.btp.vo.ReceivablesManagementVo;

public interface ReceivablesManagementService {


  List<NcccNssfvoueHeaderBatch> getNssfvoueHeaderBatch();

  List<NcccAccountingList> getNcccAccountingList();

  List<ReceivablesManagementVo> query(ReceivablesManagementQueryVo receivablesManagementQueryVo);
  
  List<NcccNssfvoueRecData> findDetailsByBatch(String nssfvoueDataBatch);

  List<ReceivablesManagementVo> findAll();
  
  List<ReceivablesManagementVo> save(ReceivablesManagementVo receivablesManagementVo);

  List<ReceivablesManagementVo> add(ReceivablesManagementVo receivablesManagementVo);
  
  List<ReceivablesManagementVo> edit(ReceivablesManagementVo receivablesManagementVo);

  String delete(List<String> nssfvoueHeaderBatchList);

  String getDocTypeByNssfvoueHeaderBatch(String nssfvoueHeaderBatch);

  String toSAP(List<ReceivablesManagementVo> receivablesManagementVoList);

  String confirmation(List<String> batchList);
  
  String confirmReturn(List<String> batchList);
  
  String reviewAndApproval(List<String> batchList);
  
  String reviewAndReturn(List<String> batchList);
  
  String importFromFtp(String fileName) throws Exception;

}

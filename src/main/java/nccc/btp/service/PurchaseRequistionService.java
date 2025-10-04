package nccc.btp.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import nccc.btp.dto.CategoryOptionDto;
import nccc.btp.dto.PurchaseRequestDto;
import nccc.btp.entity.NcccCostCenter;

public interface PurchaseRequistionService {

  void createAndStartProcess(PurchaseRequestDto dto, List<MultipartFile> attachment,
      List<MultipartFile> quotationFile);

  // ← 新增
  /**
   * 取得所有的預算部門 (成本中心) 列表
   */
  List<NcccCostCenter> getCostCenters();
  
  List<CategoryOptionDto> listCategoryOptions();

  String startProcess(PurchaseRequestDto dto);

  PurchaseRequestDto query(String prNo);
  
  

}

package nccc.btp.service;

import nccc.btp.response.ApiResponse;
import nccc.btp.vo.RemittanceVo;

public interface AmluploadApiService {

  ApiResponse<Void> uploadCathaybankFile(RemittanceVo remittanceVo);

  ApiResponse<Void> uploadFirstbankFile(RemittanceVo remittanceVo);

  boolean uploadFile(String fileName, String data);
}

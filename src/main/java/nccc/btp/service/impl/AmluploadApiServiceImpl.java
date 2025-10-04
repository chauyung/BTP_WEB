package nccc.btp.service.impl;

import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import nccc.btp.entity.CathaybankRemittance;
import nccc.btp.entity.FirstbankRemittance;
import nccc.btp.ftp.FtpsUtil;
import nccc.btp.repository.CathaybankRemittanceRepository;
import nccc.btp.repository.FirstbankRemittanceRepository;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.AmluploadApiService;
import nccc.btp.vo.RemittanceVo;

@Service
public class AmluploadApiServiceImpl implements AmluploadApiService {

  protected static Logger LOG = LoggerFactory.getLogger(AmluploadApiServiceImpl.class);

  @Autowired
  private CathaybankRemittanceRepository cathaybankRemittanceRepository;

  @Autowired
  private FirstbankRemittanceRepository firstbankRemittanceRepository;

  @Autowired
  @Qualifier("sap2assFtpsUtil")
  private FtpsUtil ftpsUtil;

  @Override
  public ApiResponse<Void> uploadCathaybankFile(RemittanceVo remittanceVo) {
    CathaybankRemittance cathaybankRemittance =
        cathaybankRemittanceRepository.findByIdLaufdAndIdLaufiAndIdSndDate(remittanceVo.getLAUFD(),
            remittanceVo.getLAUFI(), remittanceVo.getSND_DATE());
    if (cathaybankRemittance == null) {
      return ApiResponse.error("data not found");
    }
    String fileName = "SAPDTA01" + cathaybankRemittance.getId().getSndDate().substring(4,
        cathaybankRemittance.getId().getSndDate().length()) + ".txt";
    if (uploadFile(fileName, cathaybankRemittance.getData())) {
      return ApiResponse.ok("ok");
    } else {
      return ApiResponse.error("error");
    }
  }

  @Override
  public ApiResponse<Void> uploadFirstbankFile(RemittanceVo remittanceVo) {
    FirstbankRemittance firstbankRemittance =
        firstbankRemittanceRepository.findByIdLaufdAndIdLaufiAndIdSndDate(remittanceVo.getLAUFD(),
            remittanceVo.getLAUFI(), remittanceVo.getSND_DATE());
    if (firstbankRemittance == null) {
      return ApiResponse.error("data not found");
    }
    String fileName = "SAPDTA02" + firstbankRemittance.getId().getSndDate().substring(4,
        firstbankRemittance.getId().getSndDate().length()) + ".txt";
    if (uploadFile(fileName, firstbankRemittance.getData())) {
      return ApiResponse.ok("ok");
    } else {
      return ApiResponse.error("error");
    }
  }

  @Override
  public boolean uploadFile(String fileName, String data) {
    byte[] bytes = data.getBytes(Charset.forName("Big5"));
    String remoteFullName = "/R6/EDS2NCC/" + fileName;
    try {
      return ftpsUtil.uploadFile(remoteFullName, bytes);
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(e.getMessage());
    }
    return false;
  }

}

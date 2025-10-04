package nccc.btp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.response.ApiResponse;
import nccc.btp.service.AmluploadApiService;
import nccc.btp.vo.RemittanceVo;

@RestController
@RequestMapping("/amlupload/api")
public class AmluploadApiController {

  protected static Logger LOG = LoggerFactory.getLogger(AmluploadApiController.class);

  @Autowired
  private AmluploadApiService amluploadApiService;

  @PostMapping("/remittance")
  public ApiResponse<Void> remittance(@RequestBody RemittanceVo remittanceVo) {
    if (remittanceVo.getFileName().equals("cathaybank")) {
      return amluploadApiService.uploadCathaybankFile(remittanceVo);
    } else if (remittanceVo.getFileName().equals("firstbank")) {
      return amluploadApiService.uploadFirstbankFile(remittanceVo);
    } else {
      throw new IllegalArgumentException(
          "Unsupported remittance fileName: " + remittanceVo.getFileName());
    }
  }

}

package nccc.btp.controller;

import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.entity.NcccCommitteeGroup;
import nccc.btp.entity.NcccCommitteeList;
import nccc.btp.entity.NcccFinancialInstitution;
import nccc.btp.service.NcccCommitteeListService;
import nccc.btp.vo.CommitteeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ncccCommitteeList")
public class NcccCommitteeListController {

    protected static Logger LOG = LoggerFactory.getLogger(NcccCommitteeListController.class);

    @Autowired
    private NcccCommitteeListService ncccCommitteeListService;

    @GetMapping("/findAll")
    public ResponseEntity<List<CommitteeVo>> findAll() {
        return ResponseEntity.ok(ncccCommitteeListService.findAll());
    }

    @PostMapping("/addNcccCommitteeList")
    public ResponseEntity<NcccCommitteeList> addNcccCommitteeList(
            @RequestBody CommitteeVo vo) {
        return ResponseEntity.ok(ncccCommitteeListService.add(vo));
    }

    @PostMapping("/updateNcccCommitteeList")
    public ResponseEntity<NcccCommitteeList> updateNcccCommitteeList(
            @RequestBody CommitteeVo vo) {
        return ResponseEntity.ok(ncccCommitteeListService.update(vo));
    }

    @PostMapping("/deleteNcccCommitteeList")
    public ResponseEntity<String> deleteNcccCommitteeList(
            @RequestBody CommitteeVo vo) {
        try {
            String message = ncccCommitteeListService.delete(vo.getId());
            return ResponseEntity.ok(message); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /*
     * 取得付款方式資料
     */
    @GetMapping("/getPayMethodSelectOptions")
    public List<PayMethodSelectOption> getPayMethodSelectOptions() {

        return ncccCommitteeListService.getPayMethodSelectOptions();

    }

    /*
     * 取得金融機構代碼資料
     */
    @GetMapping("/getNcccFinancialInstitutionList")
    public List<NcccFinancialInstitution> getNcccFinancialInstitutionList() {

        return ncccCommitteeListService.getNcccFinancialInstitutionList();

    }

    /*
     * 取得研發委員組別資料
     */
    @GetMapping("/getNcccCommitteeGroupList")
    public List<NcccCommitteeGroup> getNcccCommitteeGroupList() {

        return ncccCommitteeListService.getNcccCommitteeGroupList();

    }
}

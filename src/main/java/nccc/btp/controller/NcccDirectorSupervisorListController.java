package nccc.btp.controller;

import nccc.btp.entity.NcccDirectorSupervisorList;
import nccc.btp.vo.DirectorSupervisorVo;
import nccc.btp.dto.PayMethodSelectOption;
import nccc.btp.service.NcccDirectorSupervisorListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ncccDirectorSupervisorList")
public class NcccDirectorSupervisorListController {

    protected static Logger LOG = LoggerFactory.getLogger(NcccDirectorSupervisorListController.class);

    @Autowired
    private NcccDirectorSupervisorListService ncccDirectorSupervisorListService;


    @GetMapping("/findAll")
    public ResponseEntity<List<DirectorSupervisorVo>> findAll() {
        return ResponseEntity.ok(ncccDirectorSupervisorListService.findAll());
    }

    @PostMapping("/addNcccDirectorSupervisorList")
    public ResponseEntity<NcccDirectorSupervisorList> addNcccDirectorSupervisorList(
            @RequestBody DirectorSupervisorVo vo) {
        return ResponseEntity.ok(ncccDirectorSupervisorListService.add(vo));
    }

    @PostMapping("/updateNcccDirectorSupervisorList")
    public ResponseEntity<NcccDirectorSupervisorList> updateNcccDirectorSupervisorList(
            @RequestBody DirectorSupervisorVo vo) {
        return ResponseEntity.ok(ncccDirectorSupervisorListService.update(vo));
    }

    @PostMapping("/deleteNcccDirectorSupervisorList")
    public ResponseEntity<String> deleteNcccDirectorSupervisorList(
            @RequestBody DirectorSupervisorVo vo) {
        try {
            String message = ncccDirectorSupervisorListService.delete(vo.getId());
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

        return ncccDirectorSupervisorListService.getPayMethodSelectOptions();

    }

}

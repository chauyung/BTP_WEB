package nccc.btp.controller;

import java.util.List;

import nccc.btp.entity.NcccUtilityBudgetAttribution;
import nccc.btp.service.NcccUtilityBudgetAttributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nccc.btp.entity.NcccUtilityBudgetAttributionB;
import nccc.btp.service.NcccUtilityBudgetAttributionBService;

@RestController
@RequestMapping("/ncccUtilityBudgetAttributionB")
public class NcccUtilityBudgetAttributionBController {

    protected static Logger LOG = LoggerFactory.getLogger(NcccUtilityBudgetAttributionBController.class);

    @Autowired
    private NcccUtilityBudgetAttributionBService ncccUtilityBudgetAttributionBService;

    @GetMapping("/findAll")
    public ResponseEntity<List<NcccUtilityBudgetAttributionB>> findAll() {
        return ResponseEntity.ok(ncccUtilityBudgetAttributionBService.findAll());
    }

    @PostMapping("/addNcccUtilityBudgetAttributionB")
    public ResponseEntity<NcccUtilityBudgetAttributionB> addNcccHtcMobile(@RequestBody NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB) {
        return ResponseEntity.ok(ncccUtilityBudgetAttributionBService.add(ncccUtilityBudgetAttributionB));
    }

    @PostMapping("/updateNcccUtilityBudgetAttributionB")
    public ResponseEntity<NcccUtilityBudgetAttributionB> updateNcccHtcMobile(@RequestBody NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB) {
        return ResponseEntity.ok(ncccUtilityBudgetAttributionBService.update(ncccUtilityBudgetAttributionB));
    }

    @PostMapping("/deleteNcccUtilityBudgetAttributionB")
    public ResponseEntity<String> deleteNcccHtcMobile(@RequestBody NcccUtilityBudgetAttributionB ncccUtilityBudgetAttributionB) {
        try {
            String message = ncccUtilityBudgetAttributionBService.delete(ncccUtilityBudgetAttributionB.getOuCode());
            return ResponseEntity.ok(message); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

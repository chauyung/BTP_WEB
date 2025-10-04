package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

public interface BpmRevMCRepository extends JpaRepository<BpmRevMC, Long> {

    /*
     * 取得驗收單憑證資料集
     */
    List<BpmRevMC> findByRevNo(String RevNo);

    /*
     * 取得驗收單附件資料集
     */
    List<BpmRevMC> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

}

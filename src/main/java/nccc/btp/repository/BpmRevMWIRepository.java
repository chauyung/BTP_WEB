package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

public interface BpmRevMWIRepository extends JpaRepository<BpmRevMWI, Long> {

    /*
     * 取得驗收單代扣繳資料
     */
    List<BpmRevMWI> findByRevNo(String RevNo);

    /*
     * 取得驗收單代扣繳資料
     */
    List<BpmRevMWI> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

}

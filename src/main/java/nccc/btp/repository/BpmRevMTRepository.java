package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

/**
 * 驗收單任務倉儲
 */
public interface BpmRevMTRepository extends JpaRepository<BpmRevMT, Long> {

    /*
     * 取得驗收單任務資料
     */
    List<BpmRevMT> findByRevNo(String RevNo);

    /*
     * 取得驗收單任務資料
     */
    BpmRevMT findFirstByRevNo(String RevNo);
    
}

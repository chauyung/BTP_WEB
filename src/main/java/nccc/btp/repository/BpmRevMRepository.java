package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;

/**
 * 驗收單主檔倉儲
 */
public interface BpmRevMRepository extends JpaRepository<BpmRevM, String> {

    /**
     * 取得驗收單主檔資料
     * @param RevNo 驗收單單號
     */
    BpmRevM findFirstByRevNo(String RevNo);
    
}

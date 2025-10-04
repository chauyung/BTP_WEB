package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

/*
 * 驗收單固定資產倉儲
 */
public interface BpmRevMFARepository extends JpaRepository<BpmRevMFA, Long> {

    /*
     * 取得驗收單明細分期資料集
     */
    List<BpmRevMFA> findByRevNo(String RevNo);

    /*
     * 取得驗收單明細分期資料集
     */
    List<BpmRevMFA> findBydId(Long DId);

    /*
     * 取得驗收單固定資產資料
     */
    List<BpmRevMFA> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

}

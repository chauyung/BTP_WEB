package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

public interface BpmRevMARRepository extends JpaRepository<BpmRevMAR, Long> {

    /**
     * 取得驗收單明細分期資料集
     * @param RevNo 驗收單單號
     */
    List<BpmRevMAR> findByRevNo(String RevNo);

    /**
     * 取得驗收單明細分期資料集
     * @param DId 驗收明細主鍵
     */
    List<BpmRevMAR> findBydId(Long DId);

}

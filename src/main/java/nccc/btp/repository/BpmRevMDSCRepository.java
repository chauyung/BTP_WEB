package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

/*
 * 驗收單明細分期倉儲
 */
public interface BpmRevMDSCRepository extends JpaRepository<BpmRevMDSC, Long> {

  /*
   * 取得驗收單明細分期資料集
   */
  List<BpmRevMDSC> findByRevNo(String RevNo);

  /*
   * 取得驗收單明細分期資料集
   */
  List<BpmRevMDSC> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

  /*
   * 取得驗收單明細分期資料集
   * 
   * @param DId 驗收明細主鍵
   */
  List<BpmRevMDSC> findBydId(Long DId);

}

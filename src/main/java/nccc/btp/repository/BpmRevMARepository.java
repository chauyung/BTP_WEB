package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

public interface BpmRevMARepository extends JpaRepository<BpmRevMA, Long> {

    /*
     * 取得驗收單附件資料集
     */
    List<BpmRevMA> findByRevNo(String RevNo);

    /*
     * 取得驗收單附件資料集
     */
    List<BpmRevMA> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

}

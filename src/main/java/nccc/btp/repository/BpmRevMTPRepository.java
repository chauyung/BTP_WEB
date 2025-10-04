package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.*;
import java.util.*;

public interface BpmRevMTPRepository extends JpaRepository<BpmRevMTP, Long> {

    /**
     * 取得驗收單所得人資料
     * 
     * @param RevNo 驗收單單號
     */
    List<BpmRevMTP> findByRevNo(String RevNo);

    /*
     * 取得驗收單所得人資料
     */
    List<BpmRevMTP> findByRevNoAndHandleIdentificationKey(String RevNo, String HandleIdentificationKey);

}

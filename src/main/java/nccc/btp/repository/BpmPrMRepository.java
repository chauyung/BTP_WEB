package nccc.btp.repository;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmPrM;

public interface BpmPrMRepository extends JpaRepository<BpmPrM, String> {

  /**
   * 針對同一天（SUBSTRING(pr_no,3,4) = :prefix）， 依 prNo DESC 排序，只取第一筆，並加悲觀鎖 FOR UPDATE
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT b FROM BpmPrM b " + "WHERE SUBSTRING(b.prNo, 3, 4) = :prefix "
      + "ORDER BY b.prNo DESC")
  List<BpmPrM> findTopOneByPrefixForUpdate(@Param("prefix") String prefix, Pageable pageable);

}

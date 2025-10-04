package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.entity.LevAgent;

public interface LevAgentRepository extends JpaRepository<LevAgent, String> {

  List<LevAgent> findAllByEmpIdAndEformStatusCodAndLevTypeCodInAndBtStatusIsNullAndBtNoIsNull(
      String empId, String eformStatusCod, List<String> levTypeCods);

  @Modifying
  @Transactional
  @Query("UPDATE LevAgent l SET l.btNo = :btNo, l.btStatus = :btStatus WHERE l.leApplyNo = :leApplyNo")
  int updateBtNoAndStatus(@Param("leApplyNo") String leApplyNo, @Param("btNo") String btNo,
      @Param("btStatus") String btStatus);

  @Modifying
  @Transactional
  @Query("UPDATE LevAgent l SET l.btStatus = :btStatus WHERE l.leApplyNo = :leApplyNo")
  int updateBtStatusByLeApplyNo(@Param("leApplyNo") String leApplyNo,
      @Param("btStatus") String btStatus);


}

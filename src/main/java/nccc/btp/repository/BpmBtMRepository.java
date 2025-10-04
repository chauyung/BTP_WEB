package nccc.btp.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.BpmBtM;

public interface BpmBtMRepository extends JpaRepository<BpmBtM, String> {

  /**
   * 取當月（yyMM）最大的 4 碼流水號
   */
  @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(b.btNo, 7, 4) AS int)), 0) FROM BpmBtM b WHERE SUBSTRING(b.btNo, 3, 4) = :prefix")
  Integer findMaxSerialByPrefix(@Param("prefix") String prefix);

  @Modifying
  @Query("UPDATE BpmBtM b SET b.tripMember = :tripMember,b.postingDate = :postingDate,b.itemText = :itemText, "
      + " b.applyAmount = :applyAmount,b.tax = :tax, b.untaxAmount = :untaxAmount WHERE b.btNo = :btNo")
  int updateFields(@Param("btNo") String btNo, @Param("tripMember") String tripMember,
      @Param("postingDate") LocalDate postingDate, @Param("itemText") String itemText,
      @Param("applyAmount") BigDecimal applyAmount, @Param("tax") BigDecimal tax,
      @Param("untaxAmount") BigDecimal untaxAmount);

  BpmBtM findByTaskId(String taskId);
}

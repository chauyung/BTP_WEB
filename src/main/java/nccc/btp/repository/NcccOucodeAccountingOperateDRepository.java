package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nccc.btp.entity.NcccOucodeAccountingOperateD;

@Repository
public interface NcccOucodeAccountingOperateDRepository extends JpaRepository<NcccOucodeAccountingOperateD, Long> {

  // 精確比對：年+部門+科目
  @Query(value =
      "SELECT * " +
      "FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D " +
      "WHERE YEAR = :year AND OUCODE = :ou AND ACCOUNTING = :acc",
      nativeQuery = true)
  List<NcccOucodeAccountingOperateD> findByYearOuAcc(
      @Param("year") String year,
      @Param("ou") String ou,
      @Param("acc") String acc);

  // 若要保護 Oracle 空字串，或只抓 5101 且排除空白，可用此替代法
  @Query(value =
      "SELECT * " +
      "FROM NCCC_OUCODE_ACCOUNTING_OPERATE_D " +
      "WHERE YEAR = :year " +
      "  AND OUCODE = :ou " +
      "  AND SUBSTR(ACCOUNTING,1,4) = '5101' " +
      "  AND NVL(LENGTH(TRIM(ACCOUNTING)),0) > 0",
      nativeQuery = true)
  List<NcccOucodeAccountingOperateD> findPersonnelOps5101(
      @Param("year") String year,
      @Param("ou") String ou);
}

package nccc.btp.repository;

import nccc.btp.entity.NcccBudgetReserveM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface NcccBudgetReserveRepository extends JpaRepository<NcccBudgetReserveM, String> {

    interface ReserveAgg {
        String getYear();
        String getOuCode();
        String getAccounting();
        BigDecimal getAmt();
    }

    @Query("select m.year as year, " +
           "       m.applyOuCode as ouCode, " +
           "       d.accounting as accounting, " +
           "       sum(coalesce(d.reserverAmt,0)) as amt " +
           "from NcccBudgetReserveM m, NcccBudgetReserveD d " +
           "where d.rsvNo = m.rsvNo " +
           "  and m.year = :yy " +
           "  and (:ouEmpty = true or upper(m.applyOuCode) in :ou) " +
           "  and (m.flowStatus is null or m.flowStatus <> '2') " +
           "group by m.year, m.applyOuCode, d.accounting")
    List<ReserveAgg> findReserveAgg(@Param("yy") String yy,
                                    @Param("ou") List<String> ou,
                                    @Param("ouEmpty") boolean ouEmpty);

    interface ReqInfoAgg {
        String getYear();
        String getOuCode();
        String getAccounting();
        String getReqNo();
        String getPurpose();
        String getHandler();
    }

    @Query("select m.year as year, " +
           "       m.applyOuCode as ouCode, " +
           "       d.accounting as accounting, " +
           "       min(coalesce(d.prNo, coalesce(d.poNo, m.rsvNo))) as reqNo, " +
           "       max(coalesce(d.resReason, coalesce(d.poRemark, m.remark))) as purpose, " +
           "       max(coalesce(m.applyUser, '')) as handler " +
           "from NcccBudgetReserveM m, NcccBudgetReserveD d " +
           "where d.rsvNo = m.rsvNo " +
           "  and m.year = :yy " +
           "  and (:ouEmpty = true or upper(m.applyOuCode) in :ou) " +
           "  and (m.flowStatus is null or m.flowStatus <> '2') " +
           "group by m.year, m.applyOuCode, d.accounting")
    List<ReqInfoAgg> findReqInfoAgg(@Param("yy") String yy,
                                    @Param("ou") List<String> ou,
                                    @Param("ouEmpty") boolean ouEmpty);

    interface PoPickAgg {
        String getYear();
        String getOuCode();
        String getAccounting();
        String getPoNo();
        String getBargainDocNo();
    }

    @Query("select m.year as year, " +
           "       m.applyOuCode as ouCode, " +
           "       d.accounting as accounting, " +
           "       max(coalesce(d.poNo, '')) as poNo, " +
           "       max(coalesce(d.poDocNo, coalesce(d.prDocNo, ''))) as bargainDocNo " +
           "from NcccBudgetReserveM m, NcccBudgetReserveD d " +
           "where d.rsvNo = m.rsvNo " +
           "  and m.year = :yy " +
           "  and (:ouEmpty = true or upper(m.applyOuCode) in :ou) " +
           "  and (m.flowStatus is null or m.flowStatus <> '2') " +
           "group by m.year, m.applyOuCode, d.accounting")
    List<PoPickAgg> findPoPickAgg(@Param("yy") String yy,
                                  @Param("ou") List<String> ou,
                                  @Param("ouEmpty") boolean ouEmpty);
}

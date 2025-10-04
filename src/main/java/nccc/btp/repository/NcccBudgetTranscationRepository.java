package nccc.btp.repository;

import nccc.btp.entity.NcccBudgetTranscation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface NcccBudgetTranscationRepository extends JpaRepository<NcccBudgetTranscation, Long> {

    interface ReserveAgg {
        String getYear();
        String getOuCode();
        String getAccounting();
        BigDecimal getAmt();
    }
    @Query("select t.year as year, t.ouCode as ouCode, t.accounting as accounting, sum(t.amount) as amt " +
           "from NcccBudgetTranscation t " +
           "where t.transcationType = :ty " +
           "and (:yy is null or t.year = :yy) " +
           "and (:ouEmpty = true or t.ouCode in :ou) " +
           "group by t.year, t.ouCode, t.accounting")
    List<ReserveAgg> findReserveAgg(@Param("ty") String type,
                                    @Param("yy") String year,
                                    @Param("ou") List<String> ou,
                                    @Param("ouEmpty") boolean ouEmpty);


    
    interface TxRow {
        LocalDate getTranscationDate();
        String getTranscationNo();
        String getTranscationType();
        java.math.BigDecimal getAmount();
        String getDocNo();
        String getTranscationSource();
        String getBpNo();
        String getBpName();
        String getOuCode();
        String getAccCode();
    }

    @Query("select " +
            " t.transcationDate as transcationDate, " +
            " t.transcationNo as transcationNo, " +
            " t.transcationType as transcationType, " +
            " coalesce(t.amount,0) as amount, " +
            " coalesce(t.docNo,'') as docNo, " +
            " coalesce(t.transcationSource,'') as transcationSource, " +
            " coalesce(t.bpNo,'') as bpNo, " +
            " coalesce(t.bpName,'') as bpName, " +
            " t.ouCode as ouCode, " +
            " t.accounting as accCode " +
            "from NcccBudgetTranscation t " +
            "where t.transcationDate between ?1 and ?2 " +
            "  and (?3 = 1 or t.ouCode in ?4) " +
            "  and t.accounting = ?5 " +
            "order by t.transcationDate, t.transcationNo")
    List<TxRow> findTxByDateOuAndAcc(LocalDate dateStart,
                                     LocalDate dateEnd,
                                     int allowAll,
                                     List<String> ouCodes,
                                     String accCode);

    List<NcccBudgetTranscation> findAll(Specification<NcccBudgetTranscation> spec);
}

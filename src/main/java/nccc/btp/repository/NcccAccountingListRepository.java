package nccc.btp.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nccc.btp.entity.NcccAccountingList;
import java.util.*;
import java.util.Collection;

public interface NcccAccountingListRepository extends JpaRepository<NcccAccountingList, String> {

    /*
     * 取得會計科目清單
     */
    List<NcccAccountingList> findBySubjectIn(Collection<String> Subjects);

    /**
     * 根據科目名稱查找會計科目
     * @param accounting 科目名稱
     * @return Optional<NcccAccountingList>
     */
    @Query("SELECT a FROM NcccAccountingList a WHERE a.subject = ?1")
    NcccAccountingList findBySubject(String accounting);
    
    /**
     * 只取科目代號與名稱，用於資產保留總表
     */
    interface AccName {
        String getSubject();
        String getName();
    }

    @Query("select a.subject as subject, coalesce(a.accountLong, a.essay) as name " +
           "from NcccAccountingList a where a.subject in :subjects")
    List<AccName> findNames(@Param("subjects") Collection<String> subjects);

    @Query("select l.subject as code, " +
            "coalesce(cast(l.accountLong as string), coalesce(cast(l.essay as string), '')) as name " +
            "from NcccAccountingList l " +
            "where l.subject in :codes")
    List<AccName> findNamesBySubjectIn(@Param("codes") Collection<String> codes);

    @Query("SELECT a FROM NcccAccountingList a WHERE a.budget = 'Y'")
    List<NcccAccountingList> findByBudget();

}

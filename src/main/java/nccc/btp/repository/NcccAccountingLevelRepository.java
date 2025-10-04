package nccc.btp.repository;

import nccc.btp.entity.NcccAccountingLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface NcccAccountingLevelRepository extends Repository<NcccAccountingLevel, String> {

    interface AccName {
        String getCode();
        String getName();
    }

    @Query("select trim(a.accountingLevelCode) as code, " +
           "       a.accountingLevelName as name " +
           "from NcccAccountingLevel a " +
           "where trim(a.accountingLevelCode) in :codes")
    List<AccName> findLevelNames(@Param("codes") Collection<String> codes);
    
    @Query("SELECT trim(a.accountingLevelCode) AS code, a.accountingLevelName AS name FROM NcccAccountingLevel a")
    List<AccName> findLevelNames();
}

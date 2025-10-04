package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccApportionmentRuleD;

public interface NcccApportionmentRuleDRepository extends JpaRepository<NcccApportionmentRuleD, NcccApportionmentRuleD.ConfigId> {

	@Query(value =
	        "SELECT * FROM NCCC_APPORTIONMENT_RULE_D " +
	        " WHERE YEAR=:yy AND MONTH=:mm AND ACCOUNTING=:acc " +
	        " ORDER BY OU_CODE",
	        nativeQuery = true)
	    List<NcccApportionmentRuleD> findByYearAndMonthAndAccounting(@Param("yy") String yy,
	                                                                 @Param("mm") String mm,
	                                                                 @Param("acc") String acc);

    @Modifying
    @Query("DELETE FROM NcccApportionmentRuleD d WHERE d.year = :year AND d.month = :month AND d.accounting = :accounting")
    void deleteByYearAndMonthAndAccounting(
            @Param("year") String year,@Param("month") String month,@Param("accounting") String accounting);
    
    @Query(value=
    	    "SELECT * FROM NCCC_APPORTIONMENT_RULE_D " +
    	    "WHERE YEAR=:year AND MONTH=:mm AND ACCOUNTING=:acc", nativeQuery=true)
    	  List<NcccApportionmentRuleD> findD(@Param("year") String year, @Param("mm") String mm, @Param("acc") String acc);

    
}

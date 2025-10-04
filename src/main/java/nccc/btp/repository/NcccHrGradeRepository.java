package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.NcccHrGradeM;

public interface NcccHrGradeRepository extends JpaRepository<NcccHrGradeM, String> {
	
	List<NcccHrGradeM> findAll();

	@Query("SELECT n from NcccHrGradeM n WHERE n.year = :year and n.version = :version and n.accounting = :accounting")
	List<NcccHrGradeM> findByYearAndVersionAndAccounting(@Param("year") String year,
			@Param("version") String version, @Param("accounting") String accounting);

}

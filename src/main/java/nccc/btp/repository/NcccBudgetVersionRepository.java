package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccBudgetVersion;

public interface NcccBudgetVersionRepository extends JpaRepository<NcccBudgetVersion, NcccBudgetVersion.ConfigId> {

    @Query("SELECT n FROM NcccBudgetVersion n WHERE n.id.year=:year")
    List<NcccBudgetVersion> findAllByYear(@Param("year") String year);

    @Query("SELECT n FROM NcccBudgetVersion n WHERE n.id.year=:year AND n.id.version=:version")
    NcccBudgetVersion findByYearAndVersion(@Param("year") String year, @Param("version") String version);

    boolean existsByYearAndVersion(String targetYear, String targetVersion);
}

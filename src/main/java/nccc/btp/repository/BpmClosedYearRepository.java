package nccc.btp.repository;

import nccc.btp.entity.BpmClosedYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BpmClosedYearRepository extends JpaRepository<BpmClosedYear,Long>{

    BpmClosedYear findByYear(String year);
}

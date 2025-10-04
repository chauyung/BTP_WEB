package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.NcccIncomePerson;

public interface NcccIncomePersonRepository extends JpaRepository<NcccIncomePerson, Long> {


    NcccIncomePerson findByIdAndName(String id,String name);

    NcccIncomePerson findById(String id);
}

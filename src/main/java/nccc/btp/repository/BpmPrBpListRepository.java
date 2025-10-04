package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.BpmPrBpList;

public interface BpmPrBpListRepository extends JpaRepository<BpmPrBpList, Integer> {

  List<BpmPrBpList> findByPrNo(String prNo);

  List<BpmPrBpList> findByPrNoAndPrItemNo(String prNo, String prItemNo);

}

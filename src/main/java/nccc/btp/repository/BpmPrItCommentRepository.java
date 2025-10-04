package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.BpmPrItComment;

@Repository
public interface BpmPrItCommentRepository extends JpaRepository<BpmPrItComment, String> {

  BpmPrItComment findByPrNo(String prNo);

}

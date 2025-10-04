package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.SysFun;

@Repository
public interface SysFunRepository extends JpaRepository<SysFun, String> {

  SysFun findByUri(String uri);

}

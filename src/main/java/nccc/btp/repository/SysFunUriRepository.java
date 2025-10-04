package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.SysFunUri;

@Repository
public interface SysFunUriRepository extends JpaRepository<SysFunUri, String> {

  // 只查 AP_LOG = 'Y'
  @Query("SELECT a FROM SysFunUri a WHERE a.apLog = 'Y'")
  List<SysFunUri> findAllApLogUri();

  SysFunUri findByUri(String uri);

}

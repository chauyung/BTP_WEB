package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.CathaybankRemittance;
import nccc.btp.entity.RemittanceId;

public interface CathaybankRemittanceRepository
    extends JpaRepository<CathaybankRemittance, RemittanceId> {

  CathaybankRemittance findByIdLaufdAndIdLaufiAndIdSndDate(String laufd, String laufi,
      String sndDate);
}

package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.FirstbankRemittance;
import nccc.btp.entity.RemittanceId;

public interface FirstbankRemittanceRepository
    extends JpaRepository<FirstbankRemittance, RemittanceId> {

  FirstbankRemittance findByIdLaufdAndIdLaufiAndIdSndDate(String laufd, String laufi,
      String sndDate);
}

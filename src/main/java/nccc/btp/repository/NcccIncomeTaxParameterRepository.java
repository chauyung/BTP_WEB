package nccc.btp.repository;

import nccc.btp.entity.NcccIncomeTaxParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NcccIncomeTaxParameterRepository extends JpaRepository<NcccIncomeTaxParameter, String> {

    /*
     * 最新年度所得稅參數資料
     */
    NcccIncomeTaxParameter findFirstByOrderByReportingYearDesc();

}

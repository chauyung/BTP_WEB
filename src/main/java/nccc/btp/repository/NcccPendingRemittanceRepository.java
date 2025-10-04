package nccc.btp.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.NcccPendingRemittance;

public interface NcccPendingRemittanceRepository extends JpaRepository<NcccPendingRemittance, Long>,
    JpaSpecificationExecutor<NcccPendingRemittance> {

  @Query("SELECT MAX(n.id) FROM NcccPendingRemittance n WHERE str(n.id) LIKE :prefix")
  Long findMaxIdLike(@Param("prefix") String prefix);

  @Modifying
  @Query("UPDATE NcccPendingRemittance p SET p.checkoutDate = :date WHERE p.id IN :ids")
  int updateCheckoutDateByIds(@Param("date") String date, @Param("ids") List<Long> ids);

  @Modifying
  @Query("UPDATE NcccPendingRemittance p SET p.description = :description , p.descriptionUser = :descriptionUser , p.descriptionDate = :descriptionDate ,p.descriptionOuCode = :descriptionOuCode WHERE p.id = :id")
  int updateDescriptionById(@Param("description") String description,
      @Param("descriptionUser") String descriptionUser,
      @Param("descriptionDate") LocalDate descriptionDate,
      @Param("descriptionOuCode") String descriptionOuCode, @Param("id") Long id);

  long countByIdIn(List<Long> ids);

  @EntityGraph(attributePaths = "sapPendingRemittanceStatus")
  List<NcccPendingRemittance> findAll(Specification<NcccPendingRemittance> spec);

}

package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.ZmmtSupplier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZmmtSupplierRepository extends JpaRepository<ZmmtSupplier, String> {

    @Query("SELECT n FROM ZmmtSupplier n WHERE n.partner IN :batches")
    List<ZmmtSupplier>  GetByPartnerList(@Param("batches") List<String> batches);
}

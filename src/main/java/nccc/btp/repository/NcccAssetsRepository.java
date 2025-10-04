package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAssets;

public interface NcccAssetsRepository extends JpaRepository<NcccAssets, String> {

    interface AssetOU {
        String getAssetsCode();
        String getOuCode();
    }

    @Query(value = "SELECT a.ASSETS_CODE AS assetsCode, a.OUCODE AS ouCode " +
                   "  FROM NCCC_ASSETS a " +
                   " WHERE a.ASSETS_CODE = :assetsCode",
           nativeQuery = true)
    AssetOU findOuByAssetsCode(@Param("assetsCode") String assetsCode);
}

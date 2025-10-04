package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccOperateItems;

public interface NcccOperateItemsRepository extends JpaRepository<NcccOperateItems, String> {

    @Query("SELECT N FROM NcccOperateItems N WHERE operateItemCode = :code")
    NcccOperateItems findByOperateItemCode(@Param("code") String code);

    @Query("SELECT N FROM NcccOperateItems N WHERE operateItem = :name")
    NcccOperateItems findByOperateItemName(@Param("name") String name);


}

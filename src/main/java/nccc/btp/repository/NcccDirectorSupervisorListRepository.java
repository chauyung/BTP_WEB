package nccc.btp.repository;

import nccc.btp.entity.NcccDirectorSupervisorList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NcccDirectorSupervisorListRepository extends JpaRepository<NcccDirectorSupervisorList, String> {

    @Query("SELECT n FROM NcccDirectorSupervisorList n WHERE n.id IN :batches")
    List<NcccDirectorSupervisorList> GetByIdList(@Param("batches") List<String> batches);

    @Modifying
    @Query("UPDATE NcccDirectorSupervisorList b SET b.phone1 = :phone1,b.address = :address,b.contactAddress = :contactAddress WHERE b.id = :id")
    int updateFields(@Param("id") String id,@Param("phone1") String phone1, @Param("address") String address,  @Param("contactAddress") String contactAddress);
}

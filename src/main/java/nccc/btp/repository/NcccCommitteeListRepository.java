package nccc.btp.repository;

import nccc.btp.entity.NcccCommitteeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NcccCommitteeListRepository extends JpaRepository<NcccCommitteeList, String> {

    @Modifying
    @Query("UPDATE NcccCommitteeList b SET b.phone1 = :phone1,b.address = :address,b.contactAddress = :contactAddress WHERE b.id = :id")
    int updateFields(@Param("id") String id,@Param("phone1") String phone1, @Param("address") String address, @Param("contactAddress") String contactAddress);
}

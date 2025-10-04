package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.BpmSplitMD1;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BpmSplitMD1Repository  extends JpaRepository<BpmSplitMD1, Long>{

    @Query("SELECT n FROM BpmSplitMD1 n WHERE n.mId=:mId")
    List<BpmSplitMD1> findBymId(@Param("mId") long mId);

    @Modifying
    @Query("DELETE FROM BpmSplitMD1 n WHERE n.mId=:mId")
    void deleteBymId(@Param("mId") long mId);
}

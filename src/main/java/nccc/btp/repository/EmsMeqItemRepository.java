package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.EmsMeqItem;
import nccc.btp.entity.EmsMeqItemId;

public interface EmsMeqItemRepository extends JpaRepository<EmsMeqItem, EmsMeqItemId> {


}

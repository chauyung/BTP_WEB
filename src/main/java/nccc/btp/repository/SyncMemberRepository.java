package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.SyncMember;
import nccc.btp.entity.SyncMemberId;

public interface SyncMemberRepository extends JpaRepository<SyncMember, SyncMemberId> {

}

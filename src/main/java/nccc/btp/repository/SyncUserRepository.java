package nccc.btp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.entity.SyncUser;
import nccc.btp.entity.SyncUserId;

public interface SyncUserRepository extends JpaRepository<SyncUser, SyncUserId> {

  List<SyncUser> findByOuCodeAndAccountNotAndDisabled(String ouCode, String accountToExclude,
      String disabledFlag);
  
  SyncUser findByAccount(String hrAccount);

  SyncUser findByHrid(String hrid);

  @Query("SELECT u FROM SyncUser u WHERE u.account = (SELECT o.mgrAccount FROM SyncOU o WHERE o.ouCode = :ouCode)")
  Optional<SyncUser> findManagerUserByOuCode(@Param("ouCode") String ouCode);

  List<SyncUser> findByOuCode(String ouCode);

  Optional<SyncUser> findFirstByHrid(String hrid);

  @Query("select lower(u.account) as k, max(u.displayName) as v " +
         "from SyncUser u " +
         "where lower(u.account) in :accs " +
         "group by lower(u.account)")
  List<Object[]> findDisplayNameByAccounts(@Param("accs") List<String> accs);


}

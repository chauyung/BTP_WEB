package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import nccc.btp.aplog.model.SysFunCombinedDTO;
import nccc.btp.entity.SysFun;

@Repository
public interface SysFunCombinedRepository extends JpaRepository<SysFun, String> {

  @Query("SELECT new nccc.btp.aplog.model.SysFunCombinedDTO(" + "sf.uuid, " + // 1
      "sf.menuLink, " + // 2
      "sf.funId, " + // 3
      "sf.funName, " + // 4
      "sf.uri, " + // 5
      "sf.pageEntry, " + // 6
      "sf.menuId, " + // 7
      "sf.ldapFunId, " + // 8
      "sf.ldapAttribute, " + // 9
      "sf.approvalLevel, " + // 10
      "sf.apiName, " + // 11
      "sf.apiGroup, " + // 12

      "sfu.loginRequired, " + // 13
      "sfu.apLog, " + // 14
      "sfu.apLogAccessType, " + // 15
      "sfu.apLogCount, " + // 16
      "sfu.uriDescription, " + // 17

      "sm.menuName, " + // 18
      "sm.menuType, " + // 19
      "sm.parentId, " + // 20
      "sm.odrNum, " + // 21
      "sm.status, " + // 22
      "sm.iconName, " + // 23
      "sm.pageUri, " + // 24
      "sm.ldapSn) " + // 25
      "FROM SysFun sf " + "LEFT JOIN SysFunUri sfu ON sf.funId = sfu.funId AND sf.uri = sfu.uri "
      + "LEFT JOIN SysMenu sm ON sf.menuId = sm.menuId " + // <-- 這裡改為 menuId 關聯
      "WHERE sf.funId = :funId")
  List<SysFunCombinedDTO> findCombinedDataByFunId(String funId);

  @Query("SELECT new nccc.btp.aplog.model.SysFunCombinedDTO(" + "sf.uuid, " + "sf.menuLink, "
      + "sf.funId, " + "sf.funName, " + "sf.uri, " + "sf.pageEntry, " + "sf.menuId, "
      + "sf.ldapFunId, " + "sf.ldapAttribute, " + "sf.approvalLevel, " + "sf.apiName, "
      + "sf.apiGroup, " +

      "sfu.loginRequired, " + "sfu.apLog, " + "sfu.apLogAccessType, " + "sfu.apLogCount, "
      + "sfu.uriDescription, " +

      "sm.menuName, " + "sm.menuType, " + "sm.parentId, " + "sm.odrNum, " + "sm.status, "
      + "sm.iconName, " + "sm.pageUri, " + "sm.ldapSn) " + "FROM SysFun sf "
      + "LEFT JOIN SysFunUri sfu ON sf.funId = sfu.funId AND sf.uri = sfu.uri "
      + "LEFT JOIN SysMenu sm ON sf.menuId = sm.menuId") // <-- 這裡改為 menuId 關聯
  List<SysFunCombinedDTO> findAllCombinedData();
}
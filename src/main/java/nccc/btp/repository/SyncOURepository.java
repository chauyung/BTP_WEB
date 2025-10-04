package nccc.btp.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import nccc.btp.dto.SyncOuWithManagerDto;
import nccc.btp.entity.SyncOU;
import nccc.btp.entity.SyncOUId;
import nccc.btp.vo.SyncOUVo;

public interface SyncOURepository extends JpaRepository<SyncOU, SyncOUId> {

  SyncOU findByOuCode(String ouCode);

  @Query(
      value = "SELECT so.ou_code AS ouCode,so.ou_name AS ouName,so.ou_level AS ouLevel,so.parent_ou_code AS parentOuCode, "
          + " so.order_index AS orderIndex,so.mgr_account AS mgrAccount,su.display_name AS displayName,su.cost_center AS costCenter, "
          + " su.email AS email,su.hrid AS hrid FROM sync_ou so LEFT JOIN sync_user su ON so.mgr_account = su.account "
          + " WHERE so.ou_code = :ouCode",
      nativeQuery = true)
  SyncOuWithManagerDto findByOuCodeWithManager(@Param("ouCode") String ouCode);


  // 取得某員工的直屬主管及以上主管（不含自己）
  @Query(
      value = "SELECT ouCode, ouName, ouLevel, parentOuCode, orderIndex, mgrAccount, displayName, costCenter, email, hrid "
          + " FROM (SELECT so.ou_code AS ouCode,so.ou_name AS ouName,so.ou_level AS ouLevel,so.parent_ou_code AS parentOuCode, "
          + " so.order_index AS orderIndex,so.mgr_account AS mgrAccount,su.display_name AS displayName,su.cost_center AS costCenter, "
          + " su.email AS email,su.hrid AS hrid,LEVEL AS lvl FROM sync_ou so LEFT JOIN sync_user su ON so.mgr_account = su.account "
          + " START WITH so.ou_code = (SELECT ou_code FROM sync_user WHERE hrid = :hrid) CONNECT BY NOCYCLE PRIOR so.parent_ou_code = so.ou_code "
          + " AND PRIOR so.ou_level <> '20' ORDER SIBLINGS BY so.order_index ) WHERE NOT (lvl = 1 AND hrid = :hrid)",
      nativeQuery = true)
  List<SyncOuWithManagerDto> findSupervisor(@Param("hrid") String hrid);

  /**
   * 取得預算部門
   * 
   * @return 部門列表
   */
  @Query("SELECT s FROM SyncOU s WHERE s.ouLevel = '30' OR s.ouCode = '1200'")
  List<SyncOU> findBudgetOU();

  /**
   * 倉部經辦
   */
  @Query(
      value = "SELECT ou.ou_code AS OUCODE,ou.ou_name AS OUNAME,u.account AS USERID,u.display_name AS USERNAME"
          + " FROM sync_ou ou INNER JOIN sync_user u ON ou.mgr_account = u.account",
      nativeQuery = true)
  List<SyncOUVo.OuManager> getAllByMgrAccount();

  /**
   * 取得對應部門底下經辦
   * 
   * @param targetOU
   * @return
   */
  @Query(
      value = "SELECT t.OU_CODE AS OUCODE, t.OU_NAME AS OUNAME, u.HRID AS USERID, u.DISPLAY_NAME AS USERNAME , :targetOU AS BUDGETOUCODE "
          + "FROM SYNC_OU t " + "JOIN SYNC_USER u ON u.OU_CODE = t.OU_CODE "
          + "START WITH t.OU_CODE = :targetOU " + "CONNECT BY PRIOR t.OU_CODE = t.PARENT_OU_CODE",
      nativeQuery = true)
  List<SyncOUVo.BudgetOuManager> getTargetOuUnderAccount(@Param("targetOU") String targetOU);

  /**
   * 取得預算部門代號
   * 
   * @return 部門列表
   */
  @Query("SELECT s.ouCode FROM SyncOU s WHERE s.ouLevel = '30' OR s.ouCode = '1200'")
  List<String> findBudgetOUCodeList();

  @Query("SELECT s FROM SyncOU s WHERE s.ouLevel = :ouLevel")
  List<SyncOU> findAllByOuLevel(@Param("ouLevel") String ouLevel);

  @Query("select s.ouCode, s.ouName from SyncOU s where s.ouCode in :codes")
  List<Object[]> findNames(@Param("codes") Set<String> codes);
}

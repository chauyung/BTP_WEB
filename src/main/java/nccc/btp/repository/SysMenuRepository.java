package nccc.btp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import nccc.btp.entity.SysMenu;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, String> {

  // 只查 MENU_ID 和 MENU_NAME
  @Query("SELECT m.menuId, m.menuName FROM SysMenu m")
  List<Object[]> findAllMenuIdAndName();

  @Query("SELECT m FROM SysMenu m WHERE m.status = :status AND m.odrNum <> 999 ORDER BY m.odrNum")
  List<SysMenu> findByStatusAndOdrNumNot999(@Param("status") String status);

  SysMenu findByMenuId(String menuId);
}

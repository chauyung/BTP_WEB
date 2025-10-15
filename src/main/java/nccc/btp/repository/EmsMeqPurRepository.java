package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nccc.btp.entity.EmsMeqPur;

/**
 * 報表管理：設備新購資料檔(Repository)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
public interface EmsMeqPurRepository extends JpaRepository<EmsMeqPur, String> {


}

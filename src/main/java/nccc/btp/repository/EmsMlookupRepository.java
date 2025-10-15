package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nccc.btp.entity.EmsMlookup;

/**
 * 報表管理：設備項目資料檔(Repository)
 * ------------------------------------------------------
 * 建立人員: ChauYung
 * 建立日期: 2025-10-08
 */
public interface EmsMlookupRepository extends JpaRepository<EmsMlookup, String> {

}

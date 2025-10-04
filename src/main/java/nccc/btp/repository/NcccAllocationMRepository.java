package nccc.btp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nccc.btp.entity.NcccAllocationM;

public interface NcccAllocationMRepository extends JpaRepository<NcccAllocationM, NcccAllocationM.ConfigId> {

    /**
     * 取得採購單資料
     * @return
     */
    @Query(value = "SELECT po_item_no, po_no, accounting, bp_name, bp_no, calcel_total, create_date, create_user, item_code, item_text, oucode, ouname, subject, total, total_remain " +
            "FROM ( " +
            " SELECT m.*, ROW_NUMBER() OVER (PARTITION BY m.po_no ORDER BY m.po_item_no) AS rn " +
            " FROM NCCC_ALLOCATION_M m " +
            ") " +
            "WHERE rn = 1 " +
            "ORDER BY create_date DESC", nativeQuery = true)
    List<NcccAllocationM> GetPoList();

    /**
     * 取得採購單資料
     * @param poNo
     * @return
     */
    @Query(value = "SELECT m FROM NcccAllocationM m WHERE po_no = :poNo")
    List<NcccAllocationM> GetAllByPoNo(@Param("poNo") String poNo);

    /**
     * 取得採購單資料
     * @param poNo
     * @return
     */
    @Query(value = "SELECT m FROM NcccAllocationM m WHERE m.poNo = :poNo AND m.poItemNo = :poItemNo")
    NcccAllocationM findByPoNoAndPoItemNo(@Param("poNo") String poNo,@Param("poItemNo") String poItemNo);
}

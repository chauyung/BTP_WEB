package nccc.btp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 報表管理：設備項目資料檔(Eitity)
 * ------------------------------------------------------
 * 修訂人員: ChauYung
 * 修訂日期: 2025-10-08
 */
@Entity
@Table(name = "EMS_MLOOKUP")
@NamedQuery(name = "EmsMlookup.findAll", query = "SELECT e FROM EmsMlookup e")
public class EmsMlookup implements Serializable {
	private static final long serialVersionUID = -6663327633527627603L;
}

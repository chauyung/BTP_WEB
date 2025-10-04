/**
 * 
 */
package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;

/**
 * 複合主鍵類別，對應 VIEW 的主鍵欄位：ACCOUNT, DISPLAYNAME, HRID
 */
public class SyncUserId implements Serializable {

  private static final long serialVersionUID = 1L;
  
    private String account;
    private String displayName;
    private String hrid;

	public SyncUserId() {}

    public SyncUserId(String account, String displayName, String hrid) {
        this.account = account;
        this.displayName = displayName;
        this.hrid = hrid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHrid() {
        return hrid;
    }

    public void setHrId(String hrid) {
        this.hrid = hrid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncUserId that = (SyncUserId) o;
        return Objects.equals(account, that.account) &&
               Objects.equals(displayName, that.displayName) &&
               Objects.equals(hrid, that.hrid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, displayName, hrid);
    }
}

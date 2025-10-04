package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;

public class NcccAssetsDId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String assetsCode;
    private String operateItemCode;

    public NcccAssetsDId() {}

    public String getAssetsCode() { return assetsCode; }
    public void setAssetsCode(String assetsCode) { this.assetsCode = assetsCode; }

    public String getOperateItemCode() { return operateItemCode; }
    public void setOperateItemCode(String operateItemCode) { this.operateItemCode = operateItemCode; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NcccAssetsDId)) return false;
        NcccAssetsDId that = (NcccAssetsDId) o;
        return Objects.equals(assetsCode, that.assetsCode)
            && Objects.equals(operateItemCode, that.operateItemCode);
    }
    @Override public int hashCode() { return Objects.hash(assetsCode, operateItemCode); }
}

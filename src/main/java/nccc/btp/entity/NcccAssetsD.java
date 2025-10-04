package nccc.btp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "NCCC_ASSETS_D")
@IdClass(NcccAssetsDId.class)
public class NcccAssetsD implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ASSETS_CODE", length = 20)
    private String assetsCode;

    @Id
    @Column(name = "OPERATE_ITEM_CODE", length = 10)
    private String operateItemCode;

    @Column(name = "OPERATE_ITEM", length = 100)
    private String operateItem;

    @Column(name = "OPERATE_AMT")
    private BigDecimal operateAmt;

    @Column(name = "OPERATE_RATIO")
    private BigDecimal operateRatio;

    @Column(name = "CREATE_USER", length = 50)
    private String createUser;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_USER", length = 50)
    private String updateUser;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updateDate = createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

}

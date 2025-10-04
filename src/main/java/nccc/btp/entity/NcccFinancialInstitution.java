package nccc.btp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_FINANCIAL_INSTITUTION")
public class NcccFinancialInstitution {

    // 銀行代號
    @Id
    @Column(name = "BANK_NO", length = 10)
    private String bankNo;

    // 銀行帳戶
    @Column(name = "BANK", length = 100)
    private String bank;

    // 排序
    @Column(name = "SORT", length = 100)
    private String sort;

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}

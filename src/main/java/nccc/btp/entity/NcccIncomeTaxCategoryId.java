package nccc.btp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NcccIncomeTaxCategoryId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "TAX_CATEGOORY", length = 10)
  private String taxCategory;

  @Column(name = "TAX_CODE", length = 5)
  private String taxCode;

  public NcccIncomeTaxCategoryId() {}

  public NcccIncomeTaxCategoryId(String taxCategory, String taxCode) {
    this.taxCategory = taxCategory;
    this.taxCode = taxCode;
  }

  public String getTaxCategory() {
    return taxCategory;
  }

  public void setTaxCategory(String taxCategory) {
    this.taxCategory = taxCategory;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof NcccIncomeTaxCategoryId))
      return false;
    NcccIncomeTaxCategoryId that = (NcccIncomeTaxCategoryId) o;
    return Objects.equals(taxCategory, that.taxCategory) && Objects.equals(taxCode, that.taxCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taxCategory, taxCode);
  }
}

package nccc.btp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NCCC_INCOME_TAX_CATEGORY")
@NamedQuery(name = "NcccIncomeTaxCategory.findAll", query = "SELECT n FROM NcccIncomeTaxCategory n")
public class NcccIncomeTaxCategory implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private NcccIncomeTaxCategoryId id;

  @Column(name = "TAX_CONTENT", length = 300)
  private String taxContent;


  public NcccIncomeTaxCategory() {}

  public NcccIncomeTaxCategoryId getId() {
    return id;
  }

  public void setId(NcccIncomeTaxCategoryId id) {
    this.id = id;
  }

  public String getTaxContent() {
    return taxContent;
  }

  public void setTaxContent(String taxContent) {
    this.taxContent = taxContent;
  }

}

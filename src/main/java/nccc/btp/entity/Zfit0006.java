package nccc.btp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ZFIT0006")
public class Zfit0006 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BUKRS", length = 4)
    private String bukrs;

    @Id
    @Column(name = "GJAHR", length = 4)
    private String gjahr;

    @Id
    @Column(name = "POPER", length = 3)
    private String poper;

    @Id
    @Column(name = "ANLN1", length = 12)
    private String anln1;

    @Id
    @Column(name = "ANLN2", length = 4)
    private String anln2;

    @Id
    @Column(name = "HKONT", length = 10)
    private String hkont;

    @Column(name = "WRBTR", precision = 23, scale = 2)
    private BigDecimal wrbtr;

    @Column(name = "TXT20", length = 20)
    private String txt20;
}
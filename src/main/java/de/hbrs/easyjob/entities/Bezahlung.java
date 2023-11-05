package de.hbrs.easyjob.entities;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "Bezahlung", schema = "EasyJob")
public class Bezahlung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Bezahlung")
    private Integer id_Bezahlung;

    @Column(name = "Zahlungsart")
    private String zahlungsart;

    @Column(name = "Abrechnungszeitraum")
    private String abrechnungszeitraum;

    @Column(name = "Abrechnungstag")
    private Date abrechnungstag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bezahlung bezahlung = (Bezahlung) o;
        return Objects.equals(id_Bezahlung, bezahlung.id_Bezahlung) &&
                Objects.equals(zahlungsart, bezahlung.zahlungsart) &&
                Objects.equals(abrechnungszeitraum, bezahlung.abrechnungszeitraum) &&
                Objects.equals(abrechnungstag, bezahlung.abrechnungstag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Bezahlung, zahlungsart,abrechnungszeitraum,abrechnungstag);
    }
}

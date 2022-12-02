package org.acme.schooltimetabling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Predmet {

    @PlanningId
    private UUID id;

    private String oznaka;
    private int plan;
    private String naziv;

    private int godina;
    private String semestar;

    private String tipoviNastave;

    private int brojCasovaPred;
    private int brojCasovaAud;
    private int brojCasovaLab;

    public Predmet(String oznaka, int plan, String naziv, int godina, String semestar, String tipoviNastave, int brojCasovaPred, int brojCasovaAud, int brojCasovaLab) {
        this.id = UUID.randomUUID();
        this.oznaka = oznaka;
        this.plan = plan;
        this.naziv = naziv;
        this.godina = godina;
        this.semestar = semestar;
        this.tipoviNastave = tipoviNastave;
        this.brojCasovaPred = brojCasovaPred;
        this.brojCasovaAud = brojCasovaAud;
        this.brojCasovaLab = brojCasovaLab;
    }

    @Override
    public String toString() {
        return oznaka;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predmet predmet = (Predmet) o;
        return id.equals(predmet.id);
    }
}

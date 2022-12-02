package org.acme.schooltimetabling.domain;

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
public class Predavac {

    @PlanningId
    private UUID id;

    private Long oznaka;

    private String ime;

    private String prezime;

    private String titula;

    private OrganizacionaJedinica orgJedinica;

    private boolean organizacijaFakulteta;
    private boolean dekanat;

    public Predavac(Long oznaka, String ime, String prezime, boolean organizacijaFakulteta, boolean dekanat, OrganizacionaJedinica orgJedinica) {
        this.id = UUID.randomUUID();
        this.oznaka = oznaka;
        this.ime = ime;
        this.prezime = prezime;
        this.organizacijaFakulteta = organizacijaFakulteta;
        this.dekanat = dekanat;
        this.orgJedinica = orgJedinica;
    }

    public Predavac(Long oznaka, String ime, String prezime, String titula, boolean organizacijaFakulteta, boolean dekanat, OrganizacionaJedinica orgJedinica) {
        this(oznaka, ime, prezime, organizacijaFakulteta, dekanat, orgJedinica);
        this.titula = titula;
    }

    @Override
    public String toString() {
        return prezime + " " + (titula == null ? "" : titula) + " " + ime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predavac predavac = (Predavac) o;
        return id.equals(predavac.id);
    }

}

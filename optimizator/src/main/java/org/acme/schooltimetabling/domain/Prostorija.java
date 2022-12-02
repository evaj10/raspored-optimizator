package org.acme.schooltimetabling.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Prostorija {

    /**
     * Allowed room overflow is 1 for exercises
     */
    public static final int ROOM_OVERFLOW = 1;

    @PlanningId
    private UUID id;

    private String oznaka;
    private TipProstorije tip;
    private int kapacitet;
//    private String objekat;

    private List<OrganizacionaJedinica> orgJedinica;

    private List<OrganizacionaJedinica> prosireneOrgJedinice;

    public Prostorija(String oznaka, TipProstorije tip, int kapacitet, List<OrganizacionaJedinica> orgJedinica) {
        this(UUID.randomUUID(), oznaka, tip, kapacitet, orgJedinica);
    }

    public Prostorija(UUID id, String oznaka, TipProstorije tip, int kapacitet, List<OrganizacionaJedinica> orgJedinica) {
        this.id = id;
        this.oznaka = oznaka;
        this.tip = tip;
        this.kapacitet = kapacitet;
        this.orgJedinica = orgJedinica;
        if (orgJedinica != null) {
            this.prosireneOrgJedinice = new ArrayList<>(orgJedinica);;
            for (OrganizacionaJedinica org : orgJedinica) {
                if (org instanceof Katedra) {
                    this.prosireneOrgJedinice.add(((Katedra) org).getDepartman());
                } else {
                    this.prosireneOrgJedinice.addAll(((Departman) org).getKatedre());
                }
            }
        }
    }

    @Override
    public String toString() {
        return oznaka;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prostorija prostorija = (Prostorija) o;
        return id.equals(prostorija.id);
    }
}

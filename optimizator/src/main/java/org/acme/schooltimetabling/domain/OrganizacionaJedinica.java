package org.acme.schooltimetabling.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.acme.schooltimetabling.domain.deserialize.OrgJedinicaDeserializer;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@JsonDeserialize(using = OrgJedinicaDeserializer.class)
public abstract class OrganizacionaJedinica {

    @PlanningId
    private UUID id;

    private String oznaka;

    private String ssluzbaOznaka;

    private String naziv;

    public OrganizacionaJedinica(UUID id, String oznaka, String ssluzbaOznaka, String naziv) {
        this.id = id;
        this.oznaka = oznaka;
        this.ssluzbaOznaka = ssluzbaOznaka;
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizacionaJedinica orgJed = (OrganizacionaJedinica) o;
        return id.equals(orgJed.id);
    }
}

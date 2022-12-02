package org.acme.schooltimetabling.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Katedra extends OrganizacionaJedinica {

    private Departman departman;

    public Katedra(String oznaka, String ssluzbaOznaka, String naziv, Departman departman) {
        this(UUID.randomUUID(), oznaka, ssluzbaOznaka, naziv, departman);
    }

    public Katedra(UUID id, String oznaka, String ssluzbaOznaka, String naziv, Departman departman) {
        super(id, oznaka, ssluzbaOznaka, naziv);
        this.departman = departman;
    }

}

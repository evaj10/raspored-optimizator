package org.acme.schooltimetabling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Departman extends OrganizacionaJedinica {

    @JsonIgnore
    private Set<Katedra> katedre;

    public Departman(String oznaka, String ssluzbaOznaka, String naziv) {
        this(UUID.randomUUID(), oznaka, ssluzbaOznaka, naziv);
    }

    public Departman(UUID id, String oznaka, String ssluzbaOznaka, String naziv) {
        super(id, oznaka, ssluzbaOznaka, naziv);
        this.katedre = new HashSet<>();
    }

    public void addKatedra(Katedra katedra) {
        katedre.add(katedra);
    }

}

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
public class StudijskiProgram {

    @PlanningId
    private UUID id;

    private int stepen;
    private int nivo;
    private String oznaka;
    private String naziv;

    public StudijskiProgram(int stepen, int nivo, String oznaka, String naziv) {
        this.id = UUID.randomUUID();
        this.stepen = stepen;
        this.nivo = nivo;
        this.oznaka = oznaka;
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return oznaka;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudijskiProgram studijskiProgram = (StudijskiProgram) o;
        return id.equals(studijskiProgram.id);
    }

}

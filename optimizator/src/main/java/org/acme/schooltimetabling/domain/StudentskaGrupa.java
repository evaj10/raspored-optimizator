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
public class StudentskaGrupa {

    @PlanningId
    private UUID id;

    private String oznaka;
    private int godina;
    private String semestar;
    private int brojStudenata;
    private StudijskiProgram studijskiProgram;

    public StudentskaGrupa(String oznaka, int godina, String semestar, int brojStudenata, StudijskiProgram studijskiProgram) {
        this.id = UUID.randomUUID();
        this.oznaka = oznaka;
        this.godina = godina;
        this.semestar = semestar;
        this.brojStudenata = brojStudenata;
        this.studijskiProgram = studijskiProgram;
    }

    @Override
    public String toString() {
        return oznaka + ", " + godina + ". godina";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentskaGrupa studentskaGrupa = (StudentskaGrupa) o;
        return id.equals(studentskaGrupa.id);
    }

}

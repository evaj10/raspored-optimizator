package org.acme.schooltimetabling.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dan {

    @PlanningId
    private Long id;

    // ponedeljak = 0
    // utorak = 1
    // ...
    private int danUNedelji;

    public Dan(int danUNedelji) {
        this.id = (long) danUNedelji;
        this.danUNedelji = danUNedelji;
    }

    public String getDateString() {
        switch (this.danUNedelji) {
            case 0:
                return "PON";
            case 1:
                return "UTO";
            case 2:
                return "SRE";
            case 3:
                return "CET";
            case 4:
                return "PET";
            case 5:
                return "SUB";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return getDateString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dan dan = (Dan) o;
        return id.equals(dan.id);
    }

}

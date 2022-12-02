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
public class TimeGrain {

    /**
     * Time granularity is 15 minutes.
     */
    public static final int GRAIN_LENGTH_IN_MINUTES = 15;

    @PlanningId
    private UUID id;

    private int grainIndex; // unique

    private int pocetniMinut;

    private Dan dan;

    public TimeGrain(int grainIndex, int pocetniMinut, Dan dan) {
        this.id = UUID.randomUUID();
        this.grainIndex = grainIndex;
        this.dan = dan;
        this.pocetniMinut = pocetniMinut;
    }

    public String getTimeString() {
        int hourOfDay = pocetniMinut / 60;
        int minuteOfHour = pocetniMinut % 60;
        return (hourOfDay < 10 ? "0" : "") + hourOfDay
                + ":" + (minuteOfHour < 10 ? "0" : "") + minuteOfHour;
    }

    public static String getTimeString(int pocetniMinut) {
        int hourOfDay = pocetniMinut / 60;
        int minuteOfHour = pocetniMinut % 60;
        return (hourOfDay < 10 ? "0" : "") + hourOfDay
                + ":" + (minuteOfHour < 10 ? "0" : "") + minuteOfHour;
    }

    public String getDateTimeString() {
        return dan.getDateString() + " " + getTimeString();
    }

    @Override
    public String toString() {
        return grainIndex + " (" + getDateTimeString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeGrain timeGrain = (TimeGrain) o;
        return id.equals(timeGrain.id);
    }


}

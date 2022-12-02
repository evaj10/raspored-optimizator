package org.acme.schooltimetabling.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Meeting implements Comparable<Meeting> {

    @PlanningId
    private UUID id;

    private TipProstorije tipProstorije;

    private MeetingType meetingTip;

    private Predavac predavac;

    private List<Predavac> ostaliPredavaci;

    private Predmet predmet;

    private int brojCasova;

    private int durationInGrains;

    private boolean biWeekly;

    private List<StudentskaGrupa> studentskeGrupe;

    public Meeting(UUID id, TipProstorije tipProstorije, MeetingType meetingTip, int brojCasova, Predavac predavac,
                   List<Predavac> ostaliPredavaci, Predmet predmet, List<StudentskaGrupa> studentskeGrupe,
                   boolean biWeekly) {
        this.id = id;
        this.tipProstorije = tipProstorije;
        this.meetingTip = meetingTip;
        this.predavac = predavac;
        this.ostaliPredavaci = ostaliPredavaci;
        this.predmet = predmet;
        this.brojCasova = brojCasova;
        // racunanje ukupne duzine trajanja na osnovu broja casova i tipa nastave (+15 minuta za pauzu!)
        int breakDuration = meetingTip == MeetingType.PRED ? brojCasova - 1 : (brojCasova > 2 ? 1 : 0);
        this.durationInGrains = brojCasova * 3 + breakDuration + 1;
        this.studentskeGrupe = studentskeGrupe;
        this.biWeekly = biWeekly;
    }

    public Meeting(TipProstorije tipProstorije, MeetingType meetingTip, Predavac predavac, Predmet predmet, List<StudentskaGrupa> studentskeGrupe) {
        this.id = UUID.randomUUID();
        this.tipProstorije = tipProstorije;
        this.meetingTip = meetingTip;
        this.predavac = predavac;
        this.predmet = predmet;
//        this.brojCasova = brojCasova;
        switch (meetingTip) {
            case PRED:
                this.brojCasova = predmet.getBrojCasovaPred();
                break;
            case AUD:
                this.brojCasova = predmet.getBrojCasovaAud();
                break;
            default:
                this.brojCasova = predmet.getBrojCasovaLab();
        }
        // racunanje ukupne duzine trajanja na osnovu broja casova i tipa nastave (+15 minuta za pauzu!)
        int breakDuration = meetingTip == MeetingType.PRED ? brojCasova - 1 : (brojCasova > 2 ? 1 : 0);
        this.durationInGrains = brojCasova * 3 + breakDuration + 1;
        this.studentskeGrupe = studentskeGrupe;
        this.biWeekly = false;
    }

    public Meeting(TipProstorije tipProstorije, MeetingType meetingTip, Predavac predavac, List<Predavac> ostaliPredavaci, Predmet predmet, List<StudentskaGrupa> studentskeGrupe) {
        this(tipProstorije, meetingTip, predavac, predmet, studentskeGrupe);
        this.ostaliPredavaci = ostaliPredavaci;
    }

    public void setBrojCasova(int brojCasova) {
        this.brojCasova = brojCasova;
        int breakDuration = meetingTip == MeetingType.PRED ? brojCasova - 1 : (brojCasova > 2 ? 1 : 0);
        this.durationInGrains = brojCasova * 3 + breakDuration + 1;
    }


    public int getRequiredCapacity() {
        int total = studentskeGrupe.stream().mapToInt(StudentskaGrupa::getBrojStudenata).sum();
        if (biWeekly) {
            return total / 2;
        }
        return total;
    }

    public String getDurationString() {
        return (durationInGrains * TimeGrain.GRAIN_LENGTH_IN_MINUTES) + " minutes";
    }

    @Override
    public String toString() {
        return this.id + " | " + predmet.getNaziv() + " | " + this.tipProstorije + " | "
                + this.getRequiredCapacity() + this.predavac.getOrgJedinica().getNaziv();
    }

    @Override
    public int compareTo(Meeting o) {
        return Comparator
                .comparing(Meeting::getDurationInGrains)
                .compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return id.equals(meeting.id);
    }

}

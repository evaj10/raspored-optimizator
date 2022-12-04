package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.*;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.*;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                roomConflict(constraintFactory),
                requiredRoomType(constraintFactory),
                requiredRoomCapacity(constraintFactory),
                requiredRoomCapacityExercises(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                studentMaxMeetings(constraintFactory),
                startAndEndOnSameDay(constraintFactory),
                avoidOvertime(constraintFactory),
                facultyOrganisationConflict(constraintFactory),
                deanOrganisationConflict(constraintFactory),
                teacherRoomLink(constraintFactory),

                // Medium constraints
                limitWorkingHoursEnd(constraintFactory),
                limitWorkingHoursStart(constraintFactory),
                limitRoomCapacityOverflow(constraintFactory),
                generalRoom(constraintFactory),
                teacherLimitWorkingDays(constraintFactory),
                studentLimitBreaks(constraintFactory),
                teacherLimitBreaks(constraintFactory),
                teacherMaxMeetings(constraintFactory),
                splitCourseMeetings(constraintFactory),

                // Soft constraints
                doMeetingsAsSoonAsPossible(constraintFactory),
                studentRoomStability(constraintFactory),
                taRoomStability(constraintFactory),
        };
    }

    // ************************************************************************
    // Hard constraints
    // ************************************************************************

    protected Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        MeetingAssignment.class,
                        equal(MeetingAssignment::getProstorija),
                        overlapping(assignment -> assignment.getStartingTimeGrain().getGrainIndex(),
                                assignment -> assignment.getStartingTimeGrain().getGrainIndex() +
                                        assignment.getMeeting().getDurationInGrains()))
                .penalize("Room conflict", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint requiredRoomType(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                .filter(meetingAssignment -> meetingAssignment.getRequiredRoomType() != meetingAssignment.getRoomType())
                .penalize("Required room type", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint requiredRoomCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                // dozvoljen overflow za predavanja i auditorne je 10%, za ostale, dozvoli da prelazi za ROOM_OVERFLOW
                .filter(meetingAssignment -> meetingAssignment.getMeeting().getMeetingTip() == MeetingType.PRED ||
                                            meetingAssignment.getMeeting().getMeetingTip() == MeetingType.AUD)
                .filter(meetingAssignment -> {
                    int roomCapacity = meetingAssignment.getRoomCapacity();
                    // 10% overflow ili barem 10 studenta
                    int overflow = (int)(roomCapacity * (10 / 100.0f));
                    if (overflow < 10) {
                        overflow = 10;
                    }
                    return meetingAssignment.getRequiredCapacity() > roomCapacity + overflow;
                })
                .penalize("Required room capacity", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint requiredRoomCapacityExercises(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                // dozvoljen overflow za predavanja i auditorne je 10%, za ostale, dozvoli da prelazi za ROOM_OVERFLOW
                .filter(meetingAssignment -> meetingAssignment.getMeeting().getMeetingTip() == MeetingType.RAC ||
                        meetingAssignment.getMeeting().getMeetingTip() == MeetingType.LAB)
                .filter(meetingAssignment ->
                        meetingAssignment.getRequiredCapacity() > meetingAssignment.getRoomCapacity() + Prostorija.ROOM_OVERFLOW)
                .penalize("Required room capacity - exercises", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint teacherConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta koji se preklapaju vremenski
                        MeetingAssignment.class,
                        overlapping(assignment -> assignment.getStartingTimeGrain().getGrainIndex(),
                                assignment -> assignment.getStartingTimeGrain().getGrainIndex() +
                                        assignment.getMeeting().getDurationInGrains()),
                        // i imaju istog profesora
                        equal(assignment -> assignment.getMeeting().getPredavac()))
                .penalize("Teacher conflict", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta koji se preklapaju vremenski
                        MeetingAssignment.class,
                        overlapping(assignment -> assignment.getStartingTimeGrain().getGrainIndex(),
                                assignment -> assignment.getStartingTimeGrain().getGrainIndex() +
                                        assignment.getMeeting().getDurationInGrains())
                )
                // HARD * broj preklapajucih grupa za par MA koji su u isto vreme
                .penalize("Student group conflict", HardMediumSoftScore.ONE_HARD,
                        MeetingAssignment::calculateGroupOverlap);
    }

    protected Constraint studentMaxMeetings(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // spoj sa mitingom po id-u mitinga
                .join(Meeting.class,
                        filtering((meetingAssignment, meeting) -> meetingAssignment.getMeeting().equals(meeting)))
                // spoj sa danom kada se odrzava
                .join(Dan.class,
                        filtering((meetingAssignment, meeting, day) -> meetingAssignment.getStartingTimeGrain().getDan().equals(day)))
                // spoj sa grupom koja slusa
                .join(StudentskaGrupa.class,
                        filtering((meetingAssignment, meeting, day, studentGroup) -> meeting.getStudentskeGrupe().contains(studentGroup)))
                // grupisi po danu i studentskoj grupi
                // sumiraj ukupno trajanje mitinga
                .groupBy((meetingAssignment, meeting, day, studentGroup) -> studentGroup,
                        (meetingAssignment, meeting, day, studentGroup) -> day,
                        sum((meetingAssignment, meeting, day, studentGroup) -> meeting.getDurationInGrains()))
                // ako ukupno traje vise od 8h
                .filter((studentGroup, day, totalMeetings) -> totalMeetings > 8*4)
                // kazni HARD * broj 15-minutnih delova preko 8h
                .penalize("Student maximum number of daily meetings", HardMediumSoftScore.ONE_HARD,
                        (studentGroup, day, totalMeetings) -> totalMeetings - (8 * 4));
    }

    protected Constraint startAndEndOnSameDay(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                // svi miting asajnmenti koji imaju dodeljeno vreme
                .filter(meetingAssignment -> meetingAssignment.getStartingTimeGrain() != null)
                .join(
                        // spoj sa timegrain klasom po vremenu zavrsetka
                        // U SLUCAJU DA NE POSTOJI TIMEGRAIN SA LASTTIMEGRAININDEX NECE BITI KAZNJEN! -> avoidOvertime ce ga kazniti
                        TimeGrain.class,
                        equal(MeetingAssignment::getLastTimeGrainIndex, TimeGrain::getGrainIndex),
                        filtering((meetingAssignment, timeGrain) ->
                                !meetingAssignment.getStartingTimeGrain().getDan().equals(timeGrain.getDan())))
                .penalize("Start and end on same day", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint avoidOvertime(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                .filter(meetingAssignment -> meetingAssignment.getStartingTimeGrain() != null)
                .ifNotExists(
                        TimeGrain.class,
                        equal(MeetingAssignment::getLastTimeGrainIndex, TimeGrain::getGrainIndex))
                .penalize("Don't go in overtime", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint facultyOrganisationConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // ako je predavac deo organizacije fakulteta i nije deo dekanata
                .filter(meetingAssignment ->
                        meetingAssignment.getTeacher().isOrganizacijaFakulteta() &&
                                !meetingAssignment.getTeacher().isDekanat())
                // i dan odrzavanja je sreda
                .filter(meetingAssignment -> meetingAssignment.getStartingTimeGrain().getDan().getDanUNedelji() == 2)
                // i vreme odrzavanja je izmedju 11 (11*60=660) i 14 (14*60=840)
                .filter(meetingAssignment -> meetingAssignment.isOverlapping(11*60, 14*60))
                // HARD
                .penalize("Faculty organisation conflict", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint deanOrganisationConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // ako je predavac deo dekanata
                .filter(meetingAssignment -> meetingAssignment.getTeacher().isDekanat())
                // i dan odrzavanja je sreda
                .filter(meetingAssignment -> meetingAssignment.getStartingTimeGrain().getDan().getDanUNedelji() == 2)
                // i vreme odrzavanja je izmedju 8 (8*60=480) i 14 (14*60=840)
                .filter(meetingAssignment -> meetingAssignment.isOverlapping(8*60, 14*60))
                // HARD
                .penalize("Dean organisation conflict", HardMediumSoftScore.ONE_HARD);
    }

    protected Constraint teacherRoomLink(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // cija prostorija nije opsta
                .filter(meetingAssignment -> meetingAssignment.getProstorija().getOrgJedinica() != null)
                // ciji predavac i prostorija ne pripadaju istoj katedri ili departmanu (uz pogled na nested strukturu)
                .filter(meetingAssignment -> !meetingAssignment.getProstorija().getProsireneOrgJedinice().contains(meetingAssignment.getTeacher().getOrgJedinica()))
                // MEDIUM * 100
                .penalize("Teacher-Room link", HardMediumSoftScore.ONE_HARD);
    }

    // ************************************************************************
    // Medium constraints
    // ************************************************************************

    // Overconstrained planning -> nedovoljno prostorija i/ili vremena
    // timeGrain ce biti nullable
    protected Constraint penalizeUnassigned(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                // za predavanja i auditorne dodatno MEDIUM pravilo koje vrednuje sto manji overflow
                .filter(meetingAssignment ->
                        meetingAssignment.getRequiredCapacity() > meetingAssignment.getRoomCapacity())
                .penalize("Limit room capacity overflow", HardMediumSoftScore.ONE_MEDIUM,
                        (meetingAssignment) -> {
                            int overflow = meetingAssignment.getRequiredCapacity() - meetingAssignment.getRoomCapacity();
                            // ako je prekoracen kapacitet, kazni za to prekoracenje * 1000
                            return 1000 * Math.max(overflow, 0);
                        });
    }

    protected Constraint limitRoomCapacityOverflow(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                // za predavanja i auditorne dodatno MEDIUM pravilo koje vrednuje sto manji overflow
                .filter(meetingAssignment ->
                        meetingAssignment.getRequiredCapacity() > meetingAssignment.getRoomCapacity())
                .penalize("Limit room capacity overflow", HardMediumSoftScore.ONE_MEDIUM,
                        (meetingAssignment) -> {
                    int overflow = meetingAssignment.getRequiredCapacity() - meetingAssignment.getRoomCapacity();
                    // ako je prekoracen kapacitet, kazni za to prekoracenje * 1000
                    return 1000 * Math.max(overflow, 0);
                });
    }

    protected Constraint limitWorkingHoursEnd(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // ako se zavrsava nakon 20
                .filter(meetingAssignment -> meetingAssignment.getEndTime() > 20*60)
                // MEDIUM * prekoracenje_u_minutima * 50
                .penalize("End hours overtime", HardMediumSoftScore.ONE_MEDIUM,
                        (meetingAssignment) -> (meetingAssignment.getEndTime() - 20*60) * 50);
    }

    protected Constraint limitWorkingHoursStart(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // ako pocinje pre 8
                .filter(meetingAssignment -> meetingAssignment.getStartTime() < 8*60)
                // MEDIUM * prekoracenje_u_minutima * 50
                .penalize("Start hours overtime", HardMediumSoftScore.ONE_MEDIUM,
                        (meetingAssignment) -> (8*60 - meetingAssignment.getStartTime()) * 50);
    }

    protected Constraint generalRoom(ConstraintFactory constraintFactory) {
        return constraintFactory
                // za svaki miting asajnment
                .forEach(MeetingAssignment.class)
                // cija prostorija jeste opsta
                .filter(meetingAssignment -> meetingAssignment.getProstorija().getOrgJedinica() == null)
                // MEDIUM * 1000
                .penalize("General room", HardMediumSoftScore.ONE_MEDIUM, (meetingAssignment) -> 1000);
    }

    protected Constraint studentLimitBreaks(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta
                        MeetingAssignment.class,
                        // koji su u istom danu
                        equal(assignment -> assignment.getStartingTimeGrain().getDan()),
                        // ciji je broj preklapajucih grupa > 1
                        filtering((assignment1, assignment2) -> assignment1.calculateGroupOverlap(assignment2) > 0),
                        // i nisu povezani
                        filtering((assignment1, assignment2) -> assignment1.calculateBreak(assignment2) > 0)
                )
                // MEDIUM * broj preklapajucih grupa za par MA * broj pauza od 15 minuta
                .penalize("Student breaks", HardMediumSoftScore.ONE_MEDIUM,
                        (assignment1, assignment2) -> assignment1.calculateGroupOverlap(assignment2) * assignment1.calculateBreak(assignment2));
    }

    protected Constraint teacherLimitBreaks(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta
                        MeetingAssignment.class,
                        // koji su u istom danu
                        equal(assignment -> assignment.getStartingTimeGrain().getDan()),
                        // koji imaju istog predavaca
                        filtering((assignment1, assignment2) ->
                                assignment1.getMeeting().getPredavac().equals(assignment2.getMeeting().getPredavac())),
                        // i nisu povezani
                        filtering((assignment1, assignment2) -> assignment1.calculateBreak(assignment2) > 0)
                )
                // MEDIUM * broj pauza od 15 minuta
                // MOZDA STAVITI AKCENAT NA STUDENTIMA, DA TU BUDE 2*SVE
                .penalize("Teacher limit breaks", HardMediumSoftScore.ONE_MEDIUM,
                        MeetingAssignment::calculateBreak);
    }

    protected Constraint teacherLimitWorkingDays(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(MeetingAssignment.class)
                // grupisi miting asajnmente po predavacu
                .groupBy(
                        MeetingAssignment::getTeacher,
                        // nadji min krajnji tajm grejn
                        min(MeetingAssignment::getLastTimeGrainIndex),
                        // nadji max krajnji tajm grejn
                        max(MeetingAssignment::getLastTimeGrainIndex))
                // kazni za njihovu razliku
                .penalize("Teacher limit working days", HardMediumSoftScore.ONE_MEDIUM,
                        (teacher, minTimeGrain, maxTimeGrain) -> maxTimeGrain - minTimeGrain);
    }

    protected Constraint teacherMaxMeetings(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(MeetingAssignment.class)
                .join(Meeting.class,
                        filtering((meetingAssignment, meeting) -> meetingAssignment.getMeeting().getId().equals(meeting.getId())))
                .join(Dan.class,
                        filtering((meetingAssignment, meeting, day) -> meetingAssignment.getStartingTimeGrain().getDan().equals(day)))
                .groupBy((meetingAssignment, meeting, day) -> meeting.getPredavac(),
                        (meetingAssignment, meeting, day) -> day,
                        sum((meetingAssignment, meeting, day) -> meeting.getDurationInGrains()/3))
                .filter((teacher, day, totalMeetings) -> totalMeetings > 6)
                .penalize("Teacher maximum number of daily meetings", HardMediumSoftScore.ONE_MEDIUM);
    }

    protected Constraint splitCourseMeetings(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta
                        MeetingAssignment.class,
                        // koji su u istom danu
                        equal(assignment -> assignment.getStartingTimeGrain().getDan()),
                        // koji su tip predavanja
                        equal(assignment -> assignment.getMeeting().getMeetingTip() == MeetingType.PRED),
                        // ciji je predmet isti
                        filtering((assignment1, assignment2) ->
                                assignment1.getMeeting().getPredmet() == assignment2.getMeeting().getPredmet()),
                        // i sve studentske grupe su iste -> broj preklapajucih jednak je broju svih
                        filtering((assignment1, assignment2) ->
                                assignment1.calculateGroupOverlap(assignment2) == assignment1.calculateGroupCount())
                )
                // MEDIUM * 1000
                .penalize("Split course meetings over more days",
                        HardMediumSoftScore.ONE_MEDIUM, (assignment1, assignment2) -> 1000);
    }

    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    protected Constraint doMeetingsAsSoonAsPossible(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(MeetingAssignment.class)
                .filter(meetingAssignment -> meetingAssignment.getStartingTimeGrain() != null)
                // za svaki meeting kome je dodeljeno pocetno vreme, sto je vece krajnje vreme, veca je kazna
                .penalize("Do all meetings as soon as possible", HardMediumSoftScore.ONE_SOFT,
                        MeetingAssignment::getLastTimeGrainIndex);
    }

    protected Constraint studentRoomStability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta
                        MeetingAssignment.class,
                        // koji su u istom danu
                        equal(assignment -> assignment.getStartingTimeGrain().getDan()),
                        // ciji je broj preklapajucih grupa > 1
                        filtering((assignment1, assignment2) -> assignment1.calculateGroupOverlap(assignment2) > 0),
                        // uzastopni su
                        filtering((assignment1, assignment2) ->
                                (assignment1.getEndTimeGrainIndex() == assignment2.getStartingTimeGrain().getGrainIndex())
                                || (assignment2.getEndTimeGrainIndex() == assignment1.getStartingTimeGrain().getGrainIndex())
                        ),
                        // i nisu u istoj prostoriji
                        filtering((assignment1, assignment2) ->
                            !assignment1.getProstorija().equals(assignment2.getProstorija())
                        )
                )
                // SOFT * broj grupa koje bi morale da promene lokaciju
                .penalize("Student room stability", HardMediumSoftScore.ONE_SOFT, MeetingAssignment::calculateGroupOverlap);
    }

    protected Constraint taRoomStability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(
                        // par dva miting asajnmenta
                        MeetingAssignment.class,
                        // koji su u istom danu
                        equal(assignment -> assignment.getStartingTimeGrain().getDan()),
                        // koji imaju istog predavaca
                        filtering((assignment1, assignment2) ->
                                assignment1.getMeeting().getPredavac().equals(assignment2.getMeeting().getPredavac())),
                        // i uzastopni su
                        filtering((assignment1, assignment2) ->
                                        (assignment1.getEndTimeGrainIndex() == assignment2.getStartingTimeGrain().getGrainIndex())
                                        || (assignment2.getEndTimeGrainIndex() == assignment1.getStartingTimeGrain().getGrainIndex())
                        ),
                        // i nisu u istoj prostoriji
                        filtering((assignment1, assignment2) -> !assignment1.getProstorija().equals(assignment2.getProstorija()))
                )
                .penalize("TA room stability", HardMediumSoftScore.ONE_SOFT);
    }

}

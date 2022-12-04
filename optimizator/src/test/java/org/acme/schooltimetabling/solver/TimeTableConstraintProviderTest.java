package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.*;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TimeTableConstraintProviderTest {

    private static final Departman RAC = new Departman("21", "21", "Departman za racunarstvo i automatiku");
    private static final Departman ARH = new Departman("32", "11", "Departman za arhitekturu i urbanizam");
    private static final Katedra INF = new Katedra("216", "110", "Katedra za informatiku", RAC);
    private static final Katedra ACS = new Katedra("215", "107", "Katedra za primenjene racunarske nauke", RAC);
    private static final StudijskiProgram SIIT = new StudijskiProgram(1, 1, "SE1", "Softversko izenjerstvo i informacione tehnologije");
    private static final Prostorija PROSTORIJA_1 = new Prostorija("NTP-307", TipProstorije.RAC, 16, List.of(INF));
    private static final Prostorija PROSTORIJA_2 = new Prostorija("NTP-309", TipProstorije.RAC, 16, List.of(INF));
    private static final Prostorija PROSTORIJA_AUD = new Prostorija("NTP-001", TipProstorije.AUD, 80, null);
    private static final Prostorija PROSTORIJA_AUD_SMALL = new Prostorija("NTP-002", TipProstorije.AUD, 24, null);
    private static final Prostorija PROSTORIJA_ACS = new Prostorija("NTP-312", TipProstorije.RAC, 16, List.of(ACS));
    private static final Prostorija PROSTORIJA_ARH = new Prostorija("NTP-419", TipProstorije.RAC, 16, List.of(ARH));
    private static final Dan DAN_1 = new Dan(1L, 0);
    private static final Dan DAN_2 = new Dan(2L, 1);
    private static final Dan DAN_3 = new Dan(3L, 2);
    private static final TimeGrain GRAIN1 = new TimeGrain(0, 9*60, DAN_1);
    private static final TimeGrain GRAIN2 = new TimeGrain(1, 9*60+15, DAN_1);
    private static final TimeGrain GRAIN3 = new TimeGrain(2, 9*60+30, DAN_1);
    private static final TimeGrain GRAIN4 = new TimeGrain(3, 9*60+45, DAN_1);
    private static final TimeGrain GRAIN5 = new TimeGrain(4, 10*60, DAN_1);
    private static final TimeGrain GRAIN6 = new TimeGrain(5, 10*60+15, DAN_1);
    private static final TimeGrain GRAIN7 = new TimeGrain(6, 10*60+30, DAN_1);
    private static final TimeGrain GRAIN8 = new TimeGrain(7, 10*60+45, DAN_1);
    private static final TimeGrain GRAIN9 = new TimeGrain(8, 11*60, DAN_1);
    private static final TimeGrain GRAIN10 = new TimeGrain(9, 11*60+15, DAN_1);

    private static final TimeGrain GRAIN11 = new TimeGrain(10, 11*60+30, DAN_2);
    private static final TimeGrain GRAIN12 = new TimeGrain(11, 11*60+45, DAN_2);
    private static final TimeGrain GRAIN_WEN_9 = new TimeGrain(20, 9*60, DAN_3);
    private static final TimeGrain GRAIN_WEN_15 = new TimeGrain(22, 15*60, DAN_3);
    private static final TimeGrain GRAIN_LATE = new TimeGrain(23, 19*60+45, DAN_3);
    private static final TimeGrain GRAIN_EARLY = new TimeGrain(24, 7*60, DAN_3);
//    private static final TimeGrain GRAIN12 = new TimeGrain(12L, 11, DAY2, 11*60+45);
//    private static final TimeGrain GRAIN13 = new TimeGrain(13L, 12, DAY2, 12*60);
//    private static final TimeGrain GRAIN14 = new TimeGrain(14L, 13, DAY2, 12*60+15);
//    private static final TimeGrain GRAIN15 = new TimeGrain(15L, 14, DAY2, 12*60+30);
//    private static final TimeGrain GRAIN16 = new TimeGrain(16L, 15, DAY2, 12*60+45);
//    private static final TimeGrain GRAIN17 = new TimeGrain(17L, 16, DAY2, 13*60);
//    private static final TimeGrain GRAIN18 = new TimeGrain(18L, 17, DAY2, 13*60+15);
//    private static final TimeGrain GRAIN19 = new TimeGrain(19L, 18, DAY2, 13*60+30);
//    private static final TimeGrain GRAIN20 = new TimeGrain(20L, 19, DAY2, 13*60+45);
//    private static final TimeGrain GRAIN21 = new TimeGrain(21L, 20, DAY1, 14*60);
//    private static final TimeGrain GRAIN22 = new TimeGrain(22L, 21, DAY1, 14*60+15);
//    private static final TimeGrain GRAIN23 = new TimeGrain(23L, 22, DAY1, 14*60+30);
//    private static final TimeGrain GRAIN24 = new TimeGrain(24L, 23, DAY1, 14*60+45);
//    private static final TimeGrain GRAIN25 = new TimeGrain(25L, 24, DAY1, 15*60);
//    private static final TimeGrain GRAIN26 = new TimeGrain(26L, 25, DAY1, 15*60+15);
//    private static final TimeGrain GRAIN27 = new TimeGrain(27L, 26, DAY1, 15*60+30);
//    private static final TimeGrain GRAIN28 = new TimeGrain(28L, 27, DAY1, 15*60+45);
//    private static final TimeGrain GRAIN29 = new TimeGrain(29L, 28, DAY1, 16*60);
//    private static final TimeGrain GRAIN30 = new TimeGrain(30L, 29, DAY1, 16*60+15);
//    private static final TimeGrain GRAIN31 = new TimeGrain(31L, 30, DAY1, 16*60+30);
//    private static final TimeGrain GRAIN32 = new TimeGrain(32L, 31, DAY1, 16*60+45);
    private static final Predavac PREDAVAC_1 = new Predavac(1L, "Eva", "Jankovic", false, false, INF);
    private static final Predavac PREDAVAC_2 = new Predavac(2L, "Ivan", "Peric", false, false, INF);
    private static final Predavac PREDAVAC_3 = new Predavac(3L, "Mitar", "Perovic", false, false, INF);
    private static final Predavac PREDAVAC_ORG = new Predavac(4L, "Goran", "Sladic", true, false, INF);
    private static final Predavac PREDAVAC_DEAN = new Predavac(5L, "Srdjan", "Kolakovic", true, true, INF);
    private static final StudentskaGrupa STUDENT_GROUP1 = new StudentskaGrupa("1", 1, "L", 16, SIIT);
    private static final StudentskaGrupa STUDENT_GROUP2 = new StudentskaGrupa("2", 1, "L", 16, SIIT);
    private static final StudentskaGrupa STUDENT_GROUP3 = new StudentskaGrupa("3", 1, "L", 16, SIIT);
    private static final StudentskaGrupa STUDENT_GROUP_RAC_OVERFLOW = new StudentskaGrupa("4", 1, "L", 17, SIIT);
    private static final StudentskaGrupa STUDENT_GROUP_OVERFLOW = new StudentskaGrupa("5", 1, "L", 26, SIIT);
    private static final StudentskaGrupa STUDENT_GROUP_BIG = new StudentskaGrupa("6", 1, "L", 40, SIIT);
    private static final Predmet PREDMET_2_CAS = new Predmet("2", 1, "Internet mreze", 1, "L", "P|R", 2, 0, 2);
    private static final Predmet PREDMET_3_CAS = new Predmet("3", 1, "WD", 1, "L", "P|R", 2, 0, 3);
    private static final Predmet PREDMET_5_CAS = new Predmet("5", 1, "ASP", 1, "L", "P|R", 2, 0, 5);
    private static final Meeting MEETING1 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP1));
    private static final Meeting MEETING2 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_2, PREDMET_2_CAS, List.of(STUDENT_GROUP2));
    private static final Meeting MEETING3 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_3, PREDMET_2_CAS, List.of(STUDENT_GROUP3));
    private static final Meeting MEETING4 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP2));
    private static final Meeting MEETING_LONG = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_3_CAS, List.of(STUDENT_GROUP3));
    private static final Meeting MEETING_BIG = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP_BIG));
    private static final Meeting MEETING_G1_G2 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP1, STUDENT_GROUP2));
    private static final Meeting MEETING_G1_G2_G3 = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP1, STUDENT_GROUP2, STUDENT_GROUP3));
    private static final Meeting MEETING_VERY_LONG = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_5_CAS, List.of(STUDENT_GROUP3));
    private static final Meeting MEETING_RAC_OVERFLOW = new Meeting(TipProstorije.RAC, MeetingType.RAC, PREDAVAC_1, PREDMET_3_CAS, List.of(STUDENT_GROUP_RAC_OVERFLOW));
    private static final Meeting MEETING_OVERFLOW = new Meeting(TipProstorije.AUD, MeetingType.PRED, PREDAVAC_1, PREDMET_3_CAS, List.of(STUDENT_GROUP_OVERFLOW));
    private static final Meeting MEETING_ORG = new Meeting(TipProstorije.AUD, MeetingType.PRED, PREDAVAC_ORG, PREDMET_3_CAS, List.of(STUDENT_GROUP1));
    private static final Meeting MEETING_DEAN = new Meeting(TipProstorije.AUD, MeetingType.PRED, PREDAVAC_DEAN, PREDMET_3_CAS, List.of(STUDENT_GROUP1));
    private static final Meeting MEETING_PRED = new Meeting(TipProstorije.AUD, MeetingType.PRED, PREDAVAC_1, PREDMET_3_CAS, List.of(STUDENT_GROUP1, STUDENT_GROUP2));
    private static final Meeting MEETING_PRED1 = new Meeting(TipProstorije.AUD, MeetingType.PRED, PREDAVAC_1, PREDMET_2_CAS, List.of(STUDENT_GROUP1, STUDENT_GROUP2));

    @Autowired
    ConstraintVerifier<TimeTableConstraintProvider, MeetingSchedule> constraintVerifier;

    // ************************************************************************
    // Hard constraints
    // ************************************************************************

    @Test
    void roomConflictSameStartGrain() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void roomConflictOverlappingGrain() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void roomConflictOverlappingStartEndGrain() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN7, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void roomConflictNotOverlapping() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN8, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(0);
    }

    @Test
    void roomConflictOneOverlappingOneNotOverlapping() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN4, PROSTORIJA_1);
        MeetingAssignment nonConflictingMeeting = new MeetingAssignment(MEETING3, GRAIN11, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
                .given(firstMeeting, conflictingMeeting, nonConflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void requiredRoomCapacitySatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_PRED, GRAIN1, PROSTORIJA_AUD);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacity)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void requiredRoomCapacityNotSatisfied() {
        // MEETING_PRED consists of 32 people witch is more than PROSTORIJA_1 capacity (16) + 10% overflow or 10 people
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_PRED, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacity)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void requiredRoomCapacityAllowedOverflow() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_OVERFLOW, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacity)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void requiredRoomCapacityExercisesSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacityExercises)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void requiredRoomCapacityExercisesNotSatisfied() {
        // MEETING_BIG consists of 40 people witch is more than PROSTORIJA_1 capacity + overflow of 1 person
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_BIG, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacityExercises)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void requiredRoomCapacityExercisesAllowedOverflow() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_RAC_OVERFLOW, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomCapacityExercises)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void requiredRoomTypeSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomType)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void requiredRoomTypeNotSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_AUD);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::requiredRoomType)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void teacherConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING4, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void teacherNoTimeConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING4, GRAIN10, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(0);
    }

    @Test
    void teacherNoConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentGroupSameGroupConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING4, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void studentGroupOneGroupConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING_G1_G2, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(1);
    }

    @Test
    void studentGroupTwoGroupConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_G1_G2, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING_G1_G2_G3, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(2);
    }

    @Test
    void studentGroupNoTimeConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING4, GRAIN10, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentGroupNoConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment conflictingMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
                .given(firstMeeting, conflictingMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentMaxMeetingsBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_G1_G2_G3, GRAIN2, PROSTORIJA_1); // 7
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_LONG, GRAIN2, PROSTORIJA_1); // 11
        MeetingAssignment longMeeting = new MeetingAssignment(MEETING_VERY_LONG, GRAIN2, PROSTORIJA_1); // 17
        // => 35 = 8.75h => 8.75-8=0.75 => 0.75*4 = 3 hard
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentMaxMeetings)
                .given(firstMeeting, secondMeeting, longMeeting, MEETING_G1_G2_G3, MEETING_LONG, MEETING_VERY_LONG, DAN_1, STUDENT_GROUP3)
                .penalizesBy(3);
    }

    @Test
    void studentMaxMeetingsNotBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1); // 6/3=2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_G1_G2, GRAIN7, PROSTORIJA_1); // 6/3=2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentMaxMeetings)
                .given(firstMeeting, secondMeeting, MEETING1, MEETING_G1_G2, DAN_1, STUDENT_GROUP1)
                .penalizesBy(0);
    }

    @Test
    void startAndEndOnSameDaySatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::startAndEndOnSameDay)
                .given(firstMeeting, GRAIN6)
                .penalizesBy(0);
    }

    @Test
    void startAndEndOnSameDayEdgeSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN5, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::startAndEndOnSameDay)
                .given(firstMeeting, GRAIN10)
                .penalizesBy(0);
    }

    @Test
    void startAndEndOnSameDayNotSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN6, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::startAndEndOnSameDay)
                .given(firstMeeting, GRAIN12)
                .penalizesBy(1);
    }

    @Test
    void avoidOvertimeSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::avoidOvertime)
                .given(firstMeeting, GRAIN8)
                .penalizesBy(0);
    }

    @Test
    void avoidOvertimeNotSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::avoidOvertime)
                .given(firstMeeting, GRAIN6)
                .penalizesBy(1);
    }

    @Test
    void facultyOrganisationOverlappingTime() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_ORG, GRAIN_WEN_9, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::facultyOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void facultyOrganisationDifferentDay() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_ORG, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::facultyOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void facultyOrganisationNoConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_ORG, GRAIN_WEN_15, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::facultyOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void deanOrganisationOverlappingTime() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_DEAN, GRAIN_WEN_9, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::deanOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void deanOrganisationDifferentDay() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_DEAN, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::deanOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void deanOrganisationNoConflict() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_DEAN, GRAIN_WEN_15, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::deanOrganisationConflict)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    // ************************************************************************
    // Medium constraints
    // ************************************************************************

    @Test
    void limitRoomCapacityOverflowSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_OVERFLOW, GRAIN1, PROSTORIJA_AUD);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitRoomCapacityOverflow)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void limitRoomCapacityOverflowNotSatisfied() {
        int overflow = MEETING_OVERFLOW.getRequiredCapacity() - PROSTORIJA_AUD_SMALL.getKapacitet();
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_OVERFLOW, GRAIN1, PROSTORIJA_AUD_SMALL);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitRoomCapacityOverflow)
                .given(firstMeeting)
                .penalizesBy(1000 * overflow);
    }

    @Test
    void limitWorkingHoursEndSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitWorkingHoursEnd)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void limitWorkingHoursEndNotSatisfied() {
        // pocetak: 19:45, trajanje: 1:30 => kraj: 21:15 => prekoracenje = 75
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN_LATE, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitWorkingHoursEnd)
                .given(firstMeeting)
                .penalizesBy(75 * 50);
    }

    @Test
    void limitWorkingHoursStartSatisfied() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitWorkingHoursStart)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void limitWorkingHoursStartNotSatisfied() {
        // pocetak: 07:00 => prekoracenje = 60
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN_EARLY, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::limitWorkingHoursStart)
                .given(firstMeeting)
                .penalizesBy(60 * 50);
    }

    @Test
    void teacherRoomLinkBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_ACS);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomLink)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void teacherRoomLinkNotBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomLink)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void teacherRoomLinkGeneralRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_AUD);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomLink)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void teacherRoomLinkDepartmanRoomBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_ARH);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomLink)
                .given(firstMeeting)
                .penalizesBy(1);
    }

    @Test
    void teacherRoomLinkDepartmanRoomNotBroken() {
        RAC.addKatedra(INF);
        RAC.addKatedra(ACS);
        Prostorija PROSTORIJA_RAC = new Prostorija("NTP-418", TipProstorije.RAC, 16, List.of(RAC));
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_RAC);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomLink)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void generalRoomUsed() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_AUD);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::generalRoom)
                .given(firstMeeting)
                .penalizesBy(1000);
    }

    @Test
    void generalRoomNotUsed() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::generalRoom)
                .given(firstMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentLimitBreaksNoBreaks() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1); // grupa 2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN7, PROSTORIJA_1); // grupa 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentLimitBreaksThreeGrainNoGroupBreak() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // grupa 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING2, GRAIN10, PROSTORIJA_1); // grupa 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentLimitBreaksTwoGrainOneGroupBreak() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1); // grupa 2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN10, PROSTORIJA_1); // grupa 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(2);
    }

    @Test
    void studentLimitBreaksTwoGrainTwoGroupBreak() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_G1_G2, GRAIN1, PROSTORIJA_1); // grupa 1 i 2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_G1_G2_G3, GRAIN10, PROSTORIJA_1); // grupa 1, 2 i 3
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(4);
    }

    @Test
    void teacherLimitBreaksNoBreaks() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN7, PROSTORIJA_1); // ta 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void teacherLimitBreaksThreeGrainNoTaBreak() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING2, GRAIN10, PROSTORIJA_1); // ta 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void teacherLimitBreaksTwoGrainTaBreak() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN10, PROSTORIJA_1); // ta 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherLimitBreaks)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(2);
    }

    @Test
    void limitWorkingDays() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // end = 6
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN11, PROSTORIJA_1); // end = 16
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherLimitWorkingDays)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(10);
    }

    @Test
    void limitWorkingDaysMultipleTeachers() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // end = 6
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN7, PROSTORIJA_1); // end = 12
        MeetingAssignment othersMeeting = new MeetingAssignment(MEETING2, GRAIN10, PROSTORIJA_1); // end = 15 -> min i max isti -> 0
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherLimitWorkingDays)
                .given(firstMeeting, secondMeeting, othersMeeting)
                .penalizesBy(6);
    }

    @Test
    void teacherMaxMeetingsBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1); // 6/3=2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN2, PROSTORIJA_1); // 6/3=2
        MeetingAssignment longMeeting = new MeetingAssignment(MEETING_LONG, GRAIN2, PROSTORIJA_1); //11/3=3
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherMaxMeetings)
                .given(firstMeeting, secondMeeting, longMeeting, MEETING1, MEETING4, MEETING_LONG, DAN_1)
                .penalizesBy(1);
    }

    @Test
    void teacherMaxMeetingsNotBroken() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1); // 6/3=2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN2, PROSTORIJA_1); // 6/3=2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherMaxMeetings)
                .given(firstMeeting, secondMeeting, MEETING1, MEETING4, MEETING_LONG, DAN_1)
                .penalizesBy(0);
    }

    @Test
    void splitCourseMeetingsTwoMeetingsSameDay() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_PRED, GRAIN1, PROSTORIJA_1);
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_PRED, GRAIN8, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::splitCourseMeetings)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(1000);
    }

    @Test
    void splitCourseMeetingsTwoMeetingsDiffDay() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_PRED, GRAIN1, PROSTORIJA_1);
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_PRED, GRAIN11, PROSTORIJA_2);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::splitCourseMeetings)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void splitCourseMeetingsTwoMeetingsSameDayDiffCourse() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_PRED, GRAIN1, PROSTORIJA_1); // group 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_PRED1, GRAIN8, PROSTORIJA_2); // group 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::splitCourseMeetings)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    @Test
    void doMeetingsAsSoonAsPossibleSoonerExists() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1);
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING1, GRAIN2, PROSTORIJA_1);

        constraintVerifier.verifyThat(TimeTableConstraintProvider::doMeetingsAsSoonAsPossible)
                .given(firstMeeting, GRAIN1, GRAIN2, GRAIN3, GRAIN4, GRAIN5, GRAIN6, GRAIN7, GRAIN8)
                .penalizesBy(6);
        constraintVerifier.verifyThat(TimeTableConstraintProvider::doMeetingsAsSoonAsPossible)
                .given(secondMeeting, GRAIN1, GRAIN2, GRAIN3, GRAIN4, GRAIN5, GRAIN6, GRAIN7, GRAIN8)
                .penalizesBy(7);
    }

    @Test
    void studentRoomStabilityOneSameGroupDiffRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1); // group 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN8, PROSTORIJA_2); // group 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(1);
    }

    @Test
    void studentRoomStabilityTwoSameGroupDiffRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING_G1_G2, GRAIN1, PROSTORIJA_1); // group 1, 2
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING_G1_G2_G3, GRAIN8, PROSTORIJA_2); // group 1, 2, 3
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(2);
    }

    @Test
    void studentRoomStabilityOneSameGroupSameRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING2, GRAIN1, PROSTORIJA_1); // group 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN7, PROSTORIJA_1); // group 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void studentRoomStabilityDiffGroupDiffRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // group 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING2, GRAIN7, PROSTORIJA_2); // group 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void taRoomStabilitySameTaDiffRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN8, PROSTORIJA_2); // ta 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::taRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(1);
    }

    @Test
    void taRoomStabilitySameTaSameRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING4, GRAIN7, PROSTORIJA_1); // ta 1
        constraintVerifier.verifyThat(TimeTableConstraintProvider::taRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    @Test
    void taRoomStabilityDiffTaDiffRoom() {
        MeetingAssignment firstMeeting = new MeetingAssignment(MEETING1, GRAIN1, PROSTORIJA_1); // ta 1
        MeetingAssignment secondMeeting = new MeetingAssignment(MEETING2, GRAIN7, PROSTORIJA_2); // ta 2
        constraintVerifier.verifyThat(TimeTableConstraintProvider::taRoomStability)
                .given(firstMeeting, secondMeeting)
                .penalizesBy(0);
    }

    // provera svih constraint-ova
    //constraintVerifier.verifyThat()
    //                .given(queen1, queen2, queen3)
    //                .scores(SimpleScore.of(-3));

}

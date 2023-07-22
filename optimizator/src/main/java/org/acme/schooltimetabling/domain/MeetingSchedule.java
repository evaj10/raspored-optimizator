package org.acme.schooltimetabling.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter

//@JsonDeserialize(using = MeetingScheduleDeserializer.class)
@PlanningSolution
public class MeetingSchedule {

    @PlanningId
    private Long id;

//    @ConstraintConfigurationProvider
//    private MeetingConstraintConfiguration constraintConfiguration;

    // helper field for view
    private List<StudijskiProgram> studProgramList;

    @ProblemFactCollectionProperty
    private List<Meeting> meetingList;

    @ProblemFactCollectionProperty
    private List<Dan> danList;

    @ValueRangeProvider(id = "timeGrainRange")
    @ProblemFactCollectionProperty
    private List<TimeGrain> timeGrainList;

    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    private List<Prostorija> prostorijaList;

    @ProblemFactCollectionProperty
    private List<Predavac> predavacList;

    @ProblemFactCollectionProperty
    private List<StudentskaGrupa> studentskaGrupaList;

    @PlanningEntityCollectionProperty
    private List<MeetingAssignment> meetingAssignmentList;

    private Semestar semestar;

    @PlanningScore(bendableHardLevelsSize = 1, bendableSoftLevelsSize = 4)
    private BendableLongScore score;

    public MeetingSchedule(List<Meeting> meetingList, List<Dan> danList, List<TimeGrain> timeGrainList,
                           List<Prostorija> prostorijaList, List<Predavac> predavacList, List<StudentskaGrupa> studentskaGrupaList,
                           List<MeetingAssignment> meetingAssignmentList) {
        this.meetingList = meetingList;
        this.danList = danList;
        this.timeGrainList = timeGrainList;
        this.prostorijaList = prostorijaList;
        this.predavacList = predavacList;
        this.studentskaGrupaList = studentskaGrupaList;
        this.meetingAssignmentList = meetingAssignmentList;
    }

    public MeetingSchedule(List<StudijskiProgram> studProgramList, List<Meeting> meetingList, List<Dan> danList, List<TimeGrain> timeGrainList,
                           List<Prostorija> prostorijaList, List<Predavac> predavacList, List<StudentskaGrupa> studentskaGrupaList,
                           List<MeetingAssignment> meetingAssignmentList, Semestar semestar) {
        this.studProgramList = studProgramList;
        this.meetingList = meetingList;
        this.danList = danList;
        this.timeGrainList = timeGrainList;
        this.prostorijaList = prostorijaList;
        this.predavacList = predavacList;
        this.studentskaGrupaList = studentskaGrupaList;
        this.meetingAssignmentList = meetingAssignmentList;
        this.semestar = semestar;
    }

    public MeetingSchedule(Long id, List<StudijskiProgram> studProgramList, List<Meeting> meetingList, List<Dan> danList, List<TimeGrain> timeGrainList,
                           List<Prostorija> prostorijaList, List<Predavac> predavacList, List<StudentskaGrupa> studentskaGrupaList,
                           List<MeetingAssignment> meetingAssignmentList, Semestar semestar) {
        this(studProgramList, meetingList, danList, timeGrainList, prostorijaList, predavacList, studentskaGrupaList, meetingAssignmentList, semestar);
        this.id = id;
    }

    public void setupProstorije() {
        this.prostorijaList.forEach(Prostorija::fillProsireneOrgJedinice);
    }

    public void constructMeetingAssignmentList() {
        if (this.meetingAssignmentList == null) {
            this.meetingAssignmentList = new ArrayList<>();
        }
        if (!this.meetingAssignmentList.isEmpty()) {
            // find all meetings that have already been assigned (ex. OAS)
            List<Meeting> assignedMeetings = this.meetingAssignmentList.stream().map(MeetingAssignment::getMeeting).collect(Collectors.toList());
            // remove all from meeting list
            this.meetingList.removeAll(assignedMeetings);
        }
        // create meeting assignments for remaining meetings
        this.meetingAssignmentList.addAll(this.meetingList.stream().map(MeetingAssignment::new).collect(Collectors.toList()));
    }

    //    public MeetingConstraintConfiguration getConstraintConfiguration() {
//        return constraintConfiguration;
//    }
//
//    public void setConstraintConfiguration(MeetingConstraintConfiguration constraintConfiguration) {
//        this.constraintConfiguration = constraintConfiguration;
//    }

}

package org.acme.schooltimetabling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.acme.schooltimetabling.domain.comparator.MeetingAssignmentComparator;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@PlanningEntity(difficultyComparatorClass = MeetingAssignmentComparator.class)
public class MeetingAssignment {

    @PlanningId
    private UUID id;

    private Meeting meeting;

    // Planning variables: changes during planning, between score calculations.
    @PlanningVariable(valueRangeProviderRefs = { "roomRange" })
    private Prostorija prostorija;

    @PlanningVariable(nullable = true, valueRangeProviderRefs = { "timeGrainRange" })
    private TimeGrain startingTimeGrain;

    // If Planning entity is pinned -> optaplanner what change planning variables (room and grain)
    // Used for sequential planning (first OAS then MAS)
    private boolean pinned;

    public MeetingAssignment(Meeting meeting) {
        this.id = UUID.randomUUID();
        this.meeting = meeting;
        this.pinned = false;
    }

    public MeetingAssignment(Meeting meeting, TimeGrain startingTimeGrain, Prostorija prostorija) {
        this.id = UUID.randomUUID();
        this.meeting = meeting;
        this.startingTimeGrain = startingTimeGrain;
        this.prostorija = prostorija;
        this.pinned = false;
    }

    public MeetingAssignment(UUID id, Meeting meeting, TimeGrain startingTimeGrain, Prostorija prostorija) {
        this.id = id;
        this.meeting = meeting;
        this.startingTimeGrain = startingTimeGrain;
        this.prostorija = prostorija;
        this.pinned = true;
    }

    @PlanningPin
    public boolean isPinned() {
        return pinned;
    }

    public int calculateOverlap(MeetingAssignment other) {
        if (startingTimeGrain == null || other.getStartingTimeGrain() == null) {
            return 0;
        }
        int start = startingTimeGrain.getGrainIndex();
        int end = start + meeting.getDurationInGrains();
        int otherStart = other.startingTimeGrain.getGrainIndex();
        int otherEnd = otherStart + other.meeting.getDurationInGrains();

        if (end < otherStart) {
            return 0;
        } else if (otherEnd < start) {
            return 0;
        }
        return Math.min(end, otherEnd) - Math.max(start, otherStart);
    }

    public int calculateGroupCount() {
        return this.meeting.getStudentskeGrupe().size();
    }

    public int calculateGroupOverlap(MeetingAssignment other) {
        List<StudentskaGrupa> overlapping = new ArrayList<>(this.meeting.getStudentskeGrupe());
        overlapping.retainAll(other.meeting.getStudentskeGrupe());
        return overlapping.size();
    }

    public int calculateBreak(MeetingAssignment other) {
        if (this.getStartingTimeGrain().getGrainIndex() < other.getStartingTimeGrain().getGrainIndex()) {
            return other.getStartingTimeGrain().getGrainIndex() - this.getEndTimeGrainIndex();
        } else {
            return this.getStartingTimeGrain().getGrainIndex() - other.getEndTimeGrainIndex();
        }
    }

    @JsonIgnore
    public Integer getLastTimeGrainIndex() {
        if (startingTimeGrain == null) {
            return null;
        }
        return startingTimeGrain.getGrainIndex() + meeting.getDurationInGrains() - 1;
    }

    @JsonIgnore
    public Integer getStartTime() {
        if (startingTimeGrain == null) {
            return null;
        }
        return startingTimeGrain.getPocetniMinut();
    }

    @JsonIgnore
    public Integer getEndTime() {
        if (startingTimeGrain == null) {
            return null;
        }
        return startingTimeGrain.getPocetniMinut() + (meeting.getDurationInGrains() - 1) * TimeGrain.GRAIN_LENGTH_IN_MINUTES;
    }

    @JsonIgnore
    public Integer getEndTimeGrainIndex() {
        if (startingTimeGrain == null) {
            return null;
        }
        return startingTimeGrain.getGrainIndex() + meeting.getDurationInGrains();
    }

    @JsonIgnore
    public Predavac getTeacher() {
        return meeting.getPredavac();
    }

    @JsonIgnore
    public String getStartTimeString() {
        return startingTimeGrain.getTimeString();
    }

    @JsonIgnore
    public String getEndTimeString() {
        return TimeGrain.getTimeString(this.getEndTime());
    }

    public boolean isOverlapping(int startMinute, int endMinute) {
        int meetingStart = startingTimeGrain.getPocetniMinut();
        int meetingEnd = meetingStart + meeting.getDurationInGrains() * TimeGrain.GRAIN_LENGTH_IN_MINUTES;
        return meetingEnd >= startMinute && meetingStart <= endMinute;
    }

    @JsonIgnore
    public int getRoomCapacity() {
        if (prostorija == null) {
            return 0;
        }
        return prostorija.getKapacitet();
    }

    @JsonIgnore
    public int getRequiredCapacity() {
        return meeting.getRequiredCapacity();
    }

    @JsonIgnore
    public TipProstorije getRoomType() {
        if (prostorija == null) {
            return null;
        }
        return prostorija.getTip();
    }

    @JsonIgnore
    public TipProstorije getRequiredRoomType() {
        return meeting.getTipProstorije();
    }


    @Override
    public String toString() {
        return meeting.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingAssignment meetingAssignment = (MeetingAssignment) o;
        return id.equals(meetingAssignment.id);
    }

}
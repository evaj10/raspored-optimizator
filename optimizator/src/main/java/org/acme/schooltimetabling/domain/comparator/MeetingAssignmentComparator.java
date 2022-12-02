package org.acme.schooltimetabling.domain.comparator;

import org.acme.schooltimetabling.domain.Meeting;
import org.acme.schooltimetabling.domain.MeetingAssignment;

import java.util.Comparator;

public class MeetingAssignmentComparator implements Comparator<MeetingAssignment> {

    @Override
    public int compare(MeetingAssignment ma1, MeetingAssignment ma2) {
        return Comparator
                .comparing(MeetingAssignment::getMeeting, Meeting::compareTo)
                .thenComparing(MeetingAssignment::getId)
                .compare(ma1, ma2);
    }
}

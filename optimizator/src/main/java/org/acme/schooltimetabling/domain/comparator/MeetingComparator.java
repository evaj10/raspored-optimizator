package org.acme.schooltimetabling.domain.comparator;

import org.acme.schooltimetabling.domain.Meeting;

import java.util.Comparator;

public class MeetingComparator implements Comparator<Meeting>, Comparable<Meeting> {

    @Override
    public int compare(Meeting m1, Meeting m2) {
        return Comparator
                // TODO: potencijalno bi bilo dobro prvo dodeliti predavanja ili vezbe?
                .comparing(Meeting::getRequiredCapacity)
                .thenComparing(Meeting::getDurationInGrains)
                .compare(m1, m2);
    }

    @Override
    public int compareTo(Meeting o) {
        return 0;
    }
}

package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.MeetingSchedule;

public class TimeTableState {

    public static final Long MEETING_SCHEDULE_ID = 1L;

    private MeetingSchedule state;

    public void setTimeTableState(MeetingSchedule state) {
        // called when meeting schedule created and when solving ends -> can send email or emit event to front-end
        System.out.println("Can be used to send notification that solving is finished");
        System.out.println(state.getScore());
        this.state = state;
    }

    public MeetingSchedule getTimeTableState(Long id) {
        return state;
    }

}

package org.acme.schooltimetabling.solver;

import lombok.extern.slf4j.Slf4j;
import org.acme.schooltimetabling.domain.MeetingSchedule;

@Slf4j
public class TimeTableState {

    public static final Long MEETING_SCHEDULE_ID = 1L;

    private MeetingSchedule state;

    public void bestSolutionLogger(MeetingSchedule state) {
        // called when new best solution is found
        // -> monitoring purposes
        log.info("New best score: {}", state.getScore());
    }

    public void setTimeTableState(MeetingSchedule state) {
        // called when meeting schedule created and when solving ends -> can send email or emit event to front-end
        System.out.println("Can be used to send notification that solving is finished");
        log.info("Final best score: {}", state.getScore());
        this.state = state;
    }

    public MeetingSchedule getTimeTableState(Long id) {
        return state;
    }

}

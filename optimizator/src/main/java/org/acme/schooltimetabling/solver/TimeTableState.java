package org.acme.schooltimetabling.solver;

import lombok.extern.slf4j.Slf4j;
import org.acme.schooltimetabling.domain.MeetingSchedule;
import org.acme.schooltimetabling.rest.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TimeTableState {

    @Autowired
    private NotificationService notificationService;

    public static Long MEETING_SCHEDULE_ID = 0L;

    private MeetingSchedule state;

    public void bestSolutionLogger(MeetingSchedule state) {
        // called when new best solution is found
        // -> monitoring purposes
        log.info("New best score: {}", state.getScore());
    }

    public void setTimeTableState(MeetingSchedule state) {
        // called when meeting schedule created and when solving ends
        // -> can send email or emit event to front-end
        if (this.state != null && this.state.getId().equals(state.getId())) {
            System.out.println("Sending notification that solving is finished");
            notificationService.sendNotification(state.getMeetingAssignmentList());
            log.info("Final best score: {}", state.getScore());
        }
        this.state = state;
    }

    public MeetingSchedule getTimeTableState(Long id) {
        return state;
    }

}

package org.acme.schooltimetabling.rest;

import org.acme.schooltimetabling.domain.MeetingAssignment;
import org.acme.schooltimetabling.domain.MeetingSchedule;
import org.acme.schooltimetabling.solver.TimeTableState;
import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    @Autowired
    private TimeTableState state;

    @Autowired
    private SolverManager<MeetingSchedule, Long> solverManager;
    @Autowired
    private ScoreManager<MeetingSchedule, HardMediumSoftScore> scoreManager;

    @GetMapping
    public List<MeetingAssignment> getTimeTable() {
        MeetingSchedule solution = state.getTimeTableState(TimeTableState.MEETING_SCHEDULE_ID);
        return solution.getMeetingAssignmentList();
    }

    @GetMapping("/score")
    public String getScore() {
        MeetingSchedule solution = state.getTimeTableState(TimeTableState.MEETING_SCHEDULE_ID);
        return solution.getScore().toString();
    }

    @GetMapping("/broken-rules")
    public String getScoreExplanation() {
        SolverStatus solverStatus = getSolverStatus();
        MeetingSchedule solution = state.getTimeTableState(TimeTableState.MEETING_SCHEDULE_ID);
        scoreManager.updateScore(solution); // Sets the score

        ScoreExplanation<MeetingSchedule, HardMediumSoftScore> scoreExplanation = scoreManager.explainScore(solution);
        StringBuilder output = new StringBuilder();
        Collection<ConstraintMatchTotal<HardMediumSoftScore>> constraintMatchTotals = scoreExplanation.getConstraintMatchTotalMap().values();
        for (ConstraintMatchTotal<HardMediumSoftScore> constraintMatchTotal : constraintMatchTotals) {
            String constraintName = constraintMatchTotal.getConstraintName();
            // The score impact of that constraint
            HardMediumSoftScore totalScore = constraintMatchTotal.getScore();
            // svako pravilo koje je prekrseno
            output.append(constraintName).append(" ").append(totalScore).append("\n\n");
            for (ConstraintMatch<HardMediumSoftScore> constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
                List<Object> justificationList = constraintMatch.getJustificationList();
                HardMediumSoftScore score = constraintMatch.getScore();
                // za svaki miting koji je prekrsio pravilo, pise njegov toString i njegov skor
                output.append(justificationList).append(" ").append(score).append("\n");
            }
            output.append("\n\n");
        }
        return output.toString();
    }

    @PostMapping("/solve")
    public void solve() {
        solverManager.solve(
                TimeTableState.MEETING_SCHEDULE_ID,
                state.getTimeTableState(TimeTableState.MEETING_SCHEDULE_ID),
                // called when solving ends
                state::setTimeTableState);
//        solverManager.solveAndListen(1L, state::getTimeTableState, state::updateTimeTableState);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(TimeTableState.MEETING_SCHEDULE_ID);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(TimeTableState.MEETING_SCHEDULE_ID);
    }

    @PostMapping
    public void createMeetingSchedule(@RequestBody MeetingSchedule ms) {
        System.out.println("Created MeetingSchedule");
        MeetingAssignment meetingAssignment = ms.getMeetingAssignmentList().get(0);
        meetingAssignment.setStartingTimeGrain(ms.getTimeGrainList().get(0));
        meetingAssignment.setProstorija(ms.getProstorijaList().get(0));

        ms.setId(TimeTableState.MEETING_SCHEDULE_ID);
        state.setTimeTableState(ms);
    }

}

package ca.lighthouselabs.seatingplansolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanSolutionPresenter {

    private SeatingPlanSolution seatingPlanSolution;

    public SeatingPlanSolutionPresenter(SeatingPlanSolution seatingPlanSolution) {
        this.seatingPlanSolution = seatingPlanSolution;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Score: ");
        sb.append(seatingPlanSolution.getScore());
        sb.append("\nDetails: \n");
        List<Seat> seats = new ArrayList<>(seatingPlanSolution.getSeats());
        Collections.sort(seats, (a, b) -> a.getId() - b.getId());
        for (Seat seat : seats) {
            sb.append("Seat: ");
            sb.append(seat.getId());
            sb.append(", Guest: ");
            Guest guest = seat.getGuest();
            if (guest == null) {
                sb.append("[empty seat]");
            } else {
                sb.append(guest.getFirstName());
                sb.append(" ");
                sb.append(guest.getLastName());
                sb.append(" (");
                sb.append(guest.getGender());
                sb.append(")");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public SeatingPlanSolution getSeatingPlanSolution() {
        return seatingPlanSolution;
    }

}

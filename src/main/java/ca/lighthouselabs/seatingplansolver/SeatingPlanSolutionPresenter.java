package ca.lighthouselabs.seatingplansolver;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanSolutionPresenter {

    private SeatingPlanSolution seatingPlanSolution;

    public SeatingPlanSolutionPresenter(SeatingPlanSolution seatingPlanSolution) {
        this.seatingPlanSolution = seatingPlanSolution;
    }

    public String displaySeatAssignments() {
        StringBuilder sb = new StringBuilder();
        List<Seat> seats = new ArrayList<>(seatingPlanSolution.getSeats());
        seats.sort((seat1, seat2) -> new CompareToBuilder()
                .append(seat1.getTableNumber(), seat2.getTableNumber())
                .append(seat1.getSeatNumber(), seat2.getSeatNumber())
                .toComparison());
        for (Seat seat : seats) {
            sb.append("Table: ");
            sb.append(seat.getTableNumber());
            sb.append(", Seat: ");
            sb.append(seat.getSeatNumber());
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

}

package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Represents an assigned seat at an event. Includes information to uniquely identify the seat such
 * as table and seat number, the Guest assigned to the seat, and also links to the adjacent seats
 * at the same table.
 *
 * @author David VanDusen
 */
// This is an example of a Bean class. It has a collection of properties with getters and setters.
//
// The following @PlanningEntity annotation indicates that this Bean is part of a planning problem
// and that it has a property that can be updated from a set of known values that will change the
// score for the solution that it is part of.
@PlanningEntity
public class Seat {

    private Integer id;

    private Integer tableNumber;

    private Integer seatNumber;

    private Seat left;

    private Seat right;

    private Guest guest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Seat getLeft() {
        return left;
    }

    public void setLeft(Seat left) {
        this.left = left;
    }

    public Seat getRight() {
        return right;
    }

    public void setRight(Seat right) {
        this.right = right;
    }

    // The following @PlanningVariable annotation indicates that this property may be set from the
    // "guests" value range defined in the SeatingPlanSolution class to change the score of the
    // solution that this planning entity is part of. By setting nullable to true this property
    // is allowed to be left empty (as an unassigned seat.)
    @PlanningVariable(valueRangeProviderRefs = {"guests"}, nullable = true)
    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

}

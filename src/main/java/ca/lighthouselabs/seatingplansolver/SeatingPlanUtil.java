package ca.lighthouselabs.seatingplansolver;

import java.util.Collection;
import java.util.HashSet;

/**
 * Contains utility methods for working with SeatingPlan and related objects, such as Seat and
 * Guest.
 *
 * @author David VanDusen
 */
// This is a static utility class, which is a common thing to find in Java projects. It contains
// only static methods that are typically related to a specific class or group of classes. By
// organizing the code into utility classes they can be imported statically into other class files
// and the static methods can be called as if they are defined locally.
public class SeatingPlanUtil {

    // Because this class only contains static methods, there will never be any reason to
    // instantiate it, therefore the default constructor is made private so that it is not
    // accidentally instantiated anywhere in the codebase. A comment is added inside the method to
    // explain why the method has no code.
    private SeatingPlanUtil() {
        // static class
    }

    /**
     * Given a seat, returns the guests in other seats at the same table.
     *
     * @param seat the seat to start from
     * @return guests in all seats at the same table, excluding the given seat
     */
    public static Collection<Guest> getGuestsAtTable(Seat seat) {
        Collection<Guest> guestsAtTable = new HashSet<>();
        Collection<Seat> seatsVisited = new HashSet<>();
        seatsVisited.add(seat);
        // Start going around the table to the left
        Seat nextSeat = seat.getLeft();
        // Go around until arriving back at a previously visited seat or until the table ends
        while (nextSeat != null && !seatsVisited.contains(nextSeat)) {
            seatsVisited.add(nextSeat);
            guestsAtTable.add(nextSeat.getGuest());
            nextSeat = nextSeat.getLeft();
        }
        // Go around to the right
        nextSeat = seat.getRight();
        // Only if all the seats at the table haven't already been visited
        while (nextSeat != null && !seatsVisited.contains(nextSeat)) {
            seatsVisited.add(nextSeat);
            guestsAtTable.add(nextSeat.getGuest());
            nextSeat = nextSeat.getRight();
        }
        return guestsAtTable;
    }

    /**
     * Given a seat, return the guests in the seats in directly adjacent seats.
     *
     * @param seat the seat to start from
     * @return the guests in seats directly to the left or right of the given seat
     */
    public static Collection<Guest> getNeighbouringGuests(Seat seat) {
        Collection<Guest> neighbouringGuests = new HashSet<>();
        Seat leftSeat = seat.getLeft();
        if (leftSeat != null) {
            Guest leftGuest = leftSeat.getGuest();
            if (leftGuest != null) neighbouringGuests.add(leftGuest);
        }
        Seat rightSeat = seat.getRight();
        if (rightSeat != null) {
            Guest rightGuest = rightSeat.getGuest();
            if (rightGuest != null) neighbouringGuests.add(rightGuest);
        }
        return neighbouringGuests;
    }

}

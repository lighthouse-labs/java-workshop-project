package ca.lighthouselabs.seatingplansolver;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains methods for presenting SeatingPlanSolution objects in different ways. Instances of this
 * class should be initialized with a reference to a SeatingPlanSolution object so that it can be
 * used to construct different representations.
 *
 * @author David VanDusen
 */
// This is an example of a presenter, which is an implementation of the visitor pattern. The visitor
// pattern is a way of adding functionality to an existing class without having to add methods to
// it. This is a way of organizing complex functionality for a kind of object into multiple
// different classes that each have their own responsibilities.
public class SeatingPlanSolutionPresenter {

    private SeatingPlanSolution seatingPlanSolution;

    /**
     * Creates a presenter for the given SeatingPlanSolution object.
     *
     * @param seatingPlanSolution the solution to be presented.
     */
    public SeatingPlanSolutionPresenter(SeatingPlanSolution seatingPlanSolution) {
        this.seatingPlanSolution = seatingPlanSolution;
    }

    /**
     * Returns a multi-line string representation of the SeatingPlanSolution that includes
     * information about each seat assignment, the seat identification as well as the assigned
     * guest identification.
     *
     * @return representation of the solution outlining the seat assignments
     */
    // This method uses a StringBuilder instead of concatenating a bunch of String objects together.
    // Because String objects are immutable in Java, each concatenation produces a new String
    // object, which is immediately thrown away due to ensuing concatenations. Java's StringBuilder
    // exists as a computationally efficient way of building complex String objects from many parts.
    public String displaySeatAssignments() {
        StringBuilder sb = new StringBuilder();
        // Get the seats as a list so that they can be sorted before being represented
        List<Seat> seats = new ArrayList<>(seatingPlanSolution.getSeats());
        // Sort the seats first by table number and then by seat number using the Apache
        // commons-lang library's CompareToBuilder class which helps to make comparators for
        // multiple properties
        seats.sort((seat1, seat2) -> new CompareToBuilder()
                .append(seat1.getTableNumber(), seat2.getTableNumber())
                .append(seat1.getSeatNumber(), seat2.getSeatNumber())
                .toComparison());
        for (Seat seat : seats) {
            sb.append("table (");
            sb.append(seat.getTableNumber());
            sb.append("), seat (");
            sb.append(seat.getSeatNumber());
            sb.append("), guest (");
            Guest guest = seat.getGuest();
            if (guest == null) {
                sb.append("[empty seat]");
            } else {
                sb.append(guest.getFirstName());
                sb.append(" ");
                sb.append(guest.getLastName());
                sb.append(", ");
                sb.append(guest.getGender());
            }
            sb.append(")\n");
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the SeatingPlanSolution that includes statistical
     * information about the solution, including counts of various properties.
     *
     * @return representation of statistical information about the solution
     */
    // Although String concatenation can be inefficient in Java, when a single long concatenation
    // operation like the return statement of this method is encountered, merely concatenating is
    // either as or more efficient than using a StringBuilder.
    public String displaySolutionStats() {
        long femaleGuests = seatingPlanSolution.getGuests()
                .stream()
                .filter(guest -> guest.getGender() == Guest.Gender.FEMALE)
                .count();
        long maleGuests = seatingPlanSolution.getGuests()
                .stream()
                .filter(guest -> guest.getGender() == Guest.Gender.MALE)
                .count();
        int seatedGuests = seatingPlanSolution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .map(Seat::getGuest)
                .collect(Collectors.toSet())
                .size();
        long emptySeats = seatingPlanSolution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() == null)
                .count();
        int totalSeats = seatingPlanSolution.getSeats().size();
        int totalGuests = seatingPlanSolution.getGuests().size();
        return "total guests (" + totalGuests +
                "), total seats (" + totalSeats +
                "), empty seats (" + emptySeats +
                "), seated guests (" + seatedGuests +
                "), female guests (" + femaleGuests +
                "), male guests (" + maleGuests + ")";
    }

}

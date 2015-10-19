package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.Collection;
import java.util.stream.Collectors;

import static ca.lighthouselabs.seatingplansolver.SeatingPlanUtil.getGuestsAtTable;
import static ca.lighthouselabs.seatingplansolver.SeatingPlanUtil.getNeighbouringGuests;

/**
 * Calculates the HardSoft score of a SeatingPlanSolution. Optimises for solutions where each guest
 * is assigned exactly one seat, where genders alternate at each table, and where guests' seating
 * preferences are adhered to.
 *
 * @author David VanDusen
 */
public class SeatingPlanScoreCalculator implements EasyScoreCalculator<SeatingPlanSolution> {

    /**
     * Returns a HardSoftScore based on the scoring criteria. Ensuring that every Guest has a seat
     * and that no Guest is assigned multiple seats are prioritised by reducing the hard score.
     * Seat by Seat and Guest by Guest preferences contribute to the soft score.
     *
     * @param solution the solution to score
     * @return the score for the solution
     */
    @Override
    public Score calculateScore(SeatingPlanSolution solution) {
        int hard = getHardScore(solution);
        int soft = getSoftScore(solution);
        return HardSoftScore.valueOf(hard, soft);
    }

    private int getHardScore(SeatingPlanSolution solution) {
        int hard = 0;
        // Remove a hard point for each unseated guest
        hard -= getUnseatedGuests(solution);
        // Remove a hard point for each redundant seat assignment
        hard -= getRedundantSeatAssignments(solution);
        return hard;
    }

    // Calculates how many guests in the guest list do not have a seat assigned to them.
    private int getUnseatedGuests(SeatingPlanSolution solution) {
        // Get the number of guests with seat assignments
        // Start with all the seats
        int seatedGuests = solution.getSeats()
                // Turn the collection into a stream
                .stream()
                // Remove the seats that aren't assigned to anyone
                .filter(seat -> seat.getGuest() != null)
                // Turn it into a collection of guests that have seats
                .map(Seat::getGuest)
                // Collect those values to a Set so that duplicate guests are omitted
                .collect(Collectors.toSet())
                // Get the size of the resulting set
                .size();
        // Get the total number of guests
        int totalGuests = solution.getGuests().size();
        // Return the difference between the total number of guests and the number of guests with
        // seat assignments
        return totalGuests - seatedGuests;
    }

    // Calculates how many seats are assigned to guests that already have a seat.
    private long getRedundantSeatAssignments(SeatingPlanSolution solution) {
        // Sum the redundant seat assignments
        // Start with all the seats
        return solution.getSeats()
                // Turn it into a stream
                .stream()
                // Remove unassigned seats
                .filter(seat -> seat.getGuest() != null)
                // Turn it into a map with guests as keys and the number of seats they have assigned
                // as values
                .collect(Collectors.groupingBy(Seat::getGuest, Collectors.counting()))
                // Get the values from the map - the number of seats each guest has assigned
                .values()
                // Turn that collection into a stream
                .stream()
                // Subtract 1 from each count so that guests that have only one seat end up at 0
                .mapToLong(count -> count - 1)
                // Sum the counts after they have had 1 subtracted from each of them which will
                // result in the number of seats that were assigned to a guest that already had a
                // seat assigned
                .sum();
    }

    // The soft score is the sum of the soft score for each seat.
    private int getSoftScore(SeatingPlanSolution solution) {
        // Sum the soft score for each seat
        // Start with all the seats
        return solution.getSeats()
                // Make a stream
                .stream()
                // Remove unassigned seats
                .filter(seat -> seat.getGuest() != null)
                // Call getSoftScoreForSeat on each remaining seat to produce a collection of ints
                .mapToInt(this::getSoftScoreForSeat)
                // Sum the ints
                .sum();
    }

    // Calculate the soft score for a specific seat.
    private int getSoftScoreForSeat(Seat seat) {
        // Starting at 0
        int soft = 0;
        // Use the static import of the utility class method to get the guests next to the one
        // having its score calculated
        Collection<Guest> neighbouringGuests = getNeighbouringGuests(seat);
        Guest guest = seat.getGuest();
        // Lose a point if the gender of an adjacent guest is the same as this guest
        soft -= getNonAlternatingGenders(guest, neighbouringGuests);
        // Add a point for each adjacent guest in this guest's "seat beside" preferences
        soft += countIntersection(guest.getSeatBeside(), neighbouringGuests);
        // Lose a point for each adjacent guest in this guest's "do not seat beside" preferences
        soft -= countIntersection(guest.getDoNotSeatBeside(), neighbouringGuests);
        // Use the static import of the utility class method to get the guests at the same table as
        // the one having its score calculated
        Collection<Guest> guestsAtTable = getGuestsAtTable(seat);
        // Add a point for each guest at the table in this guest's "seat at same table" preferences
        soft += countIntersection(guest.getSeatAtSameTable(), guestsAtTable);
        // Lose a point for each guest at the table in this guest's "do not seat at same table" preferences
        soft -= countIntersection(guest.getDoNotSeatAtSameTable(), guestsAtTable);
        return soft;
    }

    // Counts how many adjacent guests are of the same gender as the guest.
    private long getNonAlternatingGenders(Guest guest, Collection<Guest> neighbouringGuests) {
        // Start with the guests stream
        return neighbouringGuests.stream()
                // Filter out the ones that are a different gender
                .filter(g -> g.getGender() == guest.getGender())
                // Count what's left
                .count();
    }

    // Counts how many elements are in the intersection of two collections.
    private long countIntersection(Collection<?> collection1, Collection<?> collection2) {
        // Starting with a stream of the first collection
        return collection1.stream()
                // Call collection2.contains(object) on each object in collection1 to get a
                // stream containing only the objects that are in both collections
                .filter(collection2::contains)
                // Count the result
                .count();
    }

}

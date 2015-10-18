package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanScoreCalculator implements EasyScoreCalculator<SeatingPlanSolution> {

    @Override
    public Score calculateScore(SeatingPlanSolution solution) {
        int hard = getHard(solution);
        int soft = getSoft(solution);
        return HardSoftScore.valueOf(hard, soft);
    }

    private int getHard(SeatingPlanSolution solution) {
        int hard = 0;
        hard -= getUnseatedGuests(solution);
        hard -= getMultipleSeatAssignments(solution);
        return hard;
    }

    private int getUnseatedGuests(SeatingPlanSolution solution) {
        int seatedGuests = solution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .map(Seat::getGuest)
                .collect(Collectors.toSet())
                .size();
        int totalGuests = solution.getGuests().size();
        return totalGuests - seatedGuests;
    }

    private long getMultipleSeatAssignments(SeatingPlanSolution solution) {
        return solution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .collect(Collectors.groupingBy(Seat::getGuest))
                .values()
                .stream()
                .mapToInt(seats -> seats.size() - 1)
                .filter(size -> size > 0)
                .sum();
    }

    private int getSoft(SeatingPlanSolution solution) {
        return solution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .mapToInt(this::getSoftForSeat)
                .sum();
    }

    private int getSoftForSeat(Seat seat) {
        int soft = 0;
        Guest guest = seat.getGuest();
        Collection<Guest> neighbouringGuests = getNeighbouringGuests(seat);
        soft -= getNonAlternatingGenders(guest, neighbouringGuests);
        soft += getDesiredNeighbours(guest, neighbouringGuests);
        soft -= getUndesiredNeighbours(guest, neighbouringGuests);
        Collection<Guest> guestsAtTable = getGuestsAtTable(seat);
        soft += getDesiredCompanions(guest, guestsAtTable);
        soft -= getUndesiredCompanions(guest, guestsAtTable);
        return soft;
    }

    private Collection<Guest> getNeighbouringGuests(Seat seat) {
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

    private long getNonAlternatingGenders(Guest guest, Collection<Guest> neighbouringGuests) {
        return neighbouringGuests.stream()
                .filter(g -> g.getGender() == guest.getGender())
                .count();
    }

    private long getDesiredNeighbours(Guest guest, Collection<Guest> neighbouringGuests) {
        return guest.getSeatBeside()
                .stream()
                .filter(neighbouringGuests::contains)
                .count();
    }

    private long getUndesiredNeighbours(Guest guest, Collection<Guest> neighbouringGuests) {
        return guest.getDoNotSeatBeside()
                .stream()
                .filter(neighbouringGuests::contains)
                .count();
    }

    private Collection<Guest> getGuestsAtTable(Seat seat) {
        Collection<Guest> guestsAtTable = new HashSet<>();
        Collection<Seat> seatsVisited = new HashSet<>();
        seatsVisited.add(seat);
        // Start going around the table to the left
        Seat nextSeat = seat.getLeft();
        // Go around until arriving back at the original seat
        // or until the table ends.
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

    private long getDesiredCompanions(Guest guest, Collection<Guest> guestsAtTable) {
        return guest.getSeatAtSameTable()
                .stream()
                .filter(guestsAtTable::contains)
                .count();
    }

    private long getUndesiredCompanions(Guest guest, Collection<Guest> guestsAtTable) {
        return guest.getDoNotSeatAtSameTable()
                .stream()
                .filter(guestsAtTable::contains)
                .count();
    }

}

package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.Collection;
import java.util.stream.Collectors;

import static ca.lighthouselabs.seatingplansolver.SeatingPlanUtil.getGuestsAtTable;
import static ca.lighthouselabs.seatingplansolver.SeatingPlanUtil.getNeighbouringGuests;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanScoreCalculator implements EasyScoreCalculator<SeatingPlanSolution> {

    @Override
    public Score calculateScore(SeatingPlanSolution solution) {
        int hard = getHardScore(solution);
        int soft = getSoftScore(solution);
        return HardSoftScore.valueOf(hard, soft);
    }

    private int getHardScore(SeatingPlanSolution solution) {
        int hard = 0;
        hard -= getUnseatedGuests(solution);
        hard -= getRedundantSeatAssignments(solution);
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

    private long getRedundantSeatAssignments(SeatingPlanSolution solution) {
        return solution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .collect(Collectors.groupingBy(Seat::getGuest, Collectors.counting()))
                .values()
                .stream()
                .mapToLong(count -> count - 1)
                .sum();
    }

    private int getSoftScore(SeatingPlanSolution solution) {
        return solution.getSeats()
                .stream()
                .filter(seat -> seat.getGuest() != null)
                .mapToInt(this::getSoftScoreForSeat)
                .sum();
    }

    private int getSoftScoreForSeat(Seat seat) {
        int soft = 0;
        Guest guest = seat.getGuest();
        Collection<Guest> neighbouringGuests = getNeighbouringGuests(seat);
        soft -= getNonAlternatingGenders(guest, neighbouringGuests);
        soft += countIntersection(guest.getSeatBeside(), neighbouringGuests);
        soft -= countIntersection(guest.getDoNotSeatBeside(), neighbouringGuests);
        Collection<Guest> guestsAtTable = getGuestsAtTable(seat);
        soft += countIntersection(guest.getSeatAtSameTable(), guestsAtTable);
        soft -= countIntersection(guest.getDoNotSeatAtSameTable(), guestsAtTable);
        return soft;
    }

    private long getNonAlternatingGenders(Guest guest, Collection<Guest> neighbouringGuests) {
        return neighbouringGuests.stream()
                .filter(g -> g.getGender() == guest.getGender())
                .count();
    }

    private long countIntersection(Collection<?> collection1, Collection<?> collection2) {
        return collection1.stream()
                .filter(collection2::contains)
                .count();
    }

}

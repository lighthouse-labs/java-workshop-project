package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a solution for a seating plan problem with a score to indicate how successfully it
 * solved the planning problem.
 *
 * @author David VanDusen
 */
// By extending SeatingPlan the need to add the score and problemFacts properties to the plain old
// SeatingPlan class is eliminated. This is an example of the open/closed principle by which we can
// add functionality to an existing class by extending it to prevent the need to change an existing,
// working class by adding irrelevant or confusing new behaviour.
//
// The following @PlanningSolution annotation indicates that this is a solution class for a planning
// problem in conjunction with the implementation of the Solution interface (which takes the kind
// of score that this solution uses as a type parameter.)
@PlanningSolution
public class SeatingPlanSolution extends SeatingPlan implements Solution<HardSoftScore> {

    private HardSoftScore score;

    // It is necessary to implement every method in the Solution interface, even if the
    // functionality of the method is never used.
    @Override
    public Collection<?> getProblemFacts() {
        return null;
    }

    // This method from the SeatingPlan class is overridden in order to annotate the property with
    // @ValueRangeProvider which indicates that this method can be called to get references to
    // objects that can be set on planning variables up change the score of the solution.
    @Override
    @ValueRangeProvider(id = "guests")
    public Set<Guest> getGuests() {
        // The super keyword is a reference to the superclass on which the original method is called
        return super.getGuests();
    }

    // This method from the SeatingPlan class is overridden in order to annotate the property with
    // @PlanningEntityCollectionProperty which indicates that this method can be called to get
    // references to planning entity object which have properties that can be updates to change the
    // score of the solution.
    @Override
    @PlanningEntityCollectionProperty
    public Set<Seat> getSeats() {
        // The super keyword is a reference to the superclass on which the original method is called
        return super.getSeats();
    }

    @Override
    public HardSoftScore getScore() {
        return score;
    }

    @Override
    public void setScore(HardSoftScore score) {
        this.score = score;
    }

}

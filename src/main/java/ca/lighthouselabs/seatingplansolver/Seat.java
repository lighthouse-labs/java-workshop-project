package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created by David VanDusen.
 */
@PlanningEntity
public class Seat {

    private Integer id;

    private Seat left;

    private Seat right;

    private Guest guest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @PlanningVariable(valueRangeProviderRefs = {"guests"}, nullable = true)
    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

}

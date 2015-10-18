package ca.lighthouselabs.seatingplansolver;

import java.util.Set;

/**
 * Created by David VanDusen.
 */
public class SeatingPlan {

    private Set<Guest> guests;

    private Set<Seat> seats;

    public Set<Guest> getGuests() {
        return guests;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

}

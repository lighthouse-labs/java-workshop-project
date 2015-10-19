package ca.lighthouselabs.seatingplansolver;

import java.util.Set;

/**
 * Represents the assignment of event guests at seats. Contains references to all guests as well
 * as all seats, whether the seats are assigned or not. It is expected that some seats do not have
 * assigned guests because most events will have more seats than guests.
 *
 * @author David VanDusen
 */
// This is an example of a Bean class. It has a collection of properties with getters and setters.
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

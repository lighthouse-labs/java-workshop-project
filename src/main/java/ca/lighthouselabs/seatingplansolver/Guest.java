package ca.lighthouselabs.seatingplansolver;

import java.util.Set;

/**
 * Represents a guest at an event. Contains information identifying the individual person as well
 * as preferences they have about seating beside or at the same table as specific other guests.
 *
 * @author David VanDusen
 */
// This is an example of a Bean class. It has a collection of properties with getters and setters.
public class Guest {

    // This is an enum, which is basically a class that there are only a very specific number of
    // instances of. In this case, there are only allowed to be exactly two instances of this class
    // and they are accessible through the constants Gender.MALE and Gender.FEMALE. This makes it
    // possible to guarantee that the gender field below is only allowed exactly one of these
    // values.
    public enum Gender {
        MALE, FEMALE
    }

    private Integer id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Set<Guest> seatAtSameTable;

    private Set<Guest> seatBeside;

    private Set<Guest> doNotSeatAtSameTable;

    private Set<Guest> doNotSeatBeside;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Guest> getSeatAtSameTable() {
        return seatAtSameTable;
    }

    public void setSeatAtSameTable(Set<Guest> seatAtSameTable) {
        this.seatAtSameTable = seatAtSameTable;
    }

    public Set<Guest> getSeatBeside() {
        return seatBeside;
    }

    public void setSeatBeside(Set<Guest> seatBeside) {
        this.seatBeside = seatBeside;
    }

    public Set<Guest> getDoNotSeatAtSameTable() {
        return doNotSeatAtSameTable;
    }

    public void setDoNotSeatAtSameTable(Set<Guest> doNotSeatAtSameTable) {
        this.doNotSeatAtSameTable = doNotSeatAtSameTable;
    }

    public Set<Guest> getDoNotSeatBeside() {
        return doNotSeatBeside;
    }

    public void setDoNotSeatBeside(Set<Guest> doNotSeatBeside) {
        this.doNotSeatBeside = doNotSeatBeside;
    }

}

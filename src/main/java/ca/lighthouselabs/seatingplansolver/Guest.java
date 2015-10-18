package ca.lighthouselabs.seatingplansolver;

import java.util.Set;

/**
 * Created by David VanDusen.
 */
public class Guest {

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

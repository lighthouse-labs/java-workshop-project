package ca.lighthouselabs.seatingplansolver;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains methods for creating fully configured SeatingPlanSolution objects.
 *
 * @author David VanDusen
 */
// This is an example of a factory class. Factory classes have static factory methods which
// construct complex objects that have multiple ways of being constructed. In this case, we have
// defined a way to create and configure a SeatingPlanSolution object from a YAML file. This is an
// alternative to having complex code in the constructors of what should be simple Bean classes,
// and also eliminate the need to have multiple constructors.
public class SeatingPlanSolutionFactory {

    // Because this class only contains static methods, there will never be any reason to
    // instantiate it, therefore the default constructor is made private so that it is not
    // accidentally instantiated anywhere in the codebase. A comment is added inside the method to
    // explain why the method has no code.
    private SeatingPlanSolutionFactory() {
        // static class
    }

    /**
     * Creates a fully configured SeatingPlanSolution from the configuration data in the specified
     * YAML file.
     *
     * @param yamlPath path to the YAML configuration file
     * @return fully configured solution
     */
    // This is an example of the factory method pattern. It takes some configuration as an argument
    // and returns a fully configured object.
    public static SeatingPlanSolution createFromYamlResource(String yamlPath) {
        // Get an InputStream for the specified YAML file with the guest data in it
        InputStream inputStream = SeatingPlanSolutionFactory.class.getClassLoader().getResourceAsStream(yamlPath);
        // Use the Snake YAML library to parse that InputStream into a Java object that can be
        // queried for configuration data.
        List<Map<String, Object>> data = (List<Map<String, Object>>) new Yaml().load(inputStream);
        // Create the solution object
        SeatingPlanSolution solution = new SeatingPlanSolution();
        // Set its guests collection from the parsed configuration data
        solution.setGuests(new HashSet<>(buildGuests(data)));
        // Set its seats collection to objects created based on configuration data
        // TODO The YAML structure could change to include configuration such as table size
        solution.setSeats(new HashSet<>(buildSeats(solution.getGuests().size(), 8)));
        return solution;
    }

    // Take the data from the configuration file and turn it into a collection of Guest objects to
    // set on the SeatingPlan.
    private static Collection<Guest> buildGuests(List<Map<String, Object>> data) {
        // Create a Map of each Guest's id to that Guest instance so that each Guest can be looked
        // up easily by their id.
        Map<Integer,Guest> guests = new HashMap<>();
        // Iterate over the list of guest data from the configuration
        data.forEach(guestData -> {
            // Create a new Guest
            Guest guest = new Guest();
            // Set its properties from the configuration data
            guest.setId((Integer) guestData.get("id"));
            guest.setFirstName((String) guestData.get("firstName"));
            guest.setLastName((String) guestData.get("lastName"));
            guest.setGender(Guest.Gender.valueOf((String) guestData.get("gender")));
            // Put it in the map using its id as the key
            guests.put(guest.getId(), guest);
        });
        // Iterate over the list of guest data again, now that the Map is complete
        data.forEach(guestData -> {
            // For each guest in the Map
            Guest guest = guests.get(guestData.get("id"));
            // Set each of its preference collections using the configuration data, which lists the
            // ids of other guests
            guest.setSeatBeside(buildGuestSet(guests, (Collection) guestData.get("seatBeside")));
            guest.setSeatAtSameTable(buildGuestSet(guests, (Collection) guestData.get("seatAtSameTable")));
            guest.setDoNotSeatBeside(buildGuestSet(guests, (Collection) guestData.get("doNotSeatBeside")));
            guest.setDoNotSeatAtSameTable(buildGuestSet(guests, (Collection) guestData.get("doNotSeatAtSameTable")));
        });
        return guests.values();
    }

    // Using the Map of Guest ids to Guest instances created above, we can turn the lists of ids
    // from the configuration into sets of Guest objects.
    private static Set<Guest> buildGuestSet(Map<?, Guest> guests, Collection<?> guestIds) {
        if (guestIds == null) return new HashSet<>();
        return guestIds.stream()
                .map(guests::get)
                .collect(Collectors.toSet());
    }

    // Iterates over the calculated number of tables and the table size to produce the seats for a
    // SeatingPlan. Each table is a tableSize sized doubly linked list of Seat objects. By setting
    // up the Seat objects in this way, more complex seating arrangements are possible because a
    // single collection of Seat objects can represent tables of varying sizes, with or without
    // edges.
    private static Collection<Seat> buildSeats(int numberOfGuests, int tableSize) {
        int numberOfTables = numberOfGuests / tableSize;
        // Ensure that there are at least enough seats for every guest
        if (numberOfGuests % tableSize != 0) numberOfTables++;
        // Initialize the capacity of the list to the number of seats that are known to be needed
        Collection<Seat> seats = new ArrayList<>(numberOfTables * tableSize);
        // Start at table number 1
        for (int tableNumber = 1; tableNumber <= numberOfTables; tableNumber++) {
            // Create a Seat to start the linked list from
            Seat firstSeat = new Seat();
            // Ensure the seat is added to the list
            seats.add(firstSeat);
            // Set the known properties of the Seat object
            firstSeat.setTableNumber(tableNumber);
            firstSeat.setSeatNumber(1);
            Seat currentSeat = firstSeat;
            // Loop from 2 to tableSize
            for (int seatNumber = 2; seatNumber <= tableSize; seatNumber++) {
                // Create new seat
                Seat newSeat = new Seat();
                // Ensure it is added to the list
                seats.add(newSeat);
                // Set the known properties
                newSeat.setTableNumber(tableNumber);
                newSeat.setSeatNumber(seatNumber);
                // Link this seat to adjacent seats
                currentSeat.setRight(newSeat);
                newSeat.setLeft(currentSeat);
                // Shift over for the next iteration
                currentSeat = newSeat;
            }
            // Finalize the loop by linking the first Seat to the last one
            currentSeat.setRight(firstSeat);
            firstSeat.setLeft(currentSeat);
        }
        return seats;
    }

}

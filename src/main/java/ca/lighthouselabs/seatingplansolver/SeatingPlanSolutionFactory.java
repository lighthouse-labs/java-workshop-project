package ca.lighthouselabs.seatingplansolver;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SeatingPlanSolutionFactory {

    private SeatingPlanSolutionFactory() {
        // static class
    }

    public static SeatingPlanSolution createFromYamlResource(String yamlPath) {
        InputStream inputStream = SeatingPlanSolutionFactory.class.getClassLoader().getResourceAsStream(yamlPath);
        List<Map<String, Object>> data = (List<Map<String, Object>>) new Yaml().load(inputStream);
        SeatingPlanSolution solution = new SeatingPlanSolution();
        solution.setGuests(new HashSet<>(buildGuests(data)));
        // TODO The YAML structure could change to include configuration such as table size (8 hard-coded below)
        solution.setSeats(new HashSet<>(buildSeats(solution.getGuests().size(), 8)));
        return solution;
    }

    private static Collection<Guest> buildGuests(List<Map<String, Object>> data) {
        Map<Integer,Guest> guests = new HashMap<>();
        data.forEach(guestData -> {
            Guest guest = new Guest();
            guest.setId((Integer) guestData.get("id"));
            guest.setFirstName((String) guestData.get("firstName"));
            guest.setLastName((String) guestData.get("lastName"));
            guest.setGender(Guest.Gender.valueOf((String) guestData.get("gender")));
            guests.put(guest.getId(), guest);
        });
        data.forEach(guestData -> {
            Guest guest = guests.get(guestData.get("id"));
            guest.setSeatBeside(buildGuestSet(guests, (Collection) guestData.get("seatBeside")));
            guest.setSeatAtSameTable(buildGuestSet(guests, (Collection) guestData.get("seatAtSameTable")));
            guest.setDoNotSeatBeside(buildGuestSet(guests, (Collection) guestData.get("doNotSeatBeside")));
            guest.setDoNotSeatAtSameTable(buildGuestSet(guests, (Collection) guestData.get("doNotSeatAtSameTable")));
        });
        return guests.values();
    }

    private static Set<Guest> buildGuestSet(Map<?, Guest> guests, Collection<?> guestIds) {
        if (guestIds == null) return new HashSet<>();
        return guestIds.stream()
                .map(guests::get)
                .collect(Collectors.toSet());
    }

    private static Collection<Seat> buildSeats(int numberOfGuests, int tableSize) {
        int numberOfTables = numberOfGuests / tableSize;
        if (numberOfGuests % tableSize != 0) numberOfTables++;
        Collection<Seat> seats = new ArrayList<>(numberOfTables * tableSize);
        for (int tableNumber = 1; tableNumber <= numberOfTables; tableNumber++) {
            Seat firstSeat = new Seat();
            seats.add(firstSeat);
            firstSeat.setSeatNumber(1);
            firstSeat.setTableNumber(tableNumber);
            Seat currentSeat = firstSeat;
            for (int seatNumber = 2; seatNumber <= tableSize; seatNumber++) {
                Seat newSeat = new Seat();
                seats.add(newSeat);
                newSeat.setSeatNumber(seatNumber);
                newSeat.setTableNumber(tableNumber);
                currentSeat.setRight(newSeat);
                newSeat.setLeft(currentSeat);
                currentSeat = newSeat;
            }
            currentSeat.setRight(firstSeat);
            firstSeat.setLeft(currentSeat);
        }
        return seats;
    }

}

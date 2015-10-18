package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanSolverApp {

    private static final Logger LOG = LoggerFactory.getLogger(SeatingPlanSolverApp.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("path to data YAML file must be first arg");
        }
        if (args.length < 2) {
            throw new IllegalArgumentException("path to solver XML file must be second arg");
        }
        LOG.info("building planning problem");
        SeatingPlanSolution planningProblem = buildSolutionFromYaml(args[0]);
        LOG.info("solving planning problem");
        SeatingPlanSolution solution = solveSeatingPlanProblem(planningProblem, args[1]);
        LOG.info("best solution");
        LOG.info(new SeatingPlanSolutionPresenter(solution).display());
    }

    private static SeatingPlanSolution solveSeatingPlanProblem(SeatingPlanSolution planningProblem, String solverXmlPath) {
        Solver solver = SolverFactory.createFromXmlResource(solverXmlPath).buildSolver();
        solver.solve(planningProblem);
        return (SeatingPlanSolution) solver.getBestSolution();
    }

    private static SeatingPlanSolution buildSolutionFromYaml(String yamlPath) {
        InputStream configStream = SeatingPlanSolverApp.class.getClassLoader().getResourceAsStream(yamlPath);
        List<Map<String, Object>> data = (List<Map<String, Object>>) new Yaml().load(configStream);
        SeatingPlanSolution solution = new SeatingPlanSolution();
        solution.setGuests(new HashSet<>(buildGuests(data)));
        // TODO The YAML structure could change to include configuration such as table size (8 hard-coded below)
        solution.setSeats(new HashSet<>(buildSeats(solution.getGuests().size(), 8)));
        return solution;
    }

    private static Collection<Seat> buildSeats(int numberOfGuests, int tableSize) {
        int numberOfTables = numberOfGuests / tableSize;
        if (numberOfGuests % tableSize != 0) numberOfTables++;
        Collection<Seat> seats = new ArrayList<>(numberOfTables * tableSize);
        for (int tableId = 0; tableId < numberOfTables; tableId++) {
            Seat firstSeat = new Seat();
            seats.add(firstSeat);
            firstSeat.setId(tableId * tableSize + 1);
            Seat currentSeat = firstSeat;
            for (int seatId = 1; seatId < tableSize; seatId++) {
                Seat newSeat = new Seat();
                seats.add(newSeat);
                newSeat.setId(tableId * tableSize + seatId + 1);
                currentSeat.setRight(newSeat);
                newSeat.setLeft(currentSeat);
                currentSeat = newSeat;
            }
            currentSeat.setRight(firstSeat);
            firstSeat.setLeft(currentSeat);
        }
        return seats;
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
            Map<String,Collection<Guest>> requests = new HashMap<>();
            guest.setSeatBeside(new HashSet<>());
            requests.put("seatBeside", guest.getSeatBeside());
            guest.setSeatAtSameTable(new HashSet<>());
            requests.put("seatAtSameTable", guest.getSeatAtSameTable());
            guest.setDoNotSeatBeside(new HashSet<>());
            requests.put("doNotSeatBeside", guest.getDoNotSeatBeside());
            guest.setDoNotSeatAtSameTable(new HashSet<>());
            requests.put("doNotSeatAtSameTable", guest.getDoNotSeatAtSameTable());
            requests.forEach((guestDataSetName, guestSet) -> {
                Collection ids = (Collection) guestData.get(guestDataSetName);
                if (ids != null) {
                    guestSet.addAll((Collection<Guest>) ids.stream().map(guests::get).collect(Collectors.toSet()));
                }
            });
        });
        return guests.values();
    }

}

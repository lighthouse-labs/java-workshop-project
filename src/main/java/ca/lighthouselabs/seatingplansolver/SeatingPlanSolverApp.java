package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Program that solves a planning problem involving seating guests at an event using configuration
 * supplied in classpath resources.
 *
 * @author David VanDusen
 */
// This is an application class that has a code entry point so that it can be run as a program. It
// is the only place where standard IO is being interacted with, and that it its primary
// responsibility so it has only one method for accomplishing everything it needs to.
public class SeatingPlanSolverApp {

    // By using a Logger object configured in this way using SLF4J, the logging code in the
    // application is decoupled from the logging configuration, including the logging level and
    // which output streams are used.
    private static final Logger LOG = LoggerFactory.getLogger(SeatingPlanSolverApp.class);

    /**
     * Takes the path to a YAML file containing data about a SeatingPlanSolution and the path to an
     * XML file containing configuration for a Solver and solves the planning problem, logging the
     * best solution.
     *
     * @param args 0 - path to a problem YAML file, 1 - the path to a solver XML file
     */
    // This method signature is required exactly as it is in order for this method to act as a code
    // entry point for the program. The args parameter is the list of space separated text arguments
    // that followed the program invocation.
    public static void main(String[] args) throws Exception {
        if (args.length < 1) throw new IllegalArgumentException("path to problem YAML file must be first arg");
        if (args.length < 2) throw new IllegalArgumentException("path to solver XML file must be second arg");
        LOG.info("Building seating plan problem from file (" + args[0] + ").");
        SeatingPlanSolution planningProblem = SeatingPlanSolutionFactory.createFromYamlResource(args[0]);
        LOG.info("Building solver from file (" + args[1] + ").");
        Solver solver = SolverFactory.createFromXmlResource(args[1]).buildSolver();
        solver.solve(planningProblem);
        SeatingPlanSolution solution = (SeatingPlanSolution) solver.getBestSolution();
        SeatingPlanSolutionPresenter presenter = new SeatingPlanSolutionPresenter(solution);
        LOG.info("Solution stats: " + presenter.displaySolutionStats() + ".");
        LOG.info("Solution seat assignments:\n" + presenter.displaySeatAssignments());
    }

}

package ca.lighthouselabs.seatingplansolver;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by David VanDusen.
 */
public class SeatingPlanSolverApp {

    private static final Logger LOG = LoggerFactory.getLogger(SeatingPlanSolverApp.class);

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("path to problem YAML file must be first arg");
        if (args.length < 2) throw new IllegalArgumentException("path to solver XML file must be second arg");
        LOG.info("Building seating plan problem from \"" + args[0] + "\".");
        SeatingPlanSolution planningProblem = SeatingPlanSolutionFactory.createFromYamlResource(args[0]);
        LOG.info("Building solver from \"" + args[1] + "\".");
        Solver solver = SolverFactory.createFromXmlResource(args[1]).buildSolver();
        solver.solve(planningProblem);
        SeatingPlanSolution solution = (SeatingPlanSolution) solver.getBestSolution();
        LOG.info("Solution details:\n" + new SeatingPlanSolutionPresenter(solution).displaySeatAssignments());
    }

}

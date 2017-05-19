package org.fmarin.admintournoi.admin.pool;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PoolGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(PoolGenerationService.class);

    public void compose(List<Integer> teams) {
        logger.debug(String.valueOf(teams));
        Map<Integer, Long> teamsCountByLevel = teams.stream().collect(Collectors.groupingBy(level -> level, Collectors.counting()));


        Model model = new Model("test");
        IntVar[][] vars = new IntVar[4][16];
        for (int level = 0; level < 4; level++) {
            for (int pool = 0; pool < 16; pool++) {
                String varName = "L" + level + "_P" + pool;
                vars[level][pool] = model.intVar(varName, 0, 3, false);
            }
        }

        // Nombre d'équipes du niveau affectées
        for (int level = 0; level < 4; level++) {
            model.sum(vars[level], "=", teamsCountByLevel.get(level + 1).intValue()).post();
        }

        // Nombre d'équipes par pool
        IntVar[][] reversedVars = getReversedMatrix(vars);
        for (int pool = 0; pool < 16; pool++) {
            model.sum(reversedVars[pool], "=", 3).post();
        }

        int[] levelCoeff = new int[]{1, 2, 3, 4};

        // Niveau de pool
        IntVar[] poolsLevel = new IntVar[16];
        for (int pool = 0; pool < 16; pool++) {
            poolsLevel[pool] = model.intVar("PoolLevel" + pool, 3, 12);
            model.scalar(reversedVars[pool], levelCoeff, "=", poolsLevel[pool]).post();
        }

        IntVar[] poolsLevelDiff = new IntVar[15];
        for (int pool = 0; pool < 15; pool++) {
            poolsLevelDiff[pool] = model.intVar("PoolLevelDiff" + pool + "_" + (pool + 1), -12, 12);
            model.sum(new IntVar[]{poolsLevel[pool], model.intMinusView(poolsLevel[pool + 1])}, "=", poolsLevelDiff[pool]).post();
            poolsLevelDiff[pool] = model.intAbsView(poolsLevelDiff[pool]);
        }

        IntVar objective = model.intVar("PoolsLevelHomogeneity", 0, 16);
        model.sum(poolsLevelDiff, "=", objective).post();
        model.setObjective(Model.MINIMIZE, objective);

        int[][] solution = new int[4][16];
        while(model.getSolver().solve()) {
            logger.debug("SOLUTION : " + objective.getValue());
            for (int level = 0; level < 4; level++) {
                for (int pool = 0; pool < 16; pool++) {
                    solution[level][pool] = vars[level][pool].getValue();
                }
            }
        }
        // https://stackoverflow.com/questions/30065577/how-to-loop-and-print-2d-array-using-java-8
        logger.debug(String.valueOf(solution));
    }

    IntVar[][] getReversedMatrix(IntVar[][] matrix) {
        IntVar[][] reversedMatrix = new IntVar[16][4];
        for (int level = 0; level < 4; level++) {
            for (int pool = 0; pool < 16; pool++) {
                reversedMatrix[pool][level] = matrix[level][pool];
            }
        }
        return reversedMatrix;
    }

}

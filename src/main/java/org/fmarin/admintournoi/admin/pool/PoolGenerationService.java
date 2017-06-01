package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.fmarin.admintournoi.subscription.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PoolGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(PoolGenerationService.class);

    private static Integer TEAMS_COUNT_BY_POOL = 3;
    private static List<Integer> COEFFS = Lists.newArrayList(1, 3, 5, 8);

    private final PoolRepository poolRepository;
    private final RoundRepository roundRepository;

    @Autowired
    public PoolGenerationService(PoolRepository poolRepository, RoundRepository roundRepository) {
        this.poolRepository = poolRepository;
        this.roundRepository = roundRepository;
    }

    @Async
    public void generatePools(Round round) {
        List<Team> teams = round.getTeams();
        Integer poolsCount = teams.size() / TEAMS_COUNT_BY_POOL;
        Map<Integer, Long> teamsCountByLevel = countTeamsByLevel(teams);
        int[][] poolsModel = findFirstRoundLevelDistribution(teamsCountByLevel, poolsCount);
        List<Pool> pools = affectTeamsToPool(poolsModel, teams, round);
        pools.parallelStream().forEach(poolRepository::save);
        round.setStatus(RoundStatus.COMPOSED);
        roundRepository.save(round);
    }

    Map<Integer, Long> countTeamsByLevel(List<Team> teams) {
        return teams.stream().collect(Collectors.groupingBy(team -> team.getLevel().getValue(), Collectors.counting()));
    }

    int[][] findFirstRoundLevelDistribution(Map<Integer, Long> countByLevel, Integer poolsCount) {
        logger.debug(String.valueOf(countByLevel));

        Integer levelsCount = countByLevel.size();

        Model model = new Model("First Round Model");
        IntVar[][] levelCountForPools = new IntVar[levelsCount][poolsCount];

        for (int level = 0; level < levelsCount; level++) {
            for (int pool = 0; pool < poolsCount; pool++) {
                String varName = "L" + (level + 1) + "_P" + pool;
                levelCountForPools[level][pool] = model.intVar(varName, 0, TEAMS_COUNT_BY_POOL, false);
            }
        }

        // Nombre d'équipes du niveau affectées
        for (int level = 0; level < levelsCount; level++) {
            model.sum(levelCountForPools[level], "=", countByLevel.get(level + 1).intValue()).post();
        }

        // Nombre d'équipes par poule
        IntVar[][] reversedVars = getReversedMatrix(levelCountForPools, poolsCount, levelsCount);
        for (int pool = 0; pool < poolsCount; pool++) {
            model.sum(reversedVars[pool], "=", TEAMS_COUNT_BY_POOL).post();
        }

        // Niveau de pool
        int[] coeff = COEFFS.subList(0, levelsCount).stream().mapToInt(i->i).toArray();
        IntVar[] poolsLevel = new IntVar[poolsCount];
        for (int pool = 0; pool < poolsCount; pool++) {
            poolsLevel[pool] = model.intVar("PoolLevel" + pool, TEAMS_COUNT_BY_POOL, TEAMS_COUNT_BY_POOL * levelsCount);
            model.scalar(reversedVars[pool], coeff, "=", poolsLevel[pool]).post();
        }

        int poolsCountMinus1 = poolsCount - 1;
        IntVar[] poolsLevelDiff = new IntVar[poolsCountMinus1];
        for (int pool = 0; pool < poolsCountMinus1; pool++) {
            poolsLevelDiff[pool] = model.intVar("PoolLevelDiff" + pool + "_" + (pool + 1), -TEAMS_COUNT_BY_POOL * levelsCount, TEAMS_COUNT_BY_POOL * levelsCount);
            model.sum(new IntVar[]{poolsLevel[pool], model.intMinusView(poolsLevel[pool + 1])}, "=", poolsLevelDiff[pool]).post();
            poolsLevelDiff[pool] = model.intAbsView(poolsLevelDiff[pool]);
        }

        IntVar objective = model.intVar("PoolsLevelHomogeneity", 0, poolsCount);
        model.sum(poolsLevelDiff, "=", objective).post();
        model.setObjective(Model.MINIMIZE, objective);

        int[][] solution = new int[levelsCount][poolsCount];
        while (model.getSolver().solve()) {
            logger.debug("SOLUTION : " + objective.getValue());
            for (int level = 0; level < levelsCount; level++) {
                for (int pool = 0; pool < poolsCount; pool++) {
                    solution[level][pool] = levelCountForPools[level][pool].getValue();
                }
            }
        }
        logger.debug(Arrays.deepToString(solution));
        return solution;
    }

    List<Pool> affectTeamsToPool(int[][] poolsModel, List<Team> teams, Round round) {
        Map<Integer, List<Team>> teamsToAffectByLevel = teams.stream().collect(Collectors.groupingBy(team -> team.getLevel().getValue(), Collectors.toList()));
        List<Pool> pools = Lists.newArrayList();
        for (int levelIndex = 0; levelIndex < poolsModel.length; levelIndex++) {
            for (int poolIndex = 0; poolIndex < poolsModel[levelIndex].length; poolIndex++) {
                Pool pool;
                if (pools.size() <= poolIndex) {
                    pool = new Pool();
                    pool.setPosition(poolIndex + 1);
                    pool.setRound(round);
                    pools.add(pool);
                }
                else {
                    pool = pools.get(poolIndex);
                }
                for (int teamCount = 0; teamCount < poolsModel[levelIndex][poolIndex]; teamCount++) {
                    Team team = teamsToAffectByLevel.get(levelIndex + 1).remove(0);
                    pool.addTeam(team);
                }
            }
        }
        return pools;
    }

    IntVar[][] getReversedMatrix(IntVar[][] matrix, int poolsCount, int levelsCount) {
        IntVar[][] reversedMatrix = new IntVar[poolsCount][levelsCount];
        for (int level = 0; level < levelsCount; level++) {
            for (int pool = 0; pool < poolsCount; pool++) {
                reversedMatrix[pool][level] = matrix[level][pool];
            }
        }
        return reversedMatrix;
    }

}

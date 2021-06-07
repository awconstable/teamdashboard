package team.dashboard.web.metrics.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import team.dashboard.web.metrics.domain.TeamCollectionStat;
import team.dashboard.web.metrics.domain.TeamMetric;
import team.dashboard.web.metrics.domain.TeamMetricTrend;
import team.dashboard.web.metrics.domain.TeamMetricType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class TeamMetricAggregationRepositoryImpl implements TeamMetricAggregationRepository
    {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TeamMetricAggregationRepositoryImpl(MongoTemplate mongoTemplate)
        {
        this.mongoTemplate = mongoTemplate;
        }

    private LocalDate getDateMonthsAgo(int months){
        return LocalDate.ofInstant(LocalDateTime.now().minusMonths(months).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);   
    }
    
    @Override
    public List<TeamMetricTrend> getDailyChildMetrics(String[] slugs, TeamMetricType metricType, int months)
        {
        ProjectionOperation dateProjection = project()
                .and("teamMetricType").as("teamMetricType")
                .and("value").as("value")
                .and("date").extractYear().as("year")
                .and("date").extractMonth().as("month")
                .and("date").extractDayOfMonth().as("day");

        GroupOperation groupBy = group("teamMetricType", "year", "month", "day")
                .avg("value").as("avg")
                .sum("value").as("sum")
                .count().as("count");

        TypedAggregation<TeamMetric> agg = Aggregation.newAggregation(TeamMetric.class,
                match(Criteria.where("teamId").in(slugs).and("teamMetricType").is(metricType).and("date").gt(getDateMonthsAgo(months))),
                dateProjection,
                groupBy,
                sort(Sort.Direction.ASC, "year", "month", "day"));

        AggregationResults<TeamMetricTrend> result = mongoTemplate.aggregate(agg, TeamMetricTrend.class);

        return result.getMappedResults();
        }

    @Override
    public Set<TeamCollectionStat> getCollectionStats(String[] slugs, Integer year, Integer month)
        {
        ProjectionOperation dateProjection = project()
                .and("teamId").as("teamId")
                .and("date").extractYear().as("year")
                .and("date").extractMonth().as("month");

        GroupOperation groupBy = group("teamId", "year", "month")
                .count().as("count");

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

        TypedAggregation<TeamMetric> agg = Aggregation.newAggregation(TeamMetric.class,
                match(Criteria.where("teamId").in(slugs)
                        .and("date").gte(startDate).lte(endDate)
                        .and("teamMetricType").nin(TeamMetricType.TEST_AUTOMATION_EXECUTION_COUNT, TeamMetricType.TEST_TOTAL_EXECUTION_COUNT)),
                dateProjection,
                groupBy,
                sort(Sort.Direction.ASC, "year", "month", "teamId"));

        AggregationResults<TeamCollectionStat> results = mongoTemplate.aggregate(agg, TeamCollectionStat.class);
        
        return new HashSet<>(results.getMappedResults());
        }
    }

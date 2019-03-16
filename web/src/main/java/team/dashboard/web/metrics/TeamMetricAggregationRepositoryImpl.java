package team.dashboard.web.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class TeamMetricAggregationRepositoryImpl implements TeamMetricAggregationRepository
    {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TeamMetricAggregationRepositoryImpl(MongoTemplate mongoTemplate)
        {
        this.mongoTemplate = mongoTemplate;
        }

    @Override
    public List<TeamMetricTrend> getMonthlyMetrics(String slug, TeamMetricType metricType)
        {

        ProjectionOperation dateProjection = project()
                .and("teamId").as("teamId")
                .and("teamMetricType").as("teamMetricType")
                .and("value").as("value")
                .and("date").extractYear().as("year")
                .and("date").extractMonth().as("month");

        GroupOperation groupBy = group("teamId", "teamMetricType", "year", "month")
                .avg("value").as("avg")
                .sum("value").as("sum")
                .count().as("count");

        TypedAggregation<TeamMetric> agg = Aggregation.newAggregation(TeamMetric.class,
                match(Criteria.where("teamId").is(slug).and("teamMetricType").is(metricType)),
                dateProjection,
                groupBy,
                sort(Sort.Direction.ASC, "year", "month"));

        AggregationResults<TeamMetricTrend> result = mongoTemplate.aggregate(agg, TeamMetricTrend.class);

        System.out.println(result.getRawResults());

        return result.getMappedResults();
        }

    @Override
    public List<TeamMetricTrend> getMonthlyChildMetrics(String[] slugs, TeamMetricType metricType)
        {
        ProjectionOperation dateProjection = project()
                .and("teamMetricType").as("teamMetricType")
                .and("value").as("value")
                .and("date").extractYear().as("year")
                .and("date").extractMonth().as("month");

        GroupOperation groupBy = group("teamMetricType", "year", "month")
                .avg("value").as("avg")
                .sum("value").as("sum")
                .count().as("count");

        TypedAggregation<TeamMetric> agg = Aggregation.newAggregation(TeamMetric.class,
                match(Criteria.where("teamId").in(slugs).and("teamMetricType").is(metricType)),
                dateProjection,
                groupBy,
                sort(Sort.Direction.ASC, "year", "month"));

        AggregationResults<TeamMetricTrend> result = mongoTemplate.aggregate(agg, TeamMetricTrend.class);

        System.out.println(result.getRawResults());

        return result.getMappedResults();
        }
    }

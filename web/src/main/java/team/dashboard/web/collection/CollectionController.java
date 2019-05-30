package team.dashboard.web.collection;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.options.elements.Fill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.metrics.TeamCollectionStat;
import team.dashboard.web.metrics.TeamMetricRepository;
import team.dashboard.web.metrics.TeamMetricsController;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/collection-stats")
public class CollectionController
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamRestRepository teamRepository;

    @Autowired
    public CollectionController(TeamMetricRepository teamMetricRepository, TeamRestRepository teamRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.teamRepository = teamRepository;
        }

    @GetMapping("/{teamId}/")
    @ResponseBody
    public String chartCollectionStats(@PathVariable String teamId)
        {
        ArrayList<String> labels = new ArrayList<>();
        LinkedHashMap<String, Integer> teamCollectionStats = new LinkedHashMap<>();
        ArrayList<LineDataset> datasets = new ArrayList<>();

        Team team = teamRepository.findByTeamSlug(teamId);

        ArrayList<String> teams = new ArrayList<>();
        teams.add(teamId);

        for (TeamRelation child : team.getChildren())
            {
            teams.add(child.getSlug());
            }

        List<TeamCollectionStat> stats = teamMetricRepository.getMonthlyCollectionStats(teams.toArray(new String[]{}));

        for (TeamCollectionStat stat : stats)
            {
            String label = TeamMetricsController.createDataPointLabel(stat.getYear(), stat.getMonth());
            if (!labels.contains(label))
                {
                labels.add(label);
                }
            teamCollectionStats.put(stat.getTeamId() + label, stat.getCount());
            }

        for (String teamId2 : teams)
            {
            Color lineColour = Color.random();
            LineDataset dataset = new LineDataset().setLabel(teamId2);
            dataset.setFill(new Fill(false));
            dataset.setBackgroundColor(Color.TRANSPARENT);
            dataset.setBorderColor(lineColour);
            dataset.setBorderWidth(4);
            ArrayList<Color> pointsColors = new ArrayList<>();
            pointsColors.add(lineColour);
            dataset.setPointBackgroundColor(pointsColors);
            dataset.setYAxisID("y-axis-1");

            for (String label : labels)
                {
                dataset.addData(teamCollectionStats.getOrDefault(teamId2 + label, 0));
                }
            datasets.add(dataset);
            }

        for (TeamCollectionStat stat : stats)
            {
            String label = TeamMetricsController.createDataPointLabel(stat.getYear(), stat.getMonth());
            teamCollectionStats.put(stat.getTeamId() + label, stat.getCount());
            }

        LineData data = new LineData()
                .addLabels(labels.toArray(new String[]{}));
        datasets.forEach(data::addDataset);

        ObjectWriter writer = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .forType(LineData.class);

        try
            {
            return writer.writeValueAsString(data);
            } catch (JsonProcessingException e)
            {
            throw new RuntimeException(e);
            }
        }
    }

package team.dashboard.web.metrics;

import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.dashboard.web.team.Team;
import team.dashboard.web.team.TeamRelation;
import team.dashboard.web.team.TeamRestRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
@RequestMapping("/metrics")
public class TeamMetricsController
    {

    private final TeamMetricRepository teamMetricRepository;

    private final TeamRestRepository teamRepository;

    @Autowired
    public TeamMetricsController(TeamMetricRepository teamMetricRepository, TeamRestRepository teamRepository)
        {
        this.teamMetricRepository = teamMetricRepository;
        this.teamRepository = teamRepository;
        }

    @GetMapping("/{metricType}/{teamId}/{date}/{value}")
    @ResponseBody
    public TeamMetric metricingest(@PathVariable String metricType, @PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable Double value, HttpServletResponse response)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, date);

        if (metric.isPresent())
            {
            teamMetricRepository.deleteById(metric.get().getId());
            }

        TeamMetric newMetric = new TeamMetric(teamId, type, value, date);
        teamMetricRepository.save(newMetric);

        return newMetric;

        }

    @GetMapping("/{metricType}/{teamId}/{date}")
    @ResponseBody
    public TeamMetric getmetric(@PathVariable String metricType, @PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpServletResponse response)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        if (type == null)
            {
            System.out.println("Unknown metric type: " + metricType);
            return null;
            }

        Optional<TeamMetric> metric = teamMetricRepository.findByTeamIdAndTeamMetricTypeAndDate(teamId, type, date);

        if (metric.isPresent())
            {
            return metric.get();
            }

        return null;
        }

    private TableRow createTableRow(int year, int month, int dayOfMonth, double rowValue)
        {
        TableRow tr = new TableRow();

        DateValue date = new DateValue(year, month - 1, dayOfMonth);

        tr.addCell(new TableCell(date
                , Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", " + year));
        tr.addCell(rowValue);

        return tr;
        }

    @GetMapping("/{metricType}/{teamId}/")
    @ResponseBody
    public String metrictrend(Model model, @PathVariable String metricType, @PathVariable String teamId) throws Exception
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("month", ValueType.DATE, "Month"));
        data.addColumn(new ColumnDescription(type.getKey(), ValueType.NUMBER, type.getName()));

        try
            {
            ArrayList<TableRow> rows = new ArrayList<>();

            Team team = teamRepository.findByTeamSlug(teamId);

            ArrayList<String> teams = new ArrayList<>();
            teams.add(teamId);

            for (TeamRelation child : team.getChildren())
                {
                teams.add(child.getSlug());
                }

            List<TeamMetricTrend> metrics = teamMetricRepository.getMonthlyChildMetrics(teams.toArray(new String[]{}), type);

            for (TeamMetricTrend metric : metrics)
                {

                Double value;

                if (TeamMetricType.AggMethod.AVG.equals(metric.getTeamMetricType().getMethod()))
                    {
                    value = metric.getAvg();
                    } else if (TeamMetricType.AggMethod.SUM.equals(metric.getTeamMetricType().getMethod()))
                    {
                    value = metric.getSum();
                    } else
                    {
                    throw new Exception("Unknown AggMethod type");
                    }

                rows.add(createTableRow(metric.getYear(), metric.getMonth(), 1, value));
                }

            if (metrics.isEmpty())
                {
                LocalDate now = LocalDate.now();
                rows.add(createTableRow(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), 0));
                }

            data.addRows(rows);

            } catch (TypeMismatchException e)
            {
            System.out.print(e);
            }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

        }

    @GetMapping(value = "/capture/{slug}/")
    public String graph(Model model, @PathVariable String slug)
        {
        model.addAttribute("slug", slug);
        return "capture";
        }

    }
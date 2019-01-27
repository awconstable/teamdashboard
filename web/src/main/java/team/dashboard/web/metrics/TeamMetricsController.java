package team.dashboard.web.metrics;

import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
public class TeamMetricsController
    {

    @Autowired
    private TeamMetricRepository teamMetricRepository;

    @RequestMapping("/{metricType}/{teamId}/{date}/{value}")
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

    @RequestMapping("/{metricType}/{teamId}/{date}")
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

    private TableRow createTableRow(int year, Month month, int dayOfMonth, double rowValue)
        {
        TableRow tr = new TableRow();

        GregorianCalendar cal = new GregorianCalendar(year, month.getValue(), dayOfMonth);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        tr.addCell(new TableCell(new DateValue(cal)
                , month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
        tr.addCell(rowValue);

        return tr;
        }

    @RequestMapping("/{metricType}/{teamId}/")
    @ResponseBody
    public String metrictrend(Model model, @PathVariable String metricType, @PathVariable String teamId)
        {

        TeamMetricType type = TeamMetricType.get(metricType);

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("month", ValueType.DATE, "Month"));
        data.addColumn(new ColumnDescription(type.getKey(), ValueType.NUMBER, type.getName()));

        try
            {
            ArrayList<TableRow> rows = new ArrayList<>();

            List<TeamMetric> metrics = teamMetricRepository.findByTeamIdIgnoreCaseAndTeamMetricTypeOrderByDateDesc(teamId, type);

            //TODO fill in months that are blank

            for (TeamMetric metric : metrics)
                {
                rows.add(createTableRow(metric.getDate().getYear(), metric.getDate().getMonth(), metric.getDate().getDayOfMonth(), metric.getValue()));
                }

            data.addRows(rows);

            } catch (TypeMismatchException e)
            {
            System.out.print(e);
            }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

        }

    @GetMapping("/capture/{teamId}/")
    public String graph(Model model, @PathVariable String teamId)
        {
        model.addAttribute("team", teamId);
        return "capture";
        }

    }
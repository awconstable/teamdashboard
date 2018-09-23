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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
public class CycleTimeController
    {

    @Autowired
    private CycleTimeRepository repository;

    @RequestMapping("/cycletime/{teamId}/{date}/{cycleTimeSubmission}")
    public void rating(@PathVariable String teamId, @PathVariable @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable int cycleTimeSubmission, HttpServletResponse response) throws Exception
        {

        Optional<CycleTime> time = repository.findByTeamIdAndDate(teamId, date);

        if(time.isPresent()){
            repository.deleteById(time.get().getId());
        }

        CycleTime cycleTime = new CycleTime(teamId, cycleTimeSubmission, date);
            repository.save(cycleTime);
            
            response.sendRedirect("/graph/" + teamId + "/");
        }

    @RequestMapping("/cycletime/{teamId}/")
    @ResponseBody
    public String rollingTrend(Model model, @PathVariable String teamId) {

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("month", ValueType.DATE, "Month"));
        data.addColumn(new ColumnDescription("cycletime", ValueType.NUMBER, "cycletime"));

        try {
            ArrayList<TableRow> rows = new ArrayList<>();

            List<CycleTime> times = repository.findByTeamIdIgnoreCaseOrderByDateDesc(teamId);

            //TODO fill in months that are blank

            for (CycleTime time:times) {
                TableRow tr = new TableRow();

                GregorianCalendar cal = new GregorianCalendar(time.getDate().getYear(), time.getDate().getMonthValue(), time.getDate().getDayOfMonth());
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));

                tr.addCell(new TableCell(new DateValue(cal)
                        , time.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
                tr.addCell(time.getCycleTime());
                rows.add(tr);
            }

            data.addRows(rows);

        } catch (TypeMismatchException e) {
            System.out.print(e);
        }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

        }

    @GetMapping("/graph/{teamId}/")
    public String graph(Model model, @PathVariable String teamId) {
        model.addAttribute("team", teamId);
        return "trendgraph";
    }

    }
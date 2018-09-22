package team.dashboard.web.trend;

import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.mobile.device.Device;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by awconstable on 26/02/2017.
 */
@Controller
public class RatingTrendController {

    @Autowired
    private RatingTrendService ratingTrendService;

    @RequestMapping("/trend/{team}")
    @ResponseBody
    public String rollingTrend(Model model, @PathVariable String team) {

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("month", ValueType.DATE, "Month"));
        data.addColumn(new ColumnDescription("satisfaction", ValueType.NUMBER, "Satisfaction"));
        data.addColumn(new ColumnDescription("responses", ValueType.NUMBER, "Responses"));

        try {
            ArrayList<TableRow> rows = new ArrayList<>();

            ArrayList<RatingTrend> trends = ratingTrendService.getTrendData(team);

            for (RatingTrend trend:trends) {
                TableRow tr = new TableRow();
                tr.addCell(new TableCell(new DateValue(trend.getTrendDate().getYear(), trend.getTrendDate().getMonth().getValue() - 1, 1)
                        , trend.getTrendDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
                tr.addCell(trend.getAvgRating());
                tr.addCell(trend.getResponseCount());
                rows.add(tr);
            }

            data.addRows(rows);

        } catch (TypeMismatchException e) {
            System.out.print(e);
        }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

    }


    @GetMapping("/")
    public String index(Device device) throws Exception {

        if(device.isMobile() || device.isTablet()){
            return "thankyou";
        }

        return "trendgraph";
    }

}

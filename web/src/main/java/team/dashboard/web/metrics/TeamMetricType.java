package team.dashboard.web.metrics;

import be.ceau.chart.color.Color;

public enum TeamMetricType
    {
        LEAD_TIME_FOR_CHANGE("lead_time", "Average Lead Time for Changes", AggMethod.AVG, new Color(0, 123, 255)),
        CYCLE_TIME("cycletime", "Average Cycle Time", AggMethod.AVG, Color.AQUA),
        DEPLOYMENT_FREQUENCY("deployment_frequency", "Deployment Frequency", AggMethod.AVG, Color.GOLD),
        INCIDENTS_DUE_TO_CHANGE("incidents_due_to_change", "Incidents due to change", AggMethod.SUM, Color.RED),
        PRODUCTION_DEFECTS("production_defects", "Production Defects", AggMethod.SUM, Color.DARK_SALMON),
        TEAM_HAPPINESS("team_happiness", "Team Happiness", AggMethod.AVG, Color.DARK_GREEN),
        CUSTOMER_SATISFACTION("customer_satisfaction", "Customer Satisfaction", AggMethod.AVG, Color.BLUE_VIOLET),
        MTTR("mttr", "Mean Time to Recovery", AggMethod.AVG, Color.KHAKI),
        CHANGE_FAILURE_RATE("change_failure_rate", "Change Failure Rate %age", AggMethod.AVG, Color.PALE_TURQUOISE),
        BATCH_SIZE("batch_size", "Batch Size", AggMethod.AVG, Color.LAVENDER);

    private AggMethod method;
    private String key;
    private String name;
    private Color graphColour;

    TeamMetricType(String key, String name, AggMethod method, Color graphColour)
        {
        this.key = key;
        this.name = name;
        this.method = method;
        this.graphColour = graphColour;
        }

    public AggMethod getMethod()
        {
        return method;
        }

    public static TeamMetricType get(String key)
        {

        TeamMetricType[] types = TeamMetricType.values();

        for (TeamMetricType type : types)
            {
            if (type.key.equals(key))
                {
                return type;
                }
            }
        return null;
        }

    public String getKey()
        {
        return key;
        }

    public String getName()
        {
        return name;
        }

    public Color getGraphColour()
        {
        return graphColour;
        }

    @Override
    public String toString()
        {
        return "TeamMetricType{" +
                "method=" + method +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", graphColour=" + graphColour +
                '}';
        }

    public enum AggMethod
        {
            AVG, SUM
        }
    }

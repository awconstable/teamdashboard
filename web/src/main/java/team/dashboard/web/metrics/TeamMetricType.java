package team.dashboard.web.metrics;

public enum TeamMetricType
    {
        CYCLE_TIME("cycletime", "Average Cycle Time", AggMethod.AVG),
        DEPLOYMENT_FREQUENCY("deployment_frequency", "Deployment Frequency", AggMethod.AVG),
        INCIDENTS_DUE_TO_CHANGE("incidents_due_to_change", "Incidents due to change", AggMethod.SUM),
        PRODUCTION_DEFECTS("production_defects", "Production Defects", AggMethod.SUM),
        TEAM_HAPPINESS("team_happiness", "Team Happiness", AggMethod.AVG),
        CUSTOMER_SATISFACTION("customer_satisfaction", "Customer Satisfaction", AggMethod.AVG);

    private AggMethod method;
    private String key;
    private String name;

    TeamMetricType(String key, String name, AggMethod method)
        {
        this.key = key;
        this.name = name;
        this.method = method;
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

    @Override
    public String toString()
        {
        return "TeamMetricType{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", method=" + method +
                '}';
        }

    public enum AggMethod
        {
            AVG, SUM
        }
    }

package team.dashboard.web.metrics;

public enum TeamMetricType
    {
        CYCLE_TIME("cycletime", "Cycle Time"),
        DEPLOYMENT_FREQUENCY("deployment_frequency", "Deployment Frequency"),
        INCIDENTS_DUE_TO_CHANGE("incidents_due_to_change", "Incidents due to change"),
        PRODUCTION_DEFECTS("production_defects", "Production Defects");

    private String key;
    private String name;

    TeamMetricType(String key, String name)
        {
        this.key = key;
        this.name = name;
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
    }

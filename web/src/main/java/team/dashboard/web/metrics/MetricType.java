package team.dashboard.web.metrics;

public class MetricType
    {
    private final String key;
    private final String description;

    public MetricType(String key, String description)
        {
        this.key = key;
        this.description = description;
        }

    public String getKey()
        {
        return key;
        }

    public String getDescription()
        {
        return description;
        }

    @Override
    public String toString()
        {
        return "MetricType{" +
                "key='" + key + '\'' +
                ", description='" + description + '\'' +
                '}';
        }
    }

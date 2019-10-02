package team.dashboard.web.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityType
    {
        COMPANY("Company"),
        DOMAIN("Domain"),
        PLATFORM("Platform"),
        TEAM_OF_TEAMS("Team of Teams"),
        TEAM("Team"),
        APPLICATION("Application"),
        RELEASE("Release");

    private final String key;
    private final String name;

    EntityType(String name)
        {
        this.key = this.name();
        this.name = name;
        }

    @JsonCreator
    public static EntityType fromObject(final Map<String, Object> obj)
        {
        if (obj != null && obj.containsKey("key"))
            {
            String key = (String) obj.get("key");
            return fromKey(key);
            }
        return null;
        }

    public static EntityType fromKey(final String key)
        {
        if (key != null)
            {
            for (EntityType e : EntityType.values())
                {
                if (key.equals(e.getKey()))
                    {
                    return e;
                    }
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
        return "EntityType{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
        }
    }

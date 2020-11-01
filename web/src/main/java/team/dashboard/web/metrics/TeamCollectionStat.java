package team.dashboard.web.metrics;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class TeamCollectionStat
    {
    @Id
    private final TeamCollectionStatId teamId;
    private final Integer count;

    public TeamCollectionStat(TeamCollectionStatId teamId, Integer count)
        {
        this.teamId = teamId;
        this.count = count;
        }

    public TeamCollectionStatId getTeamId()
        {
        return teamId;
        }

    public Integer getCount()
        {
        return count;
        }

    @Override
    public String toString()
        {
        return "TeamCollectionStat{" +
            "teamId=" + teamId +
            ", count=" + count +
            '}';
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCollectionStat that = (TeamCollectionStat) o;
        return Objects.equals(teamId, that.teamId) &&
            Objects.equals(count, that.count);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, count);
        }
    }

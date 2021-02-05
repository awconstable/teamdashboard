package team.dashboard.web.dora.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class Deployment
    {
    private final String id;
    private final String deploymentId;
    private final String deploymentDesc;
    private final String applicationId;
    private final String rfcId;
    private final Date created;
    private final String source;
    private final HashSet<Change> changes;
    private long leadTimeSeconds;
    private DORALevel leadTimePerfLevel;

    public Deployment(String id, String deploymentId, String deploymentDesc, String applicationId, String rfcId, Date created, String source, HashSet<Change> changes)
        {
        this.id = id;
        this.deploymentId = deploymentId;
        this.deploymentDesc = deploymentDesc;
        this.applicationId = applicationId;
        this.rfcId = rfcId;
        this.created = created;
        this.source = source;
        this.changes = changes;
        }

    public String getId()
        {
        return id;
        }

    public String getDeploymentId()
        {
        return deploymentId;
        }

    public String getDeploymentDesc() { return deploymentDesc; }

    public String getApplicationId()
        {
        return applicationId;
        }

    public String getRfcId() { return rfcId; }
    
    public Date getCreated()
        {
        return created;
        }

    public String getSource()
        {
        return source;
        }

    public HashSet<Change> getChanges()
        {
        return changes;
        }

    public long getLeadTimeSeconds()
        {
        return leadTimeSeconds;
        }

    public void setLeadTimeSeconds(long leadTimeSeconds)
        {
        this.leadTimeSeconds = leadTimeSeconds;
        }

    public DORALevel getLeadTimePerfLevel()
        {
        return leadTimePerfLevel;
        }

    public void setLeadTimePerfLevel(DORALevel leadTimePerfLevel)
        {
        this.leadTimePerfLevel = leadTimePerfLevel;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deployment that = (Deployment) o;
        return id.equals(that.id) &&
            created.equals(that.created) &&
            Objects.equals(source, that.source) &&
            Objects.equals(changes, that.changes);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(id, created, source, changes);
        }

    @Override
    public String toString()
        {
        return "Deployment{" +
            "id='" + id + '\'' +
            ", deploymentId='" + deploymentId + '\'' +
            ", deploymentDesc='" + deploymentDesc + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", rfcId='" + rfcId + '\'' +
            ", created=" + created +
            ", source='" + source + '\'' +
            ", changes=" + changes +
            ", leadTimeSeconds=" + leadTimeSeconds +
            ", leadTimePerfLevel=" + leadTimePerfLevel +
            '}';
        }
    }

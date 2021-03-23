package team.dashboard.web.dora.domain;

import java.util.Date;
import java.util.Objects;

public class Incident
    {
    private final String incidentId;
    private final String incidentDesc;
    private final String applicationId;
    private final Date created;
    private final Date resolved;
    private final String source;

    private long mttrSeconds;
    private DORALevel mttrPerfLevel;

    public Incident(String incidentId, String incidentDesc, String applicationId, Date created, Date resolved, String source)
        {
        this.incidentId = incidentId;
        this.incidentDesc = incidentDesc;
        this.applicationId = applicationId;
        this.created = created;
        this.resolved = resolved;
        this.source = source;
        }

    public String getIncidentId()
        {
        return incidentId;
        }

    public String getIncidentDesc()
        {
        return incidentDesc;
        }

    public String getApplicationId()
        {
        return applicationId;
        }

    public Date getCreated()
        {
        return created;
        }

    public Date getResolved()
        {
        return resolved;
        }

    public String getSource()
        {
        return source;
        }

    public long getMttrSeconds()
        {
        return mttrSeconds;
        }

    public void setMttrSeconds(long mttrSeconds)
        {
        this.mttrSeconds = mttrSeconds;
        }

    public DORALevel getMttrPerfLevel()
        {
        return mttrPerfLevel;
        }

    public void setMttrPerfLevel(DORALevel mttrPerfLevel)
        {
        this.mttrPerfLevel = mttrPerfLevel;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return mttrSeconds == incident.mttrSeconds &&
            Objects.equals(incidentId, incident.incidentId) &&
            Objects.equals(incidentDesc, incident.incidentDesc) &&
            Objects.equals(applicationId, incident.applicationId) &&
            Objects.equals(created, incident.created) &&
            Objects.equals(resolved, incident.resolved) &&
            Objects.equals(source, incident.source) &&
            mttrPerfLevel == incident.mttrPerfLevel;
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(incidentId, incidentDesc, applicationId, created, resolved, source, mttrSeconds, mttrPerfLevel);
        }

    @Override
    public String toString()
        {
        return "Incident{" +
            "incidentId='" + incidentId + '\'' +
            ", incidentDesc='" + incidentDesc + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", created=" + created +
            ", resolved=" + resolved +
            ", source='" + source + '\'' +
            ", mttrSeconds=" + mttrSeconds +
            ", mttrPerfLevel=" + mttrPerfLevel +
            '}';
        }
    }

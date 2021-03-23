package team.dashboard.web.dora.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

public class MTTR
    {
    @Id
    private String id;
    private final String applicationId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date reportingDate;
    private final long meanTimeToRecoverSeconds;
    private final Integer incidentCount;
    private final DORALevel doraLevel;

    public MTTR(String applicationId, Date reportingDate, long meanTimeToRecoverSeconds, Integer incidentCount, DORALevel doraLevel)
        {
        this.applicationId = applicationId;
        this.reportingDate = reportingDate;
        this.meanTimeToRecoverSeconds = meanTimeToRecoverSeconds;
        this.incidentCount = incidentCount;
        this.doraLevel = doraLevel;
        }

    public String getApplicationId()
        {
        return applicationId;
        }

    public Date getReportingDate()
        {
        return reportingDate;
        }

    public Integer getIncidentCount()
        {
        return incidentCount;
        }

    public DORALevel getDoraLevel()
        {
        return doraLevel;
        }

    public long getMeanTimeToRecoverSeconds()
        {
        return meanTimeToRecoverSeconds;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MTTR mttr = (MTTR) o;
        return Objects.equals(applicationId, mttr.applicationId) &&
            Objects.equals(reportingDate, mttr.reportingDate) &&
            Objects.equals(meanTimeToRecoverSeconds, mttr.meanTimeToRecoverSeconds) &&
            Objects.equals(incidentCount, mttr.incidentCount) && 
            doraLevel == mttr.doraLevel;
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(applicationId, reportingDate, meanTimeToRecoverSeconds, incidentCount, doraLevel);
        }

    @Override
    public String toString()
        {
        return "MTTR{" +
            "applicationId='" + applicationId + '\'' +
            ", reportingDate=" + reportingDate +
            ", meanTimeToRecoverSeconds=" + meanTimeToRecoverSeconds +
            ", incidentCount=" + incidentCount +
            ", doraLevel=" + doraLevel +
            '}';
        }
    }

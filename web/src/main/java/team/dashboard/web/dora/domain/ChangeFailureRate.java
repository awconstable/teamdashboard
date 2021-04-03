package team.dashboard.web.dora.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

public class ChangeFailureRate
    {
    @Id
    private String id;
    private final String applicationId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private final Date reportingDate;
    private final Double changeFailureRatePercent;
    private final Integer changeRequestCount;
    private final DORALevel doraLevel;

    public ChangeFailureRate(String applicationId, Date reportingDate, Double changeFailureRatePercent, Integer changeRequestCount, DORALevel doraLevel)
        {
        this.applicationId = applicationId;
        this.reportingDate = reportingDate;
        this.changeFailureRatePercent = changeFailureRatePercent;
        this.changeRequestCount = changeRequestCount;
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

    public Double getChangeFailureRatePercent()
        {
        return changeFailureRatePercent;
        }

    public Integer getChangeRequestCount()
        {
        return changeRequestCount;
        }

    public DORALevel getDoraLevel()
        {
        return doraLevel;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeFailureRate that = (ChangeFailureRate) o;
        return Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(reportingDate, that.reportingDate) &&
                Objects.equals(changeFailureRatePercent, that.changeFailureRatePercent) &&
                Objects.equals(changeRequestCount, that.changeRequestCount) &&
                doraLevel == that.doraLevel;
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(applicationId, reportingDate, changeFailureRatePercent, changeRequestCount, doraLevel);
        }

    @Override
    public String toString()
        {
        return "ChangeFailureRate{" +
                "applicationId='" + applicationId + '\'' +
                ", reportingDate=" + reportingDate +
                ", changeFailureRatePercent=" + changeFailureRatePercent +
                ", changeRequestCount=" + changeRequestCount +
                ", doraLevel=" + doraLevel +
                '}';
        }
    }

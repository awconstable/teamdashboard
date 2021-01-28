package team.dashboard.web.dora.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class DeploymentFrequency
    {
    @Id
    private String id;
    private final String applicationId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "UTC")
    private final Date reportingDate;
    private final Integer deploymentCount;
    private final TimePeriod timePeriod;
    private final DORALevel deployFreqLevel;

    public DeploymentFrequency(String applicationId, Date reportingDate, Integer deploymentCount, TimePeriod timePeriod, DORALevel deployFreqLevel)
        {
        this.applicationId = applicationId;
        this.reportingDate = reportingDate;
        this.deploymentCount = deploymentCount;
        this.timePeriod = timePeriod;
        this.deployFreqLevel = deployFreqLevel;
        }

    public String getApplicationId()
        {
        return applicationId;
        }

    public Date getReportingDate()
        {
        return reportingDate;
        }

    public TimePeriod getTimePeriod()
        {
        return timePeriod;
        }

    public Integer getDeploymentCount()
        {
        return deploymentCount;
        }

    public DORALevel getDeployFreqLevel()
        {
        return deployFreqLevel;
        }

    @Override
    public String toString()
        {
        return "DeploymentFrequency{" +
            "applicationId='" + applicationId + '\'' +
            ", reportingDate=" + reportingDate +
            ", deploymentCount=" + deploymentCount +
            ", timePeriod=" + timePeriod +
            ", deployFreqLevel=" + deployFreqLevel +
            '}';
        }
    }

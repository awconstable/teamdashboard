package team.dashboard.web.dora.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TeamPerformance
    {
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date reportingDate;
    private final DeploymentFrequency deploymentFrequency;
    private final LeadTime leadTime;

    public TeamPerformance(Date reportingDate, DeploymentFrequency deploymentFrequency, LeadTime leadTime)
        {
        this.reportingDate = reportingDate;
        this.deploymentFrequency = deploymentFrequency;
        this.leadTime = leadTime;
        }

    public Date getReportingDate()
        {
        return reportingDate;
        }

    public DeploymentFrequency getDeploymentFrequency()
        {
        return deploymentFrequency;
        }

    public LeadTime getLeadTime()
        {
        return leadTime;
        }

    @Override
    public String toString()
        {
        return "TeamPerformance{" +
            "reportingDate=" + reportingDate +
            ", deploymentFrequency=" + deploymentFrequency +
            ", leadTime=" + leadTime +
            '}';
        }
    }

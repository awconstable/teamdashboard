package team.dashboard.web.dora.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TeamPerformance
    {
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date reportingDate;
    private final DeploymentFrequency deploymentFrequency;
    private final LeadTime leadTime;
    private final MTTR mttr;
    private final ChangeFailureRate changeFailureRate;

    public TeamPerformance(Date reportingDate, DeploymentFrequency deploymentFrequency, LeadTime leadTime, MTTR mttr, ChangeFailureRate changeFailureRate)
        {
        this.reportingDate = reportingDate;
        this.deploymentFrequency = deploymentFrequency;
        this.leadTime = leadTime;
        this.mttr = mttr;
        this.changeFailureRate = changeFailureRate;
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

    public MTTR getMttr() { return mttr; }

    public ChangeFailureRate getChangeFailureRate(){ return changeFailureRate; }

    @Override
    public String toString()
        {
        return "TeamPerformance{" +
            "reportingDate=" + reportingDate +
            ", deploymentFrequency=" + deploymentFrequency +
            ", leadTime=" + leadTime +
            ", mttr=" + mttr +
            ", changeFailureRate=" + changeFailureRate +
            '}';
        }
    }

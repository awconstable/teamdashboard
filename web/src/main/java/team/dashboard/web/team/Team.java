package team.dashboard.web.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team
    {

    private final String slug;
    private final String name;
    private final String parentSlug;
    private final Collection<TeamRelation> ancestors;
    private final Collection<TeamRelation> children;
    private final Collection<TeamMember> teamMembers;
    private final Collection<Application> applications;

    public Team(String slug, String name, String parentSlug, Collection<TeamRelation> ancestors, Collection<TeamRelation> children, Collection<TeamMember> teamMembers, Collection<Application> applications)
        {
        this.slug = slug;
        this.name = name;
        this.parentSlug = parentSlug;
        this.ancestors = ancestors;
        this.children = children;
        this.teamMembers = teamMembers;
        this.applications = applications;
        }

    public String getSlug()
        {
        return slug;
        }

    public String getName()
        {
        return name;
        }

    public String getParentSlug()
        {
        return parentSlug;
        }

    public Collection<TeamRelation> getAncestors()
        {
        return ancestors;
        }

    public Collection<TeamRelation> getChildren()
        {
        return children;
        }

    public Collection<TeamMember> getTeamMembers()
        {
        return teamMembers;
        }

    public Collection<Application> getApplications()
        {
        return applications;
        }

    @Override
    public String toString()
        {
        return "Team{" +
                "slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", parentSlug='" + parentSlug + '\'' +
                ", ancestors=" + ancestors +
                ", children=" + children +
                ", teamMembers=" + teamMembers +
                ", applications=" + applications +
                '}';
        }
    }

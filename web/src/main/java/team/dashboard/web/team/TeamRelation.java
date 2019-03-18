package team.dashboard.web.team;

import java.util.Collection;

public class TeamRelation
    {

    private final String slug;
    private final String name;
    private final String parentSlug;
    private final Collection<TeamRelation> children;

    public TeamRelation(String slug, String name, String parentSlug, Collection<TeamRelation> children)
        {
        this.slug = slug;
        this.name = name;
        this.parentSlug = parentSlug;
        this.children = children;
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

    public Collection<TeamRelation> getChildren()
        {
        return children;
        }

    @Override
    public String toString()
        {
        return "TeamRelation{" +
                "slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", parentSlug='" + parentSlug + '\'' +
                ", children=" + children +
                '}';
        }
    }

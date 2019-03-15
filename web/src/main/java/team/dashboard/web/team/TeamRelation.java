package team.dashboard.web.team;

public class TeamRelation
    {

    private final String slug;
    private final String name;
    private final String parentSlug;

    public TeamRelation(String slug, String name, String parentSlug)
        {
        this.slug = slug;
        this.name = name;
        this.parentSlug = parentSlug;
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

    @Override
    public String toString()
        {
        return "TeamRelation{" +
                "slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", parentSlug='" + parentSlug + '\'' +
                '}';
        }
    }

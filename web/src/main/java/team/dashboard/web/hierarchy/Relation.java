package team.dashboard.web.hierarchy;

import java.util.ArrayList;
import java.util.Collection;

public class Relation
    {

    private final String slug;
    private final EntityType entityType;
    private final String name;
    private final String parentSlug;
    private final Collection<Relation> children;

    public Relation(String slug, EntityType entityType, String name, String parentSlug, Collection<Relation> children)
        {
        this.slug = slug;
        this.entityType = entityType;
        this.name = name;
        this.parentSlug = parentSlug;
        this.children = children;
        }

    public String getSlug()
        {
        return slug;
        }

    public EntityType getEntityType()
        {
        return entityType;
        }
    
    public String getName()
        {
        return name;
        }

    public String getParentSlug()
        {
        return parentSlug;
        }

    public Collection<Relation> getChildren()
        {
        if(children == null){
            return new ArrayList<>();
        }
        return children;
        }

    @Override
    public String toString()
        {
        return "Relation{" +
                "slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                ", parentSlug='" + parentSlug + '\'' +
                ", children=" + children +
                '}';
        }


    }

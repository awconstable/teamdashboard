package team.dashboard.web.hierarchy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;

public class HierarchyEntity
    {

    private final String slug;
    private final EntityType entityType;
    private final String name;
    private final String parentSlug;
    private final Collection<Relation> ancestors;
    private final Collection<Relation> children;
    private final Collection<Member> members;
    private final Collection<ApplicationId> applicationIds;
    private String id;

    public HierarchyEntity(String slug, EntityType entityType, String name, String parentSlug, Collection<Relation> ancestors, Collection<Relation> children, Collection<Member> members, Collection<ApplicationId> applicationIds)
        {
        this.slug = slug;
        this.entityType = entityType;
        this.name = name;
        this.parentSlug = parentSlug;
        this.ancestors = ancestors;
        this.children = children;
        this.members = members;
        this.applicationIds = applicationIds;
        }

    public String getId()
        {
        return id;
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

    public Collection<Relation> getAncestors()
        {
        if(ancestors == null){
            return new ArrayList<>();
        }
        return ancestors;
        }

    public Collection<Relation> getChildren()
        {
        if(children == null){
            return new ArrayList<>();
        }
        return children;
        }

    public Collection<Member> getMembers()
        {
        if(members == null){
            return new ArrayList<>();
        }
        return members;
        }

    public Collection<ApplicationId> getApplicationIds()
        {
        if(applicationIds == null){
            return new ArrayList<>();
        }
        return applicationIds;
        }

    @JsonIgnore
    public Relation getRelation()
        {
        return new Relation(this.slug, entityType, this.name, this.parentSlug, null);
        }

    @Override
    public String toString()
        {
        return "HierarchyEntity{" +
                "id='" + id + '\'' +
                ", slug='" + slug + '\'' +
                ", entityType=" + entityType +
                ", name='" + name + '\'' +
                ", parentSlug='" + parentSlug + '\'' +
                ", ancestors=" + ancestors +
                ", children=" + children +
                ", teamMembers=" + members +
                ", applicationIds=" + applicationIds +
                '}';
        }
    }

package com.example.n8engine.dto;

import java.io.Serializable;
import java.util.Set;

public class EntityResponse  implements Serializable {
    private Object entityDescription;
    private Set<Object> entityProperties;
    private Set<Object> entityClasses;

    public Object getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(Object entityDescription) {
        this.entityDescription = entityDescription;
    }

    public Set<Object> getEntityProperties() {
        return entityProperties;
    }

    public void setEntityProperties(Set<Object> entityProperties) {
        this.entityProperties = entityProperties;
    }

    public Set<Object> getEntityClasses() {
        return entityClasses;
    }

    public void setEntityClasses(Set<Object> entityClasses) {
        this.entityClasses = entityClasses;
    }
}

package com.poly.dao;

import java.util.List;

public abstract class EduSysDAO<EntityType, KeyType> {
    public abstract void insert(EntityType entity);
    public abstract void update(EntityType entity);
    public abstract void delete(KeyType id);
    public abstract List<EntityType> selectAll();
    public abstract EntityType selectbyId(KeyType id);
    public abstract List<EntityType> selectbySQL(String sql, Object... args);
}

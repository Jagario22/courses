package com.nix.courses.dao.entitydao.abstractDao;

import java.util.List;

public interface AbstractEntityDao<T> {
    List<T> findAll();

    void saveOrUpdate(T entity);
}

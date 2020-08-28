package com.nix.courses.dao.entityDao;

import com.nix.courses.dao.entityDao.abstractDao.AbstractEntityDao;
import com.nix.courses.entity.Topic;
import org.hibernate.Session;

import java.util.List;

public class TopicEntityDao implements AbstractEntityDao<Topic> {
    private Session session;

    public TopicEntityDao(Session session) {
        this.session = session;
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = session.createQuery("from Topic").list();
        return topics;
    }

    @Override
    public void saveOrUpdate(Topic topic) {
        session.saveOrUpdate(topic);
    }

    public Topic findById(Long id) {
        return session.get(Topic.class, id);
    }
}

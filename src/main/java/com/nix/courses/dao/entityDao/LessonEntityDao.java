package com.nix.courses.dao.entityDao;

import com.nix.courses.dao.entityDao.abstractDao.AbstractEntityDao;
import com.nix.courses.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class LessonEntityDao implements AbstractEntityDao<Lesson> {
    private Session session;

    public LessonEntityDao(Session session) {
        this.session = session;
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> lessons = session.createQuery("from Lesson").list();
        return lessons;
    }

    @Override
    public void saveOrUpdate(Lesson lesson) {
        session.saveOrUpdate(lesson);
    }

    public Lesson findById(Long id) {
        return session.get(Lesson.class, id);
    }

    public Lesson findFirstLessonOfGroupAfterDate(Date date, Long groupId) {
        Query query = session.createQuery("from Lesson L " +
                "where date = (select min(date) from Lesson ls" +
                " where ls.group.id = :qGroupId and date > :qDate) AND L.group.id = :qGroupId");
        query.setParameter("qDate", date);
        query.setParameter("qGroupId", groupId);

        Lesson lesson = (Lesson) query.list().get(0);
        return lesson;
    }

    public Lesson findLastLessonsByGroup(Long groupId) {
        Query query = session.createQuery("from Lesson where date = " +
                "(select max(date) from Lesson where group.id = :qGroupId) and group.id = :qGroupId");
        query.setParameter("qGroupId", groupId);

        return (Lesson) query.list().get(0);
    }
}

package com.nix.courses.dao.entitydao;

import com.nix.courses.dao.entitydao.abstractDao.AbstractEntityDao;
import com.nix.courses.entity.Grade;
import org.hibernate.Session;
import org.hibernate.query.Query;


import java.util.List;

public class GradeEntityDao implements AbstractEntityDao<Grade> {
    private Session session;

    public GradeEntityDao(Session session) {
        this.session = session;
    }

    @Override
    public List<Grade> findAll() {
        List<Grade> grades = session.createQuery("from Grade ").list();
        return grades;
    }

    @Override
    public void saveOrUpdate(Grade grade) {
        session.saveOrUpdate(grade);
    }

    public Grade findById(Long id) {
        return session.get(Grade.class, id);
    }

    public List<Grade> findAllGradesByLesson(Long lessonId) {
        Query query = session.createQuery("from Grade where lesson.id = :qLessonId");
        query.setParameter("qLessonId", lessonId);

        return query.list();
    }
}

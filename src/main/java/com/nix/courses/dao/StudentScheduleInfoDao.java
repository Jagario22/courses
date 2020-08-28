package com.nix.courses.dao;

import com.nix.courses.dao.entityDao.GroupEntityDao;
import com.nix.courses.dao.entityDao.LessonEntityDao;
import com.nix.courses.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;

public class StudentScheduleInfoDao {
    private final SessionFactory sessionFactory;
    private final GroupEntityDao groupDao;
    private final LessonEntityDao lessonDao;

    public StudentScheduleInfoDao(SessionFactory sessionFactory, GroupEntityDao groupDao,
                                  LessonEntityDao lessonDao) {
        this.sessionFactory = sessionFactory;
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
    }

    public Lesson findNextLessonByStId(Long studentId) {
        Session session = sessionFactory.getCurrentSession();
        Date currentDate = new Date();

        session.beginTransaction();
        Long groupId = groupDao.findGroupIdByStudent(studentId).getId();
        Lesson lesson = lessonDao.findFirstLessonOfGroupAfterDate(currentDate, groupId);
        session.getTransaction().commit();

        return lesson;
    }
}

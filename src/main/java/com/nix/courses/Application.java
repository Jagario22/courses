package com.nix.courses;

import com.nix.courses.dao.StudentScheduleInfoDao;
import com.nix.courses.dao.GroupInfoDao;
import com.nix.courses.dao.entitydao.*;
import com.nix.courses.entity.Group;
import com.nix.courses.entity.Lesson;
import com.nix.courses.service.system.InitCoursesDbService;
import com.nix.courses.util.DateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.cfg.Configuration;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        Configuration configuration = new Configuration().configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            final CourseEntityDao courseDao = new CourseEntityDao(session);
            final GradeEntityDao gradeDao = new GradeEntityDao(session);
            final GroupEntityDao groupDao = new GroupEntityDao(session);
            final LessonEntityDao lessonDao = new LessonEntityDao(session);
            final StudentEntityDao studentDao = new StudentEntityDao(session);
            final TeacherEntityDao teacherDao = new TeacherEntityDao(session);
            final TopicEntityDao topicDao = new TopicEntityDao(session);
            final InitCoursesDbService initDB = new InitCoursesDbService(courseDao, gradeDao, groupDao,
                    lessonDao, studentDao, teacherDao, topicDao);

            final StudentScheduleInfoDao studentSchedule = new StudentScheduleInfoDao(sessionFactory,
                    groupDao, lessonDao);
            final GroupInfoDao groupInfoDao = new GroupInfoDao(sessionFactory,
                    groupDao, lessonDao, gradeDao);

            session.getTransaction().begin();
            initDB.init();
            session.getTransaction().commit();

            Long studentId = 1L;
            Lesson lesson = studentSchedule.findNextLessonByStId(studentId);

            log.info("Next lesson: Teacher full name: " + lesson.getGroup().getTeacher().getFullName());
            log.info("Next lesson: Topic full name: " + lesson.getTopic().getName());
            log.info("Next lesson: Time: " + DateUtil.formatTime(lesson.getTime()));
            log.info("Next lesson: Date: " + DateUtil.formatDate(lesson.getDate()));

            Long teacherId = 1L;
            Group newGroup = groupInfoDao.newGetMostSucceedGroupById(teacherId);

            log.info("Most succeed group's name: " + newGroup.getName());
            log.info("Most succeed group's teacher: " + newGroup.getTeacher().getFullName());
            log.info("Most succeed group's course: " + newGroup.getCourse().getName());
            log.info("Most succeed group's number: " + newGroup.getCourse().getId());
        }

    }
}
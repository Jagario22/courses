package com.nix.courses.dao;

import com.nix.courses.dao.entityDao.GradeEntityDao;
import com.nix.courses.dao.entityDao.GroupEntityDao;
import com.nix.courses.dao.entityDao.LessonEntityDao;
import com.nix.courses.entity.Grade;
import com.nix.courses.entity.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.*;

public class GroupInfoDao {

    private final SessionFactory sessionFactory;
    private final GroupEntityDao groupDao;
    private final LessonEntityDao lessonDao;
    private final GradeEntityDao gradeDao;

    public GroupInfoDao(SessionFactory sessionFactory, GroupEntityDao groupDao, LessonEntityDao lessonDao, GradeEntityDao gradeDao) {
        this.sessionFactory = sessionFactory;
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
        this.gradeDao = gradeDao;
    }

    public Group newGetMostSucceedGroupById(Long teacherId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<Group> groups = groupDao.findGroupsByTeacher(teacherId);
        Map<Long, Long> groupLastLesson = new HashMap<>();

        for (Group group : groups) {
            Long lessonId = lessonDao.findLastLessonsByGroup(group.getId()).getId();
            groupLastLesson.put(lessonId, group.getId());
        }

        Map<Long, List<Integer>> lastLessonGradesOfGroup = new HashMap<>();
        for (Long lessonId : groupLastLesson.keySet()) {
            List<Grade> grades = gradeDao.findAllGradesByLesson(lessonId);
            lastLessonGradesOfGroup.put(lessonId, gradesToInteger(grades));
        }

        Long lastLessonId = getLessonIdWithMaxMedian(lastLessonGradesOfGroup);
        Long mostSucceedGroupId = groupLastLesson.get(lastLessonId);
        Group mostSucceedGroup = groupDao.findById(mostSucceedGroupId);

        session.getTransaction().commit();
        return mostSucceedGroup;
    }

    private Long getLessonIdWithMaxMedian(Map<Long, List<Integer>> lastLessonGradesOfGroups) {
        Long succeedLessonId = null;
        float maxMedian = 0;
        float median;
        List<Integer> grades;

        for (Long lessonId : lastLessonGradesOfGroups.keySet()) {
            grades = lastLessonGradesOfGroups.get(lessonId);
            Collections.sort(grades);

            if (grades.size() % 2 == 0) {
                median = ((grades.get(grades.size() / 2) + grades.get(grades.size() / 2 - 1)) / 2f);
            } else {
                median = grades.get(grades.size() / 2);
            }

            if (median > maxMedian) {
                maxMedian = median;
                succeedLessonId = lessonId;
            }
        }
        return succeedLessonId;
    }

    private List<Integer> gradesToInteger(List<Grade> grades) {
        List<Integer> list = new ArrayList<>();

        for (Grade grade : grades) {
            list.add(grade.getGrade());
        }

        return list;
    }
}

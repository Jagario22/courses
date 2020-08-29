package com.nix.courses.dao.entitydao;

import com.nix.courses.dao.entitydao.abstractDao.AbstractEntityDao;
import com.nix.courses.entity.Course;
import org.hibernate.Session;

import java.util.List;

public class CourseEntityDao implements AbstractEntityDao<Course> {
    private Session session;

    public CourseEntityDao(Session session) {
        this.session = session;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = session.createQuery("from Course ").list();
        return courses;
    }

    @Override
    public void saveOrUpdate(Course course) {
        session.saveOrUpdate(course);
    }

    public Course findById(Long id) {
        Course course = session.get(Course.class, id);
        return course;
    }

}

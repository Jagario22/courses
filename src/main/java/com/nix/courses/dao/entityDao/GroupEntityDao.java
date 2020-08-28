package com.nix.courses.dao.entityDao;

import com.nix.courses.dao.entityDao.abstractDao.AbstractEntityDao;
import com.nix.courses.entity.Group;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class GroupEntityDao implements AbstractEntityDao<Group> {
    private Session session;

    public GroupEntityDao(Session session) {
        this.session = session;
    }

    public Group findGroupIdByStudent(Long studentId) {
        Query query = session.createQuery("select g from Group g inner join Student s on s.group.id = g.id" +
                " where s.id = :stId");
        query.setParameter("stId", studentId);
        return (Group) query.list().get(0);
    }

    @Override
    public List<Group> findAll() {
        return session.createQuery("from Group").list();
    }

    @Override
    public void saveOrUpdate(Group group) {
        session.saveOrUpdate(group);
    }

    public Group findById(Long id) {
        return session.get(Group.class, id);
    }

    public List<Group> findGroupsByTeacher(Long teacherId) {
        Query query = session.createQuery("select g from Group g inner join Teacher t on g.teacher.id = t.id " +
                "where t.id = :qTeacherId");
        query.setParameter("qTeacherId", teacherId);
        return query.list();
    }
}

package com.nix.courses.service.system;

import com.nix.courses.dao.entityDao.*;
import com.nix.courses.entity.*;
import com.nix.courses.util.DateUtil;
import com.nix.courses.util.UploadUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InitCoursesDbService implements AbstractInitDbService {

    private final CourseEntityDao courseDao;
    private final GradeEntityDao gradeDao;
    private final GroupEntityDao groupDao;
    private final LessonEntityDao lessonDao;
    private final StudentEntityDao studentDao;
    private final TeacherEntityDao teacherDao;
    private final TopicEntityDao topicDao;

    public InitCoursesDbService(CourseEntityDao courseDao, GradeEntityDao gradeDao, GroupEntityDao groupDao,
                                LessonEntityDao lessonDao, StudentEntityDao studentDao, TeacherEntityDao teacherDao,
                                TopicEntityDao topicDao) {
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.topicDao = topicDao;
    }

    @Override
    public void init() {
        Collection<File> files = FileUtils.listFiles(
                new File(UploadUtil.getPath(UploadUtil.Folder.CSV)),
                new String[]{"csv"}, true
        );

        List<List<String>> courses = new ArrayList<>();
        List<List<String>> grades = new ArrayList<>();
        List<List<String>> lessons = new ArrayList<>();
        List<List<String>> teachers = new ArrayList<>();
        List<List<String>> topics = new ArrayList<>();
        List<List<String>> groups = new ArrayList<>();
        List<List<String>> students = new ArrayList<>();


        for (File file : files) {
            try (CSVReader reader = new CSVReader(new FileReader(file.getAbsolutePath()))) {
                String[] values;

                while ((values = reader.readNext()) != null) {
                    if (file.getName().endsWith(Constants.COURSES_CSV)) {
                        courses.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.GROUPS_CSV)) {
                        groups.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.GRADES_CSV)) {
                        grades.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.LESSONS_CSV)) {
                        lessons.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.STUDENTS_CSV)) {
                        students.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.TEACHER_CSV)) {
                        teachers.add(Arrays.asList(values));
                    }
                    if (file.getName().endsWith(Constants.TOPIC_CSV)) {
                        topics.add(Arrays.asList(values));
                    }

                }

            } catch (CsvValidationException | IOException e) {
                e.printStackTrace();
            }
        }

        createCourses(courses);
        createTeachers(teachers);
        createGroups(groups);
        createStudents(students);
        createTopic(topics);
        createLessons(lessons);
        createGrades(grades);
    }

    private void createCourses(List<List<String>> list) {
        List<Course> courses = courseDao.findAll();
        if (courses.size() != list.size()) {
            for (List<String> strings : list) {
                Course course = new Course();
                course.setName(strings.get(0));
                courseDao.saveOrUpdate(course);
            }
        }
    }

    private void createGrades(List<List<String>> list) {
        List<Grade> grades = gradeDao.findAll();

        if (grades.size() != list.size()) {
            Lesson lesson;
            Student student;
            for (List<String> strings : list) {
                Grade grade = new Grade();
                grade.setGrade(Integer.valueOf(strings.get(0)));
                lesson = lessonDao.findById(Long.parseLong(strings.get(1)));
                grade.setLesson(lesson);
                student = studentDao.findById(Long.parseLong(strings.get(2)));
                grade.setStudent(student);
                gradeDao.saveOrUpdate(grade);
            }
        }
    }

    private void createGroups(List<List<String>> list) {
        List<Group> groups = groupDao.findAll();

        if (groups.size() != list.size()) {
            Course course;
            Teacher teacher;
            for (List<String> strings : list) {
                Group group = new Group();
                group.setName(strings.get(0));
                course = courseDao.findById(Long.parseLong(strings.get(1)));
                teacher = teacherDao.findById(Long.parseLong(strings.get(2)));
                group.setCourse(course);
                group.setTeacher(teacher);
                groupDao.saveOrUpdate(group);
            }
        }
    }

    private void createLessons(List<List<String>> list) {
        List<Lesson> lessons = lessonDao.findAll();

        if (lessons.size() != list.size()) {
            Group group;
            Topic topic;
            for (List<String> strings : list) {
                Lesson lesson = new Lesson();
                lesson.setDate(DateUtil.formatDate(strings.get(0)));
                lesson.setTime(DateUtil.formatTime(strings.get(1)));
                group = groupDao.findById(Long.parseLong(strings.get(2)));
                topic = topicDao.findById(Long.parseLong(strings.get(3)));
                lesson.setGroup(group);
                lesson.setTopic(topic);
                lessonDao.saveOrUpdate(lesson);
            }
        }
    }

    private void createStudents(List<List<String>> list) {
        List<Student> students = studentDao.findAll();

        if (students.size() != list.size()) {
            Group group;
            for (List<String> strings : list) {
                Student student = new Student();
                student.setEmail(strings.get(0));
                student.setFullName(strings.get(1));
                group = groupDao.findById(Long.parseLong(strings.get(2)));
                student.setGroup(group);
                studentDao.saveOrUpdate(student);
            }
        }
    }

    private void createTeachers(List<List<String>> list) {
        List<Teacher> teachers = teacherDao.findAll();

        if (teachers.size() != list.size()) {
            for (List<String> strings : list) {
                Teacher teacher = new Teacher();
                teacher.setEmail(strings.get(0));
                teacher.setFullName(strings.get(1));
                teacherDao.saveOrUpdate(teacher);
            }
        }
    }

    private void createTopic(List<List<String>> list) {
        List<Topic> topics = topicDao.findAll();

        if (topics.size() != list.size()) {
            Course course;
            for (List<String> strings : list) {
                Topic topic = new Topic();
                topic.setName(strings.get(0));
                course = courseDao.findById(Long.parseLong(strings.get(1)));
                topic.setCourse(course);
                topicDao.saveOrUpdate(topic);
            }
        }
    }
}
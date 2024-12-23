package ManagerService.Model;

import core.model.Employee;
import core.model.EmployeeType;
import core.model.Gender;
import core.model.Role;
import core.model.User;
import CourseService.Course;
import LibrarianService.Model.Librarian;
import TeacherService.Model.Teacher;
import StudentService.Model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager extends Employee {

    public Manager(Connection connection, User user) throws SQLException {
        super(user, 0, null, connection); // Initialize with default values, will be updated later
        this.connection = connection;
        String query = "SELECT * FROM managers WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Manager with user ID " + user.getId() + " does not exist.");
                }
            }
        }
    }

    public void addNews(String title, String content, int authorId, String topic, boolean isPinned) throws SQLException {
        String query = "INSERT INTO news (title, content, author_id, topic, is_pinned) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setInt(3, authorId);
            statement.setString(4, topic);
            statement.setBoolean(5, isPinned);
            statement.executeUpdate();
        }
    }

    public void createCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses (code, name, description, credits, type, department, prerequisites, language) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseId());
            statement.setString(2, course.getName());
            statement.setString(3, course.getDescription());
            statement.setInt(4, course.getCredits());
            statement.setString(5, course.getType());
            statement.setString(6, course.getDepartment());
            statement.setString(7, course.getPrerequisites());
            statement.setString(8, course.getLanguage());
            statement.executeUpdate();
        }
    }

    public Manager(int id, String email, String password, String name, String surname,
                String phoneNumber, Gender gender, Role role, int salary, String type) {
        super(id, email, password, name, surname, phoneNumber, gender, role, salary, EmployeeType.valueOf(type));
    }

    @Override
    public void run() {
        System.out.println("Manager is running");
    }

    public void createTeacher(Teacher teacher) throws SQLException {
        String query = "INSERT INTO teachers (user_id, salary, type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacher.getId());
            statement.setInt(2, teacher.getSalary());
            statement.setString(3, teacher.getType().name());
            statement.executeUpdate();
        }
    }

    public void removeTeacher(int teacherId) throws SQLException {
        String query = "DELETE FROM teachers WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            statement.executeUpdate();
        }
        removeUser(teacherId);
    }

    public void createStudent(Student student) throws SQLException {
        String query = "INSERT INTO students (user_id, major, year, membership, total_credits, gpa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setString(2, student.getFaculty());
            statement.setInt(3, student.getYearOfStudy());
            statement.setString(4, student.getMember());
            statement.setInt(5, student.getTotalCredits());
            statement.setDouble(6, student.getGpa());
            statement.executeUpdate();
        }
    }

    public void removeStudent(int studentId) throws SQLException {
        String query = "DELETE FROM students WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
        removeUser(studentId);
    }

    public void createLibrarian(Librarian librarian) throws SQLException {
        String query = "INSERT INTO librarians (user_id, salary, type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, librarian.getId());
            statement.setInt(2, librarian.getSalary());
            statement.setString(3, librarian.getType().name());
            statement.executeUpdate();
        }
    }

    public void removeLibrarian(int librarianId) throws SQLException {
        String query = "DELETE FROM librarians WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, librarianId);
            statement.executeUpdate();
        }
        removeUser(librarianId);
    }

    private void removeUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }
}
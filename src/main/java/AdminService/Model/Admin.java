package AdminService.Model;

import core.model.Employee;
import core.model.Gender;
import core.model.Role;
import core.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ManagerService.Model.Manager;
import ResearcherService.Model.Researcher;
import StudentService.Model.Student;
import TeacherService.Model.Teacher;

public class Admin extends Employee {

    Connection connection;

    public int remove(Integer id) {
        System.out.println("Removing user with ID: " + id);
        return 1;
    }

    public void remove() {
        System.out.println("Removing all users or default user.");
    }

    public String toString() {
        return "Admin extends " + super.toString();
    }

    public Admin(Connection connection, User user) throws SQLException {
        super(user, 0, null, connection); // Initialize with default values, will be updated later
        String query = "SELECT * FROM managers WHERE user_id = ?";
        this.connection = connection;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Admin with user ID " + user.getId() + " does not exist.");
                }
            }
        }
    }
    
    public void createStudent(Student student) throws SQLException {
        String query = "INSERT INTO students (user_id, major, year, member, total_credits, gpa) VALUES (?, ?, ?, ?, ?, ?)";
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
    }

    public Student getStudent(int studentId) throws SQLException {
        String query = "SELECT u.*, s.major, s.year, s.member, s.total_credits, s.gpa " +
                       "FROM users u " +
                       "JOIN students s ON u.id = s.user_id " +
                       "WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone"),
                        Gender.valueOf(resultSet.getString("gender")),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getString("major"),
                        resultSet.getInt("year"),
                        resultSet.getString("member"),
                        resultSet.getInt("total_credits"),
                        resultSet.getDouble("gpa")
                    );
                } else {
                    throw new SQLException("Student with ID " + studentId + " does not exist.");
                }
            }
        }
    }

    public void createTeacher(Teacher teacher) throws SQLException {
        String query = "INSERT INTO teachers (user_id, salary, type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacher.getId());
            statement.setInt(2, teacher.getSalary());
            statement.setString(3, teacher.getType().toString());
            statement.executeUpdate();
        }
    }

    public void removeTeacher(int teacherId) throws SQLException {
        String query = "DELETE FROM teachers WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            statement.executeUpdate();
        }
    }

    public Teacher getTeacher(int teacherId) throws SQLException {
        String query = "SELECT u.*, t.salary, t.type " +
                       "FROM users u " +
                       "JOIN teachers t ON u.id = t.user_id " +
                       "WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Teacher teacher = new Teacher(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone"),
                        Gender.valueOf(resultSet.getString("gender")),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getInt("salary"),
                        resultSet.getString("type")
                    );

                    // Check if the teacher is also a researcher
                    String researcherQuery = "SELECT * FROM researchers WHERE employee_id = ?";
                    try (PreparedStatement researcherStatement = connection.prepareStatement(researcherQuery)) {
                        researcherStatement.setInt(1, teacherId);
                        try (ResultSet researcherResultSet = researcherStatement.executeQuery()) {
                            if (researcherResultSet.next()) {
                                teacher.setResearcherInstance(new Researcher(
                                    researcherResultSet.getInt("id")
                                ));
                            }
                        }
                    }

                    return teacher;
                } else {
                    throw new SQLException("Teacher with ID " + teacherId + " does not exist.");
                }
            }
        }
    }

    public void createManager(Manager manager) throws SQLException {
        String query = "INSERT INTO managers (user_id, salary, type) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, manager.getId());
            statement.setInt(2, manager.getSalary());
            statement.setString(3, manager.getType().toString());
            statement.executeUpdate();
        }
    }

    public void removeManager(int managerId) throws SQLException {
        String query = "DELETE FROM managers WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, managerId);
            statement.executeUpdate();
        }
    }

    public Manager getManager(int managerId) throws SQLException {
        String query = "SELECT u.*, m.salary, m.type " +
                       "FROM users u " +
                       "JOIN managers m ON u.id = m.user_id " +
                       "WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, managerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Manager(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone"),
                        Gender.valueOf(resultSet.getString("gender")),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getInt("salary"),
                        resultSet.getString("type")
                    );
                } else {
                    throw new SQLException("Manager with ID " + managerId + " does not exist.");
                }
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Admin is running");
        System.out.println(this.toString());
    }
}

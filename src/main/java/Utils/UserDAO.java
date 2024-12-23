package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import AdminService.Model.Admin;
import LibrarianService.Model.Librarian;
import ManagerService.Model.Manager;
import StudentService.Model.Student;
import TeacherService.Model.Teacher;
import core.model.Gender;
import core.model.Role;
import core.model.User;

public class UserDAO {
    private Connection connection;
    private ResourceBundle resourceBundle;
    public User loginnedIn = null;

    public UserDAO(Connection connection, ResourceBundle resourceBundle) {
        this.connection = connection;
        this.resourceBundle = resourceBundle;
    }

    public void loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User() {};
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("first_name"));
                user.setSurname(resultSet.getString("last_name"));
                user.setPhoneNumber(resultSet.getString("phone"));
                user.setGender(resultSet.getString("gender") != null ? Gender.valueOf(resultSet.getString("gender")) : null);
                user.setRole(resultSet.getString("role") != null ? Role.valueOf(resultSet.getString("role")) : null);

                loginnedIn = user;

                System.out.println(resourceBundle.getString("login_success"));

                // Redirect based on user role
                switch (resultSet.getString("role")) {
                    case "STUDENT":
                        Student student = new Student(connection, user);
                        student.run(); // should run console based program
                        break;
                    case "TEACHER":
                        Teacher teacher = new Teacher(connection, user);
                        teacher.run(); // should run console based program
                        break;
                    case "LIBRARIAN":
                        Librarian librarian = new Librarian(user, connection);
                        librarian.run(); // should run console based program
                        break;
                    case "MANAGER":
                        Manager manager = new Manager(connection, user);
                        manager.run(); // should run console based program
                        break;
                    case "ADMIN":
                        Admin admin = new Admin(connection, user);
                        admin.run(); // should run console based program
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown role: " + user.getRole());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
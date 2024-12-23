package TeacherService.Model;

import core.model.Employee;
import core.model.EmployeeType;
import core.model.Gender;
import core.model.Role;
import core.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ResearcherService.Model.Researcher;

public class Teacher extends Employee {
    
    private Researcher researcherInstance;

    public Researcher getResearcherInstance() {
        return this.researcherInstance;
    }
    
    public void setResearcherInstance(Researcher researcherInstance) {
        this.researcherInstance = researcherInstance;
    }

    public void putMark(String studentId, String courseName, double mark) {
        System.out.println("Оценка " + mark + " поставлена студенту с ID " + studentId + " за курс " + courseName);
    }
    
    public void putAttendance(String studentId, boolean isPresent) {
        String status = isPresent ? "присутствует" : "отсутствует";
        System.out.println("Студент с ID " + studentId + " " + status + " на занятии.");
    }
    
    public void viewTeacherSchedules(String teacherId) {
        System.out.println("Расписание преподавателя с ID " + teacherId);
    }

    public Teacher(Connection connection, User user) throws SQLException {
        super(user, 0, null, connection); // Initialize with default values, will be updated later

        String query = "SELECT * FROM teachers WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) { // Execute the query
                if (resultSet.next()) {
                    this.setSalary(resultSet.getInt("salary"));
                    this.setType(EmployeeType.valueOf(resultSet.getString("type")));
                }
            }
        }

        // Check if the teacher is also a researcher
        String researcherQuery = "SELECT * FROM researchers WHERE user_id = ?";
        try (PreparedStatement researcherStatement = connection.prepareStatement(researcherQuery)) {
            researcherStatement.setInt(1, user.getId());
            try (ResultSet researcherResultSet = researcherStatement.executeQuery()) { 
                if (researcherResultSet.next()) {
                    this.researcherInstance = new Researcher(
                        researcherResultSet.getInt("id")
                    );
                }
            }
        }
    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Teacher is running");
        System.out.println(this.toString());
    }

    public Teacher(int id, String email, String password, String name, String surname,
                String phoneNumber, Gender gender, Role role, int salary, String type) {
        super(id, email, password, name, surname, phoneNumber, gender, role, salary, EmployeeType.valueOf(type));
    }

    @Override
    public String toString() {
        return "Teacher [researcherInstance=" + researcherInstance + ", toString()=" + super.toString() + "]";
    }
}
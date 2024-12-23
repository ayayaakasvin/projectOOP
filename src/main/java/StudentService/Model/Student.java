package StudentService.Model;

import ResearcherService.Model.*;
import core.model.Gender;
import core.model.Role;
import core.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student extends User {
    
    private String faculty;
    private int yearOfStudy;
    private String member;
    private int totalCredits;
    private double gpa;


    private Researcher researcherInstance;

    public Student(Connection connection, User user) throws SQLException {
        super(user);
        String query = "SELECT * FROM students WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, super.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    this.faculty = resultSet.getString("major");
                    this.yearOfStudy = resultSet.getInt("year");
                    this.member = resultSet.getString("membership");
                    this.totalCredits = resultSet.getInt("total_credits");
                    this.gpa = resultSet.getDouble("gpa");
                }
            }
        }

        // Check if the student is also a researcher
        String researcherQuery = "SELECT * FROM researchers WHERE user_id = ?";
        try (PreparedStatement researcherStatement = connection.prepareStatement(researcherQuery)) {
            researcherStatement.setInt(1, super.getId());
            try (ResultSet researcherResultSet = researcherStatement.executeQuery()) {
                if (researcherResultSet.next()) {
                    this.researcherInstance = new Researcher(
                        researcherResultSet.getInt("id")
                    );
                }
            }
        }
    }
    
    public Student(int id, String email, String password, String firstName, String lastName, String phone, Gender gender, Role role, String faculty, int yearOfStudy, String member, int totalCredits, double gpa) {
        super(id, email, password, firstName, lastName, phone, gender, role);
        this.faculty = faculty;
        this.yearOfStudy = yearOfStudy;
        this.member = member;
        this.totalCredits = totalCredits;
        this.gpa = gpa;
    }

    public String getFaculty() {
        return this.faculty;
    }
    
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    
    public int getYearOfStudy() {
        return this.yearOfStudy;
    }
    
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    public String getMember() {
        return this.member;
    }
    
    public void setMember(String member) {
        this.member = member;
    }
    
    public Researcher getResearcherInstance() {
        return this.researcherInstance;
    }
    
    public void setResearcherInstance(Researcher researcherInstance) {
        this.researcherInstance = researcherInstance;
    }
    
    public int getTotalCredits() {
        return this.totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", faculty='" + faculty + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", member='" + member + '\'' +
                ", totalCredits=" + totalCredits +
                ", gpa=" + gpa +
                ", researcherInstance=" + (researcherInstance != null ? researcherInstance.toString() : "null") +
                '}';
    }

    @Override
    public void run() {
        System.out.println("Student is running");
        System.out.println(this.toString());
    }

    public void viewMarks(int courseID) {
        System.out.println("Student marks");
    }
    // things with marks, attendance, schedules and researches, a lot time to implement
}

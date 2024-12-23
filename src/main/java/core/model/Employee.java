package core.model;

import java.sql.Connection;

public abstract class Employee extends User {

    private int salary;
    private EmployeeType type;
    protected Connection connection;

    public Employee() {
        super();
    }

    public Employee(User user, int salary, EmployeeType type, Connection connection) {
        super(user);
        this.salary = salary;
        this.type = type;
        this.connection = connection;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", role='" + getRole() + '\'' +
                ", salary=" + salary +
                ", type=" + type +
                '}';
    }

    public Employee(int id, String email, String password, String name, String surname,
            String phoneNumber, Gender gender, Role role, int salary, EmployeeType type) {
        super(id, email, password, name, surname, phoneNumber, gender, role);
        this.salary = salary;
        this.type = type;
    }

    @Override
    public void run() {
        System.out.println("Employee is running, but type is not specified");
    }
}
package LibrarianService.Model;

import java.sql.Connection;
import java.util.List;

import core.model.Employee;
import core.model.EmployeeType;
import core.model.Gender;
import core.model.Role;
import core.model.User;
import LibrarianService.Book;

public class Librarian extends Employee {


    public Librarian(int id, String email, String password, String name, String surname,
                     String phoneNumber, Gender gender, Role role, int salary, EmployeeType type) {
        super(id, email, password, name, surname, phoneNumber, gender, role, salary, type);
    }

    public Librarian() {
        super();
    }

    public Librarian(User user, Connection connection) {
        super(user, 0, EmployeeType.LIBRARIAN, connection);
    }
    
    // public void addBook(Book book) {
    //     this.books.add(book);
    // }
    
    // public void removeBook(Book book) {
    //     this.books.remove(book);
    // }
    
    // public List<Book> viewAvailableBooks() {
    //     return this.books;
    // }

    public void getBook () {

    }

    @Override
    public void run() {
        System.out.println("Librarian is running");
        System.out.println(this.toString());
    }
}

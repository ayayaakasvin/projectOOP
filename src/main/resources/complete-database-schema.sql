-- Удаление существующих таблиц (если нужно пересоздать)
DROP TABLE IF EXISTS research_paper_authors;
DROP TABLE IF EXISTS research_project_participants;
DROP TABLE IF EXISTS course_instructors;
DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS course_marks;
DROP TABLE IF EXISTS library_borrows;
DROP TABLE IF EXISTS news_comments;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS work_messages;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS research_papers;
DROP TABLE IF EXISTS research_projects;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS users;

-- Создание основных таблиц

-- Таблица пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'TEACHER', 'STUDENT', 'RESEARCHER', 'LIBRARIAN', 'MANAGER')),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица студентов (расширение таблицы users)
CREATE TABLE students (
    user_id INTEGER PRIMARY KEY REFERENCES users(id),
    year INTEGER NOT NULL CHECK (year BETWEEN 1 AND 4),
    gpa DECIMAL(3,2) CHECK (gpa BETWEEN 0 AND 4.0),
    major VARCHAR(50) NOT NULL,
    total_credits INTEGER DEFAULT 0,
    supervisor_id INTEGER REFERENCES users(id)
);

-- Таблица преподавателей (расширение таблицы users)
CREATE TABLE teachers (
    user_id INTEGER PRIMARY KEY REFERENCES users(id),
    title VARCHAR(50) CHECK (title IN ('TUTOR', 'LECTOR', 'SENIOR_LECTOR', 'PROFESSOR')),
    department VARCHAR(100) NOT NULL,
    is_professor BOOLEAN DEFAULT FALSE
);

-- Таблица исследователей (расширение таблицы users)
CREATE TABLE researchers (
    user_id INTEGER PRIMARY KEY REFERENCES users(id),
    h_index INTEGER DEFAULT 0,
    research_area VARCHAR(100),
    total_citations INTEGER DEFAULT 0
);

-- Таблица курсов
CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    credits INTEGER NOT NULL CHECK (credits > 0),
    type VARCHAR(20) CHECK (type IN ('MAJOR', 'MINOR', 'FREE_ELECTIVE')),
    department VARCHAR(100),
    prerequisites TEXT,
    language VARCHAR(2) CHECK (language IN ('KZ', 'RU', 'EN'))
);

-- Таблица занятий
CREATE TABLE lessons (
    id SERIAL PRIMARY KEY,
    course_id INTEGER REFERENCES courses(id),
    type VARCHAR(20) CHECK (type IN ('LECTURE', 'PRACTICE')),
    room_number VARCHAR(10),
    start_time TIME,
    end_time TIME,
    day_of_week INTEGER CHECK (day_of_week BETWEEN 1 AND 7)
);

-- Таблица для связи студентов и курсов
CREATE TABLE student_courses (
    student_id INTEGER REFERENCES users(id),
    course_id INTEGER REFERENCES courses(id),
    semester INTEGER NOT NULL,
    year INTEGER NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, course_id, semester, year)
);

-- Таблица оценок
CREATE TABLE course_marks (
    student_id INTEGER REFERENCES users(id),
    course_id INTEGER REFERENCES courses(id),
    att1_mark DECIMAL(5,2) CHECK (att1_mark BETWEEN 0 AND 100),
    att2_mark DECIMAL(5,2) CHECK (att2_mark BETWEEN 0 AND 100),
    final_mark DECIMAL(5,2) CHECK (final_mark BETWEEN 0 AND 100),
    total_mark DECIMAL(5,2) CHECK (total_mark BETWEEN 0 AND 100),
    semester INTEGER NOT NULL,
    year INTEGER NOT NULL,
    PRIMARY KEY (student_id, course_id, semester, year)
);

-- Таблица преподавателей курса
CREATE TABLE course_instructors (
    course_id INTEGER REFERENCES courses(id),
    teacher_id INTEGER REFERENCES users(id),
    PRIMARY KEY (course_id, teacher_id)
);

-- Таблица научных проектов
CREATE TABLE research_projects (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    budget DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица научных статей
CREATE TABLE research_papers (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    journal VARCHAR(100),
    publication_date DATE,
    doi VARCHAR(100) UNIQUE,
    citations INTEGER DEFAULT 0,
    pages INTEGER,
    abstract TEXT,
    keywords TEXT,
    language VARCHAR(2) CHECK (language IN ('KZ', 'RU', 'EN')),
    project_id INTEGER REFERENCES research_projects(id)
);

-- Таблица авторов научных статей
CREATE TABLE research_paper_authors (
    paper_id INTEGER REFERENCES research_papers(id),
    researcher_id INTEGER REFERENCES researchers(user_id),
    author_order INTEGER NOT NULL,
    PRIMARY KEY (paper_id, researcher_id)
);

-- Таблица участников исследовательских проектов
CREATE TABLE research_project_participants (
    project_id INTEGER REFERENCES research_projects(id),
    researcher_id INTEGER REFERENCES researchers(user_id),
    role VARCHAR(50),
    join_date DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (project_id, researcher_id)
);

-- Таблица книг
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    isbn VARCHAR(13) UNIQUE,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    publisher VARCHAR(100),
    publication_year INTEGER,
    quantity INTEGER DEFAULT 0,
    available_quantity INTEGER DEFAULT 0,
    language VARCHAR(2) CHECK (language IN ('KZ', 'RU', 'EN'))
);

-- Таблица выдачи книг
CREATE TABLE library_borrows (
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES books(id),
    student_id INTEGER REFERENCES users(id),
    borrow_date DATE DEFAULT CURRENT_DATE,
    due_date DATE,
    return_date DATE,
    status VARCHAR(20) CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')),
    CONSTRAINT valid_borrow_period CHECK (due_date <= borrow_date + INTERVAL '6 months')
);

-- Таблица новостей
CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id INTEGER REFERENCES users(id),
    topic VARCHAR(50),
    is_pinned BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Таблица комментариев к новостям
CREATE TABLE news_comments (
    id SERIAL PRIMARY KEY,
    news_id INTEGER REFERENCES news(id),
    user_id INTEGER REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица жалоб
CREATE TABLE complaints (
    id SERIAL PRIMARY KEY,
    from_teacher_id INTEGER REFERENCES users(id),
    student_id INTEGER REFERENCES users(id),
    content TEXT NOT NULL,
    urgency_level VARCHAR(20) CHECK (urgency_level IN ('LOW', 'MEDIUM', 'HIGH')),
    status VARCHAR(20) CHECK (status IN ('PENDING', 'REVIEWED', 'RESOLVED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

-- Таблица служебных записок
CREATE TABLE work_messages (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER REFERENCES users(id),
    receiver_id INTEGER REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('DRAFT', 'SENT', 'READ', 'ARCHIVED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP
);

-- Создание индексов для оптимизации запросов
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_courses_type ON courses(type);
CREATE INDEX idx_courses_department ON courses(department);
CREATE INDEX idx_research_papers_date ON research_papers(publication_date);
CREATE INDEX idx_news_created_at ON news(created_at);
CREATE INDEX idx_complaints_urgency ON complaints(urgency_level, status);

-- Добавление тестовых данных
INSERT INTO users (username, password, first_name, last_name, role, email) 
VALUES 
('admin', 'admin123', 'Admin', 'User', 'ADMIN', 'admin@university.com'),
('teacher1', 'teacher123', 'John', 'Doe', 'TEACHER', 'john.doe@university.com'),
('student1', 'student123', 'Alice', 'Smith', 'STUDENT', 'alice.smith@university.com');

-- Добавление тестового курса
INSERT INTO courses (code, name, credits, type, language)
VALUES ('CS101', 'Introduction to Programming', 3, 'MAJOR', 'EN');

-- group schema aimed to store groups of students
CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    teacher VARCHAR(255) NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE group_students (
    group_id INT NOT NULL,
    student_id INT NOT NULL,
    PRIMARY KEY (group_id, student_id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (student_id) REFERENCES students(user_id)
);
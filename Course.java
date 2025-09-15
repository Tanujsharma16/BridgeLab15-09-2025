import java.util.*;

// Course classes to help to build the class
class Course {
private String courseId;
private String courseName;
private int credits;

public Course(String courseId, String courseName, int credits) {
this.courseId = courseId;
this.courseName = courseName;
this.credits = credits;
}

public String getCourseId() { return courseId; }
public String getCourseName() { return courseName; }
public int getCredits() { return credits; }
}

// Enrollment (composition)
class Enrollment {
private Course course;
private double grade; // Assume that grade is on 4.0 scale

public Enrollment(Course course, double grade) {
this.course = course;
this.grade = grade;
}

public Course getCourse() { return course; }
public double getGrade() { return grade; }
}

//Base class: Student
class Student {
private String studentId;
private String name;
private List<Enrollment> enrollments;

public Student(String studentId, String name) {
this.studentId = studentId;
this.name = name;
this.enrollments = new ArrayList<>();
}

public String getStudentId() { return studentId; }
public String getName() { return name; }

public void enroll(Course course, double grade) {
enrollments.add(new Enrollment(course, grade));
}

public List<Enrollment> getEnrollments() {
return enrollments;
}

// Polymorphic method
public double calculateGPA() {
if (enrollments.isEmpty()) return 0.0;
double totalPoints = 0;
double totalCredits = 0;

for (Enrollment e : enrollments) {
totalPoints += e.getGrade() * e.getCourse().getCredits();
totalCredits += e.getCourse().getCredits();
}

return totalPoints / totalCredits;
}

public void printTranscript() {
System.out.println("Transcript for " + name + ":");
for (Enrollment e : enrollments) {
System.out.println("- " + e.getCourse().getCourseName() + ": " + e.getGrade());
}
System.out.printf("GPA", calculateGPA());
}
}

//Subclass: UndergraduateStudent (inherits Student)
class UndergraduateStudent extends Student {
public UndergraduateStudent(String studentId, String name) {
super(studentId, name);
}

// Custom GPA logic
@Override
public double calculateGPA() {
// Example: cap grades at 4.0
double totalPoints = 0;
double totalCredits = 0;

for (Enrollment e : getEnrollments()) {
double grade = Math.min(e.getGrade(), 4.0);
totalPoints += grade * e.getCourse().getCredits();
totalCredits += e.getCourse().getCredits();
}

return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
}
}

//Subclass: PostgraduateStudent (inherits Student)
class PostgraduateStudent extends Student {
public PostgraduateStudent(String studentId, String name) {
super(studentId, name);
}

// Example: PG students need minimum 2.5 GPA
@Override
public double calculateGPA() {
double gpa = super.calculateGPA();
return gpa < 2.5 ? 0.0 : gpa; // Fail if GPA too low
}
}

//Main class
public class StudentManagementSystem {
public static void main(String[] args) {
// Create some courses
Course java = new Course("C101", "Java Programming", 3);
Course math = new Course("M101", "Mathematics", 4);
Course db = new Course("D101", "Databases", 2);

// Create students
Student s1 = new UndergraduateStudent("UG001", "Alice");
Student s2 = new PostgraduateStudent("PG001", "Bob");

// Enroll students in courses
s1.enroll(java, 3.5);
s1.enroll(math, 3.7);

s2.enroll(java, 2.2); // This should result in GPA = 0 for PG
s2.enroll(db, 2.8);

// Print transcripts
s1.printTranscript();
System.out.println("---------------------");
s2.printTranscript();
}
}

//import com.sms.model.Student;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    public static void main(String[] args) {
//// Test creating a student
//        Student student = new Student("S001", "John Doe", 20, "A", "john@example.com");
//
//        // Test getters
//        System.out.println("Student ID: " + student.getId());
//        System.out.println("Student Name: " + student.getName());
//        System.out.println("Student Age: " + student.getAge());
//        System.out.println("Student Grade: " + student.getGrade());
//        System.out.println("Student Email: " + student.getEmail());
//
//        // Test toString
//        System.out.println("\n" + student.toString());
//
//        // Test formatted string
//        System.out.println("\n" + student.toFormattedString());
//
//        // Test setters
//        student.setGrade("A+");
//        System.out.println("\nUpdated Grade: " + student.getGrade());
//
//        // Test file string conversion
//        String fileString = student.toFileString();
//        System.out.println("\nFile format: " + fileString);
//
//        // Test recreating from file string
//        Student recreated = Student.fromFileString(fileString);
//        System.out.println("Recreated student: " + recreated.getName());
//    }
//}
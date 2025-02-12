package project;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

class Student {
    private String name;
    private String rollNumber;
    private String grade;
    private String email;

    public Student(String name, String rollNumber, String grade, String email) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String toFileFormat() {
        return name + "," + rollNumber + "," + grade + "," + email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade + ", Email: " + email;
    }
}

class StudentManagementSystem {
    private static final String FILE_NAME = "students.txt";
    private List<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudents();
    }

    private void loadStudents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    students.add(new Student(data[0], data[1], data[2], data[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student data.");
        }
    }

    private void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.println(student.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving students.");
        }
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(String rollNumber) {
        students.removeIf(s -> s.getRollNumber().equals(rollNumber));
        saveStudents();
    }

    public List<Student> getStudents() {
        return students;
    }
}

public class StudentManagementApp {
    private JFrame frame;
    private StudentManagementSystem sms;
    private JTextArea textArea;

    public StudentManagementApp() {
        sms = new StudentManagementSystem();
        frame = new JFrame("Student Management System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));

        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton displayButton = new JButton("Display Students");

        addButton.addActionListener(e -> addStudent());
        removeButton.addActionListener(e -> removeStudent());
        displayButton.addActionListener(e -> displayStudents());

        panel.add(addButton);
        panel.add(removeButton);
        panel.add(displayButton);

        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addStudent() {
        String name = JOptionPane.showInputDialog("Enter Name:");
        String rollNumber = JOptionPane.showInputDialog("Enter Roll Number:");
        String grade = JOptionPane.showInputDialog("Enter Grade:");
        String email = JOptionPane.showInputDialog("Enter Email:");
        if (name != null && rollNumber != null && grade != null && email != null) {
            sms.addStudent(new Student(name, rollNumber, grade, email));
            JOptionPane.showMessageDialog(frame, "Student added successfully!");
        }
    }

    private void removeStudent() {
        String rollNumber = JOptionPane.showInputDialog("Enter Roll Number to Remove:");
        if (rollNumber != null) {
            sms.removeStudent(rollNumber);
            JOptionPane.showMessageDialog(frame, "Student removed successfully!");
        }
    }

    private void displayStudents() {
        textArea.setText("");
        for (Student student : sms.getStudents()) {
            textArea.append(student.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementApp::new);
    }
}

package JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
	public static void main(String[] args)
	{
		Connection conn = JDBCConnection.getConnection();
		Scanner sc = new Scanner(System.in);
		int choice = 0;	
		while(choice != 6) {
			System.out.println("====MENU====");
			System.out.println("1. Student Information");
			System.out.println("2. Student Wise Data");
			System.out.println("3. Subject Wise Data");
			System.out.println("4. Update assignments category");
			System.out.println("5. Display assignment categories");
			System.out.println("6. Exit");
			System.out.println("Enter a choice: ");
			choice = sc.nextInt();
			switch(choice) {
			case 1:
				int c = 0;
				while(c != 4) {

					System.out.println("1. Add new student data");
					System.out.println("2. Remove student data");
					System.out.println("3. View student data");
					System.out.println("4. Exit");
					System.out.println("Enter a choice: ");
					c = sc.nextInt();
					switch(c) {
					case 1:

						System.out.println("Enter name of the student: ");
						String n = sc.next();
						System.out.println("Enter name of the subject: ");
						sc.nextLine();
						String sub = sc.nextLine();
						System.out.println("Enter assignment category: ");
						String category = sc.next();
						System.out.println("Enter date of submission (DD-Month-YY): ");
						String date = sc.next();
						System.out.println("Enter assignment category marks: ");
						int marks = sc.nextInt();
						addStudentData(conn, n, sub, category, date, marks);
						break;
					case 2:
						System.out.println("Enter name of the student to delete data: ");
						String na = sc.next();
						deleteData(conn, na);
						break;
					case 3:
						System.out.println("Enter name of the student: ");
						String sname = sc.next();
						viewData(conn, sname);
						break;
					case 4:
						System.out.println("Exiting...");
						break;
					default:
						System.out.println("Please enter a valid choice.");
						break;
					}
				}
				break;
			case 2:
				System.out.println("Enter name of the student: ");
				String studentName = sc.next();
				displayStudentWise(conn, studentName);
				break;
			case 3:
				System.out.println("Enter name of the subject: ");
				sc.nextLine();
				String subjectName = sc.nextLine();

				displaySubjectWise(conn, subjectName);
				break;
			case 4:
				int ch = 0;
				while(ch != 3) {
					System.out.println("1. Add Category");
					System.out.println("2. Remove Category");
					System.out.println("3. Exit");
					System.out.println("Enter a choice: ");
					ch = sc.nextInt();
					switch(ch) {
					case 1:
						System.out.println("Enter name of the student to add category: ");
						String name = sc.next();
						System.out.println("Enter subject for which assignment category is going to be added: ");
						sc.nextLine();
						String subject = sc.nextLine();
						System.out.println("Enter assignment category to add: ");
						String assigncategory = sc.next();
						System.out.println("Enter date of submission (DD-Month-YY): ");
						String date = sc.next();
						System.out.println("Enter assignment category marks: ");
						int marks = sc.nextInt();
						addAssignmentCategory(conn, name, subject, assigncategory, date, marks);
						break;

					case 2:
						System.out.println("Enter name of the student to remove category: ");
						String name1 = sc.next();
						System.out.println("Enter subject for which assignment category is going to be deleted: ");
						sc.nextLine();
						String subject1 = sc.nextLine();
						System.out.println("Enter assignment category to remove (include number of category with underscore (for eg. test_1)): ");
						String assigncategory1 = sc.next();
						removeAssignmentCategory(conn, name1, subject1, assigncategory1);
						break;
					case 3:
						System.out.println("Exiting...");
						break;
					default:
						System.out.println("Please enter a valid choice.");
						break;
					}	
				}
				break;
			case 5:
				displayAssignmentCategory(conn);
				break;
			case 6:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Please enter a valid choice.");
				break;
			}
		}
		try {
			conn.close();
			sc.close();
		} catch (SQLException e) {
			System.out.println("Error while closing resources - " + e);
		}
	}

	private static void displayAssignmentCategory(Connection conn) {

		String sql = "SELECT STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, DATE, MARKS FROM STUDENTS";
		try {
			System.out.printf("%-15s %-20s %-20s %-20s %-10s", "Student Name", "Subject", "Assignment Category", "Date of Submission", "Marks");
			System.out.println();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				System.out.printf("%-15s %-20s %-20s %-20s %-10s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}

	}

	@SuppressWarnings("resource")
	private static void removeAssignmentCategory(Connection conn, String name1, String subject1, String assigncategory1) {

		try {
			String sql = "SELECT MARKS FROM STUDENTS WHERE STUDENTNAME = ? AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name1);
			st.setString(2, subject1);
			st.setString(3, assigncategory1);
			ResultSet rs = st.executeQuery();
			int marks = 0, n = 0;
			while(rs.next()) {
				marks = rs.getInt(1);
			}
			if(assigncategory1.substring(0, 4).equalsIgnoreCase("test")) {
				sql = "SELECT SEQUENCE FROM TEST WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				rs = st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET OVERALLSCORE = OVERALLSCORE - TESTSCORE, TESTSCORE = ((TESTSCORE * ?) - (? * 0.4)) / (? - 1), OVERALLSCORE = OVERALLSCORE + TESTSCORE  WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE TEST SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();

			}
			else if(assigncategory1.substring(0, 4).equalsIgnoreCase("quiz")) {
				sql = "SELECT SEQUENCE FROM QUIZ WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET QUIZSCORE = ((QUIZSCORE * ?) - (? * 0.2)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE QUIZ SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else if(assigncategory1.substring(0, 3).equalsIgnoreCase("lab")) {
				sql = "SELECT SEQUENCE FROM LAB WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET LABSCORE = ((LABSCORE * ?) - (? * 0.1)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE LAB SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else if(assigncategory1.substring(0, 4).equalsIgnoreCase("proj")) {
				sql = "SELECT SEQUENCE FROM PROJECT WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET PROJECTSCORE = ((PROJECTSCORE * ?) - (? * 0.3)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE PROJECT SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else {
				System.out.println("Please enter valid assignment category.");
			}
			if(n != 0) {
				sql = "DELETE FROM STUDENTS WHERE STUDENTNAME = ? AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.setString(3, assigncategory1);
				n = st.executeUpdate();
				if(n == 1) {
					System.out.println("Assignment Category deleted successfully.");
				}
			}
		}catch(SQLException e) {
			System.out.println("Error - " + e);
		}
	}

	private static void addAssignmentCategory(Connection conn, String name, String subject, String assigncategory, String date, int marks) {

		Assignments a = new Assignments(name, subject, assigncategory, date, marks);  
		try {
			validateAssignmentCategory(conn, a);
			System.out.println("Assignment category inserted successfully");
		} catch (SQLException e) {
			System.out.println("Data insertion failed");
			System.out.println("Error - " + e);
		}

	}

	private static void displaySubjectWise(Connection conn, String subjectName) {

		String sql = "SELECT * FROM OVERALLRATING WHERE SUBJECT = ?";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, subjectName);
			ResultSet rs = st.executeQuery();
			System.out.println("Subject: " + subjectName);
			System.out.printf("%-25s%-15s %-15s %-15s %-15s %-15s", "Student Name", "Test Score", "Quiz Score", "Lab Score", "Project Score", "Overall Rating(%)");
			System.out.println();
			while(rs.next()) {
				System.out.printf("%-25s %-15s %-15s %-15s %-15s %-15s", rs.getString(1), (rs.getFloat(3) != 0)? rs.getString(3) : "NA", (rs.getFloat(4) != 0) ? rs.getString(4) : "NA", 
						(rs.getFloat(5) != 0) ? rs.getString(5) : "NA", (rs.getFloat(6) != 0) ? rs.getString(6) : "NA", (rs.getFloat(7) != 0) ? rs.getString(7) : "NA");
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}

	}

	private static void displayStudentWise(Connection conn, String studentName) {

		String sql = "SELECT * FROM OVERALLRATING WHERE STUDENTNAME = ?";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, studentName);
			ResultSet rs = st.executeQuery();
			System.out.println("Student: " + studentName);
			System.out.printf("%-25s%-15s %-15s %-15s %-15s %-15s", "Subject", "Test Score", "Quiz Score", "Lab Score", "Project Score", "Overall Rating(%)");
			System.out.println();
			while(rs.next()) {
				System.out.printf("%-25s %-15s %-15s %-15s %-15s %-15s",rs.getString(2), (rs.getFloat(3) != 0)? rs.getString(3) : "NA", (rs.getFloat(4) != 0) ? rs.getString(4) : "NA", 
						(rs.getFloat(5) != 0) ? rs.getString(5) : "NA", (rs.getFloat(6) != 0) ? rs.getString(6) : "NA", (rs.getFloat(7) != 0) ? rs.getString(7) : "NA");
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}
	}

	private static void viewData(Connection conn, String sname) {

		String sql = "SELECT * FROM STUDENTS WHERE STUDENTNAME = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, sname);
			ResultSet rs = st.executeQuery();
			System.out.println("Student Name: " + sname);
			System.out.printf("%-20s %-20s %-20s %-10s", "Subject", "Assignment Category", 
					"Date of Submission", "Marks");
			System.out.println();
			while(rs.next()){
				System.out.printf("%-20s %-20s %-20s %-10s", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}
	}

	private static void deleteData(Connection conn, String na) {

		String sql = "DELETE FROM STUDENTS WHERE STUDENTNAME = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, na);
			int n = st.executeUpdate();
			if(n != 0) {
				System.out.println("Data deleted successfully.");
				sql = "DELETE FROM OVERALLRATING WHERE STUDENTNAME = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, na);
				st.executeUpdate();
			}
			else {
				System.out.println("Data deletion failed.");
			}
		} catch (SQLException e) {
			System.out.println("Error - " + e);
		}
	}

	private static void addStudentData(Connection conn, String n, String sub, String category, String date, int marks) {

		try {
			Assignments a = new Assignments(n, sub, category, date, marks);
			validateAssignmentCategory(conn, a);
			System.out.println("Data inserted successfully.");
		}
		catch(Exception e){
			System.out.println("Data cannot be inserted" + e);
		}

	}

	@SuppressWarnings("resource")
	private static void validateAssignmentCategory(Connection conn, Assignments a) throws SQLException {

		String sql = "INSERT INTO STUDENTS VALUES (?,?,?,?,?)";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, a.studentName);
		st.setString(2, a.subject);
		st.setString(3, a.assignmentCategory);
		st.setString(4, a.date);
		st.setInt(5, a.points);
		st.executeUpdate();
		int seq = 0;
		if(a.assignmentCategory.equalsIgnoreCase("test")){
			try {
				sql = "INSERT INTO TEST (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = TEST.SEQUENCE + 1"
						+ ", MARKS = TEST.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM TEST WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, TESTSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.4 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.4 * (SUM(MARKS) / SEQUENCE)) FROM TEST WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - TESTSCORE, TESTSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + TESTSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			}
			catch (SQLException e) {
				System.out.println("Error - TEST" + e);

			}
		}

		else if(a.assignmentCategory.equalsIgnoreCase("quiz")){
			try {

				sql = "INSERT INTO QUIZ (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = QUIZ.SEQUENCE + 1"
						+ ", MARKS = QUIZ.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM QUIZ WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, QUIZSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.2 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.2 * (SUM(MARKS) / SEQUENCE)) FROM QUIZ WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - QUIZSCORE, QUIZSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + QUIZSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - QUIZ" + e);
			}
		}
		else if(a.assignmentCategory.equalsIgnoreCase("lab")){
			try {
				sql = "INSERT INTO LAB (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = LAB.SEQUENCE + 1"
						+ ", MARKS = LAB.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM LAB WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, LABSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.1 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.1 * (SUM(MARKS) / SEQUENCE)) FROM LAB WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - LABSCORE, LABSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + LABSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - LAB" + e);
			}
		}
		else if(a.assignmentCategory.equalsIgnoreCase("project")){
			try {

				sql = "INSERT INTO PROJECT (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = PROJECT.SEQUENCE + 1"
						+ ", MARKS = PROJECT.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM PROJECT WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, PROJECTSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.3 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.3 * (SUM(MARKS) / SEQUENCE)) FROM PROJECT WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - PROJECTSCORE, PROJECTSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + PROJECTSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();	
			} catch (SQLException e) {
				System.out.println("Error - PROJECT" + e);
			}
		}
		else {
			System.out.println("Please enter valid assignment category.");
		}

		if(seq != 0) {
			sql = "UPDATE STUDENTS SET ASSIGNMENTCATEGORY = ? WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?"; 
			try {
				st = conn.prepareStatement(sql);
				String category = a.getAssignmentCategory() + "_" + seq;
				st.setString(1, category);
				st.setString(2, a.getStudentName());
				st.setString(3, a.getSubject());
				st.setString(4, a.getAssignmentCategory());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - UPDATE" + e);
			}
		}
		else {
			System.out.println("Error.Invalid assignment category");
		}
	}

}

	
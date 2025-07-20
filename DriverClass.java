package com.hm.dc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.hm.doctor.Doctor;
import com.hm.patient.Patient;

public class DriverClass {
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "jahid7400!@#";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection,scanner);
			while (true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
				System.out.println();
				System.out.println("1.  Add Patient");
				System.out.println("2.  view Patient");
				System.out.println("3.  Add Doctor");
				System.out.println("4.  view Doctor");
				System.out.println("5.  Book Appointment");
				System.out.println("6.  Exit");
				System.out.print("Enter your choice: - ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1: {
					patient.add_patient();
					System.out.println();
					break;
				}
				case 2: {
					patient.view_patient();
					System.out.println();
					break;
				}
				case 3: {
					doctor.add_doctor();
					System.out.println();
					break;
				}
				case 4: {
					doctor.view_doctors();
					System.out.println();
					break;
				}
				case 5: {
                  bookAppointment(patient,doctor,connection,scanner);
                  System.out.println();
                  break;
				}
				case 6: {
					return;
				}
				default:
					System.out.println("Enter valid choice!!!");

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.print("enter patient Id: ");
		int patient_id = scanner.nextInt();
		System.out.print("Enter doctor Id: ");
		int doctor_id = scanner.nextInt();
		System.out.print("Enter appointment date (yyyy-mm-dd): ");
		String appointmentDate = scanner.next();
		if (patient.getPatientById(patient_id) && doctor.getDoctorById(doctor_id)) {
			if (checkDoctorAvailability(doctor_id, appointmentDate,connection)) {
				String appointmentQuery = "insert into appointment(patient_id,doctor_id,appointment_date) values(?,?,?)";
				try {
				PreparedStatement ps = connection.prepareStatement(appointmentQuery);
				 ps.setInt(1,patient_id);
				ps.setInt(2,doctor_id);
				ps.setString(3,appointmentDate);
				int rowsAffected = ps.executeUpdate();
				if(rowsAffected>0) {
					System.out.println("Appointment Booked!!");
				}else{
					System.out.println("Failed to book appointment");
				}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Doctor not available on this date!!");
			}
		} else {
			System.out.println("Either patient or doctor does not exist!!");
		}

	}

	public static boolean checkDoctorAvailability(int doctorId, String AppointmentDate,Connection connection) {
		String query = "select count(*) from appointment where doctor_id = ? and appointment_date =?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, doctorId);
			ps.setString(2, AppointmentDate);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int count = rs.getInt(1);
				if(count==0) {
					return true;
				}else {
					return false;
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}

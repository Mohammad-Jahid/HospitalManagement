package com.hm.doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
	private Connection connection;
	private Scanner scanner;

	public Doctor(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;

	}

	public void add_doctor() {
		System.out.print(" Enter Doctor name: - ");
		String name = scanner.next();
		System.out.print(" Enter Doctor Specialization: - ");
		String specialization = scanner.next();

		try {
			String query = "insert into doctor(name,specialization) values(?,?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, specialization);
			int affected_rows = ps.executeUpdate();
			if (affected_rows > 0) {
				System.out.println("Doctor added successfully!!");
			} else {
				System.out.println("failed to add Doctor!!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void view_doctors() {
		String query = "select * from doctor";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("+------------+------------------+---------------------+");
			System.out.println("| Doctor Id  | Name             | Specialization      |");
			System.out.println("+------------+------------------+---------------------+");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String specialization = rs.getString("specialization");
				System.out.printf("|%-12s|%-18s|%-21s|\n", id, name, specialization);
				System.out.println("+------------+------------------+---------------------+");
				System.out.println();
				

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getDoctorById(int id) {
		String query = "select * from doctor where id=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

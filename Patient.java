package com.hm.patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;
   
	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	public void add_patient(){
		System.out.print(" Enter Patient name: - ");
		String name = scanner.next();
		System.out.print(" Enter Patient age: - ");
		int age = scanner.nextInt();
		System.out.print(" Enter Patient gender: - ");
		String gender = scanner.next();
		
		try {
			String query = "insert into patients(name, age, gender) values(?,?,?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setInt(2,age);
			ps.setString(3, gender);
			int affected_rows = ps.executeUpdate();
			if(affected_rows>0) {
				System.out.println("Patient added successfully!!");
				System.out.println();
			}
			else {
				System.out.println("failed to add Patient!!");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void view_patient(){
		String query = "select * from patients";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println("Patient: ");
			System.out.println("+------------+---------------------+----------+-------------+");
			System.out.println("| Patient Id | Name                | Age      | Gender      |");
			System.out.println("+------------+---------------------+----------+-------------+");
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				System.out.printf("| %-11s| %-20s| %-9s| %-12s|\n",id,name,age,gender);
				System.out.println("+------------+---------------------+----------+-------------+");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean	getPatientById(int id) {
		String query = "select * from patients where id=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

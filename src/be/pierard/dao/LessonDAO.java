package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.*;

public class LessonDAO extends DAO<Lesson>{
	public LessonDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Lesson obj){		
		String sql = "INSERT INTO Lesson (MinBookings, MaxBookings, Schedule, InstructorId_FK, LessonTypeId_FK) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, obj.getMinBookings());
            stmt.setInt(2, obj.getMaxBookings());
            stmt.setString(3, obj.getSchedule());
            stmt.setInt(4, obj.getInstructor().getId());
            stmt.setInt(5, obj.getLessonType().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Lesson obj){
		return false;
	}
	
	public boolean update(Lesson obj){
		String sql = "UPDATE Lesson SET MinBookings = ?, MaxBookings = ?, Schedule = ?, InstructorId_FK = ?, LessonTypeId_FK = ? WHERE LessonId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, obj.getMinBookings());
            stmt.setInt(2, obj.getMaxBookings());
            stmt.setString(3, obj.getSchedule());
            stmt.setInt(4, obj.getInstructor().getId());
            stmt.setInt(5, obj.getLessonType().getId());
            stmt.setInt(6, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Lesson find(int id){
		return null;
	}

	public ArrayList<Lesson> findAll() {
	    ArrayList<Lesson> lessonList = new ArrayList<>();
	    String sql = "SELECT l.*, lt.*, i.*, a.* " +
	                 "FROM Lesson l " +
	                 "INNER JOIN Instructor i ON l.InstructorId_FK = i.InstructorId " +
	                 "INNER JOIN LessonType lt ON l.LessonTypeId_FK = lt.LessonTypeId " +
	                 "INNER JOIN Accreditation a ON lt.AccreditationId_FK = a.AccreditationId ";

	    try (PreparedStatement stmt = connect.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        Map<Integer, Instructor> instructorMap = new HashMap<>();

	        while (rs.next()) {
	            int lessonId = rs.getInt("LessonId");
	            int minBookings = rs.getInt("MinBookings");
	            int maxBookings = rs.getInt("MaxBookings");
	            String schedule = rs.getString("Schedule");

	            LessonType lessonType = new LessonType();
	            lessonType.setId(rs.getInt("LessonTypeId"));
	            lessonType.setLevel(rs.getString("Level"));
	            lessonType.setPrice(rs.getDouble("Price"));

	            ArrayList<LessonType> lessonTypeList = new ArrayList<>();
	            lessonTypeList.add(lessonType);
	            Accreditation accreditation = new Accreditation(rs.getInt("AccreditationId"), rs.getString("AccreditationName"), lessonTypeList);
	            lessonType.setAccreditation(accreditation);

	            int instructorId = rs.getInt("InstructorId");
	            String lastname = rs.getString("Lastname");
	            String firstname = rs.getString("Firstname");
	            int age = rs.getInt("Age");
	            String address = rs.getString("Address");
	            String email = rs.getString("Email");
	            double hourlyRate = rs.getDouble("HourlyRate");

	            Instructor instructor = instructorMap.computeIfAbsent(instructorId, id -> {
	                Instructor newInstructor = new Instructor();
	                newInstructor.setId(instructorId);
	                newInstructor.setLastname(lastname);
	                newInstructor.setFirstname(firstname);
	                newInstructor.setAge(age);
	                newInstructor.setAddress(address);
	                newInstructor.setEmail(email);
	                newInstructor.setHourlyRate(hourlyRate);
	                newInstructor.setAccreditationList(new ArrayList<>());
	                return newInstructor;
	            });

	            instructor.addAccreditation(accreditation);

	            Lesson lesson = new Lesson(lessonId, minBookings, maxBookings, schedule, lessonType, instructor);

	            lessonList.add(lesson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lessonList;
	}
}

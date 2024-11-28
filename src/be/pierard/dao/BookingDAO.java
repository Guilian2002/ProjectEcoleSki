package be.pierard.dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.*;

public class BookingDAO extends DAO<Booking>{
	public BookingDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Booking obj){		
		String sql = "INSERT INTO Booking (BookingDate, Duration, Price, GroupSize, IsSpecial, LessonId_FK, SkierId_FK, PeriodId_FK) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(obj.getDate()));
            stmt.setInt(2, obj.getDuration());
            stmt.setDouble(3, obj.getPrice());
            stmt.setInt(4, obj.getGroupSize());
            stmt.setBoolean(5, obj.isSpecial());
            stmt.setInt(6, obj.getLesson().getId());
            stmt.setInt(7, obj.getSkier().getId());
            stmt.setInt(8, obj.getPeriod().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Booking obj){
		return false;
	}
	
	public boolean update(Booking obj){
		String sql = "UPDATE Booking SET BookingDate = ?, Duration = ?, Price = ?, GroupSize = ?, IsSpecial = ?, LessonId_FK = ?, SkierId_FK = ?, PeriodId_FK = ? WHERE BookingId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(obj.getDate()));
            stmt.setInt(2, obj.getDuration());
            stmt.setDouble(3, obj.getPrice());
            stmt.setInt(4, obj.getGroupSize());
            stmt.setBoolean(5, obj.isSpecial());
            stmt.setInt(6, obj.getLesson().getId());
            stmt.setInt(7, obj.getSkier().getId());
            stmt.setInt(8, obj.getPeriod().getId());
            stmt.setInt(9, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Booking find(int id){
		return null;
	}

	public ArrayList<Booking> findAll() {
	    ArrayList<Booking> bookings = new ArrayList<>();
	    String sql = "SELECT b.*, l.*, i.*, lt.*, a.*, ia.*, s.*, p.* " +
	                 "FROM Booking b " +
	                 "INNER JOIN Lesson l ON l.LessonId = b.LessonId_FK " +
	                 "INNER JOIN Instructor i ON i.InstructorId = l.InstructorId_FK " +
	                 "INNER JOIN LessonType lt ON lt.LessonTypeId = l.LessonTypeId_FK " +
	                 "LEFT JOIN InstructorAccreditation ia ON ia.InstructorId_FK = i.InstructorId " +
	                 "LEFT JOIN Accreditation a ON a.AccreditationId = lt.AccreditationId_FK " +
	                 "INNER JOIN Skier s ON s.SkierId = b.SkierId_FK " +
	                 "INNER JOIN Period p ON p.PeriodId = b.PeriodId_FK";

	    try (PreparedStatement stmt = connect.prepareStatement(sql)) {
	        try (ResultSet rs = stmt.executeQuery()) {
	            Map<Integer, Instructor> instructorMap = new HashMap<>();
	            Map<Integer, Skier> skierMap = new HashMap<>();
	            Map<Integer, Period> periodMap = new HashMap<>();
	            Map<Integer, Lesson> lessonMap = new HashMap<>();
	            Map<Integer, LessonType> lessonTypeMap = new HashMap<>();
	            Map<Integer, Accreditation> accreditationMap = new HashMap<>();

	            while (rs.next()) {
	                int lessonTypeId = rs.getInt("LessonTypeId");
	                String lessonLevel = rs.getString("Level");
	                double lessonPrice = rs.getDouble("Price");

	                int accreditationId = rs.getInt("AccreditationId");
	                String accreditationName = rs.getString("AccreditationName");

	                int instructorId = rs.getInt("InstructorId");
	                String instructorLastname = rs.getString("Lastname");
	                String instructorFirstname = rs.getString("Firstname");
	                int instructorAge = rs.getInt("Age");
	                String instructorAddress = rs.getString("Address");
	                String instructorEmail = rs.getString("Email");
	                double instructorHourlyRate = rs.getDouble("HourlyRate");

	                int skierId = rs.getInt("SkierId");
	                String skierLastname = rs.getString("Lastname");
	                String skierFirstname = rs.getString("Firstname");
	                int skierAge = rs.getInt("Age");
	                String skierAddress = rs.getString("Address");
	                String skierEmail = rs.getString("Email");
	                boolean skierInsurance = rs.getBoolean("Insurance");
	                String skierLevel = rs.getString("Level");

	                int periodId = rs.getInt("PeriodId");
	                LocalDate startDate = rs.getDate("StartDate").toLocalDate();
	                LocalDate endDate = rs.getDate("EndDate").toLocalDate();
	                boolean isVacation = rs.getBoolean("IsVacation");

	                int lessonId = rs.getInt("LessonId");
	                String lessonSchedule = rs.getString("Schedule");
	                int lessonMinBookings = rs.getInt("MinBookings");
	                int lessonMaxBookings = rs.getInt("MaxBookings");

	                int bookingId = rs.getInt("BookingId");
	                LocalDate bookingDate = rs.getDate("BookingDate").toLocalDate();
	                int bookingDuration = rs.getInt("Duration");
	                double bookingPrice = rs.getDouble("Price");
	                int bookingGroupSize = rs.getInt("GroupSize");
	                boolean isSpecial = rs.getBoolean("IsSpecial");

	                Accreditation accreditation = accreditationId > 0 ? 
	                    accreditationMap.computeIfAbsent(accreditationId, id -> new Accreditation(
	                        id, accreditationName, new ArrayList<>())) : null;

	                LessonType lessonType = lessonTypeMap.computeIfAbsent(lessonTypeId, id -> new LessonType(
	                    id, lessonLevel, lessonPrice, accreditation));

	                Instructor instructor = instructorMap.computeIfAbsent(instructorId, id -> new Instructor(
	                    id, instructorLastname, instructorFirstname, instructorAge, instructorAddress, instructorEmail,
	                    instructorHourlyRate, new ArrayList<>()
	                ));

	                Skier skier = skierMap.computeIfAbsent(skierId, id -> new Skier(
	                    id, skierLastname, skierFirstname, skierAge, skierAddress, skierEmail, skierInsurance, skierLevel,
	                    new ArrayList<>()
	                ));

	                Period period = periodMap.computeIfAbsent(periodId, id -> new Period(
	                    id, startDate, endDate, isVacation, new ArrayList<>()
	                ));

	                Lesson lesson = lessonMap.computeIfAbsent(lessonId, id -> new Lesson(
	                    id, lessonMinBookings, lessonMaxBookings, lessonSchedule, lessonType, instructor, new ArrayList<>()
	                ));

	                Booking booking = new Booking(
	                    bookingId, bookingDate, bookingDuration, bookingPrice, bookingGroupSize, isSpecial,
	                    instructor, lesson, period, skier
	                );

	                skier.getBookingList().add(booking);
	                lesson.getBookingList().add(booking);
	                period.getBookingList().add(booking);
	                bookings.add(booking);
	            }
	            Booking.linkEntities(bookings);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return bookings;
	}


}

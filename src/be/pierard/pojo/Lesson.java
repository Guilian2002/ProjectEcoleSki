package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import be.pierard.dao.LessonDAO;

public class Lesson {
	private int id;
	private int minBookings;
	private int maxBookings;
	private String schedule;
	private LessonType lessonType;
	private Instructor instructor;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Lesson(int id, int minBookings, int maxBookings, String schedule, LessonType lessonType,
			Instructor instructor, ArrayList<Booking> bookingList) {
		this.id = id;
		this.minBookings = minBookings;
		this.maxBookings = maxBookings;
		this.schedule = schedule;
		this.lessonType = lessonType;
		if(instructor.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "l'instructeur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.instructor = instructor;
		if(bookingList.isEmpty())
			this.bookingList = new ArrayList<Booking>();
		else
			this.bookingList = bookingList;
	}

	public Lesson(int id, int minBookings, int maxBookings, String schedule, LessonType lessonType,
			Instructor instructor) {
		this.id = id;
		this.minBookings = minBookings;
		this.maxBookings = maxBookings;
		this.schedule = schedule;
		this.lessonType = lessonType;
		if(instructor.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "l'instructeur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.instructor = instructor;
		this.bookingList = new ArrayList<Booking>();
	}
	
	//Getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinBookings() {
		return minBookings;
	}

	public void setMinBookings(int minBookings) {
		this.minBookings = minBookings;
	}

	public int getMaxBookings() {
		return maxBookings;
	}

	public void setMaxBookings(int maxBookings) {
		this.maxBookings = maxBookings;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public ArrayList<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(ArrayList<Booking> bookingList) {
		this.bookingList = bookingList;
	}
	
	//Business methods
	public double getLessonPrice() {
	    return lessonType.getPrice();
	}

	public int getCurrentBookingsNumber() {
	    return bookingList.size();
	}

	public boolean isBookingValidForLesson(Booking booking) {
	    int newGroupSize = this.getCurrentBookingsNumber() + booking.getGroupSize();
	    return newGroupSize >= minBookings && newGroupSize <= maxBookings;
	}
	
	//DAO methods
	public boolean createLesson(LessonDAO lessonDAO) {
		return lessonDAO.create(this);
	}
	
	public boolean updateLesson(LessonDAO lessonDAO) {
		return lessonDAO.update(this);
	}
	
	public static ArrayList<Lesson> findAllLesson(LessonDAO lessonDAO){
		return lessonDAO.findAll();
	}

	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(bookingList, id, instructor, lessonType, maxBookings, minBookings, schedule);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lesson other = (Lesson) obj;
		return Objects.equals(bookingList, other.bookingList) && id == other.id
				&& Objects.equals(instructor, other.instructor) && Objects.equals(lessonType, other.lessonType)
				&& maxBookings == other.maxBookings && minBookings == other.minBookings
				&& Objects.equals(schedule, other.schedule);
	}

	@Override
	public String toString() {
		return "Lesson {" +
				"id=" + id + 
				", minBookings=" + minBookings + 
				", maxBookings=" + maxBookings + 
				", schedule=" + schedule + 
				", lessonType=" + lessonType + 
				", instructor=" + instructor + 
				", bookingList=" + (bookingList != null ?
						bookingList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") +
				"}";
	}
}

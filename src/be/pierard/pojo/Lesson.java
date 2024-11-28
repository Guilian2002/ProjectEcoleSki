package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	                "Instructor must not be empty.",
	                "Error",
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
	                "Instructor must not be empty.",
	                "Error",
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
		if (minBookings == 1) {
	        if (schedule.contains("noon 1/2")) {
	            return 60;
	        } else if (schedule.contains("noon 2/2")) {
	            return 90;
	        }
	    }
	    return lessonType.getPrice();
	}

	public int getCurrentBookingsNumber() {
		int sum = 0;
		for(Booking booking : bookingList)
			sum += booking.getGroupSize();
	    return sum;
	}

	public boolean isBookingValidForLesson(Booking booking) {
	    int newGroupSize = this.getCurrentBookingsNumber() + booking.getGroupSize();
	    return (newGroupSize >= minBookings && newGroupSize <= maxBookings) 
	    		&& (booking.getSkier().getLevel().contains(this.lessonType.getLevel()) 
	    		&& (booking.getSkier().getLevel().contains(this.lessonType.getAccreditation().getName())));
	}
	
	public boolean makeLesson(LessonDAO lessonDAO, boolean isUpdate) {
		if (!isInstructorQualified()) {
	       JOptionPane.showMessageDialog(
	           null,
	           "Error: The instructor is not qualified to teach this lesson level.",
	           "Qualification Error",
	           JOptionPane.ERROR_MESSAGE
	       );
	       return false;
		}
		if(schedule.equals("noon 1/2") || schedule.equals("noon 2/2"))
			makeSpecial();
		else
	    	makeCollectif();
	    
		if(isUpdate) {
			if (!updateLesson(lessonDAO)) {
		       JOptionPane.showMessageDialog(null, "Error: Failed to save lesson data to the database.",
		               "Database Error", JOptionPane.ERROR_MESSAGE);
		       return false;
			}
			JOptionPane.showMessageDialog(
				null,
				"Success: The lesson has been successfully updated.",
				"Operation Successful",
				JOptionPane.INFORMATION_MESSAGE
			);
		}
		else {
			if (!createLesson(lessonDAO)) {
		       JOptionPane.showMessageDialog(null, "Error: Failed to save lesson data to the database.",
		               "Database Error", JOptionPane.ERROR_MESSAGE);
		       return false;
			}
			JOptionPane.showMessageDialog(
				null,
				"Success: The lesson has been successfully created.",
				"Operation Successful",
				JOptionPane.INFORMATION_MESSAGE
			);
		}
		return true;
	}
	
	public static void addLessonsToInstructors(ArrayList<Lesson> lessonList) {
		Map<Instructor, List<Lesson>> instructorBookings = new HashMap<>();

	    for (Lesson lesson : lessonList) {
	        instructorBookings
	            .computeIfAbsent(lesson.getInstructor(), k -> lesson.getInstructor().getLessonList())
	            .add(lesson);
	    }
	}
	
	private boolean makeCollectif() {
		String lessonName = this.lessonType.getFullLevel().toLowerCase();
	    String[] courses5To8 = {
	        "ski adulte 1", "ski adulte 2", "ski adulte 3", "ski adulte 4",
	        "télémark 1", "télémark 2", "télémark 3", "télémark 4",
	        "ski de fond 1", "ski de fond 2", "ski de fond 3", "ski de fond 4"
	    };
	    String[] competitionAndOffPisteCourses = { "compétition", "hors-piste" };

	    if (Arrays.stream(competitionAndOffPisteCourses).anyMatch(course -> course.equalsIgnoreCase(lessonName))) {
	        setMinBookings(5);
	        setMaxBookings(8);
	    } else if (Arrays.stream(courses5To8).anyMatch(course -> course.equalsIgnoreCase(lessonName))) {
	        setMinBookings(5);
	        setMaxBookings(8);
	    } else {
	        setMinBookings(6);
	        setMaxBookings(10);
	    }	
		return true;
	}
	
	private void makeSpecial() {
		setMinBookings(1);
        setMaxBookings(4);
	}
	
	private boolean isInstructorQualified() {
		return instructor.isAccreditate(this);
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
				"minBookings=" + minBookings + 
				", maxBookings=" + maxBookings + 
				", schedule=" + schedule + 
				", level = " + lessonType.getFullLevel() + 
				", instructor = " + instructor.getFirstname() + " " + instructor.getLastname() +
				"}";
	}
}

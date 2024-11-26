package be.pierard.pojo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import be.pierard.dao.InstructorDAO;

public class Instructor extends Person{
	private boolean isAvailable;
	private double hourlyRate;
	private ArrayList<Accreditation> accreditationList;
	private ArrayList<Lesson> lessonList;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Instructor(int id, String lastname, String firstname, int age, String address, String email,
			boolean isAvailable, double hourlyRate, ArrayList<Accreditation> accreditationList) {
		super(id, lastname, firstname, age, address, email);
		this.isAvailable = isAvailable;
		this.hourlyRate = hourlyRate;
		if(!accreditationList.isEmpty())
			this.accreditationList = accreditationList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste d'accréditation pour l'instructeur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		this.accreditationList = accreditationList;
		this.bookingList = new ArrayList<Booking>();
		this.lessonList = new ArrayList<Lesson>();
	}

	public Instructor(int id, String lastname, String firstname, int age, String address, String email,
			boolean isAvailable, double hourlyRate, ArrayList<Accreditation> accreditationList,
			ArrayList<Lesson> lessonList, ArrayList<Booking> bookingList) {
		super(id, lastname, firstname, age, address, email);
		this.isAvailable = isAvailable;
		this.hourlyRate = hourlyRate;
		if(!accreditationList.isEmpty())
			this.accreditationList = accreditationList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste d'accréditation pour l'instructeur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		this.lessonList = lessonList;
		this.bookingList = bookingList;
	}
	
	//Getters/setters
	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public ArrayList<Accreditation> getAccreditationList() {
		return accreditationList;
	}

	public void setAccreditationList(ArrayList<Accreditation> accreditationList) {
		this.accreditationList = accreditationList;
	}

	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}

	public ArrayList<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(ArrayList<Booking> bookingList) {
		this.bookingList = bookingList;
	}

	//Business methods
	@Override
	public String getRole() {
		return "Instructor";
	}

	public boolean isAccreditate(LessonType lessonType) {
	    return accreditationList.stream()
	           .anyMatch(accreditation -> accreditation.isInstructorAccreditedForLesson(lessonType));
	}

	public boolean isAvailableForLesson(LocalDate date, String schedule) {
	    for (Booking booking : bookingList) {
	        if (booking.getDate().isEqual(date) && booking.getLesson().getSchedule().equalsIgnoreCase(schedule)) {
	            return false;
	        }
	    }
	    return isAvailable;
	}
	
	public void addAccreditation(Accreditation accreditation) {
        if (!accreditationList.contains(accreditation)) {
            accreditationList.add(accreditation);
        }
    }
	
	//DAO methods
	public boolean createInstructor(InstructorDAO instructorDAO) {
		return instructorDAO.create(this);
	}
	
	public boolean updateInstructor(InstructorDAO instructorDAO) {
		return instructorDAO.update(this);
	}
	
	public static ArrayList<Instructor> findAllInstructor(InstructorDAO instructorDAO){
		return instructorDAO.findAll();
	}

	//Usual methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(accreditationList, bookingList, hourlyRate, isAvailable, lessonList);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instructor other = (Instructor) obj;
		return Objects.equals(accreditationList, other.accreditationList)
				&& Objects.equals(bookingList, other.bookingList)
				&& Double.doubleToLongBits(hourlyRate) == Double.doubleToLongBits(other.hourlyRate)
				&& isAvailable == other.isAvailable && Objects.equals(lessonList, other.lessonList);
	}

	@Override
	public String toString() {
		return "Instructor {" +
				"id=" + getId() +
		        ", lastname='" + getLastname() + '\'' +
		        ", firstname='" + getFirstname() + '\'' +
		        ", age=" + getAge() +
		        ", address='" + getAddress() + '\'' +
		        ", email='" + getEmail() + '\'' +
				", isAvailable=" + (isAvailable ? "yes" : "no") + 
				", hourlyRate=" + hourlyRate + 
				", accreditationList=" + (accreditationList != null ?
						accreditationList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") + 
				", lessonList=" + (lessonList != null ?
						lessonList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]")+ 
				", bookingList=" + (bookingList != null ?
						bookingList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") + 
				"}";
	}
	
}

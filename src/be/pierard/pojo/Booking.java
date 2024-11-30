package be.pierard.pojo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JOptionPane;
import be.pierard.dao.BookingDAO;

public class Booking {
	private int id;
	private LocalDate date;
	private int duration;
	private double price;
	private int groupSize;
	private boolean isSpecial;
	private Instructor instructor;
	private Lesson lesson;
	private Period period;
	private Skier skier;
	
	//CTOR
	public Booking(int id, LocalDate date, int duration, double price, int groupSize, boolean isSpecial, Instructor instructor,
			Lesson lesson, Period period, Skier skier) {
		this.id = id;
		this.date = date;
		this.duration = duration;
		this.price = price;
		this.isSpecial = isSpecial;
		this.groupSize = groupSize;
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
		if(lesson.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "Lesson must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.lesson = lesson;
		if(period.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "Period must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.period = period;
		if(skier.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "Skier must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.skier = skier;
	}

	//Getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	
	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Skier getSkier() {
		return skier;
	}

	public void setSkier(Skier skier) {
		this.skier = skier;
	}
	
	//Business methods
	public double calculatePrice() {
	    double basePrice = 0.0;
	    int totalDays = period.getNumberOfDays();
	    double instructorRate = instructor.getHourlyRate();
	    boolean hasInsurance = skier.isInsurance();

	    if (isSpecial()) {
	        int totalHours = calculateTotalHoursSpecial(totalDays);

	        basePrice = ((totalHours * instructorRate) / groupSize)
	                     + (hasInsurance ? 20 : 0)
	                     + (totalDays * lesson.getLessonPrice());

	    } else {
	        int totalWeeks = period.getNumberOfWeeks();
	        int totalHours = calculateTotalHoursGroup();

	        basePrice = ((lesson.getLessonPrice() * totalWeeks)
	                    + (hasInsurance ? 20 : 0)
	                    + ((totalHours * instructorRate) / groupSize));

	        if (lesson.getSchedule().contains("morning+afternoon")) {
	            basePrice /= 0.85;
	        }
	    }
	    
	    if (period.isVacation()) {
	        basePrice *= 1.2;
	    }

	    return Math.round(basePrice * 10.0) / 10.0;
	}
	
	public boolean dataVerification() {
	    if(lesson.getMinBookings() == 1 && lesson.getMaxBookings() == 4)
	    	setSpecial(true);
	    else
	    	setSpecial(false);
		return lesson.isBookingValidForLesson(this) && instructor.isAvailable(period, lesson) && this.isSpecialCanBookForLater();
	}
	
	public boolean makeBooking(BookingDAO bookingDAO) {
		if (!dataVerification()) {
	        JOptionPane.showMessageDialog(null, "Error: Invalid booking data. Please check and try again.",
	                "Data Validation Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
		this.setPrice(this.calculatePrice());
		this.setDuration(period.getNumberOfDays());
		this.setDate(LocalDate.now());
		if (!createBooking(bookingDAO)) {
			JOptionPane.showMessageDialog(null, "Error: Failed to save booking data to the database.",
					"Database Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		JOptionPane.showMessageDialog(null, "Success: Booking has been successfully created!",
			"Operation Successful", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
	
	private int calculateTotalHoursSpecial(int totalDays) {
	    int hoursPerDay = 0;

	    if (lesson.getSchedule().contains("noon 1/2")) {
	        hoursPerDay = 1;
	    } else if (lesson.getSchedule().contains("noon 2/2")) {
	        hoursPerDay = 2;
	    }

	    return totalDays * hoursPerDay;
	}
	
	private int calculateTotalHoursGroup() {
	    if (lesson.getSchedule().contains("morning+afternoon")) {
	        return 6 * 6 + 3; 
	    } else {
	        return 7 * 3;
	    }
	}
	
	private boolean isSpecialCanBookForLater() {
		if(isSpecial()) {
			if (period.isVacation() && !period.isWithinOneWeek()) {
			    return false;
			}
			if (!period.isVacation() && !period.isWithinOneMonth()) {
			    return false;
			}
		}
		return true;
	}
	
	//DAO methods
	public boolean createBooking(BookingDAO bookingDAO) {
		return bookingDAO.create(this);
	}
	
	public static ArrayList<Booking> findAllBookings(BookingDAO bookingDAO){
		return bookingDAO.findAll();
	}
	
	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(date, duration, groupSize, id, instructor, lesson, period, price, skier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		return Objects.equals(date, other.date) && duration == other.duration && groupSize == other.groupSize
				&& id == other.id && Objects.equals(instructor, other.instructor)
				&& Objects.equals(lesson, other.lesson) && Objects.equals(period, other.period)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(skier, other.skier);
	}

	@Override
	public String toString() {
		return "Booking {" + 
				"date = " + date + 
				", duration = " + duration + 
				", price = " + price + 
				", groupSize = " + groupSize +  
				", lesson = " + lesson + 
				", period = " + period + 
				", skier = " + skier.getFirstname() + " " + skier.getLastname() + 
				"}";
	}

}

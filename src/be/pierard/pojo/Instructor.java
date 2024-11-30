package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

import be.pierard.dao.BookingDAO;
import be.pierard.dao.EcoleSkiConnection;
import be.pierard.dao.InstructorDAO;

public class Instructor extends Person{
	private double hourlyRate;
	private ArrayList<Accreditation> accreditationList;
	private ArrayList<Lesson> lessonList;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Instructor() {}
	public Instructor(int id, String lastname, String firstname, int age, String address, String email,
			double hourlyRate, ArrayList<Accreditation> accreditationList) {
		super(id, lastname, firstname, age, address, email);
		this.hourlyRate = hourlyRate;
		if (accreditationList == null || accreditationList.isEmpty()) {
			JOptionPane.showMessageDialog(
	                null,
	                "Accreditation list must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		this.accreditationList = accreditationList;
		this.bookingList = new ArrayList<Booking>();
		this.lessonList = new ArrayList<Lesson>();
		
		System.out.println(this.toString() + accreditationList);
	}

	public Instructor(int id, String lastname, String firstname, int age, String address, String email,
			double hourlyRate, ArrayList<Accreditation> accreditationList,
			ArrayList<Lesson> lessonList, ArrayList<Booking> bookingList) {
		super(id, lastname, firstname, age, address, email);
		this.hourlyRate = hourlyRate;
		if(!accreditationList.isEmpty())
			this.accreditationList = accreditationList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "Accreditation list must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		this.lessonList = lessonList;
		this.bookingList = bookingList;
	}
	
	//Getters/setters
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

	public boolean isAccreditate(Lesson lesson) {
	    return getAccreditationList().stream()
	            .flatMap(accreditation -> accreditation.getLessonTypeList().stream())
	            .anyMatch(lessonType -> lessonType.getFullLevel().equals(lesson.getLessonType().getFullLevel()));
	}
	
	public void addAccreditation(Accreditation accreditation) {
        if (!accreditationList.contains(accreditation)) {
            accreditationList.add(accreditation);
        }
    }
	
	@Override
    public boolean dataVerification() {
        return baseDataVerification() && getAge() >= 18 && hourlyRate > 0;
    }
	
	public boolean makeInstructor(InstructorDAO instructorDAO, boolean isUpdate) {
	    if (!dataVerification()) {
	        JOptionPane.showMessageDialog(null, "Error: Invalid instructor data. Please check and try again.",
	                "Data Validation Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    if(isUpdate) {
	    	if (!updateInstructor(instructorDAO)) {
		        JOptionPane.showMessageDialog(null, "Error: Failed to save instructor data to the database.",
		                "Database Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    for(Accreditation accreditation : accreditationList) {
		    	for(LessonType lessonType : accreditation.getLessonTypeList()) {
		    		if (!updateInstructorAccreditation(instructorDAO, accreditation.getId(), lessonType.getLevel())) {
		    	        JOptionPane.showMessageDialog(null, "Error: Failed to save instructor data to the database.",
		    	                "Database Error", JOptionPane.ERROR_MESSAGE);
		    	        return false;
		    	    }
		    	}
		    }
		    JOptionPane.showMessageDialog(null, "Success: Instructor has been successfully updated!",
		            "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
	    }
	    else {
	    	if (!createInstructor(instructorDAO)) {
		        JOptionPane.showMessageDialog(null, "Error: Failed to save instructor data to the database.",
		                "Database Error", JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
	    	int id = findInstructorLastCorrespondingId(instructorDAO, getLastname(),getFirstname(), getAge(), 
	    			getAddress(), getEmail(), getHourlyRate());
	    	
	    	if (id == -1) {
    	        JOptionPane.showMessageDialog(null, "Error: Failed to retrieve instructor data from the database.",
    	                "Database Error", JOptionPane.ERROR_MESSAGE);
    	        return false;
    	    }
	    	
	    	this.setId(id);
	    	
		    for(Accreditation accreditation : accreditationList) {
		    	for(LessonType lessonType : accreditation.getLessonTypeList()) {
		    		if (!createInstructorAccreditation(instructorDAO, accreditation.getId(), lessonType.getLevel())) {
		    	        JOptionPane.showMessageDialog(null, "Error: Failed to save instructor data to the database.",
		    	                "Database Error", JOptionPane.ERROR_MESSAGE);
		    	        return false;
		    	    }
		    	}
		    }
		    JOptionPane.showMessageDialog(null, "Success: Instructor has been successfully created!",
		            "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
	    }
	    return true;
	}
	
	public boolean isAvailable(Period period, Lesson lesson) {
	    ArrayList<Booking> allBookings = Booking.findAllBookings(new BookingDAO(EcoleSkiConnection.getInstance()));
	    for (Booking booking : allBookings) {
	        Lesson bookedLesson = booking.getLesson();
	        Instructor bookedInstructor = bookedLesson.getInstructor();
	        Period bookedPeriod = booking.getPeriod();
	        if (bookedLesson == null || bookedInstructor == null || bookedPeriod == null) {
	            return true;
	        }
	        if (bookedInstructor.equals(lesson.getInstructor()) && !bookedLesson.equals(lesson)) {
	            if (doPeriodsOverlap(bookedPeriod, period)) {
	                return false;
	            }
	        }
	    }
	    return true;
	}

	private boolean doPeriodsOverlap(Period period1, Period period2) {
	    return (period1.getStartDate().isBefore(period2.getEndDate()) && 
	            period1.getEndDate().isAfter(period2.getStartDate()));
	}

	//DAO methods
	public static void filterInstructorsWithAccreditations(Map<Integer, Instructor> instructorMap, ArrayList<Instructor> instructors) {
	    for (Instructor instructor : instructorMap.values()) {
	        if (!instructor.getAccreditationList().isEmpty()) {
	            instructors.add(instructor);
	        }
	    }
	}
	
	public static void mergeAccreditations(ArrayList<Instructor> instructors) {
	    for (Instructor instructor : instructors) {
	        ArrayList<Accreditation> accreditations = instructor.getAccreditationList();
	        ArrayList<Accreditation> mergedAccreditations = new ArrayList<>();

	        for (int i = 0; i < accreditations.size(); i++) {
	            Accreditation acc1 = accreditations.get(i);
            	acc1.getLessonTypeList().getFirst().setAccreditation(acc1);
	            boolean merged = false;

	            for (int j = i + 1; j < accreditations.size(); j++) {
	                Accreditation acc2 = accreditations.get(j);

	                if (acc1.getId() == acc2.getId() && acc1.getName().equals(acc2.getName())) {
	                	acc2.getLessonTypeList().getFirst().setAccreditation(acc1);
	                	acc1.addLessonType(acc2.getLessonTypeList().getFirst());
	                    accreditations.remove(j);
	                    j--;
	                    merged = true;
	                }
	            }

	            if (!merged || !mergedAccreditations.contains(acc1)) {
	                mergedAccreditations.add(acc1);
	            }
	        }

	        instructor.setAccreditationList(mergedAccreditations);
	    }
	}
	
	public boolean createInstructor(InstructorDAO instructorDAO) {
		return instructorDAO.create(this);
	}
	
	public boolean createInstructorAccreditation(InstructorDAO instructorDAO, int accreditationId, String level) {
		return instructorDAO.createInstructorAccreditation(this.getId(), accreditationId, level);
	}
	
	public boolean updateInstructor(InstructorDAO instructorDAO) {
		return instructorDAO.update(this);
	}
	
	public boolean updateInstructorAccreditation(InstructorDAO instructorDAO, int accreditationId, String level) {
		return instructorDAO.updateInstructorAccreditation(this.getId(), accreditationId, level);
	}
	
	public static ArrayList<Instructor> findAllInstructor(InstructorDAO instructorDAO){
		return instructorDAO.findAll();
	}
	
	public static int findInstructorLastCorrespondingId(InstructorDAO instructorDAO, String lastname, 
			String firstname, int age, String address, String email, double hourlyRate){
		return instructorDAO.findInstructorLastCorrespondingId(lastname, firstname, age, address, email, hourlyRate);
	}

	//Usual methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(accreditationList, bookingList, hourlyRate, lessonList);
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
				&& Objects.equals(lessonList, other.lessonList);
	}

	@Override
	public String toString() {
		return "Instructor {" +
		        "lastname = " + getLastname() +
		        ", firstname = " + getFirstname() +
		        ", age = " + getAge() +
		        ", address = " + getAddress() +
		        ", email = " + getEmail() +
				", hourlyRate = " + hourlyRate + 
				", accreditationList = " + (accreditationList != null ?
						accreditationList.stream().map(Accreditation::toString).collect(Collectors.joining(",\n", "[\n", "\n]")) : "[]") + 
				"}";
	}
	
}

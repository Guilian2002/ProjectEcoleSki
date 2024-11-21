package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Objects;

public class Skier extends Person {
	private boolean insurance;
	private String level;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Skier(int id, String lastname, String firstname, int age, String address, String email, boolean insurance,
			String level) {
		super(id, lastname, firstname, age, address, email);
		this.insurance = insurance;
		this.level = level;
		this.bookingList = new ArrayList<Booking>();
	}
	
	public Skier(int id, String lastname, String firstname, int age, String address, String email, boolean insurance,
			String level, ArrayList<Booking> bookingList) {
		super(id, lastname, firstname, age, address, email);
		this.insurance = insurance;
		this.level = level;
		if(bookingList.isEmpty())
			this.bookingList = new ArrayList<Booking>();
		else
			this.bookingList = bookingList;
	}

	//Getters/Setters
	public boolean isInsurance() {
		return insurance;
	}

	public void setInsurance(boolean insurance) {
		this.insurance = insurance;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
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
		return "Skier";
	}

	//Usual methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(insurance, level);
		return result;
	}
	
	public String getLessonTypeFromLevel(ArrayList<Accreditation> accreditationList) {
        if (level == null || level.isEmpty()) {
            throw new IllegalArgumentException("Le niveau du Skier ne peut pas être vide.");
        }
        for (Accreditation accreditation : accreditationList) {
            if (level.startsWith(accreditation.getName())) {
                return level.substring(accreditation.getName().length()).trim();
            }
        }
        throw new IllegalArgumentException("Aucune accréditation ne correspond au niveau : " + level);
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skier other = (Skier) obj;
		return insurance == other.insurance && Objects.equals(level, other.level);
	}

	@Override
	public String toString() {
		return "Skier {" +
	            "id=" + getId() +
	            ", lastname='" + getLastname() + '\'' +
	            ", firstname='" + getFirstname() + '\'' +
	            ", age=" + getAge() +
	            ", address='" + getAddress() + '\'' +
	            ", email='" + getEmail() + '\'' +
	            ", insurance=" + (insurance ? "yes" : "no") +
	            ", level='" + level + '\'' +
	            '}';
	}
}
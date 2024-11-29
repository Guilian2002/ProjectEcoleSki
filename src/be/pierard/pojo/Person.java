package be.pierard.pojo;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public abstract class Person {
	private int id;
	private String lastname;
	private String firstname;
	private int age;
	private String address;
	private String email;
	
	//CTOR
	public Person() {}
	public Person(int id, String lastname, String firstname, int age, String address, String email) {
	    this.id = id;
	    this.lastname = validateString(lastname, "lastname");
	    this.firstname = validateString(firstname, "firstname");
	    if(age >= 4)
	    	this.age = age;
	    else {
	    	JOptionPane.showMessageDialog(
	                null,
	                "Age must be higher or equals to 4.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
	    }
	    this.address = validateString(address, "address");
	    this.email = validateString(email,"email");
	}
	
	//Getters/Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//Business Methods
	public abstract String getRole();
	
	private String validateString(String value, String fieldName) {
	    if (value == null || value.trim().isEmpty()) {
	    	JOptionPane.showMessageDialog(
	                null,
	                fieldName + " must not be empty.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
	    }
	    return value;
	}
	
	public abstract boolean dataVerification();

    protected boolean baseDataVerification() {
        return firstname != null && !firstname.isEmpty() &&
               lastname != null && !lastname.isEmpty() &&
               Pattern.matches("^[a-zA-ZÀ-ÖØ-öø-ÿ'\\- ]{1,32}$", firstname) &&
               Pattern.matches("^[a-zA-ZÀ-ÖØ-öø-ÿ'\\- ]{1,32}$", lastname) &&
               address != null && address.length() < 512 &&
               email != null && email.length() < 255;
    }
	
	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(address, age, email, firstname, id, lastname);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(address, other.address) && age == other.age && Objects.equals(email, other.email)
				&& Objects.equals(firstname, other.firstname) && id == other.id
				&& Objects.equals(lastname, other.lastname);
	}
	@Override
	public String toString() {
	    return "Person {" +
	            "lastname = " + lastname +
	            ", firstname = " + firstname +
	            ", age = " + age +
	            ", address = " + address +
	            ", email = " + email +
	            '}';
	}
	
}

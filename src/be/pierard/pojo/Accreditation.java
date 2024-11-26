package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Accreditation {
    private int id;
    private String name;
    private ArrayList<Instructor> instructorList;
    private ArrayList<LessonType> lessonTypeList;
    
    //CTOR
    public Accreditation() {}
    public Accreditation(int id, String name, ArrayList<LessonType> lessonTypeList) {
    	this.id = id;
		this.name = name;
		this.instructorList = new ArrayList<Instructor>();
		if(!lessonTypeList.isEmpty())
			this.lessonTypeList = lessonTypeList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste de type de leçons pour les accreditations ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
    }
	public Accreditation(int id, String name, ArrayList<Instructor> instructorList,
			ArrayList<LessonType> lessonTypeList) {
		this.id = id;
		this.name = name;
		if(instructorList.isEmpty())
			this.instructorList = new ArrayList<Instructor>();
		else
			this.instructorList = instructorList;
		if(!lessonTypeList.isEmpty())
			this.lessonTypeList = lessonTypeList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste de type de leçons pour les accreditations ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
	}
	
	//Getters/Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Instructor> getInstructorList() {
		return instructorList;
	}
	public void setInstructorList(ArrayList<Instructor> instructorList) {
		this.instructorList = instructorList;
	}
	public ArrayList<LessonType> getLessonTypeList() {
		return lessonTypeList;
	}
	public void setLessonTypeList(ArrayList<LessonType> lessonTypeList) {
		this.lessonTypeList = lessonTypeList;
	}
	
	//Business methods
	public boolean isInstructorAccreditedForLesson(LessonType lessonType) {
	    return lessonType.getLevel().equalsIgnoreCase(name);
	}
	
	public void addLessonType(LessonType lessonType) {
        if (!lessonTypeList.contains(lessonType)) {
            lessonTypeList.add(lessonType);
        }
    }
	
	//DAO methods
	public static Accreditation createIfAbsent(Map<Integer, Accreditation> accreditationMap, 
            int id, String name, LessonType lessonType) {
		Accreditation accreditation = accreditationMap.get(id);
		if (accreditation == null) {
			ArrayList<LessonType> lessonTypeList = new ArrayList<>();
			lessonTypeList.add(lessonType);
			accreditation = new Accreditation(id, name, lessonTypeList);
			accreditation.lessonTypeList.getFirst().setAccreditation(accreditation);
			accreditationMap.put(id, accreditation);
		} else {
			lessonType.setAccreditation(accreditation);
			accreditation.addLessonType(lessonType);
		}
		return accreditation;
	}
	
	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(id, instructorList, lessonTypeList, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accreditation other = (Accreditation) obj;
		return id == other.id && Objects.equals(instructorList, other.instructorList)
				&& Objects.equals(lessonTypeList, other.lessonTypeList) && Objects.equals(name, other.name);
	}
	@Override
	public String toString() {
		return "Accreditation {" +
	            "id=" + id +
	            ", name='" + name + '\'' +
	            ", instructorList=" + (instructorList != null ? 
	            		instructorList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") +
	            ", lessonTypeList=" + (lessonTypeList != null ? 
	            		lessonTypeList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") +
	            '}';
	}
}

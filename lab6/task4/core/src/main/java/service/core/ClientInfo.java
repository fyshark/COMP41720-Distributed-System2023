package service.core;

import service.message.MySerializable;

import java.io.Serializable;

/**
 * Data Class that contains client information
 * 
 * @author Rem
 *
 */
public class ClientInfo implements MySerializable {
	public static final char MALE				= 'M';
	public static final char FEMALE				= 'F';

	public String getName() {
		return name;
	}

	public char getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public ClientInfo(String name, char sex, int age, double height, double weight, boolean smoker, boolean medicalIssues) {
		this.name = name;
		this.gender = sex;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.smoker = smoker;
		this.medicalIssues = medicalIssues;
	}
	
	public ClientInfo() {}

	/**
	 * Public fields are used as modern best practice argues that use of set/get
	 * methods is unnecessary as (1) set/get makes the field mutable anyway, and
	 * (2) set/get introduces additional method calls, which reduces performance.
	 */
	public String name;
	public char gender;
	public int age;
	public double height;
	public double weight;
	public boolean smoker;
	public boolean medicalIssues;

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public double getWeight() {
		return weight;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public boolean isMedicalIssues() {
		return medicalIssues;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public void setMedicalIssues(boolean medicalIssues) {
		this.medicalIssues = medicalIssues;
	}
}

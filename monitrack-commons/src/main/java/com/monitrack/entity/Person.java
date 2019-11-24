package com.monitrack.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.monitrack.enumeration.UserProfile;

public class Person {

	private int idPerson;
	private String namePerson;
	private String userName;
	private String password;
	private UserProfile userProfil;
	private Timestamp creationDate;
	
	
	public Person() {
		
	}

	/**
	 * Constructor used to retrieved a Person from the database
	 * 
	 * @param idPerson
	 * @param namePerson
	 * @param creationDate
	 */
	public Person(int idPerson, String namePerson, String userName, String password, UserProfile userProfil, Timestamp creationDate) {
		this.idPerson = idPerson;
		this.namePerson = namePerson;
		this.userName = userName;
		this.password = password;
		this.userProfil = userProfil;
		this.creationDate = creationDate;
	}
  
	/**
	 * Constructor used when creating a Person from the Graphical User Interface
	 * 
	 * @param namePerson
	 */
	public Person(String namePerson,  String userName, String password, UserProfile userProfil) {
		this(0, namePerson,userName, password, userProfil, new Timestamp(System.currentTimeMillis()));
		/*this.namePerson = namePerson;
		this.creationDate = new Timestamp(System.currentTimeMillis());*/
	}

	@JsonGetter("id")
	public int getIdPerson() {
		return idPerson;
	}

	@JsonSetter("id")
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	@JsonGetter("name")
	public String getNamePerson() {
		return namePerson;
	}

	@JsonSetter("name")
	public void setNamePerson(String namePerson) {
		this.namePerson = namePerson;
	}	
	
	
	@JsonGetter("user_name")
	public String getUserName() {
		return userName;
	}

	@JsonSetter("user_name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonGetter("password")
	public String getPassword() {
		return password;
	}

	@JsonSetter("password")
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonGetter("user_profil")
	public UserProfile getUserProfil() {
		return userProfil;
	}

	@JsonSetter("user_profil")
	public void setUserProfil(UserProfile userProfil) {
		this.userProfil = userProfil;
	}

	@JsonGetter("creation_date")
	public Timestamp getCreationDate() {
		return creationDate;
	}

	@JsonSetter("creation_date")
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return idPerson + "#" + namePerson + " - créée le " + creationDate + " avec un profil de type " + userProfil;
		/*return "Person [idPerson=" + idPerson + ", namePerson=" + namePerson + ", creationDate=" + creationDate
				+ "]";*/
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + idPerson;
		result = prime * result + ((namePerson == null) ? 0 : namePerson.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userProfil == null) ? 0 : userProfil.hashCode());
		return result;
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
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (idPerson != other.idPerson)
			return false;
		if (namePerson == null) {
			if (other.namePerson != null)
				return false;
		} else if (!namePerson.equals(other.namePerson))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userProfil != other.userProfil)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	

}

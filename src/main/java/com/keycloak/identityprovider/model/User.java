package com.keycloak.identityprovider.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
    @NamedQuery(name = "getAllUsers", query = "select u from User u where u.realm = :realm"),
    @NamedQuery(name = "getUserById", query = "select u from User u where u.idUser = :id and u.realm = :realm"),
    @NamedQuery(name = "getUserByUsername", query = "select u from User u where u.username = :username and u.realm = :realm"),
    @NamedQuery(name = "getUserByEmail", query = "select u from User u where u.email = :email and u.realm = :realm"),
    @NamedQuery(name = "getUsersSize", query = "select count(u) from User u where u.realm = :realm"),
    @NamedQuery(name = "searchForUser", 
    			query = "select u from User u where "
    					+ "( lower(u.username) like :search or u.email like :search ) and "
    					+ "u.realm = :realm order by u.username"),
    @NamedQuery(name = "searchForUserByParams", 
    			query = "select u from User u where "
    					+ "(:first is null or u.name like :first) and "
    					+ "(:last is null or u.surname like :last) and "
    					+ "(:email is null or u.email like :email) and "
    					+ "(:username is null or u.username like :username) and "
    					+ "u.realm = :realm"),
    @NamedQuery(name = "searchForUsersByGroup", query = "select u from User u where u.realm = :realm and u.group = :group"),
})


@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private Integer idUser;

	@Column(name = "name")
	private String name;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
		
	@Column(name = "email")
	private String email;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "realm_name")
	private String realm;
	
	@Column(name = "group_name")
	private String group;
	
	
	
	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", name=" + name + ", surname=" + surname + ", username=" + username
				+ ", password=" + password + ", email=" + email + ", enabled=" + enabled + ", realm=" + realm
				+ ", group=" + group + "]";
	}	
	
	

}

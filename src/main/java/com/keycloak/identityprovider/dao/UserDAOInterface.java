package com.keycloak.identityprovider.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.keycloak.identityprovider.model.User;

public interface UserDAOInterface {

	List<User> getAllUsers(String realmName);

	List<User> getAllUsers(String realmName, int firstResult, int maxResults);

	int getUsersCount(String realmName);

	Optional<User> findUserById(String id, String realmName);

	Optional<User> findUserByUsername(String username, String realmName);

	Optional<User> findUserByEmail(String email, String realmName);

	List<User> findUsers(String search, String realmName);

	List<User> findUsers(String search, String realmName, int firstResult, int maxResults);

	List<User> findUsersByParams(Map<String, String> params, String realmName);

	List<User> findUsersByParams(Map<String, String> params, String realmName, int firstResult, int maxResults);
	
	List<User> findUsersByGroup(String realmName, String groupName);
	
	List<User> findUsersByGroup(String realmName, String groupName, int firstResult, int maxResults);

	List<User> findUserByAttribute(String attrName, String attrValue, String realmName);

	boolean updateCredentials(User userEntity);

	void disableCredential(User userEntity);

}
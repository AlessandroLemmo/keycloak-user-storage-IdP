package com.keycloak.identityprovider.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.keycloak.credential.CredentialInput;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import com.keycloak.identityprovider.model.User;

public interface UserServiceInterface {

	Optional<User> findUserById(String id, RealmModel realm);

	Optional<User> findUserByUsername(String username, RealmModel realm);

	Optional<User> findUserByEmail(String email, RealmModel realm);

	int getUsersCount(RealmModel realm);

	List<User> getAllUsers(RealmModel realm);

	List<User> getAllUsers(RealmModel realm, int firstResult, int maxResults);

	List<User> findUsers(String search, RealmModel realm);

	List<User> findUsers(String search, RealmModel realm, int firstResult, int maxResults);

	List<User> findUsersByParams(Map<String, String> params, RealmModel realm);

	List<User> findUsersByParams(Map<String, String> params, RealmModel realm, int firstResult, int maxResults);

	List<User> findUserByAttribute(String attrName, String attrValue, RealmModel realm);

	List<User> findUsersByGroup(RealmModel realm, GroupModel group);
	
	List<User> findUsersByGroup(RealmModel realm, GroupModel group, int firstResult, int maxResults);
	
	boolean updateCredentials(RealmModel realm, UserModel userModel, CredentialInput input);

	void disableCredential(RealmModel realm, UserModel userModel);

	boolean validateCredentials(RealmModel realm, UserModel userModel, CredentialInput input);

}
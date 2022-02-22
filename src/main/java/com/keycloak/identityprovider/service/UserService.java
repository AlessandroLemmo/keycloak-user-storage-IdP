package com.keycloak.identityprovider.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.keycloak.credential.CredentialInput;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;

import com.keycloak.identityprovider.dao.UserDAOInterface;
import com.keycloak.identityprovider.model.User;

public class UserService implements UserServiceInterface {

	public UserDAOInterface userDAO;
	
	public UserService(UserDAOInterface userDAO) {
		this.userDAO = userDAO;
	}
	
	
	@Override
	public Optional<User> findUserById(String id, RealmModel realm) { 
		String externalId = StorageId.externalId(id); 
		return userDAO.findUserById(externalId, realm.getName());
	}
	
	
	@Override
	public Optional<User> findUserByUsername(String username, RealmModel realm) {
		return userDAO.findUserByUsername(username, realm.getName());
	}
	
	
	@Override
	public Optional<User> findUserByEmail(String email, RealmModel realm) {
		return userDAO.findUserByEmail(email, realm.getName());
	}
	
	
	@Override
	public int getUsersCount(RealmModel realm) {
		return userDAO.getUsersCount(realm.getName());
	}
	
	
	@Override
	public List<User> getAllUsers(RealmModel realm) {
		return userDAO.getAllUsers(realm.getName());
	}
	
	
	@Override
	public List<User> getAllUsers(RealmModel realm, int firstResult, int maxResults) {
		return userDAO.getAllUsers(realm.getName(), firstResult, maxResults);
	}

	
	@Override
	public List<User> findUsers(String search, RealmModel realm) {
		return userDAO.findUsers(search, realm.getName());
	}
	
	
	@Override
	public List<User> findUsers(String search, RealmModel realm, int firstResult, int maxResults) {
		return userDAO.findUsers(search, realm.getName(), firstResult, maxResults);
	}
	
	
	@Override
	public List<User> findUsersByParams(Map<String, String> params, RealmModel realm) {
		params = prepareMap(params);
		return userDAO.findUsersByParams(params, realm.getName());
	}
	
	
	@Override
	public List<User> findUsersByParams(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {		
		params = prepareMap(params);	
		return userDAO.findUsersByParams(params, realm.getName(), firstResult, maxResults);		
	}
	
	
	private Map<String, String> prepareMap(Map<String, String> params) {
		List<String> keys = new ArrayList<>(Arrays.asList("first", "last", "email", "username"));		
		for(String key : keys) {
			String value = params.get(key);
			if(value == null)
				params.put(key, null);
			else
				params.put(key, "%" + value + "%");
		}	
		return params;
	}

	
	@Override
	public List<User> findUsersByGroup(RealmModel realm, GroupModel group) {
		return userDAO.findUsersByGroup(realm.getName(), group.getName());
	}


	@Override
	public List<User> findUsersByGroup(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
		return userDAO.findUsersByGroup(realm.getName(), group.getName(), firstResult, maxResults);
	}
	
	
	@Override
	public List<User> findUserByAttribute(String attrName, String attrValue, RealmModel realm) {
		return userDAO.findUserByAttribute(attrName, attrValue, realm.getName());
	}

	
	@Override
	public boolean updateCredentials(RealmModel realm, UserModel userModel, CredentialInput input) {
		User user = userDAO.findUserByUsername(realm.getName(), userModel.getUsername()).get(); //new User();
		user.setPassword(input.getChallengeResponse());
		return userDAO.updateCredentials(user);
	}
	
	
	@Override
	public void disableCredential(RealmModel realm, UserModel userModel) {				
		User user = userDAO.findUserByUsername(realm.getName(), userModel.getUsername()).get();	
		user.setPassword(null);
		userDAO.disableCredential(user);
	}

	
	@Override
	public boolean validateCredentials(RealmModel realm, UserModel userModel, CredentialInput input) {
		String username = userModel.getUsername();
		UserCredentialModel cred = (UserCredentialModel) input;
		String password = cred.getChallengeResponse();
		Optional<User> userOpt = userDAO.findUserByUsername(realm.getName(), username);
		User user = userOpt.get();
		if (user == null) {
            return false;
        }
		String pass = user.getPassword();
        return pass !=null && pass.equals(password);
	}



}






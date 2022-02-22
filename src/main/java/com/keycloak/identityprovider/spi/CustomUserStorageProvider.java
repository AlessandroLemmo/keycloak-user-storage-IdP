package com.keycloak.identityprovider.spi;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import com.keycloak.identityprovider.dao.UserDAOInterface;
import com.keycloak.identityprovider.log.Log;
import com.keycloak.identityprovider.model.User;
import com.keycloak.identityprovider.model.UserAdapter;
import com.keycloak.identityprovider.service.UserServiceInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Remove;


public class CustomUserStorageProvider implements
        UserStorageProvider,
        UserLookupProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator {

	
    private static final Log logger = Log.getLogger(CustomUserStorageProvider.class.getName());
    
    public KeycloakSession session;
    public ComponentModel model;
    public UserDAOInterface userDAO;
    public UserServiceInterface userService;

    public void setModel(ComponentModel model) {
        this.model = model;
    }

    public void setSession(KeycloakSession session) {
        this.session = session;
    }
    
    public void setService(UserServiceInterface userService) {
    	this.userService = userService;
    }
    
    
    
    /*
     *  
     * UserStorageProvider
     * 
     */
    @Override
    public void preRemove(RealmModel realm) {
    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {
    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {
    }

    @Remove
    @Override
    public void close() {
    }
    
    
    
    /*
     * 
     * UserLookupProvider per l'importazione in keycloak degli utenti nel db mysql
     *
     */
    @Override
    public UserModel getUserById(String id, RealmModel realm) {
    	
        logger.info("Get user by id: " + id);
        Optional<User> optionalUser = userService.findUserById(id, realm);
        return optionalUser.map(user -> new UserAdapter(session, realm, model, user)).orElse(null);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
    	
        logger.info("Get user by username: " + username);
        Optional<User> optionalUser = userService.findUserByUsername(username, realm);
        return optionalUser.map(user -> new UserAdapter(session, realm, model, user)).orElse(null);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
    	
    	logger.info("Get user by email: " + email);
    	Optional<User> optionalUser = userService.findUserByEmail(email, realm);
        return optionalUser.map(user -> new UserAdapter(session, realm, model, user)).orElse(null);
    }
    
    
    
    
    /*
     * 
     * UserQueryProvider per visualizzare nella console di amministrazione gli utenti recuperati dal db
     *
     */
    @Override
    public int getUsersCount(RealmModel realm) {
    	logger.info("Get number of users");        
		return userService.getUsersCount(realm);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
    	logger.info("Get list of users");
        return userService.getAllUsers(realm).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
    	
    	logger.info("Get list of users with firstResult: " + firstResult + " and maxResults: " + maxResults);
    	return userService.getAllUsers(realm, firstResult, maxResults).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
    	logger.info("Search user by parameter: " + search);  	    	
        return userService.findUsers(search, realm).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
    	logger.info("Search user by parameter: " + search + " with firstResult: " + firstResult + " and maxResults: " + maxResults);
    	return userService.findUsers(search, realm, firstResult, maxResults).stream()
                .map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {  
    	logger.info("Search users by map: " + params + " in Realm: " + realm);
        return userService.findUsersByParams(params, realm).stream()
        		.map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
    	logger.info("Search users by map: " + params + " in Realm: " + realm + " with firstResult: " + firstResult + " and maxResults: " + maxResults);
    	return userService.findUsersByParams(params, realm, firstResult, maxResults).stream()
        		.map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
    	logger.info("Search users by group: " + group.getName() + " with firstResult: " + firstResult + " and maxResults: " + maxResults);
    	return userService.findUsersByGroup(realm, group, firstResult, maxResults).stream()
        		.map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
    	logger.info("Search users by group: " + group.getName());     
    	return userService.findUsersByGroup(realm, group).stream()
        		.map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
    	return userService.findUserByAttribute(attrName, attrValue, realm).stream()
    			.map(user -> new UserAdapter(session, realm, model, user))
                .collect(Collectors.toList());

    }
    
    
    
    
    /*
     * 
     * CredentialInputUpdater è usato per persistere sul db l'aggiornamento della password utente
     * 
     */
    @Override
    public boolean updateCredential(RealmModel realm, UserModel userModel, CredentialInput input){
    	logger.info("Update user credentials");
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }   
        return userService.updateCredentials(realm, userModel, input);
    }
    
    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel userModel, String credentialType) {
    	logger.info("disable credential");
		if(!supportsCredentialType(credentialType)) 
			return;	
		userService.disableCredential(realm, userModel);
    }
 
    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        return Collections.emptySet();
    }
    
    
    
    
    /*
     * 
     * CredentialInputValidator è usato per validare le credenziali inserite in input dall'utente
     * 
     */  
    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }


    @Override
    public boolean isValid(RealmModel realm, UserModel userModel, CredentialInput input) {
        logger.info("Login is valid");
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
            return false;
        }
        return userService.validateCredentials(realm, userModel, input);
    }
    
  
}

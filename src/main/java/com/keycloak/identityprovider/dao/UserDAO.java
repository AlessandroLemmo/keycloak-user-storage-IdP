package com.keycloak.identityprovider.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import com.keycloak.identityprovider.model.User;


@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Local(UserDAO.class)
public class UserDAO implements UserDAOInterface {
	
    @PersistenceContext
    private EntityManager entityManager;
    
    @Resource
    private UserTransaction userTransaction;
  
    
    @Override
	public List<User> getAllUsers(String realmName) {     
    	TypedQuery<User> query = entityManager.createNamedQuery("getAllUsers", User.class);
    	query.setParameter("realm", realmName);
		List<User> users =  query.getResultList();	
		return users;
    }
    
    
    @Override
	public List<User> getAllUsers(String realmName, int firstResult, int maxResults) {
    	TypedQuery<User> query = entityManager.createNamedQuery("getAllUsers", User.class);
    	query.setParameter("realm", realmName);
    	query.setFirstResult(firstResult);
    	query.setMaxResults(maxResults);
    	List<User> users =  query.getResultList();		
		return users;
    }
    
    
    @Override
	public int getUsersCount(String realmName) {
    	TypedQuery<Integer> query = entityManager.createNamedQuery("getUsersSize", Integer.class);
    	query.setParameter("realm", realmName);
		Integer result =  query.getSingleResult();
		return result;
    }
    
    
    @Override
	public Optional<User> findUserById(String id, String realmName) { 	
    	//return entityManager.find(User.class, UUID.fromString(id));  	
    	TypedQuery<User> query = entityManager.createNamedQuery("getUserById", User.class);
        query.setParameter("id", Integer.parseInt(id))
        		.setParameter("realm", realmName);
        Optional<User> user = query.getResultList().stream().findFirst();
        return user;
    }
    
    
    @Override
	public Optional<User> findUserByUsername(String username, String realmName) {
    	TypedQuery<User> query = entityManager.createNamedQuery("getUserByUsername", User.class);
        query.setParameter("username", username)
        		.setParameter("realm", realmName);
        Optional<User> user = query.getResultList().stream().findFirst();
        return user;
    }
    
    
    @Override
	public Optional<User> findUserByEmail(String email, String realmName) {
    	TypedQuery<User> query = entityManager.createNamedQuery("getUserByEmail", User.class);
        query.setParameter("email", email)
        		.setParameter("realm", realmName);
        Optional<User> user = query.getResultList().stream().findFirst();
        return user;
    }

    
    @Override
	public List<User> findUsers(String search, String realmName) {
    	TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
    	query.setParameter("search", "%" + search + "%")
    			.setParameter("realm", realmName);
    	return query.getResultList();
    }

    
    @Override
	public List<User> findUsers(String search, String realmName, int firstResult, int maxResults) {
    	TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
    	query.setParameter("search", "%" + search + "%")
    		.setParameter("realm", realmName);
    	query.setFirstResult(firstResult);
    	query.setMaxResults(maxResults);
    	return query.getResultList();
    }
    
    
    @Override
	public List<User> findUsersByParams(Map<String, String> params, String realmName) {  	
    	
    	TypedQuery<User> query = entityManager.createNamedQuery("searchForUserByParams", User.class);
    	query.setParameter("realm", realmName);
    	
    	for(String key : params.keySet()) {
    		String value = params.get(key); 
    		query.setParameter(key, value);
    	} 
    	return query.getResultList();
    }
    
    
    @Override
	public List<User> findUsersByParams(Map<String, String> params, String realmName, int firstResult, int maxResults) {  
    	
    	TypedQuery<User> query = entityManager.createNamedQuery("searchForUserByParams", User.class);
    	query.setParameter("realm", realmName);
    	
    	for(String key : params.keySet()) {
    		String value = params.get(key); 
    		query.setParameter(key, value);
    	}    
    	query.setFirstResult(firstResult);
    	query.setMaxResults(maxResults);
    	return query.getResultList();
    }
    
    
    @Override
	public List<User> findUsersByGroup(String realmName, String groupName) {
    	TypedQuery<User> query = entityManager.createNamedQuery("searchForUsersByGroup", User.class);
    	query.setParameter("realm", realmName)
    			.setParameter("group", groupName);
		return query.getResultList();
	}


	@Override
	public List<User> findUsersByGroup(String realmName, String groupName, int firstResult, int maxResults) {
		TypedQuery<User> query = entityManager.createNamedQuery("searchForUsersByGroup", User.class);
		query.setParameter("realm", realmName)
				.setParameter("group", groupName);
		query.setFirstResult(firstResult)
				.setMaxResults(maxResults);
		return query.getResultList();
	}
    
    
    @Override
	@SuppressWarnings("unchecked")
	public List<User> findUserByAttribute(String attrName, String attrValue, String realmName) {	
    	Query query = entityManager.createQuery("select u from User u where u." + attrName + " like :attrValue and u.realm = :realm");
    	query.setParameter("attrValue", "%" + attrValue + "%")
    			.setParameter("realm", realmName);
    	return query.getResultList();
    }
    

    @Override
	public boolean updateCredentials(User userEntity) {
		try {
			userTransaction.begin();
			entityManager.merge(userEntity);
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}   	
       
    }

    
    @Override
	public void disableCredential(User userEntity) {   
        
        try {
			userTransaction.begin();
			entityManager.merge(userEntity);
			userTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}

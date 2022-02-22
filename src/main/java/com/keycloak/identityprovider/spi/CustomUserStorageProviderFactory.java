package com.keycloak.identityprovider.spi;


import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import com.keycloak.identityprovider.dao.UserDAO;
import com.keycloak.identityprovider.dao.UserDAOInterface;
import com.keycloak.identityprovider.service.UserService;
import com.keycloak.identityprovider.service.UserServiceInterface;

import javax.naming.InitialContext;



public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {
    
    public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
    	try {
            InitialContext ctx = new InitialContext();
            
            //CustomUserStorageProvider provider = (CustomUserStorageProvider)ctx.lookup("java:global/user-storage-jpa-example/" + CustomUserStorageProvider.class.getSimpleName());
            
            UserDAOInterface userDAO = (UserDAOInterface)ctx.lookup("java:global/user-storage-jpa-example/" + UserDAO.class.getSimpleName());
            UserServiceInterface userService = new UserService(userDAO);
            
            CustomUserStorageProvider provider = new CustomUserStorageProvider();
            provider.setModel(model);
            provider.setSession(session);       
            //provider.setDao(userDAO);
            provider.setService(userService);
                      
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    @Override
    public String getId() {
    	String id = "user-storage-spi";
		return id;
    }

    
    @Override
    public void close() {

    }
        
}

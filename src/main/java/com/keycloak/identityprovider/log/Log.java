package com.keycloak.identityprovider.log;

import java.util.logging.Logger;

public class Log {
    private Logger logger;
    
    private Log(String className) {
        logger = Logger.getLogger(className);
    }
    
    public static Log getLogger(String className) {
        return new Log(className);
    }

    public void info(String msg) {
        logger.info("******************" + msg + "******************");
    }

    public void warning(String msg) {
        logger.warning("******************" + msg + "******************");
    }
}

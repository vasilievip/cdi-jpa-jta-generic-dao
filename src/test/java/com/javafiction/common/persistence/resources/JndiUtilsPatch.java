/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javafiction.common.persistence.resources;

import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.arjuna.ats.jta.utils.JNDIManager;
import org.apache.deltaspike.core.impl.util.JndiUtils;
import org.jnp.interfaces.NamingParser;
import org.jnp.server.NamingBeanImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * Adds JbossTM into deltaspike for tests
 * See: https://issues.apache.org/jira/browse/DELTASPIKE-473
 */
public class JndiUtilsPatch {

    private static final NamingBeanImpl NAMING_BEAN = new NamingBeanImpl();
    private static final String TRANSACTION_MANAGER_JNDI = "java:/jboss/TransactionManager";
    private static final String REGISTRY_JNDI = "java:/jboss/TransactionSynchronizationRegistry";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
    private static final String JNP_INTERFACES = "org.jboss.naming:org.jnp.interfaces";
    private static final String JBOSS = "jboss";
    private static final String JDBC = "jdbc";

    static {
        inject();
    }

    public static void inject() {
        InitialContext initialContext;
        Field aField;
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.URL_PKG_PREFIXES, JNP_INTERFACES);
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            System.setProperty(Context.URL_PKG_PREFIXES, JNP_INTERFACES);
            initialContext = new InitialContext(env);
            NAMING_BEAN.start();
            // Bind JTA implementation with default names
            JNDIManager.bindJTAImplementation();
            // Bind JTA implementation with JBoss names. Needed for JTA 1.2 implementation.
            // See https://issues.jboss.org/browse/JBTM-2054
            NAMING_BEAN.getNamingInstance().createSubcontext(new NamingParser().parse(JBOSS));
            NAMING_BEAN.getNamingInstance().createSubcontext(new NamingParser().parse(JDBC));
            jtaPropertyManager.getJTAEnvironmentBean()
                    .setTransactionManagerJNDIContext(TRANSACTION_MANAGER_JNDI);
            jtaPropertyManager
                    .getJTAEnvironmentBean()
                    .setTransactionSynchronizationRegistryJNDIContext(REGISTRY_JNDI);
            JNDIManager.bindJTAImplementation();
            aField = JndiUtils.class.getDeclaredField("initialContext");
            aField.setAccessible(true);
            aField.set(null, initialContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

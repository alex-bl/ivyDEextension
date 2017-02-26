/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.ivyde.eclipse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ivy.util.url.CredentialsStore;
import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

public final class IvyDEsecurityHelper {

    private static final String IVY_DE_CREDENTIALS_BASE_NODE = "org.apache.ivyde.credentials";

    private static final String HOST_KEY = "host";

    private static final String REALM_KEY = "realm";

    private static final String USERNAME_KEY = "username";

    private static final String PASSWORD_KEY = "pwd";

    private IvyDEsecurityHelper() {

    }

    public static void addCredentialsToIvyCredentialStorage(SecuritySetup setup) {
        CredentialsStore.INSTANCE.addCredentials(setup.getRealm(), setup.getHost(),
            setup.getUserName(), setup.getPwd());
    }

    public static void cpyCredentialsFromSecureToIvyStorage() {
        List<SecuritySetup> credentials = getCredentialsFromSecureStore();
        for (SecuritySetup entry : credentials) {
            addCredentialsToIvyCredentialStorage(entry);
        }
    }

    public static void addCredentialsToSecureStorage(SecuritySetup setup) {
        ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
        ISecurePreferences baseNode = preferences.node(IVY_DE_CREDENTIALS_BASE_NODE);
        ISecurePreferences childNode = baseNode.node(setup.getHost());
        ISecurePreferences childChildNode = childNode.node(setup.getRealm());

        try {
            childChildNode.put(HOST_KEY, setup.getHost(), false);
            childChildNode.put(REALM_KEY, setup.getRealm(), false);
            childChildNode.put(USERNAME_KEY, setup.getUserName(), true);
            childChildNode.put(PASSWORD_KEY, setup.getPwd(), true);
            childChildNode.flush();
        } catch (StorageException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static List<SecuritySetup> getCredentialsFromSecureStore() {
        ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
        List<SecuritySetup> setupValues = new ArrayList<SecuritySetup>();
        if (preferences.nodeExists(IVY_DE_CREDENTIALS_BASE_NODE)) {
            ISecurePreferences node = preferences.node(IVY_DE_CREDENTIALS_BASE_NODE);
            String[] childNames = node.childrenNames();
            for (String childName : childNames) {
                ISecurePreferences childNode = node.node(childName);
                String[] childChildNames = childNode.childrenNames();
                for (String childChildName : childChildNames) {
                    ISecurePreferences childChildNode = childNode.node(childChildName);
                    try {
                        setupValues.add(new SecuritySetup(childChildNode.get(HOST_KEY, "localhost"),
                                childChildNode.get(REALM_KEY, "basic"),
                                childChildNode.get(USERNAME_KEY, null),
                                childChildNode.get(PASSWORD_KEY, null)));
                    } catch (StorageException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        Collections.sort(setupValues);
        return setupValues;
    }

    public static void removeCredentials(String host, String realm) {
        removeCredentialsFromSecureStore(host, realm);
        invalidateIvyCredentials(host, realm);
    }

    public static boolean hostExistsInSecureStorage(String host, String realm) {
        ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
        if (preferences.nodeExists(IVY_DE_CREDENTIALS_BASE_NODE)) {
            ISecurePreferences node = preferences.node(IVY_DE_CREDENTIALS_BASE_NODE);
            if (node.nodeExists(host)) {
                ISecurePreferences childNode = node.node(host);
                if (childNode.nodeExists(realm)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void removeCredentialsFromSecureStore(String host, String realm) {
        ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
        if (preferences.nodeExists(IVY_DE_CREDENTIALS_BASE_NODE)) {
            ISecurePreferences node = preferences.node(IVY_DE_CREDENTIALS_BASE_NODE);
            if (node.nodeExists(host)) {
                ISecurePreferences childNode = node.node(host);
                if (childNode.nodeExists(realm)) {
                    childNode.node(realm).removeNode();
                    try {
                        node.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void invalidateIvyCredentials(String host, String realm) {
        // need to invalidate => on credentialStore just add-ops allowed
        CredentialsStore.INSTANCE.addCredentials(host, realm, null, null);
    }

    public static boolean credentialsInSecureStorage() {
        return SecurePreferencesFactory.getDefault().nodeExists(IVY_DE_CREDENTIALS_BASE_NODE);
    }

}

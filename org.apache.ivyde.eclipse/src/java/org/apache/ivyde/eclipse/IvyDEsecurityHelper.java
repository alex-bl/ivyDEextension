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

import java.util.ArrayList;
import java.util.List;

import org.apache.ivy.util.url.CredentialsStore;
import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

public class IvyDEsecurityHelper {

    private static final String IVY_DE_CREDENTIALS_BASE_NODE = "org.apache.ivyde.credentials";

    private static final String HOST_KEY = "host";

    private static final String REALM_KEY = "realm";

    private static final String USERNAME_KEY = "username";

    private static final String PASSWORD_KEY = "pwd";

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

        try {
            childNode.put(HOST_KEY, setup.getHost(), false);
            childNode.put(REALM_KEY, setup.getRealm(), false);
            childNode.put(USERNAME_KEY, setup.getUserName(), true);
            childNode.put(PASSWORD_KEY, setup.getPwd(), true);
        } catch (StorageException e1) {
            e1.printStackTrace();
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
                try {
                    setupValues.add(new SecuritySetup(childNode.get(HOST_KEY, "localhost"),
                            childNode.get(REALM_KEY, "basic"), childNode.get(USERNAME_KEY, null),
                            childNode.get(PASSWORD_KEY, null)));
                } catch (StorageException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return setupValues;
    }

    public static boolean credentialsInSecureStorage() {
        return SecurePreferencesFactory.getDefault().nodeExists(IVY_DE_CREDENTIALS_BASE_NODE);
    }

}

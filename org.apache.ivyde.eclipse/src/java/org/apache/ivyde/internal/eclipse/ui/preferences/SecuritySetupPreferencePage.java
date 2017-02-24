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
package org.apache.ivyde.internal.eclipse.ui.preferences;

import org.apache.ivyde.eclipse.IvyDEsecurityHelper;
import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.apache.ivyde.internal.eclipse.IvyPlugin;
import org.apache.ivyde.internal.eclipse.ui.SecuritySetupEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SecuritySetupPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    /** the ID of the preference page */
    public static final String PEREFERENCE_PAGE_ID = "org.apache.ivyde.eclipse.ui.preferences.SecuritySetupPreferencePage";

    private SecuritySetupEditor securitySetupComposite;

    public SecuritySetupPreferencePage() {
        setPreferenceStore(IvyPlugin.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(IvyPlugin.getDefault().getPreferenceStore());
    }

    protected Control createContents(Composite parent) {
        securitySetupComposite = new SecuritySetupEditor(parent, SWT.NONE);
        securitySetupComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        //TODO: SecuritySetup()
        securitySetupComposite.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());        

        return securitySetupComposite;
    }

    public boolean performOk() {        
        //TODO: SecuritySetup()
        SecuritySetup credentials = securitySetupComposite.getSecuritySetup();
        IvyDEsecurityHelper.addCredentialsToSecureStorage(credentials);
        IvyDEsecurityHelper.addCredentialsToIvyCredentialStorage(credentials);
        
        return true;
    }

    protected void performDefaults() {
      //TODO: SecuritySetup()
        securitySetupComposite.init(new SecuritySetup());
    }
}

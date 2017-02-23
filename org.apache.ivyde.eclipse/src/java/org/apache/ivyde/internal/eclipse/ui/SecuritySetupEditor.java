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
package org.apache.ivyde.internal.eclipse.ui;

import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SecuritySetupEditor extends Composite {

    public static final String TOOLTIP_HOST = "The host";
    
    public static final String TOOLTIP_REALM = "The realm for authentication";

    public static final String TOOLTIP_USERNAME = "The username";
    
    public static final String TOOLTIP_PASSWORD = "The password";

    private Text hostText;
    
    private Text realmText;

    private Text userNameText;

    private Text pwdText;

    private Label hostLabel;
    
    private Label realmLabel;

    private Label userNameLabel;

    private Label pwdLabel;

    public SecuritySetupEditor(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(2, false));

        hostLabel = new Label(this, SWT.NONE);
        hostLabel.setText("Host:");

        hostText = new Text(this, SWT.SINGLE | SWT.BORDER);
        hostText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        hostText.setToolTipText(TOOLTIP_HOST);
        
        realmLabel = new Label(this, SWT.NONE);
        realmLabel.setText("Realm:");

        realmText = new Text(this, SWT.SINGLE | SWT.BORDER);
        realmText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        realmText.setToolTipText(TOOLTIP_REALM);

        userNameLabel = new Label(this, SWT.NONE);
        userNameLabel.setText("Username:");

        userNameText = new Text(this, SWT.SINGLE | SWT.BORDER);
        userNameText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        userNameText.setToolTipText(TOOLTIP_USERNAME);

        pwdLabel = new Label(this, SWT.NONE);
        pwdLabel.setText("Password:");

        pwdText = new Text(this, SWT.PASSWORD | SWT.BORDER);
        pwdText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        pwdText.setToolTipText(TOOLTIP_PASSWORD);
    }

    public void init(SecuritySetup setup) {        
        hostText.setText(setup.getHost());
        realmText.setText(setup.getRealm());
        userNameText.setText(setup.getUserName());
        pwdText.setText(setup.getPwd());
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        hostLabel.setEnabled(enabled);
        hostText.setEnabled(enabled);
        realmLabel.setEnabled(enabled);
        realmText.setEnabled(enabled);
        userNameLabel.setEnabled(enabled);
        userNameText.setEnabled(enabled);
        pwdLabel.setEnabled(enabled);
        pwdText.setEnabled(enabled);
    }

    public SecuritySetup getSecuritySetup() {
        SecuritySetup setup = new SecuritySetup();
        setup.setHost(hostText.getText());
        setup.setRealm(realmText.getText());
        setup.setUserName(userNameText.getText());
        setup.setPwd(pwdText.getText());
        
        return setup;
    }
}

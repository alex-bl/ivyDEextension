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
package org.apache.ivyde.internal.eclipse.ui.components;

import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SecuritySetupDialog extends Dialog {

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

    //needed here: otherwise disposed
    private SecuritySetup contentHolder;
    
    public SecuritySetupDialog(Shell parentShell, SecuritySetup defaultVal) {
        super(parentShell);
        this.contentHolder=defaultVal;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
//        layout.marginRight = 5;
//        layout.marginLeft = 10;
        container.setLayout(layout);

        hostLabel = new Label(container, SWT.NONE);
        hostLabel.setText("Host:");

        hostText = new Text(container, SWT.SINGLE | SWT.BORDER);
        hostText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        hostText.setToolTipText(TOOLTIP_HOST);

        realmLabel = new Label(container, SWT.NONE);
        realmLabel.setText("Realm:");

        realmText = new Text(container, SWT.SINGLE | SWT.BORDER);
        realmText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        realmText.setToolTipText(TOOLTIP_REALM);

        userNameLabel = new Label(container, SWT.NONE);
        userNameLabel.setText("Username:");

        userNameText = new Text(container, SWT.SINGLE | SWT.BORDER);
        userNameText
                .setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        userNameText.setToolTipText(TOOLTIP_USERNAME);

        pwdLabel = new Label(container, SWT.NONE);
        pwdLabel.setText("Password:");

        pwdText = new Text(container, SWT.PASSWORD | SWT.BORDER);
        pwdText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        pwdText.setToolTipText(TOOLTIP_PASSWORD);

        return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        //newShell.setText("Add");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(350, 240);
    }

    public void setEnabled(boolean enabled) {

        // super.setEnabled(enabled);
        hostLabel.setEnabled(enabled);
        hostText.setEnabled(enabled);
        realmLabel.setEnabled(enabled);
        realmText.setEnabled(enabled);
        userNameLabel.setEnabled(enabled);
        userNameText.setEnabled(enabled);
        pwdLabel.setEnabled(enabled);
        pwdText.setEnabled(enabled);

    }

    private void saveSecuritySetup() {
        this.contentHolder = new SecuritySetup();
        this.contentHolder.setHost(hostText.getText());
        this.contentHolder.setRealm(realmText.getText());
        this.contentHolder.setUserName(userNameText.getText());
        this.contentHolder.setPwd(pwdText.getText());
    }

    public Button getOkButton(){
        return super.getButton(IDialogConstants.OK_ID);
    }
    
    @Override
    protected void okPressed() {
        saveSecuritySetup();
            super.okPressed();
    }

    /**
     * @return the hostText
     */
    public Text getHostText() {
        return hostText;
    }

    /**
     * @return the realmText
     */
    public Text getRealmText() {
        return realmText;
    }

    /**
     * @return the userNameText
     */
    public Text getUserNameText() {
        return userNameText;
    }

    /**
     * @return the pwdText
     */
    public Text getPwdText() {
        return pwdText;
    }

    /**
     * @return the contentHolder
     */
    public SecuritySetup getContentHolder() {
        return contentHolder;
    }
    
    
}

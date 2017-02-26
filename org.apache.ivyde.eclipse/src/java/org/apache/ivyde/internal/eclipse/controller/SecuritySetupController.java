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
package org.apache.ivyde.internal.eclipse.controller;

import org.apache.ivyde.eclipse.GUIfactoryHelper;
import org.apache.ivyde.eclipse.IvyDEsecurityHelper;
import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.apache.ivyde.internal.eclipse.controller.validator.HostValidationExclusion;
import org.apache.ivyde.internal.eclipse.controller.validator.HostValidationReaction;
import org.apache.ivyde.internal.eclipse.controller.validator.SecuritySetupValidatorFactory;
import org.apache.ivyde.internal.eclipse.ui.SecuritySetupEditor;
import org.apache.ivyde.internal.eclipse.ui.components.SecuritySetupDialog;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SecuritySetupController {

    private SecuritySetupEditor setupEditorGUI;

    private SecuritySetupDialog addDialog;

    private MessageDialog confirmationDialog;

    private SecuritySetup currentSelection = new SecuritySetup();

    private String selectionHost;

    private String selectionRealm;

    private boolean addOperation = true;

    /**
     * @param setupEditorGUI
     * @param addDialog
     */
    public SecuritySetupController(SecuritySetupEditor setupEditorGUI) {
        this.setupEditorGUI = setupEditorGUI;
        addDialog = new SecuritySetupDialog(setupEditorGUI.getShell(), currentSelection);
    }

    public void addHandlers() {
        setupEditorGUI.getAddBtn().addSelectionListener(this.createAddBtnSelectionAdapter());
        setupEditorGUI.getEditBtn().addSelectionListener(this.createEditBtnSelectionAdapter());
        setupEditorGUI.getDeleteBtn().addSelectionListener(this.createDelBtnSelectionAdapter());
        setupEditorGUI.getTableViewer()
                .addSelectionChangedListener(this.createSelectionChangedListener());
    }

    private SelectionListener createAddBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addOperation = true;
                currentSelection = new SecuritySetup();
                addDialog.create();
                initDialog(currentSelection);
                if (addDialog.open() == Window.OK) {
                    IvyDEsecurityHelper.addCredentialsToSecureStorage(addDialog.getContentHolder());
                    IvyDEsecurityHelper
                            .addCredentialsToIvyCredentialStorage(addDialog.getContentHolder());
                    // TODO: using init to reload directly from secure storage or use an
                    // intermediate-container?
                    setupEditorGUI.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());
                } else {
                    // TODO: do something?
                }
                addDialog.close();
            }
        };
    }

    private SelectionListener createEditBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addOperation = false;                
                addDialog.create();
                initDialog(currentSelection);                
                // initDialog(currentSelection);
                if (addDialog.open() == Window.OK) {
                    IvyDEsecurityHelper.removeCredentials(selectionHost, selectionRealm);
                    IvyDEsecurityHelper.addCredentialsToSecureStorage(addDialog.getContentHolder());
                    IvyDEsecurityHelper
                            .addCredentialsToIvyCredentialStorage(addDialog.getContentHolder());
                    // TODO: using init to reload directly from secure storage or use an
                    // intermediate-container?
                    setupEditorGUI.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());
                    setupEditorGUI.getEditBtn().setEnabled(false);
                    setupEditorGUI.getDeleteBtn().setEnabled(false);
                } else {
                    // TODO: do something?
                }
                addDialog.close();
            }
        };
    }

    private SelectionListener createDelBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                confirmationDialog = GUIfactoryHelper.buildConfirmationDialog(
                    setupEditorGUI.getShell(), "Confirmation",
                    "Remove selected credentials from secure storage?");
                if (confirmationDialog.open() == 0) {
                    IvyDEsecurityHelper.removeCredentials(selectionHost, selectionRealm);
                    setupEditorGUI.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());
                    setupEditorGUI.getEditBtn().setEnabled(false);
                    setupEditorGUI.getDeleteBtn().setEnabled(false);
                }
                confirmationDialog.close();
            }
        };
    }

    private ISelectionChangedListener createSelectionChangedListener() {
        return new ISelectionChangedListener() {
            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                currentSelection = (SecuritySetup) selection.getFirstElement();
                setupEditorGUI.getEditBtn().setEnabled(true);
                setupEditorGUI.getDeleteBtn().setEnabled(true);
                if (currentSelection != null) {
                    selectionHost = currentSelection.getHost();
                    selectionRealm = currentSelection.getRealm();
                } else {
                    currentSelection = new SecuritySetup();
                }
            }
        };
    }

    private void createHostDataBinder(String selectedHost, boolean isAddOperation) {
        IObservableValue textObservable = WidgetProperties.text(SWT.Modify)
                .observe(this.addDialog.getHostText());
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(SecuritySetupValidatorFactory.createHostExistsValidator(
            new HostValidationReaction(this.addDialog.getOkButton()),
            new HostValidationExclusion(this.addOperation, this.selectionHost)));
        /* with text being the port value in your model */
        ValidationStatusProvider binding = new DataBindingContext().bindValue(textObservable,
            PojoProperties.value(SecuritySetup.class, "host").observe(this.currentSelection),
            strategy, null);
        ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT);
    }

    private void initDialog(SecuritySetup setup) {
        this.addDialog.getHostText().setText(setup.getHost());
        this.addDialog.getRealmText().setText(setup.getRealm());
        this.addDialog.getUserNameText().setText(setup.getUserName());
        this.addDialog.getPwdText().setText(setup.getPwd());        
        this.createHostDataBinder(this.selectionHost, this.addOperation);
    }
}

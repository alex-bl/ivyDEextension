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
import org.apache.ivyde.internal.eclipse.controller.validator.HostRealmValidationReaction;
import org.apache.ivyde.internal.eclipse.controller.validator.HostValidationProc;
import org.apache.ivyde.internal.eclipse.controller.validator.IValidationReaction;
import org.apache.ivyde.internal.eclipse.controller.validator.IdValidationProc;
import org.apache.ivyde.internal.eclipse.controller.validator.RealmValidationProc;
import org.apache.ivyde.internal.eclipse.controller.validator.SecuritySetupValidatorFactory;
import org.apache.ivyde.internal.eclipse.controller.validator.ValidationProcContainer;
import org.apache.ivyde.internal.eclipse.controller.validator.ValidationProcess;
import org.apache.ivyde.internal.eclipse.ui.SecuritySetupEditor;
import org.apache.ivyde.internal.eclipse.ui.components.SecuritySetupDialog;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Widget;

public class SecuritySetupController {

    private SecuritySetupEditor setupEditorGUI;

    private SecuritySetupDialog addDialog;

    private MessageDialog confirmationDialog;

    private SecuritySetup currentSelection = new SecuritySetup();

    private String selectionHost;

    private String selectionRealm;

    private boolean addOperation = true;
    
    private DataBindingContext ctx = new DataBindingContext();

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
                    currentSelection.setHost(selectionHost);
                    currentSelection.setRealm(selectionRealm);
                    IvyDEsecurityHelper.removeCredentials(currentSelection);
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
                    currentSelection.setHost(selectionHost);
                    currentSelection.setRealm(selectionRealm);
                    IvyDEsecurityHelper.removeCredentials(currentSelection);
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

    private void createHostDataBinder(String selectedHost, String selectedRealm,
            boolean isAddOperation) {
        IValidationReaction hostRealmValidationReaction = new HostRealmValidationReaction(
                this.addDialog.getOkButton(), this.addDialog.getErrorLabel(), this.addDialog.getErrorIcon());

        ValidationProcess hostValidationProc = new HostValidationProc(hostRealmValidationReaction);
        ValidationProcess realmValidationProc = new RealmValidationProc(
                hostRealmValidationReaction);
        ValidationProcess idValidationProc = new IdValidationProc(hostRealmValidationReaction,
                isAddOperation, selectedHost, selectedRealm);

        ValidationProcContainer.registerProc("host", hostValidationProc);
        ValidationProcContainer.registerProc("realm", realmValidationProc);
        ValidationProcContainer.registerProc("id", idValidationProc);

        IValidator hostValidator = SecuritySetupValidatorFactory.createValidator("host", true);
        IValidator realmValidator = SecuritySetupValidatorFactory.createValidator("realm", true);
        IValidator idValidator = SecuritySetupValidatorFactory.createValidator("id", false);

        this.addDataBinder(this.addDialog.getIdText(), idValidator, SecuritySetup.class, "id",
            this.currentSelection, true);
        this.addDataBinder(this.addDialog.getHostText(), hostValidator, SecuritySetup.class, "host",
            this.currentSelection, true);
        this.addDataBinder(this.addDialog.getRealmText(), realmValidator, SecuritySetup.class,
            "realm", this.currentSelection, true);
    }

    private void addDataBinder(Widget toObserve, IValidator validator, Class<?> observableClass,
            String propertyName, Object observedProperty, boolean textDecorationEnabled) {
        IObservableValue textObservable = WidgetProperties.text(SWT.Modify).observe(toObserve);
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(validator);

        ValidationStatusProvider binding = this.ctx.bindValue(textObservable,
            PojoProperties.value(observableClass, propertyName).observe(observedProperty), strategy,
            null);
        if(textDecorationEnabled){
            ControlDecorationSupport.create(binding, SWT.LEFT);
        }
        final IObservableValue errorObservable = WidgetProperties.text()
                .observe(this.addDialog.getErrorLabel());
        
        ctx.bindValue(errorObservable,
            new AggregateValidationStatus(ctx.getBindings(),
                            AggregateValidationStatus.MAX_SEVERITY), null, null);
        
    }

    private void initDialog(SecuritySetup setup) {
        setup.setId(setup.getHost() + "@" + setup.getRealm());
        // this.addDialog.getIdText().setText();
        this.addDialog.getHostText().setText(setup.getHost());
        this.addDialog.getRealmText().setText(setup.getRealm());
        this.addDialog.getUserNameText().setText(setup.getUserName());
        this.addDialog.getPwdText().setText(setup.getPwd());
        this.createHostDataBinder(this.selectionHost, this.selectionRealm, this.addOperation);

        addDialog.getHostText().addModifyListener(createModifyListener());
        addDialog.getRealmText().addModifyListener(createModifyListener());
    }

    private ModifyListener createModifyListener() {
        return new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                addDialog.getIdText().setText(
                    addDialog.getHostText().getText() + "@" + addDialog.getRealmText().getText());
            }
        };
    }
}

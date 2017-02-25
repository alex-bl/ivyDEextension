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


import org.apache.ivyde.eclipse.IvyDEsecurityHelper;
import org.apache.ivyde.eclipse.cp.SecuritySetup;
import org.apache.ivyde.internal.eclipse.ui.SecuritySetupEditor;
import org.apache.ivyde.internal.eclipse.ui.components.SecuritySetupDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SecuritySetupController {

    private SecuritySetupEditor setupEditorGUI;
    private SecuritySetupDialog addDialog;
    private SecuritySetup currentSelection;
    
    /**
     * @param setupEditorGUI
     * @param addDialog
     */
    public SecuritySetupController(SecuritySetupEditor setupEditorGUI) {
        this.setupEditorGUI = setupEditorGUI;       
    }

    public void addHandlers() {
        setupEditorGUI.getAddBtn().addSelectionListener(this.createAddBtnSelectionAdapter());
        setupEditorGUI.getEditBtn().addSelectionListener(this.createEditBtnSelectionAdapter());
        setupEditorGUI.getDeleteBtn().addSelectionListener(this.createDelBtnSelectionAdapter());
        setupEditorGUI.getTableViewer().addSelectionChangedListener(this.createSelectionChangedListener());
    }

    private SelectionListener createAddBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {           
                addDialog = new SecuritySetupDialog(setupEditorGUI.getShell(), new SecuritySetup());                
//                initDialog(new SecuritySetup());
                if(addDialog.open()== Window.OK){
                    //TODO: add not completely working...
                    IvyDEsecurityHelper.addCredentialsToSecureStorage(addDialog.getContentHolder());
                    IvyDEsecurityHelper.addCredentialsToIvyCredentialStorage(addDialog.getContentHolder());
                    //TODO: using init to reload directly from secure storage or use an intermediate-container?
                    setupEditorGUI.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());                    
                }else{
                    //TODO: do something?
                    System.out.println("cancel pressed");
                }
                addDialog.close();
            }
        };
    }

    //TODO: initDialog currently not working: empty values => need to set directly inside constructor?
    private SelectionListener createEditBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {                
                addDialog = new SecuritySetupDialog(setupEditorGUI.getShell(), currentSelection);
                //initDialog(currentSelection);
                if(addDialog.open()== Window.OK){
                    //TODO: edit adds and also add not completely working...
                    IvyDEsecurityHelper.addCredentialsToSecureStorage(addDialog.getContentHolder());
                    IvyDEsecurityHelper.addCredentialsToIvyCredentialStorage(addDialog.getContentHolder());
                    //TODO: using init to reload directly from secure storage or use an intermediate-container?
                    setupEditorGUI.init(IvyDEsecurityHelper.getCredentialsFromSecureStore());                    
                }else{
                    //TODO: do something?
                    System.out.println("cancel pressed");
                }
                addDialog.close();
            }            
        };
    }

    private SelectionListener createDelBtnSelectionAdapter() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("delete");
            }
        };
    }
    
    private ISelectionChangedListener createSelectionChangedListener(){
        return new ISelectionChangedListener() {
            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                currentSelection = (SecuritySetup)selection.getFirstElement();                
            }
        };
    }
    
    // TODO: need this methods?
    private void initDialog(SecuritySetup setup) {
        this.addDialog.getHostText().setText(setup.getHost());
        this.addDialog.getRealmText().setText(setup.getRealm());
        this.addDialog.getUserNameText().setText(setup.getUserName());
        this.addDialog.getPwdText().setText(setup.getPwd());

    }
}

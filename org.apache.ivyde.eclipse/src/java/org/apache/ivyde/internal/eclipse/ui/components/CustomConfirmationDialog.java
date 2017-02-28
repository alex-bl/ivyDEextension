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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class CustomConfirmationDialog extends MessageDialog{

    public CustomConfirmationDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage,
            String dialogMessage, int dialogImageType, int defaultIndex,
            String[] dialogButtonLabels) {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels,
                defaultIndex);
    }
    
    public CustomConfirmationDialog(Shell parentShell, String dialogTitle, String dialogMessage){
        super(parentShell, dialogTitle, null,
            dialogMessage, MessageDialog.CONFIRM, new String[] { "Ok",
                "Cancel"}, 0);
    }
    
    @Override
    public Point getInitialSize(){
        return new Point(400, 150);        
    }
}

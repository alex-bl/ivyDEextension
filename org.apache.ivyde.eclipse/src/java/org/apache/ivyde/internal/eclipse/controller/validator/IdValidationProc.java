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
package org.apache.ivyde.internal.eclipse.controller.validator;

import org.apache.ivyde.eclipse.IvyDEsecurityHelper;

public class IdValidationProc extends ValidationProcess {

    private boolean isAddOperation;

    private String prevHostVal;

    private String prevRealmVal;

    /**
     * @param reaction
     * @param isAddOperation
     * @param prevHostVal
     * @param prevRealmVal
     */
    public IdValidationProc(IValidationReaction reaction, boolean isAddOperation,
            String prevHostVal, String prevRealmVal) {
        super(reaction);
        this.isAddOperation = isAddOperation;
        this.prevHostVal = prevHostVal;
        this.prevRealmVal = prevRealmVal;
    }

    // just check if empty
    @Override
    public boolean doValidate(Object toValidate) {
        String id = (String) toValidate;
        if (id.equals("@")) {
            super.setErrorMessage("Properties 'Host' and 'Realm' cannot be empty");
            return false;
        }
        if (id.indexOf("@") == 0) {
            super.setErrorMessage(EMPTY_ERROR.replace("$entry", "Host"));
            return false;
        }
        if (id.indexOf("@") == id.length() - 1) {
            super.setErrorMessage(EMPTY_ERROR.replace("$entry", "Realm"));
            return false;
        }
        String[] hostRealm = id.split("@");
        IValidationExclusion exclusion = new IdValidationExclusion(isAddOperation, prevHostVal,
                hostRealm[0], prevRealmVal, hostRealm[1]);
        super.setOkMessage("Valid id: "+id);
        if (exclusion.exclusionNeeded()) {
            return true;
        }
        if (IvyDEsecurityHelper.hostExistsInSecureStorage(hostRealm[0], hostRealm[1])) {
            super.setErrorMessage(EXISTING_ENTRY_ERROR);
            return false;
        }
        return true;
    }
}

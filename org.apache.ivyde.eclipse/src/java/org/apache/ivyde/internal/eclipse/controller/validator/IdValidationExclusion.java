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

public class IdValidationExclusion implements IValidationExclusion {

    private boolean isAddOperation;

    private String prevHostVal;

    private String hostValToCheck;

    private String prevRealmVal;

    private String realmValToCheck;

    /**
     * @param isAddOperation
     * @param prevHostVal
     * @param hostValToCheck
     * @param prevRealmVal
     * @param realmValToCheck
     */
    public IdValidationExclusion(boolean isAddOperation, String prevHostVal, String hostValToCheck,
            String prevRealmVal, String realmValToCheck) {
        this.isAddOperation = isAddOperation;
        this.prevHostVal = prevHostVal;
        this.hostValToCheck = hostValToCheck;
        this.prevRealmVal = prevRealmVal;
        this.realmValToCheck = realmValToCheck;
    }

    @Override
    public boolean exclusionNeeded() {
        return !isAddOperation && prevHostVal.equals(hostValToCheck)
                && prevRealmVal.equals(realmValToCheck);
    }

}

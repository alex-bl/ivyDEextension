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

public abstract class ValidationProcess {

    private IValidationExclusion exclusion;

    private IValidationReaction reaction;

    private String errorMessage;
    
    private String okMessage;
    
    public static final String EMPTY_ERROR = "The property '$entry' cannot be empty";

    public static final String EXISTING_ENTRY_ERROR = "An entry with that host and realm already exists";

    public abstract boolean doValidate(Object toValidate);
        
    /**
     * @param exclusion
     * @param reaction
     */
    public ValidationProcess(IValidationReaction reaction) {
        this.reaction = reaction;
    }

    /**
     * @return the exclusion
     */
    public IValidationExclusion getExclusion() {
        return exclusion;
    }

    /**
     * @return the okMessage
     */
    public String getOkMessage() {
        return okMessage;
    }

    /**
     * @param okMessage the okMessage to set
     */
    public void setOkMessage(String okMessage) {
        this.okMessage = okMessage;
    }

    /**
     * @param exclusion
     *            the exclusion to set
     */
    public void setExclusion(IValidationExclusion exclusion) {
        this.exclusion = exclusion;
    }

    /**
     * @return the reaction
     */
    public IValidationReaction getReaction() {
        return reaction;
    }

    /**
     * @param reaction
     *            the reaction to set
     */
    public void setReaction(IValidationReaction reaction) {
        this.reaction = reaction;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    
    
}

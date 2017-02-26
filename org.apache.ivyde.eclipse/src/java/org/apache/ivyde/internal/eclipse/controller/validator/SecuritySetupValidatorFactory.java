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
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public final class SecuritySetupValidatorFactory {

    private SecuritySetupValidatorFactory() {

    }

    public static IValidator createHostExistsValidator(final IValidationReaction reaction,
            final IValidationExclusion exclusion) {
        return new IValidator() {
            @Override
            public IStatus validate(Object value) {
                exclusion.setCheckedValue(value);
                if (!exclusion.exclusionNeeded()) {
                    String host = (String) value;
                    if (host.equals("")) {
                        reaction.error();
                        return ValidationStatus.error("Host cannot be empty");
                    } else if (IvyDEsecurityHelper.hostExistsInSecureStorage(host)) {
                        reaction.error();
                        return ValidationStatus.error("An entry for '" + host + "' already exists");
                    }
                }
                reaction.ok();
                return ValidationStatus.ok();
            }
        };
    }
}

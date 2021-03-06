/*******************************************************************************
 * Copyright 2000-2014 JetBrains s.r.o.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package org.jetbrains.kotlin.core.resolve.lang.java.resolver;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.descriptors.FunctionDescriptor;
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor;
import org.jetbrains.kotlin.load.java.components.MethodSignatureChecker;
import org.jetbrains.kotlin.load.java.structure.JavaMethod;

public class EclipseMethodSignatureChecker implements MethodSignatureChecker  {

    @Override
    public void checkSignature(@NotNull JavaMethod method,
            boolean reportSignatureErrors,
            @NotNull SimpleFunctionDescriptor descriptor,
            @NotNull List<String> signatureErrors,
            @NotNull List<FunctionDescriptor> superFunctions) {
        // TODO Auto-generated method stub
    }
}

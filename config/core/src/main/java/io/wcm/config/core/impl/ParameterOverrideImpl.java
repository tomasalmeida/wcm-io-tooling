/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.config.core.impl;

import io.wcm.config.api.Parameter;
import io.wcm.config.management.ParameterOverride;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * Default implementation of {@link ParameterOverride}.
 */
@Component(metatype = false, immediate = true)
@Service(ParameterOverride.class)
public class ParameterOverrideImpl implements ParameterOverride {

  @Override
  public <T> T getOverrideSystemDefault(Parameter<T> parameter) {
    // TODO: sseifert@sseifert implement
    return null;
  }

  @Override
  public <T> T getOverrideForce(String configurationId, Parameter<T> parameter) {
    // TODO: sseifert@sseifert implement
    return null;
  }

}

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
package io.wcm.samples.configsampleapp.config;

import static io.wcm.config.api.ParameterBuilder.create;
import io.wcm.config.api.Parameter;
import io.wcm.config.editor.widgets.WidgetTypes;

/**
 * Defines some example paramters.
 */
public final class Params {

  private Params() {
    // contants only
  }

  /**
   * Application ID
   */
  public static final String APPLICATION_ID = "/apps/wcm-io/samples/config-sample-app";

  /**
   * String parameter
   */
  public static final Parameter<String> STRING_PARAM = create("string-param", String.class, APPLICATION_ID).properties(
      WidgetTypes.TEXTFIELD.getDefaultWidgetConfiguration()).property(WidgetTypes.Defaults.PN_GROUP, "Group 1").defaultValue("default value").build();

  /**
   * Text field parameter
   */
  public static final Parameter<String[]> TEXT_PARAM = create("text-param", String[].class, APPLICATION_ID).properties(
      WidgetTypes.TEXTAREA.getDefaultWidgetConfiguration()).property(WidgetTypes.Defaults.PN_GROUP, "Group 1").build();

  /**
   * Checkbox field parameter
   */
  public static final Parameter<Boolean> CHECKBOX_PARAM = create("checkbox-param", Boolean.class, APPLICATION_ID).properties(
      WidgetTypes.CHECKBOX.getDefaultWidgetConfiguration()).property(WidgetTypes.Defaults.PN_GROUP, "Group 2").build();

  /**
   * Path Browser field parameter
   */
  public static final Parameter<String> PATHBROWSER_PARAM = create("pathbrowser-param", String.class, APPLICATION_ID).properties(
      WidgetTypes.PATHBROWSER.getDefaultWidgetConfiguration()).property(WidgetTypes.Defaults.PN_GROUP, "Group 2").build();

}

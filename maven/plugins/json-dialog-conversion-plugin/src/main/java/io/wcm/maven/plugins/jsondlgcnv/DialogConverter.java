/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.maven.plugins.jsondlgcnv;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.fsprovider.internal.mapper.ContentFile;
import org.apache.sling.testing.mock.sling.junit.SlingContext;

class DialogConverter {

  private final Rules rules;
  private final Resource sourceRoot;
  private final Log log;

  DialogConverter(SlingContext context, String rulesPath, Log log) {
    this.rules = new Rules(context.resourceResolver().getResource(rulesPath));
    this.sourceRoot = context.resourceResolver().getResource("/source");
    this.log = log;
  }

  public void convert() {
    convertDialogs(sourceRoot);
  }

  private void convertDialogs(Resource resource) {
    if (StringUtils.equals(resource.getName(), "cq:dialog")) {
      convertDialogResource(resource);
    }
    else {
      Iterator<Resource> children = resource.listChildren();
      while (children.hasNext()) {
        convertDialogs(children.next());
      }
    }
  }

  private void convertDialogResource(Resource resource) {
    Rule rule = rules.getRule(resource);
    if (rule != null) {
      log.info("Convert " + resource.getPath() + " with rule '" + rule.getName() + "'.");

      ContentFile contentFile = resource.adaptTo(ContentFile.class);
      try {
        JSONObject jsonContent = new JSONObject(FileUtils.readFileToString(contentFile.getFile()));
        JSONObject element = getDialogObject(jsonContent, contentFile.getSubPath());

        // TODO apply rule

        FileUtils.write(contentFile.getFile(), jsonContent.toString(2));
      }
      catch (JSONException | IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    Iterator<Resource> children = resource.listChildren();
    while (children.hasNext()) {
      convertDialogResource(children.next());
    }
  }

  private JSONObject getDialogObject(JSONObject json, String path) throws JSONException {
    if (StringUtils.isEmpty(path)) {
      return json;
    }
    if (StringUtils.contains(path, "/")) {
      String name = StringUtils.substringBefore(path, "/");
      String remainder = StringUtils.substringAfter(path, "/");
      JSONObject child = json.getJSONObject(name);
      return getDialogObject(child, remainder);
    }
    else {
      return json.getJSONObject(path);
    }
  }

}

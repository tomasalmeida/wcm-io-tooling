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
package io.wcm.maven.plugins.nodejs.installation;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.Os;

/**
 * Holds the general information about the node installation. Provides node and npm executables
 */
public class NodeInstallationInformation {

  static final String TYPE_TAR_GZ = "tar.gz";
  static final String TYPE_ZIP = "zip";

  private static final String OS_WINDOWS = "win";
  private static final String OS_MACOS = "darwin";
  private static final String OS_LINUX = "linux";

  private static final String NODEJS_BINARIES_GROUPID = "org.nodejs.dist";
  private static final String NODEJS_BINARIES_ARTIFACTID = "nodejs-binaries";

  private Dependency nodeJsDependency;
  private Dependency npmDependency;
  private File archive;
  private File nodeExecutable;
  private File npmExecutable;
  private String nodeJsInstallPath;
  private String basePath;
  private String archiveExtension;

  public Dependency getNodeJsDependency() {
    return this.nodeJsDependency;
  }

  void setNodeJsDependency(Dependency nodeJsDependency) {
    this.nodeJsDependency = nodeJsDependency;
  }

  public Dependency getNpmDependency() {
    return this.npmDependency;
  }

  void setNpmDependency(Dependency npmDependency) {
    this.npmDependency = npmDependency;
  }

  public File getArchive() {
    return archive;
  }

  void setArchive(File archive) {
    this.archive = archive;
  }

  public String getArchiveExtension() {
    return archiveExtension;
  }

  void setArchiveExtension(String archiveExtension) {
    this.archiveExtension = archiveExtension;
  }

  public File getNodeExecutable() {
    return nodeExecutable;
  }

  void setNodeExecutable(File nodeExecutable) {
    this.nodeExecutable = nodeExecutable;
  }

  public File getNpmExecutable() {
    return npmExecutable;
  }

  void setNpmExecutable(File npmExecutable) {
    this.npmExecutable = npmExecutable;
  }

  public String getBasePath() {
    return basePath;
  }

  void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getNodeJsInstallPath() {
    return nodeJsInstallPath;
  }

  void setNodeJsInstallPath(String nodeJsInstallPath) {
    this.nodeJsInstallPath = nodeJsInstallPath;
  }

  /**
   * Creates a {@link NodeInstallationInformation} for a specific Node.js and npm version and directory
   * @param version Version
   * @param directory directory
   * @return {@link NodeInstallationInformation}
   * @throws MojoExecutionException Mojo execution exception
   */
  public static NodeInstallationInformation forVersion(String version, File directory) throws MojoExecutionException {
    String arch;
    if (Os.isArch("x86") || Os.isArch("i386")) {
      arch = "x86";
    }
    else if (Os.isArch("x86_64") || Os.isArch("amd64")) {
      arch = "x64";
    }
    else {
      throw new MojoExecutionException("Unsupported OS arch: " + Os.OS_ARCH);
    }

    NodeInstallationInformation result = new NodeInstallationInformation();

    String basePath = directory.getAbsolutePath() + File.separator;
    result.setBasePath(basePath);

    if (Os.isFamily(Os.FAMILY_WINDOWS) || Os.isFamily(Os.FAMILY_WIN9X)) {
      String nodeJsInstallPath = basePath + "node-v" + version + "-" + OS_WINDOWS + "-" + arch;
      result.setNodeJsInstallPath(nodeJsInstallPath);
      result.setNodeJsDependency(buildDependency(NODEJS_BINARIES_GROUPID, NODEJS_BINARIES_ARTIFACTID, version, OS_WINDOWS, arch, TYPE_ZIP));
      result.setArchive(new File(nodeJsInstallPath + "." + TYPE_ZIP));
      result.setNodeExecutable(new File(nodeJsInstallPath + File.separator + "node.exe"));
      result.setNpmExecutable(new File(nodeJsInstallPath + File.separator + "node_modules/npm/bin/npm-cli.js"));
      result.setArchiveExtension(TYPE_ZIP);
    }
    else if (Os.isFamily(Os.FAMILY_MAC)) {
      String nodeJsInstallPath = basePath + "node-v" + version + "-" + OS_MACOS + "-" + arch;
      result.setNodeJsInstallPath(nodeJsInstallPath);
      result.setNodeJsDependency(buildDependency(NODEJS_BINARIES_GROUPID, NODEJS_BINARIES_ARTIFACTID, version, OS_MACOS, arch, TYPE_TAR_GZ));
      result.setArchive(new File(nodeJsInstallPath + "." + TYPE_TAR_GZ));
      result.setNodeExecutable(new File(nodeJsInstallPath + File.separator + "bin" + File.separator + "node"));
      result.setNpmExecutable(new File(nodeJsInstallPath + File.separator + "lib" + File.separator + "node_modules/npm/bin/npm-cli.js"));
      result.setArchiveExtension(TYPE_TAR_GZ);
    }
    else if (Os.isFamily(Os.FAMILY_UNIX)) {
      String nodeJsInstallPath = basePath + "node-v" + version + "-" + OS_LINUX + "-" + arch;
      result.setNodeJsInstallPath(nodeJsInstallPath);
      result.setNodeJsDependency(buildDependency(NODEJS_BINARIES_GROUPID, NODEJS_BINARIES_ARTIFACTID, version, OS_LINUX, arch, TYPE_TAR_GZ));
      result.setArchive(new File(nodeJsInstallPath + "." + TYPE_TAR_GZ));
      result.setNodeExecutable(new File(nodeJsInstallPath + File.separator + "bin" + File.separator + "node"));
      result.setNpmExecutable(new File(nodeJsInstallPath + File.separator + "lib" + File.separator + "node_modules/npm/bin/npm-cli.js"));
      result.setArchiveExtension(TYPE_TAR_GZ);
    }
    else {
      throw new MojoExecutionException("Unsupported OS: " + Os.OS_FAMILY);
    }
    return result;
  }

  private static Dependency buildDependency(String groupId, String artifactId, String version, String os, String arch, String type) {
    String classifier = null;
    if (StringUtils.isNotEmpty(os)) {
      classifier = os;
    }
    if (StringUtils.isNotEmpty(arch)) {
      classifier += "-" + arch;
    }

    Dependency dependency = new Dependency();
    dependency.setGroupId(groupId);
    dependency.setArtifactId(artifactId);
    dependency.setVersion(version);
    dependency.setType(type);
    dependency.setClassifier(classifier);
    return dependency;
  }

  /**
   * Sets the executable of the npm to specified version, previously installed in the base directory
   * @param information Information
   * @param directory Directory
   * @throws MojoExecutionException Mojo execution exception
   */
  public static void setSpecifiedNpmExecutable(NodeInstallationInformation information, File directory) throws MojoExecutionException {
    String basePath = directory.getAbsolutePath() + File.separator;
    if (Os.isFamily(Os.FAMILY_WINDOWS) || Os.isFamily(Os.FAMILY_WIN9X)) {
      information.setNpmExecutable(new File(basePath + "node_modules/npm/bin/npm-cli.js"));
    }
    else if (Os.isFamily(Os.FAMILY_MAC)) {
      information.setNpmExecutable(new File(basePath + "node_modules/npm/bin/npm-cli.js"));
    }
    else if (Os.isFamily(Os.FAMILY_UNIX)) {
      information.setNpmExecutable(new File(basePath + "node_modules/npm/bin/npm-cli.js"));
    }
    else {
      throw new MojoExecutionException("Unsupported OS: " + Os.OS_FAMILY);
    }
  }

}

/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.jenkins.plugins.usage;

import java.io.IOException;
import java.util.UUID;

import hudson.Plugin;
import jenkins.model.Jenkins;

/**
 * A plugin that controls usage reporting to Google. This provides a means for
 * Google-originated distributions of Jenkins to report usage metrics. Reporting
 * is strictly gated on this plugin's config file, which should be configured
 * through an opt-in mechanism. Default values always assume the user has not
 * opted in.
 * <p>
 * This plugin intentionally does not expose configuration in the Jenkins UI.
 * Usage reporting is intended to be nontrivial to enable within Jenkins, so
 * that users who did not intend to send data will not accidentally do so.
 * <p>
 * Each instance of the plugin has an associated random UUID, which is used as
 * an anonymous client identifier. The plugin configuration includes an
 * optional hashed cloud project numeric ID; <b>clients should never use an
 * unhashed value,</b> as this is sent over the wire.
 */
public class GoogleUsageReportingPlugin extends Plugin {

  /**
   * Returns the instance of this plugin created by Jenkins.
   */
  public static GoogleUsageReportingPlugin getInstance() {
    Jenkins jenkins = Jenkins.getInstance();
    if (jenkins == null) {
      throw new IllegalStateException("Jenkins not running!");
    }
    return jenkins.getPlugin(GoogleUsageReportingPlugin.class);
  }

  // These fields are populated from the config file.
  private boolean enableReporting = false;
  private String analyticsId = "";
  private String cloudProjectNumberHash = "";
  // This field is not persisted in the config file.
  private final transient UUID uuid = UUID.randomUUID();

  /**
   * @return whether reporting is enabled.
   */
  public boolean getEnableReporting() {
    return enableReporting;
  }

  /**
   * @return the Google Analytics ID that will receive reports.
   */
  public String getAnalyticsId() {
    return analyticsId;
  }

  /**
   * @return the Google Cloud project ID hash associated with this instance.
   */
  public String getCloudProjectNumberHash() {
    return cloudProjectNumberHash;
  }

  /**
   * @return the random Client ID associated with this instance.
   */
  public String getClientId() {
    return uuid.toString();
  }

  @Override
  public void start() throws IOException {
    load();
  }

  @Override
  public String toString() {
    return "GoogleUsageReportingPlugin{" +
            "enableReporting=" + enableReporting +
            ";analyticsId=" + analyticsId +
            '}';
  }
}

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

import java.util.logging.Logger;
import javax.inject.Inject;

import com.google.cloud.metrics.AsyncMetricsSender;
import com.google.cloud.metrics.Event;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * Sends an event report when a build starts or stops.
 */
@Extension
public class BuildReporter extends RunListener<Run<?, ?>> {
  private static final String JENKINS_EVENT_TYPE = "jenkins";
  private static final String RUN_OBJECT_TYPE = "run";
  private static final String STARTED_EVENT = "started";
  private static final String COMPLETED_EVENT = "completed";
  private static final String RESULT_METADATA_KEY = "result";
  private static final String RESULT_FAILURE = "failure";
  private static final String RESULT_SUCCESS = "success";

  private static final Logger logger = Logger.getLogger(
      BuildReporter.class.getName());

  private AsyncMetricsSender sender;
  private final GoogleUsageReportingPlugin plugin;

  public BuildReporter() {
    plugin = GoogleUsageReportingPlugin.getInstance();
  }

  @Inject
  public void setSender(AsyncMetricsSender sender) {
    this.sender = sender;
  }

  @Override
  public void onStarted(Run<?, ?> r, TaskListener listener) {
    if (plugin == null || !plugin.getEnableReporting()) {
      return;
    }
    Event event = getCommonEventBuilder()
        .setName(STARTED_EVENT)
        .build();
    sender.send(event);
  }

  @Override
  public void onFinalized(Run<?, ?> r) {
    if (plugin == null || !plugin.getEnableReporting()) {
      return;
    }
    Event event = getCommonEventBuilder()
        .setName(COMPLETED_EVENT)
        .addMetadata(RESULT_METADATA_KEY, getResultMetadataString(r))
        .build();
    sender.send(event);
  }

  private Event.Builder getCommonEventBuilder() {
    return Event.builder()
        .setType(JENKINS_EVENT_TYPE)
        .setObjectType(RUN_OBJECT_TYPE)
        .setClientId(plugin.getClientId())
        .setProjectNumberHash(plugin.getCloudProjectNumberHash());
  }

  private static String getResultMetadataString(Run<?, ?> r) {
    if (r == null) {
      return RESULT_FAILURE;
    }
    Result result = r.getResult();
    if (result == null || result.isWorseThan(Result.SUCCESS)) {
      return RESULT_FAILURE;
    }
    return RESULT_SUCCESS;
  }
}

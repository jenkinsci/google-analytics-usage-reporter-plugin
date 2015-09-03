Google Analytics Usage Reporter Plugin
====================

This plugin provides automatic reporting of Jenkins usage to Google, via Google Analytics. This plugin is intended primarily for use with Google-provided Jenkins images, but is compatible with other Jenkins installations as well.

Usage
===
The plugin can be configured only by directly modifying the configuration file - google-usage-report.xml in the Jenkins home directory. The minimal configuration consists of two fields:

*  enableReporting : this field enables or disables usage reporting.
*  analyticsId : the Google Analytics ID to receive usage reports.

Additional optional fields:

*  cloudProjectNumberHash : the SHA-1 hash of the Google Cloud numeric project ID associated with this Jenkins instance, if such a project ID exists.

The default configuration has reporting disabled and has no associated analytics ID. The system administrator must modify the configuration file in order to enable reporting. This acts as a safeguard against accidental or unintentional reporting.

Development
===========

How to build
--------------

	mvn clean verify

Creates the plugin HPI package for use with Jenkins. Note that this requires the Google Cloud Metrics library; see https://github.com/google/cloud-reporting.


License
-------

	(The Apache v2 License)

    Copyright 2015 Google Inc. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

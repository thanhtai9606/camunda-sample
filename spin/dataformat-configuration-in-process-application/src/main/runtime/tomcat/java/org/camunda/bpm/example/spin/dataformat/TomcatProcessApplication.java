/*
 * Copyright © 2013 - 2018 camunda services GmbH and various authors (info@camunda.com)
 *
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
 */
package org.camunda.bpm.example.spin.dataformat;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;

/**
 * @author Thorben Lindhauer
 *
 */
@IgnoreBean
@ProcessApplication
public class TomcatProcessApplication extends ServletProcessApplication {
  public static TomcatProcessApplication INSTANCE;

  // empty implementation

  @PostDeploy
  public void postDeploy() {
    INSTANCE = this;
  }
}

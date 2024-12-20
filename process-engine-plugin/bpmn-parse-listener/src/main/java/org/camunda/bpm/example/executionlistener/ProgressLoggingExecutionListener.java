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
package org.camunda.bpm.example.executionlistener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * Execution listener to log property extension value
 *
 * @author kristin.polenz
 *
 */
public class ProgressLoggingExecutionListener implements ExecutionListener {

  private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

  public static List<String> progressValueList = new ArrayList<String>();

  private String propertyValue;

  public ProgressLoggingExecutionListener(String value) {
    this.propertyValue = value;
  }

  public void notify(DelegateExecution execution) throws Exception {
    progressValueList.add(propertyValue);
    LOGGER.info("value of service task extension property 'progress': " + propertyValue);
  }

}

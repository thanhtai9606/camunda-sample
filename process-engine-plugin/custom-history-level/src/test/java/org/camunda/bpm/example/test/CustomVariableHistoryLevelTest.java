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
package org.camunda.bpm.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test Bpmn Parse listener as process engine plugin and
 * parse extension properties on bpmn element
 *
 * @author kristin.polenz
 *
 */
public class CustomVariableHistoryLevelTest {

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule("camunda-custom-variable.cfg.xml");

  protected RuntimeService runtimeService;
  protected HistoryService historyService;

  @Before
  public void getEngineServices() {
    runtimeService = processEngineRule.getRuntimeService();
    historyService = processEngineRule.getHistoryService();
  }

  @Test
  @Deployment(resources = { "process.bpmn" })
  public void testCustomVariableHistoryTest() {
    runtimeService.startProcessInstanceByKey("process");

    // assert that only variables which ends with '-hist' are written to history
    List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().list();
    assertEquals(2, variableInstances.size());
    for (HistoricVariableInstance variableInstance : variableInstances) {
      assertTrue(variableInstance.getName().endsWith("-hist"));
    }
  }

  @Test
  @Deployment(resources = { "process.bpmn" })
  public void testCustomVariableHistoryWithAdditionalVariablesTest() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("a", "a");
    variables.put("b-hist", "b");
    variables.put("c-ist", "b");
    runtimeService.startProcessInstanceByKey("process", variables);

    // assert that only variables which ends with '-hist' are written to history
    List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().list();
    assertEquals(3, variableInstances.size());
    for (HistoricVariableInstance variableInstance : variableInstances) {
      assertTrue(variableInstance.getName().endsWith("-hist"));
    }
  }

}

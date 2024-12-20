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
package org.camunda.bpm.cockpit.plugin.sample;

import java.util.List;

import org.camunda.bpm.cockpit.Cockpit;
import org.camunda.bpm.cockpit.db.QueryParameters;
import org.camunda.bpm.cockpit.db.QueryService;
import org.camunda.bpm.cockpit.plugin.sample.db.ProcessInstanceCountDto;
import org.camunda.bpm.cockpit.plugin.spi.CockpitPlugin;
import org.camunda.bpm.cockpit.plugin.test.AbstractCockpitPluginTest;
import org.junit.Assert;
import org.junit.Test;

/**
*
* @author nico.rehwaldt
*/
public class SamplePluginsTest extends AbstractCockpitPluginTest {

  @Test
  public void testPluginDiscovery() {
    CockpitPlugin samplePlugin = Cockpit.getRuntimeDelegate().getPluginRegistry().getPlugin("sample-plugin");

    Assert.assertNotNull(samplePlugin);
  }

  @Test
  public void testSampleQueryWorks() {

    QueryService queryService = getQueryService();

    List<ProcessInstanceCountDto> instanceCounts =
      queryService
        .executeQuery(
          "cockpit.sample.selectProcessInstanceCountsByProcessDefinition",
          new QueryParameters<ProcessInstanceCountDto>());

    Assert.assertEquals(0, instanceCounts.size());
  }

}

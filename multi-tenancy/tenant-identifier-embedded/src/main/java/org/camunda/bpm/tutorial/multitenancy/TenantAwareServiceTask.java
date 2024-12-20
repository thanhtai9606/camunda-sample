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
package org.camunda.bpm.tutorial.multitenancy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * Sample service task which invokes a service with the tenant-id of the
 * execution (i.e. process instance).
 */
public class TenantAwareServiceTask implements JavaDelegate {

  protected static Service service;

  @Override
  public void execute(DelegateExecution execution) throws Exception {

    String tenantId = execution.getTenantId();

    service.invoke(tenantId);
  }

  public static void setService(Service service) {
    // simplified DI for testing
    TenantAwareServiceTask.service = service;
  }

}

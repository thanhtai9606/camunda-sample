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
package org.camunda.bpm.spring.boot.example.simple;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SimpleApplication.class }, properties = {
    "org.camunda.bpm.spring.boot.starter.example.simple.SimpleApplication.exitWhenFinished=false" })
public class SimpleApplicationTest {

  @Rule
  public Timeout timeout = new Timeout(5000);

  @Autowired
  private SimpleApplication application;

  @Test
  public void would_fail_if_process_not_completed_after_5_seconds() throws InterruptedException {
    while (!application.processApplicationStopped && !application.isProcessInstanceFinished()) {
      Thread.sleep(500L);
    }
  }
}

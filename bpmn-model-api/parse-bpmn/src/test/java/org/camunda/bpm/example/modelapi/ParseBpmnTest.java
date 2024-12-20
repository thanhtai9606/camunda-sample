/*
 * Copyright © 2013 - 2018 camunda services GmbH and various authors (info@camunda.com)
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
package org.camunda.bpm.example.modelapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.BpmnModelConstants;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.Script;
import org.camunda.bpm.model.bpmn.instance.ScriptTask;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaFormData;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaFormField;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.junit.Before;
import org.junit.Test;

public class ParseBpmnTest {

  protected BpmnModelInstance modelInstance;

  @Before
  public void loadProcess() {
    // read a BPMN model from an input stream
    modelInstance = Bpmn.readModelFromStream(getClass().getClassLoader().getResourceAsStream("process.bpmn"));
  }

  @Test
  /*
   * You can access every element of the BPMN model by id.
   */
  public void findElementById() {
    // get the start event of the process by id
    StartEvent startEvent = modelInstance.getModelElementById("startEvent");
    assertThat(startEvent.getName()).isEqualTo("Start Process");

    // get the forking gateway by id
    ExclusiveGateway fork = modelInstance.getModelElementById("gatewayFork");
    assertThat(fork.getName()).isEqualTo("Fork");

    // get the end event of the process by id
    EndEvent endEvent = modelInstance.getModelElementById("endEvent");
    assertThat(endEvent.getName()).isEqualTo("End Process");
  }

  @Test
  /*
   *  You can access elements by their type.
   */
  public void findElementByType() {
    // get all service tasks from the model
    Collection<ServiceTask> serviceTasks = modelInstance.getModelElementsByType(ServiceTask.class);
    // it only exists one service task
    assertThat(serviceTasks).hasSize(1);

    // get all events (eg start, intermediate, boundary and end events) from the model
    Collection<Event> events = modelInstance.getModelElementsByType(Event.class);
    // it exists a start and an end event
    assertThat(events).hasSize(2);

    // get all flow nodes in the model
    Collection<FlowNode> flowNodes = modelInstance.getModelElementsByType(FlowNode.class);
    // there exist 8 flow nodes: start event, service task, 2x exclusive gateways, 2x user tasks, script task, end event
    assertThat(flowNodes).hasSize(8);

    // get all extension elements in the model
    Collection<ExtensionElements> extensionElements = modelInstance.getModelElementsByType(ExtensionElements.class);
    // the start event and the end event contain extension elements
    assertThat(extensionElements).hasSize(2);
  }

  @Test
  /*
   * You can access attributes from model elements by special getters or by a generic getter.
   */
  public void readAttributes() {
    // get the service task and use special getters to access known attributes
    ServiceTask serviceTask = modelInstance.getModelElementById("serviceTask");
    assertThat(serviceTask.getId()).isEqualTo("serviceTask");
    assertThat(serviceTask.getName()).isEqualTo("Service Task");
    // you can also access camunda extension attributes
    assertThat(serviceTask.getCamundaExpression()).isEqualTo("${execution.setVariable('foo', 'bar')}");
    assertThat(serviceTask.isCamundaAsyncBefore()).isTrue();
    assertThat(serviceTask.isCamundaExclusive()).isFalse();

    UserTask userTaskA = modelInstance.getModelElementById("userTaskA");
    assertThat(userTaskA.getCamundaCandidateGroupsList()).containsExactly("management", "accounting");

    UserTask userTaskB = modelInstance.getModelElementById("userTaskB");
    assertThat(userTaskB.getCamundaAssignee()).isEqualTo("demo");

    // if the element contains an attribute which is not accessible by a special getter a generic getter can be used
    // Note: for demonstrating purpose we read attributes which also can be accessed by special getters
    assertThat(userTaskB.getAttributeValue("name")).isEqualTo("User Task B");
    assertThat(userTaskB.getAttributeValueNs(BpmnModelConstants.CAMUNDA_NS, "assignee")).isEqualTo("demo");
  }

  @Test
  /*
   * You can access child elements by type or name and often by special getters
   */
  public void readChildElements() {
    SequenceFlow flow4 = modelInstance.getModelElementById("flow4");
    // get all child elements of type condition expression
    Collection<ConditionExpression> conditionExpressions = flow4.getChildElementsByType(ConditionExpression.class);
    assertThat(conditionExpressions).hasSize(1);

    // get unique child element of type condition expression (throws an exception if there are more then one child element of this type)
    ConditionExpression conditionExpression = (ConditionExpression) flow4.getUniqueChildElementByType(ConditionExpression.class);
    assertThat(conditionExpression.getTextContent()).isEqualTo("${foo == 'bar'}");

    // get unique child element by namespace and name
    ScriptTask scriptTask = modelInstance.getModelElementById("scriptTask");
    Script script = (Script) scriptTask.getUniqueChildElementByNameNs(BpmnModelConstants.BPMN20_NS, "script");
    assertThat(script.getTextContent()).isEqualTo("println 'hello world'");

    // use special getters for known child elements
    ConditionExpression conditionExpression2 = flow4.getConditionExpression();
    assertThat(conditionExpression2).isEqualTo(conditionExpression);

    Script script2 = scriptTask.getScript();
    assertThat(script2).isEqualTo(script);
  }

  @Test
  /*
   * You can access extension elements like other child elements
   */
  public void readExtensionElements() {
    StartEvent startEvent = modelInstance.getModelElementById("startEvent");
    // get the BPMN 'extensionElements' element of the start event
    ExtensionElements extensionElements = startEvent.getExtensionElements();
    // get all extension elements of the start event (child elements of 'extensionElements')
    Collection<ModelElementInstance> elements = extensionElements.getElements();
    // the start event contains a single extension element (camunda:formData)
    assertThat(elements).hasSize(1);

    // get the camunda:formData extension element and containing form fields
    CamundaFormData formData = extensionElements.getElementsQuery().filterByType(CamundaFormData.class).singleResult();
    for (CamundaFormField formField : formData.getCamundaFormFields()) {
      assertThat(formField.getCamundaLabel()).isIn("Name", "Age");
      assertThat(formField.getCamundaType()).isIn("string", "long");
      assertThat(formField.getCamundaDefaultValue()).isIn("test", "0");
    }

    // the end event contains an execution listener
    EndEvent endEvent = modelInstance.getModelElementById("endEvent");
    CamundaExecutionListener executionListener = endEvent.getExtensionElements().getElementsQuery().filterByType(CamundaExecutionListener.class).singleResult();
    assertThat(executionListener.getCamundaEvent()).isEqualTo("start");
    assertThat(executionListener.getCamundaExpression()).isEqualTo("${execution.setVariable('finished', true)}");
  }

  @Test
  /*
   * You can access references between model elements by special getters. These attributes usually have the prefix
   * 'Ref' like 'sourceRef' and 'targetRef' of a sequenceFlow. But there are also other references like the
   * 'bpmnElement' reference of a BPMN DI element. The model API also supports the revere reference from the BPMN
    * element to its DI element.
   */
  public void useReferences() {
    // get the first sequence flow in the process
    SequenceFlow flow1 = modelInstance.getModelElementById("flow1");
    // get the source element of the sequence flow (the start event)
    assertThat(flow1.getSource()).isEqualTo(modelInstance.getModelElementById("startEvent"));
    // get the target element of the sequence flow (the service task)
    assertThat(flow1.getTarget()).isEqualTo(modelInstance.getModelElementById("serviceTask"));

    // get the joining gateway
    Gateway join = modelInstance.getModelElementById("gatewayJoin");
    // the joining gateway has to previous flow nodes, both user tasks
    assertThat(join.getPreviousNodes().list()).hasSize(2)
      .containsOnly(
        modelInstance.<FlowNode>getModelElementById("userTaskA"),
        modelInstance.<FlowNode>getModelElementById("userTaskB")
      );
    // the joining gateway has one succeeding flow node (the script task)
    assertThat(join.getSucceedingNodes().list()).hasSize(1)
      .containsOnly(
        modelInstance.<FlowNode>getModelElementById("scriptTask")
      );

    // get the bpmn plane DI element by type
    BpmnPlane bpmnPlane = modelInstance.getModelElementsByType(BpmnPlane.class).iterator().next();
    // the process element is the bpmn element corresponding to the bpmn plane
    assertThat(bpmnPlane.getBpmnElement()).isEqualTo(modelInstance.getModelElementById("testProcess"));

    // get the service task
    ServiceTask serviceTask = modelInstance.getModelElementById("serviceTask");
    // use the reverse reference to get the corresponding BPMN DI element
    assertThat(serviceTask.getDiagramElement()).isEqualTo(modelInstance.getModelElementById("_BPMNShape_ServiceTask_2"));
  }

}

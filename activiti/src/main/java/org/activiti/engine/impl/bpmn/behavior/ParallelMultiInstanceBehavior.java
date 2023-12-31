/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.activiti.engine.impl.bpmn.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.entity.GatewayUnmetJoinEventModel;
import org.activiti.engine.impl.event.GatewayUnmetJoinEvent;
import org.activiti.engine.impl.listener.GatewayUnmetJoinEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.BpmConstants;


/**
 * @author Joram Barrez
 */
public class ParallelMultiInstanceBehavior extends MultiInstanceActivityBehavior
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2140225274508746790L;

	public ParallelMultiInstanceBehavior(ActivityImpl activity, AbstractBpmnActivityBehavior originalActivityBehavior)
	{
		super(activity, originalActivityBehavior);
	}

	/**
	 * Handles the parallel case of spawning the instances. Will create child
	 * executions accordingly for every instance needed.
	 */
	protected void createInstances(ActivityExecution execution) throws Exception
	{
		int nrOfInstances = resolveNrOfInstances(execution);
		if (nrOfInstances <= 0)
		{
			throw new ActivitiIllegalArgumentException("Invalid number of instances: must be positive integer value" + ", but was " + nrOfInstances);
		}

		setLoopVariable(execution, NUMBER_OF_INSTANCES, nrOfInstances);
		setLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES, 0);
		setLoopVariable(execution, NUMBER_OF_ACTIVE_INSTANCES, nrOfInstances);

		List<ActivityExecution> concurrentExecutions = new ArrayList<ActivityExecution>();
		for (int loopCounter = 0; loopCounter < nrOfInstances; loopCounter++)
		{
			ActivityExecution concurrentExecution = execution.createExecution();
			concurrentExecution.setVariableLocal(BpmConstants.TOKEN_NAME, loopCounter + 100);
			concurrentExecution.setActive(true);
			concurrentExecution.setConcurrent(true);
			concurrentExecution.setScope(false);

			// In case of an embedded subprocess, and extra child execution is
			// required
			// Otherwise, all child executions would end up under the same
			// parent,
			// without any differentation to which embedded subprocess they
			// belong
			if (isExtraScopeNeeded())
			{
				ActivityExecution extraScopedExecution = concurrentExecution.createExecution();
				extraScopedExecution.setActive(true);
				extraScopedExecution.setConcurrent(false);
				extraScopedExecution.setScope(true);
				concurrentExecution = extraScopedExecution;
			}

			concurrentExecutions.add(concurrentExecution);
			logLoopDetails(concurrentExecution, "initialized", loopCounter, 0, nrOfInstances, nrOfInstances);
		}

		// Before the activities are executed, all executions MUST be created up
		// front
		// Do not try to merge this loop with the previous one, as it will lead
		// to bugs,
		// due to possible child execution pruning.
		for (int loopCounter = 0; loopCounter < nrOfInstances; loopCounter++)
		{
			ActivityExecution concurrentExecution = concurrentExecutions.get(loopCounter);
			// executions can be inactive, if instances are all automatics
			// (no-waitstate)
			// and completionCondition has been met in the meantime
			if (concurrentExecution.isActive() && !concurrentExecution.isEnded() && concurrentExecution.getParent().isActive() && !concurrentExecution.getParent().isEnded())
			{
				setLoopVariable(concurrentExecution, getCollectionElementIndexVariable(), loopCounter);
				executeOriginalBehavior(concurrentExecution, loopCounter);
			}
		}

		// See ACT-1586: ExecutionQuery returns wrong results when using multi
		// instance on a receive task
		// The parent execution must be set to false, so it wouldn't show up in
		// the execution query
		// when using .activityId(something). Do not we cannot nullify the
		// activityId (that would
		// have been a better solution), as it would break boundary event
		// behavior.
		if (!concurrentExecutions.isEmpty())
		{
			ExecutionEntity executionEntity = (ExecutionEntity) execution;
			executionEntity.setActive(false);
		}
	}

	/**
	 * Called when the wrapped {@link ActivityBehavior} calls the
	 * {@link AbstractBpmnActivityBehavior#leave(ActivityExecution)} method.
	 * Handles the completion of one of the parallel instances
	 */
	public void leave(ActivityExecution execution)
	{
		callActivityEndListeners(execution);

		int loopCounter = getLoopVariable(execution, getCollectionElementIndexVariable());
		int nrOfInstances = getLoopVariable(execution, NUMBER_OF_INSTANCES);
		int nrOfCompletedInstances = getLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES) + 1;
		int nrOfActiveInstances = getLoopVariable(execution, NUMBER_OF_ACTIVE_INSTANCES) - 1;

		if (isExtraScopeNeeded())
		{
			// In case an extra scope was created, it must be destroyed first
			// before going further
			ExecutionEntity extraScope = (ExecutionEntity) execution;
			execution = execution.getParent();
			extraScope.remove();
		}

		setLoopVariable(execution.getParent(), NUMBER_OF_COMPLETED_INSTANCES, nrOfCompletedInstances);
		setLoopVariable(execution.getParent(), NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances);
		logLoopDetails(execution, "instance completed", loopCounter, nrOfCompletedInstances, nrOfActiveInstances, nrOfInstances);

		ExecutionEntity executionEntity = (ExecutionEntity) execution;
		executionEntity.inactivate();
		executionEntity.getParent().forceUpdate();

		List<ActivityExecution> joinedExecutions = executionEntity.findInactiveConcurrentExecutions(execution.getActivity());
		// 此处修改流程引擎代码，让程序先执行外部的完成判断,将completionConditionSatisfied(execution)判断提前。
		if (completionConditionSatisfied(execution) || joinedExecutions.size() == nrOfInstances)
		{

			// Removing all active child executions (ie because
			// completionCondition is true)
			List<ExecutionEntity> executionsToRemove = new ArrayList<ExecutionEntity>();
			for (ActivityExecution childExecution : executionEntity.getParent().getExecutions())
			{
				if (childExecution.isActive())
				{
					executionsToRemove.add((ExecutionEntity) childExecution);
				}
			}
			for (ExecutionEntity executionToRemove : executionsToRemove)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Execution {} still active, but multi-instance is completed. Removing this execution.", executionToRemove);
				}
				executionToRemove.inactivate();
				executionToRemove.deleteCascade("multi-instance completed");
			}
			executionEntity.takeAll(executionEntity.getActivity().getOutgoingTransitions(), joinedExecutions);
		} else
		{
			// 并行多实例未完成时记录网关
			// 了流程结束节点，非最后一个分支聚合时触发
			ActivityImpl activity2 = (ActivityImpl) execution.getActivity();

			// 聚会条件未满足时记录堆栈信息
			GatewayUnmetJoinEventModel model = new GatewayUnmetJoinEventModel(activity2,execution,"subEndGateway","ParallelMultiInstanceEnd");
		
			GatewayUnmetJoinEvent event = new GatewayUnmetJoinEvent(model);
			//AppUtil.getBean(GatewayUnmetJoinEventListener.class);
			AppUtil.publishEvent(event);
			


		}
	}

}

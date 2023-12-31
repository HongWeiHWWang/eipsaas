<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:activiti="http://activiti.org/bpmn" 
	xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL"
	xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
	xmlns:ext="http://www.jee-soft.cn/bpm"
	>
	<xsl:param name="id" />
	<xsl:param name="name" />
	<xsl:output method="xml" version="1.0" encoding="utf-8" indent="no" />
	<xsl:template match="/">
	<ext:definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:activiti="http://activiti.org/bpmn"
    xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" 
    xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"	
    xmlns:ext="http://www.jee-soft.cn/bpm" 
    targetNamespace="http://www.jee-soft.cn/bpm"  >
		<process>
				<xsl:attribute name="id">
					<xsl:value-of select="$id"></xsl:value-of>
				</xsl:attribute>
				<xsl:attribute name="name">
					<xsl:value-of select="$name"></xsl:value-of>
				</xsl:attribute>
				<extensionElements>
					<!-- 流程开始事件监听器 -->
					<activiti:executionListener  event="start" >
						<xsl:attribute name="delegateExpression" >${startEventListener}</xsl:attribute>
					</activiti:executionListener>
					<!-- 流程结束事件监听器 -->
					<activiti:executionListener  event="end" >
						<xsl:attribute name="delegateExpression" >${endEventListener}</xsl:attribute>
					</activiti:executionListener>
				</extensionElements>
				
				<xsl:apply-templates select="/bpmn2:definitions/bpmn2:process/*"></xsl:apply-templates>
				
				
		</process>
		<xsl:if test="/bpmn2:definitions/bpmndi:BPMNDiagram">
			<xsl:copy-of select="/bpmn2:definitions/bpmndi:BPMNDiagram"></xsl:copy-of>	
		</xsl:if>
		
		
		<ext:extProcess>
	        <ext:extProperties>
	            <ext:subjectRule></ext:subjectRule>
	            <ext:description></ext:description>
	            <ext:startNotifyType></ext:startNotifyType>
	            <ext:archiveNotifyType></ext:archiveNotifyType>
	            <ext:skipFirstNode></ext:skipFirstNode>
	            <ext:firstNodeUserAssign></ext:firstNodeUserAssign>
	            <ext:skipSameUser></ext:skipSameUser>
	            <ext:allowCopyTo></ext:allowCopyTo>
	            <ext:allowTransTo></ext:allowTransTo>
	            <ext:useMainForm></ext:useMainForm>
	            <ext:allowReference></ext:allowReference>
	            <ext:allowRefCounts></ext:allowRefCounts>
	        </ext:extProperties>
      			<ext:extNodes>
         			
       			<xsl:for-each select="//bpmn2:process/descendant::*">
				<xsl:if test="name(.)!='sequenceFlow' ">
					<xsl:variable name="nodeId" select="./@id"></xsl:variable>
					<xsl:choose>
						<xsl:when test="name(.)='userTask'">
							<xsl:choose>
								<xsl:when test="./bpmn2:multiInstanceLoopCharacteristics">
									<ext:signNode  nodeType="common" >
										<xsl:call-template name="setNodeAttribute">
												<xsl:with-param name="obj" select="."></xsl:with-param>
										</xsl:call-template>
										<ext:extPlugins></ext:extPlugins>
										<ext:signRule decideType="refuse" voteType="amount" voteAmount="1" followMode="complete"/>
									</ext:signNode>
									
								</xsl:when>
								<xsl:otherwise>
									<ext:userNode  nodeType="common" >
										<xsl:call-template name="setNodeAttribute">
											<xsl:with-param name="obj" select="."></xsl:with-param>
										</xsl:call-template>
										<ext:extPlugins></ext:extPlugins>
									</ext:userNode>	
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="name(.)='scriptTask' or name(.)='serviceTask'">
							 <ext:baseNode  nodeType="common">
              						<xsl:call-template name="setNodeAttribute">
									<xsl:with-param name="obj" select="."></xsl:with-param>
								</xsl:call-template>
								<ext:extPlugins></ext:extPlugins>
          						</ext:baseNode>
						</xsl:when>
						
						<xsl:when test="name(.)='endEvent' or name(.)='startEvent'">
							 <ext:baseNode  nodeType="common">
              						<xsl:call-template name="setNodeAttribute">
									<xsl:with-param name="obj" select="."></xsl:with-param>
								</xsl:call-template>
								<ext:extPlugins></ext:extPlugins>
          						</ext:baseNode>
						</xsl:when>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
         				
      			</ext:extNodes>
  			</ext:extProcess>
			
		
	</ext:definitions>
	</xsl:template>
	
	
	
	
	<xsl:template match="bpmn2:laneSet"  name="lanSet">
		<xsl:copy-of select="."></xsl:copy-of>
	</xsl:template>
	
	<xsl:template match="bpmn2:subProcess" name="subProcess">
		<xsl:for-each select=".">
			<subProcess>
				<xsl:call-template name="setAttrubute">
					<xsl:with-param name="obj" select="."></xsl:with-param>
				</xsl:call-template>
				<extensionElements>
					<!-- 流程开始事件监听器 -->
					<activiti:executionListener  event="start"  >
						<xsl:attribute name="delegateExpression" >${subProcessStartListener}</xsl:attribute>
					</activiti:executionListener>
					<!-- 流程结束事件监听器 -->
					<activiti:executionListener  event="end" >
						<xsl:attribute name="delegateExpression" >${subProcessEndListener}</xsl:attribute>
					</activiti:executionListener>
				</extensionElements>
				<xsl:if test="./bpmn2:multiInstanceLoopCharacteristics">
					<multiInstanceLoopCharacteristics activiti:elementVariable="assignee">
						<xsl:attribute name="isSequential">
							<xsl:choose>
								<xsl:when test="./bpmn2:multiInstanceLoopCharacteristics[@isSequential='true']">true</xsl:when>
								<xsl:otherwise>false</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:attribute name="activiti:collection">${actUserService.getSubProcessUser(execution)}</xsl:attribute>
					</multiInstanceLoopCharacteristics>
				</xsl:if>
				
				<xsl:apply-templates select="./bpmn2:startEvent"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:userTask"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:endEvent"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:subProcess"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:sequenceFlow"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:callActivity"></xsl:apply-templates>
				
				<xsl:apply-templates select="./bpmn2:exclusiveGateway"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:parallelGateway"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:inclusiveGateway"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:task"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:receiveTask"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:scriptTask"></xsl:apply-templates>
				<xsl:apply-templates select="./bpmn2:serviceTask"></xsl:apply-templates>
				
				
			</subProcess>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="bpmn2:userTask" >
		
		<userTask >
			<xsl:call-template name="setAttrubute">
					<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		
			
			<extensionElements>
					<xsl:choose>
						<xsl:when test="./bpmn2:multiInstanceLoopCharacteristics">
							<activiti:taskListener event="create" >
								<xsl:attribute name="delegateExpression" >${taskSignCreateListener}</xsl:attribute>
							</activiti:taskListener>	
						</xsl:when>
						<xsl:otherwise>
							<activiti:taskListener event="create"  >
								<xsl:attribute name="delegateExpression" >${taskCreateListener}</xsl:attribute>
							</activiti:taskListener>
						</xsl:otherwise>
					</xsl:choose>
					
					<activiti:taskListener event="complete"   >
						<xsl:attribute name="delegateExpression" >${taskCompleteListener}</xsl:attribute>
					</activiti:taskListener>
			</extensionElements>
			
			<xsl:if test="./bpmn2:multiInstanceLoopCharacteristics">
				<multiInstanceLoopCharacteristics activiti:elementVariable="assignee">
					<xsl:attribute name="isSequential">
						<xsl:choose>
							<xsl:when test="./bpmn2:multiInstanceLoopCharacteristics[@isSequential='true']">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<xsl:attribute name="activiti:collection">${actUserService.getSignUser(execution)}</xsl:attribute>
					<completionCondition>${signComplete.isComplete(execution) }</completionCondition>
				</multiInstanceLoopCharacteristics>
			</xsl:if>
		</userTask>
		
	</xsl:template>
	<xsl:template match="bpmn2:endEvent" name="endEvent">
		
		<xsl:for-each select=".">
			<endEvent>
				<xsl:call-template name="setAttrubute">
					<xsl:with-param name="obj" select="."></xsl:with-param>
				</xsl:call-template>
				<xsl:if test="trigger='Error'">
					<errorEventDefinition>
						<xsl:attribute name="errorRef">
							<xsl:value-of select="'Error'"></xsl:value-of>
						</xsl:attribute>
					</errorEventDefinition>
				</xsl:if>
			</endEvent>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="bpmn2:startEvent" name="startEvent">
		<startEvent activiti:initiator="startUser">
			<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		</startEvent>
	</xsl:template>
	
	
	<xsl:template match="bpmn2:sequenceFlow" name="sequenceFlow">
		 <xsl:copy-of select="."></xsl:copy-of>
	</xsl:template>
	
	
	
	<xsl:template match="bpmn2:callActivity" name="callActivity">
		 <callActivity>
		 	<xsl:attribute name="calledElement" >
				<xsl:value-of select="./@calledElement"></xsl:value-of>
			</xsl:attribute>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>

			<extensionElements>
				<activiti:executionListener event="start" >
					<xsl:attribute name="delegateExpression" >${callSubProcessStartListener}</xsl:attribute>
				</activiti:executionListener>
				<activiti:executionListener event="end">
					<xsl:attribute name="delegateExpression" >${callSubProcessEndListener}</xsl:attribute>
				</activiti:executionListener>
				
							
			</extensionElements>
			<xsl:if test="./bpmn2:multiInstanceLoopCharacteristics">
				<multiInstanceLoopCharacteristics activiti:elementVariable="assignee">
					<xsl:attribute name="isSequential">
						<xsl:choose>
							<xsl:when test="./bpmn2:multiInstanceLoopCharacteristics[@isSequential='true']">true</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<xsl:attribute name="activiti:collection">${actUserService.getExtSubProcessMultipleUser(execution)}</xsl:attribute>
				</multiInstanceLoopCharacteristics>
			</xsl:if>
		 </callActivity>
	</xsl:template>
	
	<xsl:template match="bpmn2:exclusiveGateway" name="exclusiveGateway">
		 <exclusiveGateway>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </exclusiveGateway>
		
	</xsl:template>
	
	<xsl:template match="bpmn2:parallelGateway" name="parallelGateway">
		 <parallelGateway>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </parallelGateway>
		
	</xsl:template>
	
	<xsl:template match="bpmn2:inclusiveGateway" name="inclusiveGateway">
		 <inclusiveGateway>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </inclusiveGateway>
		
	</xsl:template>
	
	
	<xsl:template match="bpmn2:task" name="task">
		 <task>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </task>
		
	</xsl:template>
	
	<xsl:template match="bpmn2:receiveTask" name="receiveTask">
		 <receiveTask>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </receiveTask>
		
	</xsl:template>
	
	<xsl:template match="bpmn2:scriptTask" name="scriptTask">
		 <serviceTask >
		 	<xsl:attribute name="activiti:delegateExpression" >${customServiceTask}</xsl:attribute>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
		 </serviceTask>
		
	</xsl:template>
	
	<xsl:template match="bpmn2:serviceTask" name="serviceTask">
		 <serviceTask >
		 	<xsl:attribute name="activiti:delegateExpression" >${customServiceTask}</xsl:attribute>
		 	<xsl:call-template name="setAttrubute">
				<xsl:with-param name="obj" select="."></xsl:with-param>
			</xsl:call-template>
			
			
		 </serviceTask>
		
	</xsl:template>

	<xsl:template name="setAttrubute">
		<xsl:param name="obj" />
		<xsl:attribute name="id">
			<xsl:value-of select="$obj/@id"></xsl:value-of>
		</xsl:attribute>
		<xsl:attribute name="name">
			<xsl:value-of select="$obj/@name"></xsl:value-of>
		</xsl:attribute>
	</xsl:template>
	
	<xsl:template name="setNodeAttribute">
		<xsl:param name="obj" />
		<xsl:attribute name="name"  >
			<xsl:value-of select="$obj/@name"></xsl:value-of>
		</xsl:attribute>
		<xsl:attribute name="bpmnElement"  >
			<xsl:value-of select="$obj/@id"></xsl:value-of>
		</xsl:attribute>
		
	</xsl:template>

</xsl:stylesheet>


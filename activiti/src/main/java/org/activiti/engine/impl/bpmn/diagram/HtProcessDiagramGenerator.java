package org.activiti.engine.impl.bpmn.diagram;

import org.activiti.image.impl.DefaultProcessDiagramGenerator;

/**
 * 暂时不知道怎么用，先注释掉
 * @author Deng
 *
 */
public class HtProcessDiagramGenerator extends DefaultProcessDiagramGenerator{
	/*public InputStream generatePngDiagram(BpmnModel bpmnModel) {
		  return generateDiagram(bpmnModel, Collections.<String, String> emptyMap(), "png");
	  }
	
	public static InputStream generatePngDiagram(BpmnModel bpmnModel, Map<String, String> highLightedActivities) {
		  return generateDiagram(bpmnModel, highLightedActivities, "png");
	  }
	  
	  public static InputStream generateDiagram(BpmnModel bpmnModel, Map<String, String> highLightedActivities, String imageType) {
		  HtProcessDiagramCanvas canvas = generateDiagram(bpmnModel, highLightedActivities);
		  return canvas.generateImage(imageType);
	  }
	    
	  public static HtProcessDiagramCanvas generateDiagram(BpmnModel bpmnModel, Map<String, String> highLightedActivities) {
		  HtProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel);
		    
//	    // Draw pool shape, if process is participant in collaboration
	    for (Pool pool : bpmnModel.getPools()) {
	      GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
	      processDiagramCanvas.drawPoolOrLane(pool.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(), 
	              (int) graphicInfo.getWidth(),  (int) graphicInfo.getHeight());
	    }
	    
	    // Draw lanes
	    for (Process process : bpmnModel.getProcesses()) {
	      for (Lane lane : process.getLanes()) {
	        GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(lane.getId());
	        processDiagramCanvas.drawPoolOrLane(lane.getName(), (int) graphicInfo.getX(), (int) graphicInfo.getY(), 
	                (int) graphicInfo.getWidth(),  (int) graphicInfo.getHeight());
	      }
	    }
	    
	    // Draw activities and their sequence-flows
	    for (FlowNode flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(FlowNode.class)) {
	    	drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, Collections.<String> emptyList());
	    }
	    
	    // Draw artifacts
	    for (Process process : bpmnModel.getProcesses()) {
	      for (Artifact artifact : process.getArtifacts()) {
	        drawArtifact(processDiagramCanvas, bpmnModel, artifact);
	      }
	    }
	    
	    return processDiagramCanvas;
	  }
	  
	  protected static void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode, Map<String, String> highLightedActivities, List<String> highLightedFlows) {
			ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
		    if (drawInstruction != null) {

		      drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);

		      // Gather info on the multi instance marker
		      boolean multiInstanceSequential = false, multiInstanceParallel = false, collapsed = false;
		      if (flowNode instanceof Activity) {
		        Activity activity = (Activity) flowNode;
		        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
		        if (multiInstanceLoopCharacteristics != null) {
		          multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
		          multiInstanceParallel = !multiInstanceSequential;
		        }
		      }

		      // Gather info on the collapsed marker
		      GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId()); 
		      if (flowNode instanceof SubProcess) {
		        collapsed = graphicInfo.getExpanded() != null && graphicInfo.getExpanded() == false;
		      } else if (flowNode instanceof CallActivity) {
		        collapsed = true;
		      }

		      // Actually draw the markers
		      processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), 
		              multiInstanceSequential, multiInstanceParallel, collapsed);

		      // Draw highlighted activities
		      if (highLightedActivities.containsKey(flowNode.getId())) {
		    	String color = highLightedActivities.get(flowNode.getId());
		        drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()), color);
		      }

		    }

		    // TODO: use graphicInfo
		    
		    // Outgoing transitions of activity
		    for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
		      boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
		      boolean isDefault = sequenceFlow.getConditionExpression() == null && (flowNode instanceof Gateway);
		      boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null && !(flowNode instanceof Gateway);
		      
		      String sourceRef = sequenceFlow.getSourceRef();
		      String targetRef = sequenceFlow.getTargetRef();
		      FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
		      FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
		      List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
		      graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement, targetElement, graphicInfoList);
		      int xPoints[]= new int[graphicInfoList.size()];
		      int yPoints[]= new int[graphicInfoList.size()];
		      
		      for (int i=1; i<graphicInfoList.size(); i++) {
		        GraphicInfo graphicInfo = graphicInfoList.get(i);
		        GraphicInfo previousGraphicInfo = graphicInfoList.get(i-1);
		        
		        if (i == 1) {
		          xPoints[0] = (int) previousGraphicInfo.getX();
		          yPoints[0] = (int) previousGraphicInfo.getY();
		        }
		        xPoints[i] = (int) graphicInfo.getX();
		        yPoints[i] = (int) graphicInfo.getY();
		        
		      }

		      processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault, highLighted);

		      // Draw sequenceflow label
		      GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
		      if (labelGraphicInfo != null) {
		    	 GraphicInfo lineCenter = null;
		    	  if(labelGraphicInfo.getX()<0 && labelGraphicInfo.getY()==0){
		    		  lineCenter = getLineCenter(graphicInfoList);
		    	  }
		    	  if(lineCenter==null){
		    		  processDiagramCanvas.drawLabel(sequenceFlow.getName(), (int)labelGraphicInfo.getX(), (int)(labelGraphicInfo.getY() - labelGraphicInfo.getHeight()),
				          		(int) labelGraphicInfo.getWidth(), (int) labelGraphicInfo.getHeight(), false);
		    	  }else{
		    		  processDiagramCanvas.drawLabel(sequenceFlow.getName(), (int)lineCenter.getX(), (int)lineCenter.getY(),
				          		(int) labelGraphicInfo.getWidth(), (int) labelGraphicInfo.getHeight(), false);
		    	  }
		      }
		    }

		    // Nested elements
		    if (flowNode instanceof FlowElementsContainer) {
		      for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
		        if (nestedFlowElement instanceof FlowNode) {
		          drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement, highLightedActivities, highLightedFlows);
		        }
		      }
		    }
	  }
	  
	  protected static HtProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel) {
		    
		    // We need to calculate maximum values to know how big the image will be in its entirety
		    double minX = Double.MAX_VALUE;
		    double maxX = 0;
		    double minY = Double.MAX_VALUE;
		    double maxY = 0;

		    for (Pool pool : bpmnModel.getPools()) {
		      GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
		      minX = graphicInfo.getX();
		      maxX = graphicInfo.getX() + graphicInfo.getWidth();
		      minY = graphicInfo.getY();
		      maxY = graphicInfo.getY() + graphicInfo.getHeight();
		    }
		    
		    List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
		    for (FlowNode flowNode : flowNodes) {

		      GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
		      
		      // width
		      if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
		        maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
		      }
		      if (flowNodeGraphicInfo.getX() < minX) {
		        minX = flowNodeGraphicInfo.getX();
		      }
		      // height
		      if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
		        maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
		      }
		      if (flowNodeGraphicInfo.getY() < minY) {
		        minY = flowNodeGraphicInfo.getY();
		      }

		      for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
		        List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
		        for (GraphicInfo graphicInfo : graphicInfoList) {
		          // width
		          if (graphicInfo.getX() > maxX) {
		            maxX = graphicInfo.getX();
		          }
		          if (graphicInfo.getX() < minX) {
		            minX = graphicInfo.getX();
		          }
		          // height
		          if (graphicInfo.getY() > maxY) {
		            maxY = graphicInfo.getY();
		          }
		          if (graphicInfo.getY()< minY) {
		            minY = graphicInfo.getY();
		          }
		        }
		        
		        //caculate sequenceFlow label,except flash designer
		        GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
			    if (labelGraphicInfo != null && !(labelGraphicInfo.getY()==0 && labelGraphicInfo.getX() < 0)) {
			    	//width
			    	if(labelGraphicInfo.getX() < minX){
			    		minX = labelGraphicInfo.getX();
			    	}
			    	
			    	if(labelGraphicInfo.getY() < minY){
			    		minY = labelGraphicInfo.getY();
			    	}
			    	
			    	//height
			    	if(labelGraphicInfo.getX() + labelGraphicInfo.getWidth() > maxX){
			    		maxX = labelGraphicInfo.getX() + labelGraphicInfo.getWidth();
			    	}
			    	
			    	if(labelGraphicInfo.getY() + labelGraphicInfo.getHeight() > maxY){
			    		maxY = labelGraphicInfo.getY() + labelGraphicInfo.getHeight();
			    	}
			    }
		        
		      }
		    }
		    
		    List<Artifact> artifacts = gatherAllArtifacts(bpmnModel);
		    for (Artifact artifact : artifacts) {

		      GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());
		      
		      if (artifactGraphicInfo != null) {
			      // width
			      if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
			        maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
			      }
			      if (artifactGraphicInfo.getX() < minX) {
			        minX = artifactGraphicInfo.getX();
			      }
			      // height
			      if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
			        maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
			      }
			      if (artifactGraphicInfo.getY() < minY) {
			        minY = artifactGraphicInfo.getY();
			      }
		      }

		      List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
		      if (graphicInfoList != null) {
			      for (GraphicInfo graphicInfo : graphicInfoList) {
			          // width
			          if (graphicInfo.getX() > maxX) {
			            maxX = graphicInfo.getX();
			          }
			          if (graphicInfo.getX() < minX) {
			            minX = graphicInfo.getX();
			          }
			          // height
			          if (graphicInfo.getY() > maxY) {
			            maxY = graphicInfo.getY();
			          }
			          if (graphicInfo.getY()< minY) {
			            minY = graphicInfo.getY();
			          }
			      }
		      }
		    }
		    
		    int nrOfLanes = 0;
		    for (Process process : bpmnModel.getProcesses()) {
		      for (Lane l : process.getLanes()) {
		        
		        nrOfLanes++;
		        
		        GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(l.getId());
		        // // width
		        if (graphicInfo.getX() + graphicInfo.getWidth() > maxX) {
		          maxX = graphicInfo.getX() + graphicInfo.getWidth();
		        }
		        if (graphicInfo.getX() < minX) {
		          minX = graphicInfo.getX();
		        }
		        // height
		        if (graphicInfo.getY() + graphicInfo.getHeight() > maxY) {
		          maxY = graphicInfo.getY() + graphicInfo.getHeight();
		        }
		        if (graphicInfo.getY() < minY) {
		          minY = graphicInfo.getY();
		        }
		      }
		    }
		    
		    // Special case, see http://jira.codehaus.org/browse/ACT-1431
		    if (flowNodes.size() == 0 && bpmnModel.getPools().size() == 0 && nrOfLanes == 0) {
		      // Nothing to show
		      minX = 0;
		      minY = 0;
		    }
		    
		    return new HtProcessDiagramCanvas((int) maxX + 10,(int) maxY + 10, (int) minX, (int) minY);
		  }
	  
	  private static void drawHighLight(HtProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo, String color) {
		    processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), color);
	  }*/
}

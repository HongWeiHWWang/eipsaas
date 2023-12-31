package com.hotent.runtime.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.BaseException;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.runtime.manager.MeetingRoomAppointmentManager;
import com.hotent.runtime.manager.MeetingRoomManager;
import com.hotent.runtime.model.MeetingRoomAppointment;
import com.hotent.uc.api.impl.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：会议室预约 控制器类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:11:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/meetingRoomAppointment/v1")
@Api(tags="会议室预约")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class MeetingRoomAppointmentController extends BaseController<MeetingRoomAppointmentManager,MeetingRoomAppointment>{
	@Resource
	MeetingRoomAppointmentManager meetingRoomAppointmentManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmInstService bpmInstService;
	@Resource
	BoDataService boDataService;
	@Resource
	MeetingRoomManager meetingRoomManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmOpinionService bpmOpinionService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	
	/**
	 * 会议室预约列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="会议室预约数据列表", httpMethod = "POST", notes = "获取会议室预约列表")
	public PageList<ObjectNode> list(
			@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return meetingRoomAppointmentManager.getAppointList(queryFilter);
	}
	
	/**
	 * 会议室预约明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="会议室预约数据详情",httpMethod = "GET",notes = "会议室预约数据详情")
	public MeetingRoomAppointment get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return meetingRoomAppointmentManager.get(id);
	}
	
    /**
	 * 新增会议室预约
	 * @param meetingRoomAppointment
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新会议室预约数据", httpMethod = "POST", notes = "新增,更新会议室预约数据")
	public CommonResult<String> save(@ApiParam(name="meetingRoomAppointment",value="会议室预约业务对象", required = true)@RequestBody MeetingRoomAppointment meetingRoomAppointment) throws Exception{
		String msg = "添加会议室预约成功";
		if(StringUtil.isEmpty(meetingRoomAppointment.getId())){
			meetingRoomAppointmentManager.create(meetingRoomAppointment);
		}else{
			meetingRoomAppointmentManager.update(meetingRoomAppointment);
			 msg = "更新会议室预约成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除会议室预约记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除会议室预约记录", httpMethod = "DELETE", notes = "删除会议室预约记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		meetingRoomAppointmentManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除会议室预约记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除会议室预约记录", httpMethod = "DELETE", notes = "批量删除会议室预约记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String ids) throws Exception{
		meetingRoomAppointmentManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 待参加会议
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/pendingJson")
	@ApiOperation(value="待参加会议", httpMethod = "POST", notes = "待参加会议")
	public  PageList<ObjectNode> pendingJson(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception
	{
		throw new BaseException("未实现");
//		queryFilter.addFilter("PROC_DEF_KEY_", "hylc",QueryOP.EQUAL);
//		String userId = ContextUtil.getCurrentUserId();
//		// 查询列表
//		PageList<DefaultBpmTask> list = bpmTaskManager.getByNeedPendMeetingUserId(userId, queryFilter);
//		PageList<ObjectNode> objList= new  PageList<ObjectNode> ();
//		List<ObjectNode> taskList=new ArrayList<>();
//		for(int i=0;i<list.getRows().size();i++){
//			DefaultBpmTask task=  list.getRows().get(i);
//			BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
//			List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);
//			// BO数据前置处理
//			ObjectNode data =(ObjectNode) BoDataUtil.hanlerData(bpmProcessInstance,task.getNodeId(), boDatas).get("hyywdx");
//			data.put("nodeId", task.getNodeId());
//			data.put("procInstId", task.getProcInstId());
//			data.put("taskId", task.getId());
//			taskList.add(data);
//		}
//		objList.setPage(queryFilter.getPageBean().getPage());
//		objList.setPageSize(queryFilter.getPageBean().getPageSize());
//		objList.setTotal(list.getTotal());
//		objList.setRows(taskList);
//		return objList;
	}
	
	/**
	 * 我的发起的会议
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/myRequestJson")
	@ApiOperation(value="我的发起的会议", httpMethod = "POST", notes = "我的发起的会议")
	public  PageList<ObjectNode> myRequestJson(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter)  throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		queryFilter.addFilter("create_by_", userId, QueryOP.EQUAL);
		queryFilter.addFilter("PROC_DEF_KEY_", "hylc", QueryOP.EQUAL);
		queryFilter.addFilter("STATUS_", "draft", QueryOP.NOT_EQUAL);
		PageList<DefaultBpmProcessInstance> query = bpmProcessInstanceManager.query(queryFilter);
		// 查询列表
		PageList<ObjectNode> pList = new PageList<ObjectNode>();
		List<ObjectNode>  insList=new ArrayList<>();
		for(int i=0;i<query.getRows().size();i++){
			DefaultBpmProcessInstance processInstance =query.getRows().get(i);
			List<BpmTaskOpinion> bpmTaskOpinions =  bpmOpinionService.getTaskOpinions(processInstance.getId());
			BpmTaskOpinion bto =bpmTaskOpinions.get(bpmTaskOpinions.size()-1);
			List<ObjectNode> boDatas = boDataService.getDataByInst(processInstance);
			ObjectNode data =(ObjectNode) BoDataUtil.hanlerData(processInstance,bto.getTaskKey(), boDatas).get("hyywdx");
			data.put("taskId", bto.getTaskKey());
			insList.add(data);
		}
		pList.setPage(query.getPage());
		pList.setRows(insList);
		pList.setPageSize(query.getPageSize());
		pList.setTotal(query.getTotal());
		
		return pList;
	}
	
	/**
	 *历史会议
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/myCompletedJson")
	@ApiOperation(value="历史会议", httpMethod = "POST", notes = "历史会议")
	public  PageList<ObjectNode> myCompletedJson(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		queryFilter.addFilter("wfInst.PROC_DEF_KEY_", "hylc",QueryOP.EQUAL);
		queryFilter.addFilter("opinion.task_key_", "UserTask4",QueryOP.EQUAL);
		queryFilter.addFilter("opinion.auditor_", userId,QueryOP.EQUAL);
		// 查询列表
		PageList<DefaultBpmProcessInstance> query =  bpmProcessInstanceManager.getMyHandledMeeting(queryFilter);
		PageList<ObjectNode> pList = new PageList<ObjectNode>();
		List<ObjectNode>  insList=new ArrayList<>();
		for(int i=0;i<query.getRows().size();i++){
			DefaultBpmProcessInstance processInstance =query.getRows().get(i);
			List<BpmTaskOpinion> bpmTaskOpinions =  bpmOpinionService.getTaskOpinions(processInstance.getId());
		     // 按id倒序
	        Collections.sort(bpmTaskOpinions, new Comparator<BpmTaskOpinion>() {
	            public int compare(BpmTaskOpinion arg0, BpmTaskOpinion arg1) {
	                int hits0 =Integer.valueOf(arg0.getId());
	                int hits1 = Integer.valueOf(arg1.getId());
	                if (hits1 > hits0) {
	                    return 1;
	                } else if (hits1 == hits0) {
	                    return 0;
	                } else {
	                    return -1;
	                }
	            }
	        });
	        
			BpmTaskOpinion bto =bpmTaskOpinions.get(bpmTaskOpinions.size()-1);
			List<ObjectNode> boDatas = boDataService.getDataByInst(processInstance);
			ObjectNode data =(ObjectNode) BoDataUtil.hanlerData(processInstance,bto.getTaskKey(), boDatas).get("hyywdx");
			data.put("taskId", bto.getTaskKey());
			insList.add(data);
		}
		pList.setRows(insList);
		pList.setPage(queryFilter.getPageBean().getPage());
		pList.setPageSize(queryFilter.getPageBean().getPageSize());
		pList.setTotal(query.getTotal());
		return pList;
	}
	/**
	 *根据流程key获取最新的流程定义id
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value="/getBpmDefId/{id}")
	@ApiOperation(value="根据流程key获取最新的流程定义id",httpMethod = "GET",notes = "根据流程key获取最新的流程定义id")
	public String getBpmDefId(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id)throws Exception {
		List<DefaultBpmDefinition> defs = bpmDefinitionManager.queryByDefKey("hylc");
		String  defId = ""; 
	       if(defs.size()>0 && BeanUtils.isNotEmpty(defs.get(defs.size()-1))){
	    	   defId= defs.get(defs.size()-1).getDefId();
	       }
		return defId;
	}
}

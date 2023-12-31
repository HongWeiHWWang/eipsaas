<#--显示字段-->
<#assign fields=showList>
<#--字段Map-->

<#assign factFieldMap=showMap>

<#--是否显示行号-->
<#assign showRowsNum="false">
<#--是否初始化查询-->
<#assign initQuery="false">
<#--是否初始化查询-->
<#assign pageSize=queryView.pageSize>
<#--是否分组-->
<#assign supportGroup="false">

<#if (queryView.supportGroup==1)>
    <#assign supportGroup="true">
</#if>


<#if (queryView.showRowsNum==1)>
    <#assign showRowsNum="true">
</#if>

<#if (queryView.initQuery==1)>
    <#assign initQuery="true">
</#if>


<#--生成colModel对象-->
<#function getField field>
    <#assign sort="false" >
    <#assign frozen="false" >
    <#assign align=field.align >
    <#assign alarmSetting=field.alarmSetting >
    <#assign formater=field.formater>
    <#assign url=field.url >
    
    <#if (field.sortable==1)>
        <#assign sort="true" >
    </#if>
    <#if (field.frozen==1)>
        <#assign frozen="true">
    </#if>

    <#if ( align?exists && align=="" )>
        <#assign align="center" >
    </#if>

    <#assign rtn>
        {label:"${field.fieldDesc}",name:"${field.fieldName}",
        sortable:${sort},frozen:${frozen},align:"${align}"
        <#if (field.summaryType?exists && field.summaryType?trim!="") >
            ,summaryType:"${field.summaryType}"
        </#if>
        <#if (field.summaryTemplate?exists && field.summaryTemplate?trim!="") >
            ,summaryTpl:"${field.summaryTemplate}"
        </#if>
        <#if (field.width?exists && field.width != 0) >
            ,width:${field.width}
        </#if>
        <#if (util.isNotEmpty(formater))>
            ,formatter:${field.fieldName}_Formater
        <#elseif (util.isNotEmpty(alarmSetting))>
            ,formatter:${field.fieldName}_AlarmFormater
        <#elseif ( util.isNotEmpty(url))>
            ,formatter:${field.fieldName}_UrlFormater
        </#if>
        }</#assign>
    <#return rtn>
</#function>



<#--获取条件-->
<#function getCondition condition field>
    <#assign rtn="">
    <#list condition as con>
        <#--处理运算符-->
        <#assign operate=con.op >
        <#--处理值-->
        <#assign val=con.val >
        <#if (field.dataType=="varchar") >
            <#assign val="'"+val+"'" >
        </#if>
        
        <#if con_index==0>
            <#assign rtn="cellvalue" + operate + val > 
        <#else>
            <#assign rtn=rtn + " && cellvalue" + operate + val >
        </#if>
    </#list>
    <#return rtn>
</#function>
<#--生成格式化函数-->
<#macro genFormaterFunction>
    <#list fields as field>
        <#assign alarmSetting=field.alarmSetting >
        <#assign formater=field.formater>
        <#assign url=field.url >
        <#if (util.isNotEmpty(formater)) >
            function ${field.fieldName}_Formater(cellvalue, options, rowObject){
                 if(options.rowId=="" || options.rowId.indexOf("gridList")>-1){
                    if(typeof rowObject=='string'){
                        return cellvalue;
                    }else{
                        return rowObject.groupValue+":"+rowObject.groupCount;
                    }
                 }else{
                    ${formater};
                 }
            }
        <#elseif (util.isNotEmpty(alarmSetting)) >
            <#assign alarm=alarmSetting >
            function ${field.fieldName}_AlarmFormater(cellvalue, options, rowObject){
                if(options.rowId=="" || options.rowId.indexOf("gridList")>-1){
                    return cellvalue;
                }
                <#list alarm as item>
                    if(${getCondition(item.condition,field)}){
                        return "<span style='color:${item.color};font-weight:bold;'>" + cellvalue +"</span>";
                    }
                </#list>
                return cellvalue;
            }
        <#elseif (util.isNotEmpty(url)) >
            function ${field.fieldName}_UrlFormater(cellvalue, options, rowObject){
                var url= "${url}";
                if(!url.startWith("http")&&!url.startWith("https")){
	                url =__ctx + url;
	            }
                url=replaceUrl(url,rowObject);
                return "<a href='"+url+"' target='_blank'>"+cellvalue+"</a>";
            }
        </#if>      
    </#list>
</#macro>

<#--生成查询控件-->
<#macro getController con fieldName>
        <#assign dataType=con.dataType>
        <#assign content=con.controlContent>
        <#assign dateFormat=con.dateFormat>
        
        <#if (util.isEmpty(fieldName))>
        <#assign fieldName=con.fieldName>
        </#if>
        
        <#switch con.controlType>
            <#case "onetext">
                <input type="text" name="${fieldName}" class="inputText"   />
            <#break>
            <#case "select"><#--下拉列表框 -->
                <#assign options=content>
                <select class="inputText" name="${fieldName}" >
                    <option value="">全部</option>
                    <#list options as opt>
                        <option value="${opt.optionKey}">${opt.optionValue}</option>
                    </#list>
                </select>
            <#break>
            <#case "customdialog"><#-- 自定对话框 -->
                <#assign dg=content>
                <input type="text" id="${fieldName}" name="${fieldName}" class="inputText"  disabled="disabled" />
                <a class="btn btn-primary btn-xs" onclick="showCustomDialog(this,'${dg.alias}','${dg.resultField}')">选择</a>
            <#break>
            <#case "date">
                <input type="text" name="${fieldName}" datefmt="${dateFormat}" readonly="readonly"  class="wdateTime inputText"  />
            <#break>
        </#switch>
</#macro>

<#--日期条件查询框-->
<#macro genDate con>
    <#assign fieldName=con.name>
    <#assign operate=con.operate>
    <#if (operate=='BETWEEN')>
        从:
        <@getController con "begin"+con.fieldName/>
        </li><li>到:
        <@getController con "end"+con.fieldName/>
    <#else>
        <@getController con />
    </#if>
</#macro>

<#--数字条件查询框-->
<#macro genNumber con>
    <#assign fieldName=con.fieldName>
    <#assign operate=con.operate>
    <#if (operate=="BETWEEN")>
        从:
        <@getController con "begin"+con.fieldName/>
        </li><li>到:
        <@getController con "end"+con.fieldName/>
    <#else>
        <@getController con />
    </#if>
    
</#macro>

<#macro genCondition con>
    <#assign fieldName=con.fieldName>
    <#assign operate=con.operate>
    <#assign dataType=con.dataType>
    <li>
    <lable>${con.fieldDesc}:</lable>  
    <#switch dataType>
        <#case "varchar"><#--字符串-->
        <#case "text">
            <@getController con />
            <#break>
        <#--数字-->
        <#case "number">
            <@genNumber con/>
            <#break>
        <#--日期-->   
        <#case "date">
            <@genDate con/>
            <#break>
    </#switch>
    </li>
</#macro>

<#macro addCtx url><#if (!url?contains('http://') && !url?contains('https://') ) ><#noparse>${ctx}</#noparse></#if>${url}</#macro>

<link rel="stylesheet" href="${ctx}/js/lib/easyui/themes/bootstrap/easyui.css">
<script type="text/javascript" src="${ctx}/js/lib/jqGrid/extendjqGrid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/common/base/jquery.easyui.topCall.js"></script>
<#--引入处理日期类型进行取最大最小值的js-->
<script type="text/javascript" src="${ctx}/js/lib/jqGrid/extendjqGrid.js"></script>
<script type="text/javascript">
<#--调用生成格式化函数体-->
<@genFormaterFunction/>

function jqGridInit() {
    //
    $("#gridList").jqGrid({
        url:__ctx + '/system/query/queryView/data_${queryView.sqlAlias}/${queryView.alias}.ht',
        datatype: "json",
        mtype:"POST",//提交方式
        height: '100%',
        postData:__param,
        shrinkToFit:false,
        jsonReader:{
            root: "rows",// json中代表实际模型数据的入口
            total: "total", // json中代表总页数的数据
            page: "page", // json中代表当前页码的数据
            records: "records",// json中代表数据行总数的数据
            repeatitems : false// 如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
        },
        colModel:[
            <#assign idx=0>
            <#list fields as field>
                <#--隐藏列不显示-->
                <#if (field.hidden==0)>
                    <#if (idx >0) >,</#if>
                    ${getField(field)}  
                    <#assign idx=idx +1>
                </#if>
            </#list>
            
            <#if (rowButtons?exists && rowButtons?size>0) >
                ,{label:"管理", align:"center",formatter:managerFormatter}
            </#if>
        ],
        //行号
        rownumbers:${showRowsNum},
        sortname:"${sortField}",
        sortorder:"${sortSeq}",
        rownumWidth:20,
        viewrecords:true,
        //初始是否查询
        search:${initQuery},
        //上分页条
        //toppager :"#pagerNav",
        <#if (queryView.needPage==1)>
        rowNum: ${pageSize},
        rowList: [5,10,15,20,30],
        pager: "#pagerNav",
        prmNames:{page:"page",rows:"pageSize",sort:"sortField",order:"orderSeq",search:"initSearch"},
        <#else>
        prmNames:{sort:"sortField",order:"orderSeq",search:"initSearch"},
        rowNum: 10000,
        </#if>
        <#if (supportGroup?exists&&supportGroup)>
        grouping:${supportGroup},
        groupingView : ${queryView.groupSetting},
        </#if>
        caption: "${queryView.name}"
    }).closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });
}

<#--管理列-->
<#if ( rowButtons?size>0) >
function managerFormatter(cellvalue, options, rowObject){
    <#--输出JSON数组-->
    <#assign idx=0>
    var aryJson=[<#list rowButtons as btn>
                <#if (btn.inRow) >
                    <#if (idx==0)>
                        {url:'${btn.urlPath}',name:"${btn.name}",triggerType:"${btn.triggerType}"}
                    <#else>
                        ,{url:"${btn.urlPath}",name:"${btn.name}",triggerType:"${btn.triggerType}"}
                    </#if>
                    <#assign idx=idx+1>
                </#if>
            </#list>];
    var sb="";
    for(var i=0;i<aryJson.length;i++){
        var obj=aryJson[i];
        var url=obj.url;
        var name=obj.name;
        
        
        if(obj.triggerType=="onclick"){
                    if(!url){
              sb+= "<a style='margin-left:5px' href='javaScript:void(0)' onclick='dealNullUrl(1)' >"+name+"</a>";
            }else{
            sb+= "<a style='margin-left:5px' href='javaScript:void(0)' onclick='"+url+"' >"+name+"</a>";
                 }
        }
        else{
                     if(!url){
              sb+= "<a style='margin-left:5px' href='javaScript:void(0)' onclick='dealNullUrl(0)' >"+name+"</a>";
            }else{
            url=replaceUrl(url,rowObject);
            if(!url.startWith("http")&&!url.startWith("https")){
                url =__ctx + url;
            }
            sb+= "<a style='margin-left:5px' href='"+url+"' target='_blank'>"+name+"</a>";
                   }
        }
        
    }
    return sb;
}
</#if>


</script>

    <!-- 顶部查询操作 -->
    <div id="panelTop" class="toolbar-search" >
            <div class="toolbar-head">
                <!-- 顶部按钮 -->
                <div class="buttons">
                    <a class="btn btn-primary  btn-sm fa-search" id="btnSearch" href="javaScript:void(0)">
                        <span>搜索</span>
                    </a>
                    <a href="javaScript:void(0)" class="btn btn-sm btn-primary fa-rotate-left">
                        <span>重置</span>
                    </a>
                    <#if (navButtons?exists && navButtons?size>0) >
                    <#list navButtons as btn>
                    <a class="btn btn-primary  btn-sm fa-save" <#if btn.triggerType=="href"><#if btn.urlPath!="">href="${btn.urlPath}"</#if> <#if btn.urlPath=="">onclick="dealNullUrl(0)"</#if></#if> <#if btn.triggerType=="onclick"><#if btn.urlPath!="">onclick="${btn.urlPath}"</#if><#if btn.urlPath=="">onclick="dealNullUrl(1)"</#if></#if> >
                        <span>${btn.name}</span>
                    </a>
                    </#list>
                    </#if>
                </div>
            
            </div>
            <!-- 查询字段 -->
            <div class="toolbar-body">
                <form id="searchForm" class="search-form">
                    <ul>
                        <#list conditions as con >
                            <#if (con.hidden!=1&&con.valueFrom==1) >
                                <@genCondition con/>
                            </#if>
                        </#list>
                    </ul>
                </form>
            </div>
    </div>
    <div style="margin:0px auto;width:99.95%;text-align:center;">
        <table id="gridList"></table>
        <#if (queryView.needPage==1)>
        <div id="pagerNav"></div>
        </#if>
    </div>




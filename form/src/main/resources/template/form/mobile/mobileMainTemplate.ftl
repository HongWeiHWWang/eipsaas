<#setting number_format="0">
${util.getIncludeFiles(includeFiles)}
<script type="text/javascript">
	var designType = "drag";
</script>
<div class="ibox">
    <div class="ibox-title Dtitle_02">
    	${formDesc}
    </div>
    <div class="ibox-content mainDetail">
        <#list fieldList as field>
        	<#if field.ctrlType=='sub'>
            	<div class="${field.widthClass}">
            		<@subTable field=field/>
            	</div>
        	<#elseif field.ctrlType=="fileupload">
				<div class="detail_list clearfix ${field.widthClass}" ng-class="{'must':permission.fields.${field.tableName}.${field.name}=='b'}" ${util.getPermissionNgif(field)} >
					<@input field=field isSub=false ganged=ganged/>
				</div>
            <!--文本控件-->
            <#elseif field.ctrlType=='text'>
                <div><font style="font-weight:${util.getStyleBold(field,'boldText')};${util.getStyles(field,'color','textColor')};${util.getStyles(field,'font-size','textSize')}">${field.desc}</font></div>
			<#elseif field.ctrlType=="selector">
				<div class="detail_list clearfix ${field.widthClass}" ng-class="{'must':permission.fields.${field.tableName}.${field.name}=='b'}" ${util.getPermissionNgif(field)}>
					<@input field=field isSub=false ganged=ganged/>
				</div>
            <#elseif field.ctrlType=="relFlow">
				<div class="detail_list clearfix ${field.widthClass}" ng-class="{'must':permission.fields.${field.tableName}.${field.name}=='b'}" ${util.getPermissionNgif(field)}>
					<@input field=field isSub=false ganged=ganged/>
                </div>
			<#else>
            	<div class="detail_list clearfix ${field.widthClass}" ng-class="{'must':permission.fields.${field.tableName}.${field.name}=='b'}" ${util.getPermissionNgif(field)}>
					<div class="detail_list_tit">
						${field.desc}
					</div>
					<div class="detail_list_cont">
						<@input field=field isSub=false ganged=ganged/>
					</div>
				</div>
			</#if>
		</#list>
		<div class="botbg"></div>
    </div>
</div>

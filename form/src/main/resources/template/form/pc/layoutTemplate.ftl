<#-- type: 表类型，1：主表； 2：子表； 3：孙表 -->
<#function getFormItem field type>

	<#assign rtn>
		<#if field.ctrlType!='iframe'>
			<#if field.noTitle>
				<@input field=field type=type />
			<#else>
				<#if  !field.options.hideCtrl &&field.ctrlType!=''>
					<ht-form-item <#if !field.options.noBindModel> v-if="${getPermission(field,false)}!='n' && data.${field.boDefAlias}" </#if>
						<#if (!field.options.hideLabel)&&(field.options.labelstyleWidth??)>label-width="${field.options.labelstyleWidth}"</#if>
					>
						<#if !field.options.hideLabel &&field.ctrlType!=''>
							<template slot="label">
								<#if field.options.tip?default("")?trim?length gt 1>
									<el-tooltip placement="top">
										<div slot="content" >${field.options.tip}</div>
										<i class="el-icon-question" />
									</el-tooltip>
								</#if>
								<span style="${util.getFieldStyle(field.options)}">${field.desc}</span>
							</template>
						</#if>
						<@input field=field type=type />
					</ht-form-item>
				</#if>
			</#if>
		<#else>
			<iframe
					src="${field.options.iframeSrc}"
					height="${field.options.iframeSrcHeight}"
					width="${field.options.iframeSrcWidth}"
					style="border-color:${field.options.lableColor}"
					frameborder="${field.options.frameborder}" ></iframe>
		</#if>
	</#assign>
	<#return rtn>
</#function>
<#macro getLayout layout>
<#if layout.ctrlType=='grid'>
	<el-row :gutter="${layout.options.gutter}" justify="${layout.options.justify}" align="${layout.options.align}" type="flex">
		<#list layout.columns as group>
			<el-col :span="${group.span}" style="${group.style}">
				<#list group.list as field>
					${getFormItem(field,1)}
				</#list>
			</el-col>
		</#list>
	</el-row>
<#elseif  layout.ctrlType=='tab'>
		<el-tabs value="${layout.key}0" tab-position="${layout.options.align}" <#if layout.options.nextCheck?if_exists>:before-leave='leaveTabVerify'</#if> type="${layout.options.type}">
			<#list layout.columns as group>
				<el-tab-pane label="${group.span}" name="${layout.key}${group_index}" ref="${layout.key}${group_index}">
					<template slot="label">
						<#if group.span?length gt 10>
							<el-tooltip class="item" effect="dark" content="${group.span}" placement="top-start">
								<a>${group.span?substring(0,10)}</a>
							</el-tooltip>
						</#if>
						<#if group.span?length lt 10>
							<span>${group.span}</span>
						</#if>
					</template>
					<#list group.list as field>
						<#if field.ctrlType=='grid'>
							<el-row type="flex" :gutter="${field.options.gutter}" justify="${field.options.justify}" align="${field.options.align}">
								<#list field.columns as gridGroup>
									<el-col :span="${gridGroup.span}" style="${gridGroup.style}">
										<#list gridGroup.list as gridField>
											${getFormItem(gridField,1)}
										</#list>
									</el-col>
								</#list>
							</el-row>
						<#elseif field.ctrlType=='subtable'>
							<@subTable layout=field />
						<#elseif field.ctrlType=='subDiv'>
							<@subDiv layout=field />
						<#elseif field.ctrlType=='hottable'>
							<@hotTable layout=field />
						<#elseif field.ctrlType=='dataView'>
						<eip-data-view
							templateKey="${field.templateKey}"
							options='${util.objectToJsonString(field.options)}'
						>
						</eip-data-view>
						<#else>
							<el-row>
								${getFormItem(field,1)}
							</el-row>
						</#if>
					</#list>
				</el-tab-pane>
			</#list>
		</el-tabs>
<#elseif layout.ctrlType=='accordion'>
	<eip-collapse :openDefault='${util.objectToJsonString(layout.options.activeNames)}' <#if layout.options.nextCheck?if_exists>:isVerify=${layout.options.nextCheck}</#if> >
		 <template>
		<#list layout.columns as group>
			<el-collapse-item title="${group.span}" name="${group.idKey}" ref="${group.idKey}">
				<#list group.list as field>
					<#if field.ctrlType=='grid'>
						<el-row type="flex" :gutter="${field.options.gutter}" justify="${field.options.justify}" align="${field.options.align}">
							<#list field.columns as gridGroup>
								<el-col :span="${gridGroup.span}" style="${gridGroup.style}">
									<#list gridGroup.list as gridField>
										${getFormItem(gridField,1)}
									</#list>
								</el-col>
							</#list>
						</el-row>
					<#elseif field.ctrlType=='subtable'>
							<@subTable layout=field />
					<#elseif field.ctrlType=='subDiv'>
						<@subDiv layout=field />
					<#elseif field.ctrlType=='hottable'>
						<@hotTable layout=field />
					<#elseif field.ctrlType=='tab'>
						<el-tabs value="${field.key}0" tab-position="${field.options.align}" <#if field.options.nextCheck?if_exists>:before-leave='leaveTabVerify'</#if> type="${field.options.type}">
							<#list field.columns as group>
								<el-tab-pane label="${group.span}" name="${layout.key}${group_index}" ref="${field.key}${group_index}">
									<template slot="label">
										<#if group.span?length gt 10>
											<el-tooltip class="item" effect="dark" content="${group.span}" placement="top-start">
												<a>${group.span?substring(0,10)}</a>
											</el-tooltip>
										</#if>
										<#if group.span?length lt 10>
											<span>${group.span}</span>
										</#if>
									</template>
									<#list group.list as gfield>
										<#if gfield.ctrlType=='grid'>
											<el-row type="flex" :gutter="${gfield.options.gutter}" justify="${gfield.options.justify}" align="${gfield.options.align}">
												<#list gfield.columns as gridGroup>
													<el-col :span="${gridGroup.span}" style="${gridGroup.style}">
														<#list gridGroup.list as gridField>
															${getFormItem(gridField,1)}
														</#list>
													</el-col>
												</#list>
											</el-row>
										<#elseif gfield.ctrlType=='subtable'>
											<@subTable layout=gfield />
										<#elseif gfield.ctrlType=='subDiv'>
											<@subDiv layout=gfield />
										<#elseif gfield.ctrlType=='hottable'>
											<@hotTable layout=gfield />
										<#elseif gfield.ctrlType=='dataView'>
										<eip-data-view
											templateKey="${gfield.templateKey}"
											options='${util.objectToJsonString(gfield.options)}'
										>
										</eip-data-view>
										<#else>
											<el-row>
												${getFormItem(gfield,1)}
											</el-row>
										</#if>
									</#list>
								</el-tab-pane>
							</#list>
						</el-tabs>
					<#elseif field.ctrlType=='dataView'>
						<eip-data-view
							templateKey="${field.templateKey}"
							options='${util.objectToJsonString(field.options)}'
						>
						</eip-data-view>
					<#else>
						<el-row>
							${getFormItem(field,1)}
						</el-row>
					</#if>
				</#list>
			</el-collapse-item>
		</#list>
		 </template>
	</eip-collapse>
<#elseif layout.ctrlType=='subtable'>
<@subTable layout=layout />
<#elseif layout.ctrlType=='subDiv'>
<@subDiv layout=layout />
<#elseif layout.ctrlType=='page'>
	</template>
	</eip-pagination-layout>
	<eip-pagination-layout :pageIndex=${pageIndex}>
	<template>
	<#assign pageIndex = pageIndex+1>
<#elseif layout.ctrlType=='pageSteps'>

	<eip-pagination-steps :columns='${util.objectToJsonString(layout.pageStepsArr)}' :isShow=${layout.isShow}>
	</eip-pagination-steps>
	<eip-pagination-layout :pageIndex=${pageIndex}>
	<template>
	<#assign pageIndex = pageIndex+1>
<#elseif layout.ctrlType=='pageButton'>
	</template>
	</eip-pagination-layout>
	<eip-pagination :pageSize=${expandMap.pageSize} :nextButton='${util.objectToJsonString(layout.nextButton)}' :backButton='${util.objectToJsonString(layout.backButton)}'></eip-pagination>
<#elseif layout.ctrlType=='hottable'>
	<@hotTable layout=layout />
<#elseif layout.ctrlType=='dataView'>
	<eip-data-view
		templateKey="${layout.templateKey}"
		options='${util.objectToJsonString(layout.options)}'
	>
	</eip-data-view>
<#else>
<el-row>
	<el-col :span="24">
		${getFormItem(layout,1)}
	</el-col>
</el-row>


</#if>
</#macro>


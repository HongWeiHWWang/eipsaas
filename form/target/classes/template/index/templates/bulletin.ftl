<div class="widget-box border " >
	<div class="widget-header">
		<h4 class="widget-title"><i class="ht-icon fa fa-bars"></i>${model.title}</h4>
		<div class="widget-toolbar">
			
			<a href="javascript:void(0);" data-action="reload" data-placement="bottom" title="刷新">
				<i class="ht-icon fa fa-refresh"></i>
			</a>
			<a href="javascript:void(0);" data-action="collapse"  data-placement="bottom" title="折叠">
				<i class="ht-icon fa fa-chevron-up"></i>
			</a>
		</div>
	</div>
	<div class="widget-body">
		<div class="widget-scroller" data-height="${model.height}px">
			<ul class="widget-list list-unstyled">
				<li class="clearfix">
					<div class=" pull-left">
						<h4><strong>主题</strong></h4>
					</div>
					<div class=" pull-right">
						<h4><strong> 创建人 </strong></h4>
					</div>
				</li>
			</ul>
			
			<ul class="widget-list list-unstyled">
					<#list data as data>
               <li class="clearfix" onclick="javascript:jQuery.openFullWindow('${ctx}/platform/system/sysBulletin/get.ht?id=${data.id}')">
					<div class="pull-left">
							<h5><strong>${data.subject}</strong></h5>
					</div>
					<div class="text-right pull-right">
						<span class="label label-success arrow-in-right">${data.creator}</span>
					</div>
			   </li>

				</#list>
			</ul>
		</div>
	</div>
</div>
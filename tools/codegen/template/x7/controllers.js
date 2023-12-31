<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign system=vars.system>
<#assign comment=model.tabComment>
<#assign subtables=model.subTableList>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign pkType=func.getPkType(model)>
//  ${comment} 列表
function ${classVar}ListCtrl($scope, $compile,  baseService,dialogService) {
	
	$scope.operating = function(id,action){
		if(action == 'get'){
			dialogService.sidebar("${classVar}Get", {bodyClose: false, width: 'calc(100% - 225px)', pageParam: {id:id, title:'查看${comment}',action:action}});
		}else{
			var title = action == "add" ? "添加${comment}" : "编辑${comment}";
			dialogService.sidebar("${classVar}Edit", {bodyClose: false, width: 'calc(100% - 225px)', pageParam: {id:id, title:title,action:action}});
			$scope.$on('sidebar:close', function(){//添加监听事件,监听子页面是否关闭
		         $scope.dataTable.query();//子页面关闭,父页面数据刷新
		     });
		}
	}
	
}

//  ${comment} 编辑明细
function ${classVar}EditCtrl($scope, $compile, baseService,dialogService){
	//关闭
	$scope.close = function(){
		dialogService.closeSidebar();
	}
	$scope.title = $scope.pageParam.title;
	
	$scope.data = {
	}
	
	if($scope.pageParam && $scope.pageParam.id){
		baseService
		.get("<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/get/"+$scope.pageParam.id)
		.then(function(response) {
			$scope.data = response;
		});	
	}
	
	//保存
	$scope.save = function(){
			baseService
			.post("<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/save",$scope.data)
			.then(function(response) {
				if(!response.state){
					dialogService.fail(response.message);
				}else{
					dialogService.success(response.message);
					$scope.close();
				}
			});	
	}
}

/**
 *
 * Pass all functions into module
 */
angular.module('${classVar}', [])
	.controller('${classVar}EditCtrl', ${classVar}EditCtrl)
	.controller('${classVar}ListCtrl', ${classVar}ListCtrl)


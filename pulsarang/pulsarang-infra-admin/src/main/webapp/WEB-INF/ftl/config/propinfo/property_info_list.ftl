<#include "/WEB-INF/ftl/common.ftl">

<script type="text/javascript">
	function removeConfigProperty(configCategory, propertyName) {
		if(!confirm("'" + propertyName + "'을(를) 삭제하시겠습니까?")) {
			return;
		}
		
		var url = "/config/propertyInfo/remove";
		url += "/" + configCategory;
		url += "/" + propertyName;
		
		window.location = url;
	}
	
	function changeConfigCategory() {
		var form = $Form('changeRangeForm');
		
		var url = "/config/propertyInfo/list";
		url += "/" + form.value('configCategory');
		
		window.location = url;
	}
</script>

<div class="selectTargetSection">
	<form id='changeRangeForm' name='changeRangeForm' method='post' action='/config/propertyInfo/list'>
		<@selectCell name='configCategory' values=configCategoryNames prepends=[['', '전체']] selectedValue=configCategory onChange="changeConfigCategory();return false;"/>
	</form>
</div>


<div class="linkSection">
	<a href='/config/propertyInfo/create?id.configCategory=${configCategory!''}'>create</a>
	<#--
	| <a href='/config/propertyInfo?m=expireAll&${sectionInfo}?id.configCategory=${configCategory}'>expire all</a>
	| <a href='/config/config.nhn?m=list&configCategory=${configCategory}'>config list</a>
	-->
</div>

<div class="bodySection">
	<table>
		<tr>
			<th>configCategory</th>
			<th>name</th>
			<th>required</th>
			<th>modifiable</th>
			<th>data_type</th>
			<th>default_value</th>
			<th>description</th>
			<th>category</th>
			<th>validation_expression</th>
			<th>delete</th>
		</tr>
	<#list configPropertyInfos as configPropertyInfo>
		<tr>
			<td>${configPropertyInfo.id.configCategory}</td>
			<td>
				<a href="/config/propertyInfo/modify/${configPropertyInfo.id.configCategory}/${configPropertyInfo.id.propertyName}">${configPropertyInfo.id.propertyName}</a>
			</td>
			<td>${configPropertyInfo.required?string}</td>
			<td>${configPropertyInfo.modifiable?string}</td>
			<td>${configPropertyInfo.dataTypeCode}</td>
			<td>${configPropertyInfo.defaultValue}</td>
			<td>${configPropertyInfo.description}</td>
			<td>${configPropertyInfo.category!''}</td>
			<td>${configPropertyInfo.validationExpression!''}</td>
	    	<td>
	    		<a href='#' onclick="removeConfigProperty('${configPropertyInfo.id.configCategory}', '${configPropertyInfo.id.propertyName}');return false;">삭제</a>
	    	</td>
	    </tr> 
	</#list>
	</table>
</div>

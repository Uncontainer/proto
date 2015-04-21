<#include "/WEB-INF/ftl/common.ftl">

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<link rel="stylesheet" href="/css/default.css" type="text/css" />
	
	<script type="text/javascript" language="javascript" charset="utf-8" src="/js/jindo.all.js"></script>
	<script type="text/javascript">
		function configConfigSubmit() {
			var form = $Form('createForm');
			form.submit();
		}
	</script>
</head>

<body>
	<@validateMode />
	
	<div class="linkSection">
		<a href='/config/config_config_item_project_info.nhn?m=list&name=${name}&solution_name=${solution_name}&project_name=${selected_project_name!''}&range=${range!''}'>list</a>
	</div>
	
	<form id='createForm' name='createForm' method='post' action='/config/config_config_item_project_info.nhn'>
		<input type='hidden' name='m' value='${mode}Submit'/>
		<input type='hidden' name='name' value='${name}'/>
		<input type='hidden' name='solution_name' value='${solution_name}'/>
		<input type='hidden' name='range' value='${range!''}'/>
		
		<#if mode != 'read'>
			<div class="headSection">
				<a href='javascript:configConfigSubmit()'>${mode}</a>
			</div>
		</#if>
		
		<div class="bodySection">
			<table>
				<tr>
					<th>name</th>
					<th>value</th>
				</tr>
				<tr>
					<td>name</td>
					<td>${name}</td>
				</tr>
				<tr>
					<td>project_name</td>
					<td><@selectCell name='project_name' prepends=[['__all__', '전체']] values=projectNames selectedValue=(config_item_project_info['project_name'])!'' /></td>
				</tr>
			</table>
		</div>
		
		<#if mode != 'read'>
			<div class="headSection">
				<input type='submit' value="${mode}">
			</div>
		</#if>
	</form>

</body>

</html>

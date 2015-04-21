<#include "/WEB-INF/ftl/common.ftl">

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="/css/default.css" type="text/css" />
	
	<script type="text/javascript" language="javascript" charset="utf-8" src="/js/jindo.all.js"></script>
	<script type="text/javascript">
		function deleteConfigItemAuthInfo(name, projectName) {
			if(!confirm("'" + name + "." + projectName + "'을(를) 삭제하시겠습니까?")) {
				return;
			}
			
			var url = "/config/config_config_item_project_info.nhn?m=remove";
			url += "&solution_name=${solution_name}";
			url += "&project_name=" + projectName;
			url += "&range=${range!''}";
			url += "&name=" + name;
			
			window.location = url;
		}
	</script>
</head>
<body>
	<div class="linkSection">
		<a href='/config/config_config_item_info.nhn?m=modify&name=${name}&solution_name=${solution_name}&project_name=${project_name!''}&range=${range!''}'>list</a>
		| <a href='/config/config_config_item_project_info.nhn?m=create&name=${name}&solution_name=${solution_name}&selected_project_name=${project_name!''}&range=${range!''}'>create</a>
	</div>
	
	<div class="bodySection">
		<#assign fields=["name", "project_name"]>
		<table>
			<tr>
				<#list fields as field>
				<th>${field}</th>
				</#list>
				<th>delete</th>
			</tr>
			
		<#list config_item_project_infos as info>
			<tr>
			<#list fields as field>
				<td>
					${info[field]!''}
				</td>
		    </#list>
		    	<td>
		    		<a href='#' onclick="deleteConfigItemAuthInfo('${info['name']}', '${info['project_name']}');return false;">삭제</a>
		    	</td>
		    </tr> 
		</#list>
		</table>
	</div>
</body>

</html>

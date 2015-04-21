<#include "/WEB-INF/ftl/common.ftl">

<#assign configKey=configCategory + "/" + configName>

<script type="text/javascript">
	function removeConfig() {
		if(!confirm("'${configName}'을(를) 삭제하시겠습니까?")) {
			return;
		}
		
		var form = $Form('removeForm');
		form.submit();
	}
	
	function changeProject() {
		var form = $Form('changeForm');
		
		var url = "/config/read/${configKey}?solutionName=${solutionName}";
		url += "&projectName=" + form.value('projectName');
		
		window.location = url;
	}
	
	function changeServer() {
		var form = $Form('changeForm');
		
		var url = "/config/read/${configKey}?solutionName=${solutionName}";
		url += "&projectName=" + form.value('projectName');
		url += "&serverName=" + form.value('serverName');
		
		window.location = url;
	}
</script>

<form id='removeForm' name='removeForm' method='post' action='/config/remove/${configKey}'>
</form>

<#if projectNames?has_content && projectNames?size &gt; 0>
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm'>
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="changeProject();return false;"/>
			<#assign serverNames=serverNames>
			<#if serverNames?has_content && serverNames?size &gt; 0> 
				<@selectCell name='serverName' values=serverNames selectedValue=serverName onChange="changeServer();return false;"/>
			</#if>
		</form>
	</div>
</#if>

<div class="linkSection">
	<a href='/config/list<#--/${configCategory}-->'>list</a>
	 | <a href='/config/refresh/${configKey}'>refresh</a>
	 | <a href='/config/configInfo/modify/${configKey}'>modify</a>
	 | <a href='#' onclick='removeConfig();return false;'>delete</a>
</div>

<div class="bodySection"> 
	<table>
		<tr>
			<th style="width:150px">name</th>
			<th>db</th>
			<th>app</th>
		</tr>
	
	<#list configOptions as info>
		<tr>
			<td>${info['name']}</td>
			<td><@tableCell info=info key='db'/></td>
			<td><@tableCell info=info key='app'/></td>
	    </tr> 
	</#list>
	</table>
</div>

<#macro tableCell info key>
	<#if !info[key]?exists>
		<#assign value=''>
	<#elseif info[key]?is_date>
		<#assign value = info[key]?string("yyyy-MM-dd HH:mm:ss")>
	<#else>
		<#assign value = info[key]?string>
	</#if>
	
	<#if value == ''>
		<i style="color:gray">null</i>
	<#else>
		${value}
	</#if>
</#macro>

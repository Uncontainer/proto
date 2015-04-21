<#include "/WEB-INF/ftl/common.ftl">

<script type="text/javascript" language="javascript" charset="utf-8">
	function changeSolution() {
		var form = $Form('changeSolutionForm');
		
		var url = "/config/list/" + form.value('solutionName');
		if(form.value('include_stop')) {
			url += "?include_stop=true";
		}
		
		window.location = url;
	}
</script>

<#if solutionNames?has_content && solutionNames?size &gt; 1>
	<div class="selectTargetSection">
		<form id='changeSolutionForm' name='changeSolutionForm'>
			<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			&nbsp;include stop: <input type="checkbox" id="include_stop" name="include_stop" value="true" />
		</form>
	</div>
</#if>

<div class="linkSection">
	<a href='/config/configInfo/create'>create</a>
	| <a href='/config/config/expireAll?solutionName=${solutionName}'>expire all</a>
</div>

<div class="bodySection">
<table>
	<tr>
		<th>name</th>
		<th>category</th>
		<th>description</th>
		<th>abstract</th>
		<th>status</th>
		<th>super_config_ids</th>
		<th>expire</th>
		<th>refresh</th>
	</tr>
	
<#list configInfos as info>
	<#assign configKey=info.id.category + "/" + info.id.name/>
	<tr>
		<td><a href="/config/read/${configKey}">${info.id.name}</a></td>
		<td>${info.id.category}</td>
		<td>${info.description}</td>
		<td>${info.abstract?string}</td>
		<td>${info.status}</td>
		<td>${info.superConfigNames!''}</td>
    	<td><a href="/config/expire/${configKey}">expire</a></td>
    	<td><a href="/config/refresh/${configKey}">갱신</a></td>
    </tr> 
</#list>
</table>
</div>

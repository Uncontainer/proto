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
	<a href='/config/category/create'>create</a>
</div>

<div class="bodySection">
<table>
	<tr>
		<th style="width:100px">name</th>
		<th style="width:50px">scope</th>
		<th style="width:150px">super categories</th>
		<th style="width:100px">operation</th>
		<th>&nbsp;</th>
	</tr>
	
<#list configCategories as info>
	<tr>
		<td><a href="/config/category/read/${info.name}">${info.name}</a></td>
		<td>${info.scopeTypeCode}</td>
		<td>${info.superCategoryNames!''}</td>
		<td>
			<a href="/config/category/modify/${info.name}">수정</a>
			| <a href="/config/category/remove/${info.name}">삭제</a>
		</td>
		<td>&nbsp;</td>
    </tr> 
</#list>
</table>
</div>

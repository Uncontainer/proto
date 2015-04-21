<#include "/WEB-INF/ftl/common.ftl">
<@validateMode />

<script type="text/javascript">
	function configInfoSubmit() {
		var form = $Form('configInfoForm');
		form.submit();
	}
</script>

<div class="linkSection">
	<a href="/config/list?solutionName=${solutionName!''}">list</a>
	<#if mode == "read">
	 | <a href="/config/configInfo/${configInfo.id.category}/${configInfo.id.name}">modify</a>
	</#if>
</div>

<#if mode != "read">
<form id="configInfoForm" name="configInfoForm" method="post" action="/config/configInfo/${mode}">
	<div class="headSection">
		<a href="javascript:configInfoSubmit()">${mode}</a>
		<#if mode == "modify">
			| <a href="/config/property/modify/${configInfo.id.category}/${configInfo.id.name}">next</a>
		</#if>
	</div>
</#if>
	<div class="bodySection">
		<table>
			<tr>
				<th style="width:150px">이름</th>
				<th>속성</th>
			</tr>
			<tr>
				<td>name</td>
				<td><@inputCell name="id.name" value=(configInfo.id.name)!'' modifiable=(mode=="create")/></td>
		    </tr> 
		    <tr>
				<td>category</td>
				<td><@selectCell name="id.category" values=configCategoryNames selectedValue=(configInfo.id.category)!'' modifiable=(mode=="create")/></td>
		    </tr>
		    <tr>
				<td>desctiption</td>
				<td><@inputCell name="description" value=(configInfo.description)!''/></td>
		    </tr>
		    <tr>
				<td>abstract</td>
				<td><@boolCell name="abstract" value=(configInfo.abstract)!false modifiable=(mode!="read") /></td>
		    </tr>
		    <tr>
				<td>status</td>
				<td><@inputCell name="status" value=(configInfo.status)!'1' modifiable=false/></td>
		    </tr>
		    <tr>
				<td>super_config_ids</td>
				<td><@inputCell name="superConfigNames" value=(configInfo.superConfigNames)!'' modifiable=false/></td>
		    </tr>
		</table>
	</div>
<#if mode != 'read'>
	<div class="tailSection">
		<input type='submit' value="${mode}">
	</div>
</form>
</#if>

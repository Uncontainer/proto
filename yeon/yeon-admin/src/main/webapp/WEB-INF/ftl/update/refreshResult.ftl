<h3>
<#if success!false>
	Success!
<#else>
	Fail!
</#if>
</h3>

<span>${(result?size)!0} servers </span>
<table border='1'>
	<tr>
		<th style="width:100px">project</th>
		<th style="width:100px">server</th>
		<th style="width:100px">result</th>
		<th>error_message</th>
	</tr>
	
	<#list result as resultItem>
	<tr>
		<td>${(resultItem.server.projectName)!''}</td>
		<td>${(resultItem.server.name)!''}</td>
		<td>
		<#if (resultItem.success)!false>
			<span style="color: green;">OK</span>
		<#else>
			<span style="color: red;">FAIL</span>
		</#if>
		</td>
		<td>${resultItem['errorMessage']!''?replace("\n", "<br>")}</td>
    </tr> 
    </#list>
</table>

<a href="${callbackUrl}">확인</a>

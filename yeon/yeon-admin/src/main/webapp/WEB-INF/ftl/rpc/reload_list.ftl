<#include "/WEB-INF/ftl/common.ftl">

<#--
<div class="selectTargetSection">
<#assign server_names=serverNames>
<#if server_names?has_content && server_names?size &gt; 0>
	<form id='selectServerForm' name='selectServerForm'>
		<#if server_names?has_content && server_names?size &gt; 0> 
			<@selectCell name='server_name' values=server_names selectedValue=server_name onChange='changeServer()'/>
		</#if>
	</form>
</#if>
</div>
-->
<div class="tbl_type1">
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm' method='get' action='${table.refreshUrl}'>
			<#if solutionNames?has_content && solutionNames?size &gt; 1>
				<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			<#else>
				<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
			</#if>
		
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="submit();return false;"/>
		</form>
	</div>
	
	<#if table.links?has_content>
		<div class="linkSection">
			<#list table.links as link>
				<#if link_index != 0>| </#if> 
				<a href='${link.url}'>${link.name}</a>
			</#list>
		</div>
	</#if>
	
	<div class="bodySection">
		<table>
			<#assign columns = table.columns>
			
			<thead>
				<tr>
					<th style="width:50px">no</th>
					<#list columns as column>
					<th style="${column.headerCellStyle}">${column.name}</th>
					</#list>
					<th>&nbsp;</th>
				</tr>
			</thead>
			
			<tbody>
				<#list table.rows as row>
					<tr>
						<td style="text-align:center;">${row_index+1}</td>
						<#list row as cell>
							<td style="${columns[cell_index].bodyCellStyle}">${cell!''}</td>
						</#list>
						<td>&nbsp;</td>
				    </tr> 
				</#list>
			</tbody>
		</table>
	</div>
</div>

<#if errorMessage?has_content>
<div style="margin-top:10px">
	<span style="color: red;">Error: ${errorMessage}</span>
</div>
</#if>

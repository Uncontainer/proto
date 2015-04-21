<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm' method='get' action='/monitor/resource/property'>
			<#if solutionNames?has_content && solutionNames?size &gt; 1>
				<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			<#else>
				<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
			</#if>
		
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="submit();return false;"/>
			
			<#if serverNames?has_content && serverNames?size &gt; 0>
				<@selectCell name='serverName' prepends=["NONE"] values=serverNames selectedValue=serverName onChange='submit()'/>
			</#if>
			
			<@selectCell name="propertyPath" values=propertyPaths selectedValue=propertyPath onChange="submit();return false;" />
			
			<input type='submit' value='refresh' />
		</form>
	</div>
	
	<div class="linkSection">
	</div>
	
	<div class="bodySection">
		<table>
			<thead>
				<tr>
					<#list columnNames as columnName>
						<th>${columnName}</th>
					</#list>
				</tr>
			</thead>
			<#if rows?has_content>
				<tbody>
					<#list rows as row>
						<tr>
							<#list row as item>
								<td style="text-align:left">${item?string}</td>
							</#list>
					    </tr> 
					</#list>
				</tbody>
			</#if>
		</table>
	</div>
</div>

<br/>
<br/>
<p style="color:red">${message!''}</p>

<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm' method='get' action='/monitor/logger/list'>
			<#if solutionNames?has_content && solutionNames?size &gt; 1>
				<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			<#else>
				<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
			</#if>
		
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="submit();return false;"/>
			
			<#if serverNames?has_content && serverNames?size &gt; 0>
				<@selectCell name='serverName' prepends=["NONE"] values=serverNames selectedValue=serverName onChange='submit()'/>
			</#if>
		</form>
	</div>
	
	<div class="linkSection">
	</div>
	
	<div class="bodySection">
		<table>
			<thead>
				<tr>
					<th>name</th>
					<th>level</th>
				</tr>
			</thead>
			<tbody>
				<#list loggerInfos as info>
					<tr>
						<td style="text-align:left">
							${info.name}
						</td>
				    	<td>
				    		<form action="/monitor/logger/level" method='post'>
				    			<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
				    			<input type='hidden' id='projectName' name='projectName' value='${projectName}'/>
				    			<input type='hidden' id='serverName' name='serverName' value='${serverName}'/>
				    			<input type='hidden' name='name' value='${info.name}'/>
				    			<@selectCell name='level' values=levels selectedValue=info.level onChange="submit();return false;"/>
				    		</form>
				    	</td>
				    </tr> 
				</#list>
				<tr>
					<form action="/monitor/logger/level">
						<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
				    	<input type='hidden' id='projectName' name='projectName' value='${projectName}'/>
				    	<input type='hidden' id='serverName' name='serverName' value='${serverName}'/>
						<td style="text-align:left">
							<input type='text' name='name' value='com.pulsarang.' size='50'/>
							<input type='submit' value="add"/>
						</td>
						<td>
							<@selectCell name='level' values=levels selectedValue='INFO'/>
						</td>
					</form>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<#if errorMessage?has_content>
<div style="margin-top:10px">
	<span style="color: red;">Error: ${errorMessage}</span>
</div>
</#if>
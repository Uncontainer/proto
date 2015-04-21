<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm' method='get' action='/rpc/list'>
			<#if solutionNames?has_content && solutionNames?size &gt; 1>
				<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			<#else>
				<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
			</#if>
		
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="submit();return false;"/>
		</form>
	</div>
	
	<div class="linkSection">
	</div>
	
	<div class="bodySection">
		<table>
			<thead>
				<tr>
					<th>name</th>
					<th>last reload date</th>
					<th>expire</th>
					<th>refresh</th>
				</tr>
			</thead>
			<tbody>
				<#list rpcInfos as info>
					<#assign queryString='solutionName=' + solutionName +'&projectName=' + projectName>
					<tr>
						<td>
							<a href='/rpc/reload/list/${info['_target_name']}?${queryString}'>${info['_target_name']}</a>
						</td>
						<td>
							${info['last_requested_date']}
						</td>
				    	<td>
			    			<a href='/rpc/reload/expire/${info['_target_name']}?${queryString}'>expire</a>
				    	</td>
				    	<td>
			    			<a href='/rpc/reload/refresh/${info['_target_name']}?${queryString}'>갱신</a>
				    	</td>
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
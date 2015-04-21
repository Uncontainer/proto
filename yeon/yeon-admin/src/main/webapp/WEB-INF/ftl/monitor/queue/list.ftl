<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<div class="selectTargetSection">
		<form id='changeForm' name='changeForm' method='get' action='/monitor/queue/list'>
			<#if solutionNames?has_content && solutionNames?size &gt; 1>
				<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="changeSolution();return false;"/>
			<#else>
				<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
			</#if>
		
			<@selectCell name='projectName' values=projectNames selectedValue=projectName onChange="submit();return false;"/>
			<#if !summarize && serverNames?has_content && serverNames?size &gt; 0>
				<@selectCell name='serverName' prepends=["NONE"] values=serverNames selectedValue=serverName onChange='submit()'/>
			</#if>
			&nbsp; summarize: <input type="checkbox" name="summarize" value="true" <#if summarize!false>checked="checked"</#if> onChange="submit();return false;" />
		</form>
	</div>
	
	<div class="linkSection">
	</div>
	
	<div class="bodySection">
		<table>
			<thead>
				<tr style="margin-bottom:0px;padding-bottom:0px">
					<th rowSpan="2" style="width:250px;margin-bottom:0px;padding-bottom:0px">id</th>
					<th colspan="2" style="width:100px;margin-bottom:0px;padding-bottom:0px">synchronization</th>
					<th colspan="2" style="width:240px;margin-bottom:0px;padding-bottom:0px">last modified</th>
					<th colspan="3" style="width:150px;margin-bottom:0px;padding-bottom:0px">processor</th>
					<th rowSpan="2" style="margin-bottom:0px;padding-bottom:0px">processing events</th>
				</tr>
				<tr>
					<th style="margin-top:0px;padding-top:0px;">success</th>
					<th style="margin-top:0px;padding-top:0px">fail</th>
					<th style="margin-top:0px;padding-top:0px">push</th>
					<th style="margin-top:0px;padding-top:0px">pop</th>
					<th style="margin-top:0px;padding-top:0px">active</th>
					<th style="margin-top:0px;padding-top:0px">execute</th>
					<th style="margin-top:0px;padding-top:0px">error</th>
				</tr>
			</thead>
			<tbody>
				<#list table as row>
					<tr>
						<td>${row.queueId!''}</td>
						<td>${row.syncSuccessCount!''}</td>
						<td>${row.syncFailCount!''}</td>
						<td>${(row.lastPushTime?string('yyyy-MM-dd HH:mm:ss'))!''}</td>
						<td>${(row.lastPopTime?string('yyyy-MM-dd HH:mm:ss'))!''}</td>
						<td>${(row.activeCount)!''}</td>
						<td>${(row.executeCount)!''}</td>
						<td>${(row.failCount)!''}</td>
						<td>${(row.processEvents)!''}</td>
				    </tr> 
				</#list>
			</tbody>
		</table>
	</div>
</div>

<#if errorMessage?has_content>
<div style="margin-top:10px">
	<span style="color: red;">Error: ${errorMessage?replace("\n", "<br>")}</span>
</div>
</#if>
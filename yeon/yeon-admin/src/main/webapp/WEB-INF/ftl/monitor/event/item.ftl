<#include "/WEB-INF/ftl/common.ftl">

<div>
	<a href="/monitor/event/searchByDate">목록</a>
</div>
<div class="tbl_type1">
	<div class="bodySection">
		<table>
			<thead>
				<tr>
					<th style="width:100px">name</th>
					<th>value</th>
				</tr>
			</thead>
			<tbody>	
				<tr>
					<th>id</th>
					<td>${event['id']!''}</td>
			    </tr> 
			    <tr>
			    	<th>target</th>
					<td>${event['target']!''}</td>
			    </tr>
			    <tr> 
			    	<th>date</th>
			    	<td>${event['eventDate']?string('yyyy-MM-dd HH:mm:ss')}</td>
			    </tr>
			    <tr> 
			    	<th>class</th>
					<td>${event['eventClassName']!''}</td>
			    </tr>
			    <tr> 
			    	<th>operation</th>
			    	<td>${event['eventOperationName']!''}</td>
			    </tr> 
			    <tr> 
			    	<th>properties</th>
			    	<td>${event['jsonEventOptions']!''}</td>
			    </tr>
			</tbody>
		</table>
	</div>
</div>

<#if eventProcessLogs?has_content>
	<br/><br/><br/>
	<div class="tbl_type2">
		<h3>Event Process Logs</h3>
		<table>
			<thead>
				<tr>
					<th style="width:50px">id</th>
					<th style="width:90px">processor</th>
					<th style="width:70px">status</th>
					<th style="width:70px">IP</th>
					<th style="width:90px">start</th>
					<th style="width:90px">finish</th>
					<th style="width:60px">success</th>
					<th style="">exception</th>
				</tr>
			</thead>
			<tbody>
				<#list eventProcessLogs as info>
					<tr>
						<td>${info['id']}</td>
						<td style="text-align:left">${info['processorName']!''}</td>
						<td>${info['status']!''}</td>
						<td>${info['serverIp']!''}</td>
						<td>${(info['processStartTime']?string('MM-dd HH:mm:ss'))!''}</td>
						<td>${(info['processFinishTime']?string('MM-dd HH:mm:ss'))!''}</td>
						<td>${(info['success']?string)!''}</td>
						<td style="text-align:left">${(info['exception']!'')?replace("\n", "<br>")}</td>
				    </tr> 
				</#list>
			</tbody>
		</table>
	</div>
</#if>

<#if eventProcessStatuses?has_content>
	<br/><br/><br/>
	<div class="tbl_type2">
		<h3>Event Process Statuses</h3>
		<table>
			<thead>
				<tr>
					<th style="width:90px">processor</th>
					<th style="width:70px">status</th>
					<th style="width:90px">enque</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<#list eventProcessStatuses as info>
					<tr>
						<td style="text-align:left">${info.id['processorName']!''}</td>
						<td>${info['status']!''}</td>
						<td>${(info['enqueTime']?string('MM-dd HH:mm:ss'))!''}</td>
						<td>&nbsp;</td>
				    </tr> 
				</#list>
			</tbody>
		</table>
	</div>
</#if>
<#assign linkTarget = (searchBy!='target')!true>
<#assign linkStatus = (searchBy!='status')!true>
<div class="tbl_type1">
	<table>
		<thead>
			<tr>
				<th style="width:50px">id</th>
				<th style="width:100px">target</th>
				<th style="width:80px">ip</th>
				<th style="width:130px">date</th>
				<th style="width:70px">class</th>
				<th style="width:70px">operation</th>
				<th>&nbsp</th>
			</tr>
		</thead>
		<tbody>	
			<#list events as info>
				<tr>
					<td><a href="/monitor/event/item/${info['id']?c}">${info['id']}</a></td>
					<#if linkTarget>
						<td><a href="/monitor/event/searchByTarget/${info['target']!''}">${info['target']!''}</a></td> 
					<#else>
						<td>${info['target']!''}</td>
					</#if>
					<td>${(info['serverIp'])!''}</td>
					<td>${(info['eventDate']?string('yyyy-MM-dd HH:mm:ss'))!''}</td>
					<td>${info['eventClassName']!''}</td>
					<td>${info['eventOperationName']!''}</td>
					<td>&nbsp</td>
			    </tr> 
			</#list>
		</tbody>
	</table>
</div>
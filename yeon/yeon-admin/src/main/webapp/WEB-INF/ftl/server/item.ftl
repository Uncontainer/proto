<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<form id='defalutForm' name='defalutForm' method='post' action='/server/${mode}'>
		<div class="bodySection">
			<#if mode != 'read'>
				<@hiddenCell name="id" value=(server['id'])!'' show=false />
				<input type='submit' value='${mode}'}/>
			</#if>
			<table>
				<thead>
					<tr>
						<th style="width:100px">name</th>
						<th>value</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>regsit status</th>
						<td><@selectCell name="registStatus" values=['WAIT', 'NORMAL', 'IGNORE'] selectedValue=(server['registStatus'])!'WAIT'  modifiable=(mode!='read')/></td>
					</tr>
					<tr>
						<th>solution</th>
						<td><@inputCell name="solutionName" value=(server['solutionName'])!'' modifiable=(mode=='create') /></td>
				    </tr> 
				    <tr>
				    	<th>project</th>
						<td><@inputCell name="projectName" value=(server['projectName'])!'' modifiable=(mode=='create') /></td>
				    </tr>
				    <tr> 
				    	<th>name</th>
				    	<td><@inputCell name="name" value=(server['name'])!'' modifiable=(mode=='create') /></td>
				    </tr>
				    <tr> 
				    	<th>ip</th>
						<td><@inputCell name="ip" value=(server['ip'])!'' modifiable=(mode=='create') /></td>
				    </tr>
				    <tr> 
				    	<th>port</th>
				    	<td><@inputCell name="port" value=(server['port']?c)!11702 modifiable=(mode!='read') /></td>
				    </tr> 
				</tbody>
			</table>
		</div>
	</form>
</div>

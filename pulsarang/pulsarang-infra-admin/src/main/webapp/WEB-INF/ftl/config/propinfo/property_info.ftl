<#include "/WEB-INF/ftl/common.ftl">
<@validateMode />

<script type="text/javascript">
	function configPropertyInfoSubmit() {
		var form = $Form('defalutForm');
		form.submit();
	}
</script>


<div class="linkSection">
	<a href='/config/propertyInfo/list?configCatgory=${configPropertyInfo.id.configCategory!''}'>list</a>
	<#--
	<#if mode != 'create'>
	 | <a href='/config/config_config_item_auth_info.nhn?name=${configPropertyInfo['name']}&${sectionInfo}'>auth list</a>
	 | <a href='/config/config_config_item_project_info.nhn?name=${configPropertyInfo['name']}&${sectionInfo}'>project list</a>
	</#if>
	-->
</div>

<form id='defalutForm' name='defalutForm' method="post" action="/config/propertyInfo/${mode}">
	<#if mode != 'read'>
		<div class="headSection">
			<input type='submit' value="${mode}">
		</div>
	</#if>
	
	<div class="bodySection">
		<table>
			<tr>
				<th style="width:150px">name</th>
				<th>value</th>
			</tr>
			
			<tr>
				<td>name</td>
				<td><@inputCell name="id.propertyName" value=(configPropertyInfo.id['propertyName'])!'' modifiable=(mode=='create')/></td>
			</tr>
			<tr>
				<td>config category</td>
				<td><@selectCell name="id.configCategory" values=configCategoryNames selectedValue=(configPropertyInfo.id.configCategory)!'' modifiable=(mode=="create")/></td>
			</tr>
			<tr>
				<td>required_yn</td>
				<td><@boolCell name='required' value=configPropertyInfo['required'] modifiable=(mode!='read') /></td>
			</tr>
			<tr>
				<td>modifiable_yn</td>
				<td><@boolCell name='modifiable' value=configPropertyInfo['modifiable'] modifiable=(mode!='read') /></td>
			</tr>
			<tr>
				<td>dataTypeCode</td>
				<td><@selectCell name='dataTypeCode' values=dataTypeCodes selectedValue=(configPropertyInfo['dataTypeCode'])!'' caseSensitive=false modifiable=(mode!='read')/></td>
			</tr>
			<tr>
				<td>defaultValue</td>
				<td>
				<#assign value=(configPropertyInfo['defaultValue'])!''>
				<#if (configPropertyInfo['dataTypeCode'])?exists>
					<#if configPropertyInfo['dataTypeCode']?upper_case == 'BOOLEAN'>
						<@selectCell name="defaultValue" values=["", "TRUE", "FALSE"] selectedValue=value caseSensitive=false modifiable=(mode!='read')/>
					<#elseif configPropertyInfo.candidates?size &gt; 0 >
						<@selectCell name="defaultValue" prepends=[['', '']] values=configPropertyInfo['candidates'] selectedValue=value modifiable=(mode!='read')/>
					<#else>
						<@inputCell name="defaultValue" value=value modifiable=(mode!='read')/>
					</#if>
				<#else>
					<@inputCell name="defaultValue" value=value modifiable=(mode!='read') />
				</#if>
				</td>
			</tr>
			<tr>
				<td>description</td>
				<td><@inputCell name="description" value=(configPropertyInfo['description'])!'' modifiable=(mode!='read') /></td>
			</tr>
			<tr>
				<td>category</td>
				<td><@inputCell name="category" value=(configPropertyInfo['category'])!'' modifiable=(mode!='read') /></td>
			</tr>
			<tr>
				<td>validation expression</td>
				<td><@inputCell name="validationExpression" value=configPropertyInfo['validationExpression']!'' modifiable=(mode!='read') /></td>
			</tr>
		</table>
	</div>
	
	<#if mode != 'read'>
		<div class="tailSection">
			<input type='submit' value="${mode}">
		</div>
	</#if>
</form>

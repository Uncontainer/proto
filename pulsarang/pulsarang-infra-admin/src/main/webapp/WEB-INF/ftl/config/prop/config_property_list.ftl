<#include "/WEB-INF/ftl/common.ftl">
<@validateMode />

<script type="text/javascript">
	function configPropertySubmit() {
		var form = $Form('configPropertyForm');
		form.submit();
	}
</script>


<div class="linkSection">	
	<a href='/config/list?solutionName=${configInfo.solutionName!''}'>list</a>
</div>

<div class="headSection">
	<#if mode=='create'>
		<#assign additional_fields=['defaultValue']>
	<#elseif mode=='modify'>
		<#assign additional_fields=['cacheValue']>
	<#else>
		<#assign additional_fields=[]>
		<a href='/config/property/modify/${configInfo.id.category}/${configInfo.id.name}'>modify</a>
	</#if>
</div>

<div class="bodySection">
	<#if mode == 'create'>
		<form id='defaultForm' name='defaultForm' method='post' action='/config/property/${mode}'>
			<input type='hidden' name='solutionName' value='${configInfo.solutionName!''}'/>
			<input type='hidden' name='configCategory' value='${configInfo.id.category}'/>
			<input type='hidden' name='configName' value='${configInfo.id.name}'/>
			<#if default_run_env?exists>
				<#assign selected_run_env = default_run_env>
			<#elseif run_env?exists>
				<#if run_env == 'real'>
					<#assign selected_run_env = 'test'>
				<#else>
					<#assign selected_run_env = 'dev'>
				</#if>
			<#else>
				<#assign selected_run_env = ''>
			</#if>
			
			Set value with config:
			<@selectCell name='defaultRunEnv' values=["dev", "test", "real"] selectedValue=selected_run_env/>
			
			<input size='10' name='defaultConfigName' type='text' value=${defaultConfigName!''}>
			<input type='submit' value="fill">
		</form>
	</#if>
	
	<form id='configPropertyForm' name='configPropertyForm' method='post' action='/config/property/${mode}'>
		<input type='hidden' name='solutionName' value='${configInfo.solutionName!''}'/>
		<input type='hidden' name='projectName' value='${configInfo.projectName!''}'/>
		<#if mode != 'read'>
			<a href='javascript:configPropertySubmit()'>${mode}</a>
			<#if mode == 'modify'>
				| <a href='/config/configInfo/modify/${configInfo.id.category}/${configInfo.id.name}'>prev</a>
			</#if>
			&nbsp; skip validation: <input type="checkbox" id="skipValidation" name="skipValidation" value="true" />
		</#if>
		
		<input type='hidden' name='configCategory' value='${configInfo.id.category}'/>
		<input type='hidden' name='configName' value='${configInfo.id.name}'/>
		<table>
			<tr>
				<th style="width:120px">name</th>
				<th>value</th>
				<#list additional_fields as field>
				<th>${field}</th>
				</#list>
			</tr>
			<tr>
				<td>config</td>
				<td>${configInfo.id.name}</td>
				<#list additional_fields as field>
				<td>${configInfo.id.name}</td>
				</#list>
			</tr>
		    <#list configProperites as configProperty>
				<tr>
					<td>
						<#if ((configProperty.required)!false)>*</#if>${configProperty.id.propertyName}
					</td>
					<td>
						<#if mode=='read'>
							<@ummodifiableTableCell configProperty=configProperty key='propertyValue'/>
						<#else>
							<@tableCell configProperty=configProperty key='propertyValue'/>
						</#if>
					</td>
					<#list additional_fields as field>
					<td>
						<@ummodifiableTableCell configProperty=configProperty key=field/>
					</td>
					</#list>
			    </tr> 
			</#list>
		</table>
	</div>
	
	<div class="tailSection">
		<input type='submit' value="${mode}">
	</div>
</form>

<#macro tableCell configProperty key>
	<#if !configProperty[key]?exists>
		<#assign value=''>
	<#elseif configProperty[key]?is_date>
		<#assign value = configProperty[key]?string("yyyy-MM-dd HH:mm:ss")>
	<#else>
		<#assign value = configProperty[key]?string>
	</#if>
	
	<#if mode == 'create' || (configProperty.configPropertyInfo.modifiable)!false>
		<#assign propertyName=configProperty.configPropertyInfo.id.propertyName>
		<#if configProperty.configPropertyInfo.dataTypeCode?upper_case == 'BOOLEAN'>
			<@selectCell name=propertyName values=["", "TRUE", "FALSE"] selectedValue=value caseSensitive=false/>
		<#elseif configProperty.configPropertyInfo.candidates?size &gt; 0>
			<@selectCell name=propertyName prepends=[['', '']] values=configProperty['candidates'] selectedValue=value/>
		<#else>
			<@inputCell name=propertyName value=value/>
		</#if>
	<#else>
		<#if value == ''>
			<i style="color:gray">null</i>
		<#else>
			${value}
		</#if>
	</#if>
</#macro>

<#macro ummodifiableTableCell configProperty key>
	<#if !configProperty[key]?exists>
		<#assign value=''>
	<#elseif configProperty[key]?is_date>
		<#assign value = configProperty[key]?string("yyyy-MM-dd HH:mm:ss")>
	<#else>
		<#assign value = configProperty[key]?string>
	</#if>
	
	<#if value == ''>
		<i style="color:gray">null</i>
	<#else>
		${value}
	</#if>
</#macro>

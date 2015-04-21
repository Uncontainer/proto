<#macro validateMode>
	<#if mode?exists>
		<#if mode!='create' && mode=='modify' && mode=='read'>
			<#assign mode='read'>
		</#if>
	<#else>
		<#assign mode='read'>
	</#if>
</#macro>

<#macro selectCell name displays=[] values=[] selectedValue='' prepends=[] caseSensitive=true onChange='' modifiable=true >
	<#if modifiable>
		<select id="${name}" name="${name}" <#if onChange != ''>onchange="${onChange}"</#if>>
			<#list prepends as prepend>
				<#if caseSensitive>
					<#assign selected = prepend[0]==selectedValue>
				<#else>
					<#assign selected = prepend[0]?upper_case==(selectedValue?upper_case)>
				</#if>
				<option value="${prepend[0]}"<#if selected> selected</#if>>${prepend[1]}</option>
			</#list>
			<#list values as value>
				<#if caseSensitive>
					<#assign selected = value==selectedValue>
				<#else>
					<#assign selected = value?upper_case==(selectedValue?upper_case)>
				</#if>
				<#assign display=(displays[value_index])!value>
				<option value="${value}"<#if selected> selected</#if>>${display}</option>
			</#list>
		</select>
	<#else>
		<input type='hidden' id='${name}' name='${name}' value='${selectedValue?string}'>
		${selectedValue?string}
	</#if>
</#macro>

<#macro inputCell name modifiable=true value=''>
	<#if modifiable>
		<input size='50' id='${name}' name='${name}' type='text' value='${value}'>
	<#else>
		<input type='hidden' id='${name}' name='${name}' value='${value}'>
		${value}
	</#if>
</#macro>

<#macro hiddenCell name value='' show=true>
	<input type='hidden' id='${name}' name='${name}' value='${value}'>
	<#if show>
		${value}
	</#if>
</#macro>

<#macro boolCell name value=false show=true modifiable=true>
	<@selectCell name=name values=['true', 'false'] selectedValue=(value?string) modifiable=modifiable/>
</#macro>


<#macro solutionSelector solutionNames >
	<#if solutionNames?has_content && solutionNames?size &gt; 1>
		<@selectCell name='solutionName' values=solutionNames selectedValue=solutionName onChange="submit();return false;"/>
	<#else>
		<input type='hidden' id='solutionName' name='solutionName' value='${solutionName}'/>
	</#if>
</#macro>


<#macro tboxIDNameEditor tagId editorScriptObjectName>
	<input type="hidden" name="jsonLocalNames_${tagId}"/>
	<table id="lTable_${tagId}" style="width:100%; cellpadding=0; cellspacing=5">
		<tr>
			<th style="width:55px;text-align:center">locale</th>
			<th style="text-align:center">이름</th>
		</tr>
	</table>
</#macro>

<#macro searchWindow tagId target finderScriptObjectName>
	<input type="text" size="20" id="keyword_${tagId}" onkeyup="${finderScriptObjectName}.findCandidates();return false;" autocomplete="off" style="height:20px;"/>
	<a href="/${target}/create" style="height:20;" target="_blank">새로 생성</a>
	<div style="position:absolute;" id="popup_${tagId}">
		<table id="candidates_table_${tagId}" bgcolor="#FFFAFA" border="0" cellspacing="0" cellpadding="0">
		    <tbody id="candidates_table_body_${tagId}"></tbody>
		</table>
	</div>
</#macro>

<#macro nextSerial>
	<#if serial_seed?exists>
		<#assign serial_seed = serial_seed+1/>
	<#else>
		<#assign serial_seed = 1/>
	</#if>
	${serial_seed}
</#macro>

<#macro classLink classId name="class">
	<a href="/class/${classId}">${name}</a>
</#macro>

<#include "/WEB-INF/ftl/common.ftl">

<script type="text/javascript">
	function cancel(checkbox) {
		var callbackUrl = "${callbackUrl}";
		
		window.location = callbackUrl;
	}
	
	function invertSolution(checkbox) {
		var aProjectCheckbox = $$('table#'+ checkbox.id + ' td.project>input');
		
		for(var i=0; i<aProjectCheckbox.length; i++) {
			aProjectCheckbox[i].checked = checkbox.checked;
			invertProject(aProjectCheckbox[i]);
		}
	}
	
	function invertProject(checkbox) {
		var aChildren = checkbox.parentNode.nextSibling.childNodes;
		
		var projectTd = checkbox.parentNode;
		var serverTd = projectTd.nextSibling;
		while(serverTd != null && serverTd.nodeName.toUpperCase() != 'TD') {
			serverTd = serverTd.nextSibling;
		}
		
		aChildren = serverTd.childNodes;
		
		for(var i=0; i<aChildren.length; i++) {
			if(aChildren[i].nodeName.toUpperCase() == 'INPUT') {
				aChildren[i].checked = checkbox.checked;
			}
		}
	}
</script>	

<form id='selectServerForm' name='selectServerForm' method='get' action='/update/refreshSubmit'>
	<input type='hidden' name="configCategory" value="${configCategory}" />
	<input type='hidden' name="configName" value="${configName}" />
	<input type='hidden' name="callbackUrl" value="${callbackUrl}" />
	
	<div style="width:600px">
		<fieldset>
			<legend>servers</legend>

			<#list solutions as solution>
				<input type='checkbox' onclick="invertSolution(this);" checked="checked" id='${solution.name}'>${solution.name}</input>
				<table id="${solution.name}" border='1'>
				<#list solution.projects as project>
					<tr>
						<td class="project">
							<input type='checkbox' checked="checked" onclick="invertProject(this);">${project.name}</input>
						</td>
						<td class="server">
							<#list project.servers as server>
								<input type='checkbox' name="serverName" value="${solution.name}.${project.name}.${server.name}" checked="checked">${server.name}</input>
							</#list>
						</td>
					</tr>
				</#list>
				</table>
				<div style="padding-bottom:5px"></div>
			</#list>
		</fieldset>
		
		<input type="submit" value="submit">
		<input type="button" value="cancel" onclick="cancel();">
	</div>
	
</form>

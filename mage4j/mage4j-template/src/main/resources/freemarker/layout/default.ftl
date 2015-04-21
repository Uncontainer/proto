<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
	<head>
		<@tiles.insertAttribute name="meta"/>
		<#--
		<@resource src="/css/jquery-ui-1.10.4.custom.css" debug="${RequestParameters['debug']!}"/>
		<@resource src="/css/common.css" debug="${RequestParameters['debug']!}"/>
		-->
		<link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.10.4.custom.css">
		<link rel="stylesheet" type="text/css" href="/css/common.css">
        <style>
            h2 {background: none}
            h3 {background: none}
        </style>

		<title><@tiles.getAsString name="title"/></title>
		<#include "./includes/common_top_script.ftl"/>
	</head>
	<body>
		<#include "./includes/common_bottom_script.ftl"/>
		<table border='0'>
			<tr>
				<td style="width:100px;vertical-align:top;padding-left:5px;padding-top:10px">
					<@tiles.insertAttribute name="left"/>
				</td>
				<td style="vertical-align:top;padding-top:10px;padding-right:5px">
					<@tiles.insertAttribute name="body"/>
				</td>
			</tr>
		</table>
		<#--
		<@resource flush="true"/>
		-->
		<@tiles.insertAttribute name="service.script"/>
	</body>
</html>

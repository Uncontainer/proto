<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
	<head>
		<@tiles.insertAttribute name="meta"/>
		<@resource src="${urls['static']}/css/common.css" debug="${RequestParameters['debug']!}"/>
		<title><@tiles.getAsString name="title"/></title>
		<#include "/layout/includes/common_top_script.ftl"/>
	</head>
	<body>
		<#include "/layout/includes/common_bottom_script.ftl"/>
		<#--  content body -->
		<@tiles.insertAttribute name="body"/>
		<@resource flush="true"/>
		<@tiles.insertAttribute name="service.script" />
	</body>
</html>
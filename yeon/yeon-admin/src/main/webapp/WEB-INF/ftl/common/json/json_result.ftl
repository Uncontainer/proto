<#ftl attributes={"content_type":"application/json; charset=utf-8"}>
{"success":${(result.success?string)!'true'}, "code":"${result.code!}", "message":"${result.message!}", "value":${result.value!}}
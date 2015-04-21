<@resource>
    alert("${(__message!)?replace("\n", "\\n")}");
    location.href = "${__move_url!}";
</@resource>
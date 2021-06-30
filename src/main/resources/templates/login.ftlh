<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>

<@c.page>

<b>Login page</b>

${message?ifExists}
<@l.login "/login" false/>

</@c.page>


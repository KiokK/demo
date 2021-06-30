<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>
<@c.page>


  <div class="mb-1">
    <b>Registration</b>
  </div>
${message?ifExists}
  <@l.login "/registration" true/>

</@c.page>

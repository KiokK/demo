<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>
<@c.page>


    <@l.logout/>

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>

        </tbody>
    <#list userList as user>
        <tr>
            <td>${user.username}</td>
            <td> <#list user.roles as role>
                ${role}<#sep>,
                </#list> </td>
            <td> <a href="user/${user.id}">edit</a> </td>
        </tr>

    </#list>
    </table>


</@c.page>

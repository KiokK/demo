<#import "blocks/common.ftlh" as c>
<#import "blocks/login.ftlh" as l>
<@c.page>

<main>
    <span><a href="/user"> User list</a></span>
    <@l.logout/>


    <form action="/main" method="post" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Text" required>
        <input type="text" name="tag" placeholder="Tag" required>
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-success">Add</button>
    </form>
    <br>
    <form action="/main" method="get">
        <input type="text" name="filter" placeholder="Filter by tag" value="${RequestParameters.filter!}">
        <button type="submit" class="btn btn-success">Chose</button>
    </form>
    <#list messages as messages>
        <div>
            <b>ID:</b> <b> ${messages.id}</b>
            <span>TEXT:</span> <span>${messages.text}</span>
            <i>TAG:</i> <i>${messages.tag}</i>
            <b> ${messages.authorName}</b>
            <div>
                <#if messages.filename??>
                <img src="/img/${messages.filename}" alt="" width="30%">
                </#if>
            </div>
        </div>
        <#else>
            No messages
    </#list>
</main>
</@c.page>

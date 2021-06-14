<#import "blocks/common.ftlh" as c>
<@c.page>



<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Add new Message
</a>
<div class="collapse" id="collapseExample">
    <div class="form-group mt-3">
        <form action="/main" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control" name="text" placeholder="Введите сообщение" required />
            </div>
            <div class="form-group">
                <input type="text" class="form-control" name="tag" placeholder="Тэг" required>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Add</button>
            </div>
        </form>
        <br>
    </div>
</div>

<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" placeholder="Search by tag" value="${RequestParameters.filter!}">
            <button type="submit" class="btn btn-primary ml-2">Search</button>
        </form>
    </div>
</div>

<div class="card-columns">
    <#list messages as message>
    <div class="card my-3">
        <#if message.filename??>
        <img src="/img/${message.filename}" class="card-img-top">
    </#if>
    <div class="m-2">
        <span>TEXT:</span> <span>${message.text}</span>
        <b><i>TAG:</i>  <i>${message.tag}</i></b>
    </div>
    <div class="card-footer text-muted">
        ${message.authorName}
    </div>
</div>
<#else>
No message
</#list>
</div>

</@c.page>

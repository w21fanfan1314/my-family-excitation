<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>欢迎使用番茄家庭激励系统</title>
</head>
<body>


<div id="content" role="main">
    <div class="container">
        <section class="row colset-2-its">
            <div id="controllers" role="navigation">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller">
                            <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </section>
    </div>
</div>

</body>
</html>

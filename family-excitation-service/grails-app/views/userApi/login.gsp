<%--
  Created by IntelliJ IDEA.
  User: Kobe
  Date: 2024/11/1
  Time: 14:01
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>登录</title>
</head>

<body>
<div class="container d-flex">
    <div class="row d-flex justify-content-center flex-grow-1">
        <div class="mt-5" style="width: 400px">
            <g:if test="${error}">
                <div class="alert alert-danger">${error}</div>
            </g:if>
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title">请登录</h3>
                    <g:form action="login" method="post">
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" placeholder="用户名" name="loginName" type="text" value="${loginName}" autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="密码" name="password" type="password" value="${password}">
                            </div>
                            <button class="btn btn-lg btn-success btn-block" type="submit">登录</button>
                        </fieldset>
                    </g:form>
                </div>
            </div>
        </div>
        </div>
    </div>
</div>
</body>
</html>
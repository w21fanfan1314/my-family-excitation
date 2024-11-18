<%--
  Created by IntelliJ IDEA.
  User: Kobe
  Date: 2024/11/15
  Time: 15:06
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${assetPath(src: 'layui/css/layui.css')}">
    <script type="text/javascript" src="${assetPath(src: 'layui.js')}"></script>
    <script type="text/javascript" src="${assetPath(src: 'application.js')}"></script>
    <title>闯关题目发布</title>
</head>

<body>
    <table class="layui-table" id="train-data-table"></table>
    <g:javascript>
        layui.use(['table'], function() {
            const table = layui.table

            table.render({
                elem: '#train-data-table',
                url: '${createLink(action: 'generate')}.json',
                height: 'full-35',
                page: true,
                lineStyle: 'height: 80px',
                cols: [
                    [
                        { field: 'id', title: 'ID', width: 80, align: 'center', fixed: 'left' },
                        { field: 'image', title: '图片', width: 150, align: 'center',  templet: '#train-logo'},
                        { field: 'name', title: '名称'},
                        { field: 'category', title: '分类', width: 120, align: 'center'},
                        { field: 'description', title: '描述'},
                        { title: '操作', templet: '#train-row-tools', width: 200, align: 'center'}
                    ]
                ],
                parseData: function(res) {
                    return {
                        "code": res.code === 200 ? 0 : res.code,
                        "msg": res.msg,
                        "count": res.data.total,
                        "data": res.data.items
                    }
                }
            })
        })
    </g:javascript>
    <script type="text/html" id="train-logo">
        {{# if(d.image) { }}
            <img src="{{= d.image }}" style="height: 70px; width: 100%">
        {{# } }}
    </script>
    <script type="text/html" id="train-row-tools">
        <button class="layui-btn  layui-btn-sm">
            <i class="layui-icon layui-icon-edit"></i>编辑
        </button>
        <button class="layui-btn layui-btn-danger  layui-btn-sm">
            <i class="layui-icon layui-icon-delete"></i>删除
        </button>
    </script>
</body>
</html>
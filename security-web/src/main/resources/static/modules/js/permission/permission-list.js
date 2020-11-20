var $table = $('#table');
$(function () {
	var options = {
		url: contextPath + 'permission/list', // 请求后台URL(*)
		idField: "id", // 指定主键列
		parentIdField: 'parentId', // 父菜单字段名
		treeShowField: 'name', // 在哪一列展开树形
		columns: [
			{
				title: '序号', width: 20, formatter: function (value, row, index) {
					return index + 1
				}
			}, {field: 'id', visible: false},
			{field: 'name', title: '名称', width: '200px'},
			{field: 'url', title: '地址', width: '300px'},
			{field: 'code', title: '权限值', width: '300px'},
			{
				field: 'icon',
				title: '图标',
				width: '100px',
				align: 'center',
				formatter: iconFormatter
			}, {
				field: 'type', title: '类型', width: '100px', align: 'center', formatter: function (value, row, index) {
					return value == 1 ? "菜单" : "按钮";
				}
			}, {field: 'action', visible: false, title: '操作', width: 50, align: 'center', formatter: $.operationFormatter}]
	};//初始化数据渲染, 是调用common.js中的 treeTable 方法
	$.treeTable(options);
});

// 图标
function iconFormatter(value, row, index) {
	return '<span class="' + value + '"></span>';
}
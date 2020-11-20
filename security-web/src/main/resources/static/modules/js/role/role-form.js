$(function () {
	loadPermissionTree();
});
//加载权限树
function loadPermissionTree() {
	var menuSetting = {
		view: {
			showLine: true //显示连接线
		},
		check: {
			enable: true //显示勾选框
		},
		data: {
			simpleData: {
				enable: true,  //开启简单模式,List自动转json
				idKey: "id",  //唯一的标识
				pIdKey: "parentId", //父节点唯一标识的属性名称
				rootPId: 0  //根节点数据
			},
			key: {
				name: "name", //显示的节点名称对应的属性名称
				title: "name" //鼠标放上去显示的
			}
		},
		callback: {
			onClick: function (event,treeId,treeNode) {
				//treeNode代表的是点击的那个节点json对象
				//被点击之后阻止跳转
				event.preventDefault();
			}
		}
	};
	$.post(contextPath + "permission/list",function (data) {
		var permissionTree = $.fn.zTree.init($("#permissionTree"), menuSetting, data.data);
		//1.判断一下是否是修改，判断id是否有值，如果有就是修改页面，否则是新增页面
		var id = $("#id").val();
		if(id !== null && id !== null && id != undefined){
			//2.获取当前角色所拥有的权限id
			var perIds = JSON.parse($("#perIds").val());
			//3.勾选拥有的权限
			for (var i = 0; i < perIds.length; i++) {
				var nodes = permissionTree.getNodesByParam("id", perIds[i], null);
				//勾选当前选中的节点
				permissionTree.checkNode(nodes[0], true, false);
				//是否展开当前节点
				permissionTree.expandNode(nodes[0], true, false);
			}
		}
	})
}
//提交数据
$("#form").submit(function () {
	//收集所有被选中的节点
	var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
	var nodes = treeObj.getCheckedNodes(true);
	var perIds = [];
	for (var i = 0; i < nodes.length; i++) {
		perIds.push(nodes[i].id);
	}
	$("#perIds").val(perIds);
	return true;
})
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
			enable: false //不显示勾选框
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
				if(treeNode.id == $("#id").val()){
					layer.tips('自己不能作为父资源','#'+treeId,{time: 1000});
					return;
				}
				//将选择的节点放到父资源处
				parentPermission(treeNode.id,treeNode.name)
			}
		}
	};
	$.post(contextPath + "permission/list",function (data) {
		var permissionTree = $.fn.zTree.init($("#permissionTree"), menuSetting, data.data);
		if($("#parentId").val() !== null && $("#parentId").val() !== '' && $("#parentId").val() !== undefined &&
		$("#parentId").val() != 0){
			var nodes = permissionTree.getNodesByParam("id", $("#parentId").val(), null);
			$("#parentName").val(nodes[0].name);
		}
		
	})
}

function parentPermission(parentId,parentName) {
	if (parentId == null || parentName == null) {
		parentId = 0;
		parentName = '根菜单';
	}
	$("#parentId").val(parentId);
	$("#parentName").val(parentName);
}
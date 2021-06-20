window.onload = function() {
	/**
	 * text是菜单显示的文本内容 icon 是菜单图片 right_text 是菜单右侧显示的提示文本，最大长度为3 active
	 * ,值为active；有该值，则默认选中该项，高亮 open，菜单是否展开
	 */
	var menuJson = [
		{
			name: "页面1",
			icon: "nav-icon fas fa-home",
			right_text: "新",
			webPage: "index1"
		}, {
			name: "页面2",
			icon: "nav-icon fas fa-home",
			right_text: "新",
			webPage: "index2"
		}, {
			name: "一级目录",
			icon: "nav-icon fas fa-home",
			open: "menu-open",
			webPage: "#",
			children: [{
				name: "一级叶子1",
				icon: "nav-icon fas fa-home",
				right_text: "新",
				webPage: "index21"
			}, {
				name: "一级叶子2",
				icon: "nav-icon fas fa-home",
				icon: "far fa-circle nav-icon",
				webPage: "index22"
			}, {
				name: "二级目录",
				right_text: "新xin",
				icon: "nav-icon fas fa-home",
				webPage: "#",
				children: [{
					name: "二级叶子1",
					icon: "far fa-circle nav-icon",
					webPage: "index221"
				}, {
					name: "二级叶子2",
					right_text: "新",
					icon: "far fa-circle nav-icon",
					webPage: "index222"
				}]
			}]
		}, {
			name: "第三项",
			icon: "nav-icon fas fa-home",
			webPage: "#",
			children: [{
				name: "叶子",
				webPage: "baidu.com",
				icon: "far fa-circle nav-icon"
			}]
		}];
	// 构建菜单
	console.log(menuJson.toString());
	//menuTree(menuJson, document.getElementById('menutree'));
	menuLoad();
}

function menuLoad() {
	var options = {
		type: 'GET',
		url: '/guest/tree-menu',
		contentType: 'application/json',
		dataType: 'JSON',
		success: function(message) {
			// jQuery.parseJSON(jsonString)  
			//var json = JSON.stringify(message);
			console.log(message.data);
			menuTree(message.data, document.getElementById('menutree'));
		}
	};
	ajax(options);
}

function menuTree(jsonList, container) {
	for (var i = 0; i < jsonList.length; i++) {
		var right = jsonList[i];
		var oLi = document.createElement('li');
		$(oLi).addClass("nav-item");
		var html = '<a href="' + right.webPage + '" class="nav-link">';
		html += '	<i class="nav-icon fas ' + right.iconFile + '"></i>';
		html += '	<p>' + right.name;
		oLi.innerHTML = html;
		if (right.children && right.children.length > 0) {
			html += '</p><i class="fas fa-angle-left right"></i>';
			html += '</a>';
			oLi.innerHTML = html;
			var oUl = document.createElement('ul');
			$(oUl).addClass("nav nav-treeview ");
			oLi.appendChild(oUl);
			menuTree(right.children, oUl);
		} else {
			html += '</p></a>';
			oLi.innerHTML = html;
		}
		//console.log(right.name);
		container.appendChild(oLi);
	}
}
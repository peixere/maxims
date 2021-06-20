/**
*
jquery ajax 二次封装
ajax(formData,true) //上传图片
ajax(opt)//普通调用

ajax(opt).then((r)=>{
	console.log(r)
)

var opt = {
	url:'do_login',
	type:'post',//如果是post需要传递type，如果是get可以不传递
	data:{usrname,password},
	success(r){
		//your code here or callback(r)
	}
}
*/
function ajax(opt) {
	var defaultSettings = {
		data: {},
		type: 'get',
		dataType: 'json',
		headers: { 'Authorization': getStore('token') },
		beforeSend: function() {
			openLoad()
		},
		complete: function() {
			closeLoad();
		},
		error: function(ex) {
			console.log(ex.responseJSON.error);
			if (ex.status == 401) {
				showTips(ex.responseJSON.message, '/login', 500)
			} else if (ex.status == 403) {
				showTips('您没有权限')
			} else if (ex.status == 404) {
				showTips('你请求的页面找不到')
			} else if (ex.status == 500) {
				showTips(ex.responseJSON.message)
			} else {
				showTips(ex.responseJSON.message)
			}
		}
	}
	for (var key in opt) {
		defaultSettings[key] = opt[key]
	}
	if (arguments[1]) { //如果是图片上传,需要传递第二个参数，必须内容
		defaultSettings.traditional = true
		defaultSettings.processData = false
		defaultSettings.contentType = false
	}
	return $.ajax(defaultSettings)
}

function showTips(msg, href, delay) {
	var tipsEl = $('<div id="dialog-tip"></div>').text(msg).css({
		"background": 'rgba(0,0,0,0.5)',
		"color": '#ef4455',
		'position': 'fixed',
		"left": '50%',
		"top": '4%',
		"transform": 'translate(-50%,-50%)',
		"z-index": 999999,
		'padding': '10px 10px 10px 10px',
		'border-radius': '4px',
		"box-sizing": 'border-box',
		"display": 'none',
		"font-size": '14px'
	});
	var delayTime = 2000;
	if (delay) {
		delayTime = delay;
	}
	tipsEl.appendTo($('body')).fadeIn(500).delay(delayTime).fadeOut(500, function() {
		$('#dialog-tip').modal('hide');
		tipsEl.remove();
		if (href) {
			location.href = href;
		}
	});
	$('#dialog-tip').modal({
		backdrop: "static",
		keyboard: false,
		show: true
	});
}
function getStore(name) {
	if (!name) return;
	return window.localStorage.getItem(name);
}
function openLoad() {
	var loadEl_icon = $('<h2 class="display-4"><i class="fa fa-sync fa-spin"></i><h2>').css({
		"text-align": 'center',
		"margin": 'auto'
	});
	var lodeEl_inner = $('<div></div>').css({
		"text-align": 'center',
		"width": '200px',
		"height": '200px',
		"padding": '60px 60px 60px 70px',
		"margin": 'auto',
		"border-radius": '5px',
		"background": 'rgba(0,0,0,0.5)',
		"display": 'flex'
	}).append(loadEl_icon);
	$('<div id="ui-loading-mask" class="modal"></div>').css({
		"text-align": 'center',
		"position": 'absolute',
		"left": 0, "top": 0, "bottom": 0, "right": 0,
		"background": 'rgba(255,255,255,0.01)',
		"z-index": 999999,
		"display": 'flex'
	}).append(lodeEl_inner).appendTo($('body'));
}

function closeLoad() {
	$('#ui-loading-mask').remove();
}

/* 表单转json */
function form2Json(formid) {
	var data = {};
	$.map($('#' + formid).serializeArray(), function(n) {
		data[n['name']] = n['value'];
	});
	return JSON.stringify(data);
}
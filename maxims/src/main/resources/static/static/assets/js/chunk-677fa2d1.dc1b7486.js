(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-677fa2d1"],{b737:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"content is-vertical",attrs:{id:"tenantManagement"}},[a("div",{staticClass:"content-main"},[a("el-form",{attrs:{inline:!0,"label-position":"right"}},[a("el-form-item",{attrs:{label:"租户名称"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入"},model:{value:e.searchForm.name,callback:function(t){e.$set(e.searchForm,"name",t)},expression:"searchForm.name"}})],1),a("el-form-item",{attrs:{label:"租户地址"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入"},model:{value:e.searchForm.address,callback:function(t){e.$set(e.searchForm,"address",t)},expression:"searchForm.address"}})],1),a("el-form-item",{attrs:{label:"公司名称"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入"},model:{value:e.searchForm.company,callback:function(t){e.$set(e.searchForm,"company",t)},expression:"searchForm.company"}})],1),a("el-form-item",[a("el-button",{attrs:{type:"primary",icon:"el-icon-search fa-plus"},on:{click:e.handleSearch}},[e._v("搜索")])],1)],1),a("div",{staticClass:"toolbar"},[a("ri-button",{attrs:{right:"新增",type:"primary",icon:"el-icon-fa fa-plus"},on:{click:e.handleAdd}},[e._v("新增租户")]),a("ri-button",{attrs:{right:"删除",type:"danger",icon:"el-icon-delete",disabled:e.sels.length<1},on:{click:e.batchDelete}},[e._v("批量删除")])],1),a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData,border:""},on:{"selection-change":e.selsChange}},[a("el-table-column",{attrs:{type:"selection",fixed:"",width:"45px",align:"center"}}),a("el-table-column",{attrs:{type:"index",label:"序号",width:"45px",align:"center"}}),a("el-table-column",{attrs:{property:"name",label:"租户名称",fit:""}}),a("el-table-column",{attrs:{property:"address",label:"租户地址",fit:""}}),a("el-table-column",{attrs:{property:"company",label:"公司名称",fit:""}}),a("el-table-column",{attrs:{property:"mobile",label:"公司电话",fit:""}}),a("el-table-column",{attrs:{property:"board",label:"看板路由",fit:""}}),a("el-table-column",{attrs:{property:"title",label:"系统标题"}}),a("el-table-column",{attrs:{property:"",label:"操作",fixed:"right",width:"100px"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-tooltip",{staticClass:"item",attrs:{content:"编辑",placement:"top"}},[a("ri-button",{staticClass:"table-btn",attrs:{right:"编辑",type:"text",icon:"el-icon-edit"},on:{click:function(a){return e.handleEdit(t.row)}}})],1),a("el-tooltip",{staticClass:"item",attrs:{content:"删除",placement:"top"}},[a("ri-button",{staticClass:"table-btn",attrs:{right:"删除",type:"text",icon:"el-icon-delete"},on:{click:function(a){return e.handleDel(t.row)}}})],1),a("el-tooltip",{staticClass:"item",attrs:{content:"查看详细",placement:"top"}},[a("el-button",{staticClass:"table-btn",attrs:{type:"text",icon:"el-icon-document"},on:{click:function(a){return e.handleTenantDetail(t.row)}}})],1)]}}])})],1),a("el-pagination",{attrs:{layout:"total, sizes, prev, pager, next, jumper","page-sizes":[10,20,50,100],"page-size":e.searchForm.page.size,total:e.searchForm.page.total},on:{"current-change":e.pageChange,"size-change":e.handleSizeChange}})],1),a("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogTableVisible,width:"750px",top:"3vh"},on:{"update:visible":function(t){e.dialogTableVisible=t}}},[e.dialogTableVisible?a("div",[a("div",{staticStyle:{display:"flex"}},[a("el-form",{ref:"ruleForm",staticStyle:{width:"450px"},attrs:{"label-width":"100px",model:e.addEditform,rules:e.rules}},[a("el-form-item",{attrs:{label:"租户名称",prop:"name"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"租户名称"},model:{value:e.addEditform.name,callback:function(t){e.$set(e.addEditform,"name",t)},expression:"addEditform.name"}})],1),a("el-form-item",{attrs:{label:"租户地址",prop:"address"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"租户地址",maxlength:"30"},model:{value:e.addEditform.address,callback:function(t){e.$set(e.addEditform,"address",t)},expression:"addEditform.address"}})],1),a("el-form-item",{attrs:{label:"公司名称",prop:"company"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"公司名称",maxlength:"100"},model:{value:e.addEditform.company,callback:function(t){e.$set(e.addEditform,"company",t)},expression:"addEditform.company"}})],1),a("el-form-item",{attrs:{label:"公司电话",prop:"mobile"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"公司手机",maxlength:"11"},model:{value:e.addEditform.mobile,callback:function(t){e.$set(e.addEditform,"mobile",t)},expression:"addEditform.mobile"}})],1),a("el-form-item",{attrs:{label:"看板路由",prop:"board"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"看板路由",maxlength:"100"},model:{value:e.addEditform.board,callback:function(t){e.$set(e.addEditform,"board",t)},expression:"addEditform.board"}})],1),a("el-form-item",{attrs:{label:"坐标点"}},[a("el-input",{staticStyle:{width:"66%"},attrs:{placeholder:"请输入"},model:{value:e.addEditform.position,callback:function(t){e.$set(e.addEditform,"position",t)},expression:"addEditform.position"}}),a("el-button",{attrs:{type:"primary"},on:{click:e.handleMap}},[e._v("坐标拾取")])],1),a("el-form-item",{attrs:{label:"系统标题",prop:"title"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{placeholder:"系统标题"},model:{value:e.addEditform.title,callback:function(t){e.$set(e.addEditform,"title",t)},expression:"addEditform.title"}})],1),a("el-form-item",{attrs:{label:"系统图标"}},[a("icon-select",{model:{value:e.addEditform.logoId,callback:function(t){e.$set(e.addEditform,"logoId",t)},expression:"addEditform.logoId"}})],1),a("el-form-item",{attrs:{label:"主页信息"}},[a("el-input",{staticStyle:{width:"90%"},attrs:{type:"textarea",rows:2,placeholder:"请输入内容"},model:{value:e.addEditform.main,callback:function(t){e.$set(e.addEditform,"main",t)},expression:"addEditform.main"}})],1)],1),a("el-tabs",{on:{"tab-click":e.handleClick},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[a("el-tab-pane",{staticStyle:{"overflow-y":"scroll",height:"420px",width:"250px"},attrs:{label:"功能权限",name:"first"}},[a("el-tree",{ref:"elTree",attrs:{props:e.props,data:e.rightTree,"show-checkbox":"","node-expand":"","node-key":"id","check-strictly":!0,"default-checked-keys":e.addEditform.rightIdList,"default-expanded-keys":e.addEditform.rightIdList}})],1),e._e(),e._e()],1)],1)]):e._e(),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.handelCancel}},[e._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:e.handleSave}},[e._v("确 定")])],1)]),a("el-dialog",{attrs:{title:"租户详细信息",visible:e.dialogDetail,width:"700px",top:"3vh"},on:{"update:visible":function(t){e.dialogDetail=t}}},[e.dialogDetail?a("div",[a("div",{staticStyle:{display:"flex"}},[a("el-form",{staticStyle:{width:"400px"},attrs:{"label-width":"110px",model:e.tenantDetail}},[a("el-form-item",{attrs:{label:"租户名称:"}},[a("label",[e._v(e._s(e.tenantDetail.name))])]),a("el-form-item",{attrs:{label:"租户地址:"}},[a("label",[e._v(e._s(e.tenantDetail.address))])]),a("el-form-item",{attrs:{label:"公司名称:"}},[a("label",[e._v(e._s(e.tenantDetail.company))])]),a("el-form-item",{attrs:{label:"公司电话:"}},[a("label",[e._v(e._s(e.tenantDetail.mobile))])]),a("el-form-item",{attrs:{label:"创建时间:"}},[a("label",[e._v(e._s(e.tenantDetail.created))])]),a("el-form-item",{attrs:{label:"看板路由:"}},[a("label",[e._v(e._s(e.tenantDetail.board))])]),a("el-form-item",{attrs:{label:"坐标点:"}},[a("label",[e._v(e._s(e.tenantDetail.position))])]),a("el-form-item",{attrs:{label:"系统标题:"}},[a("label",[e._v(e._s(e.tenantDetail.title))])]),a("el-form-item",{attrs:{label:"系统图标:"}},[a("label",[e._v(e._s(e.tenantDetail.logoId))])]),a("el-form-item",{attrs:{label:"主页信息:"}},[a("label",[e._v(e._s(e.tenantDetail.main))])])],1),a("el-tabs",{on:{"tab-click":e.handleClick},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[a("el-tab-pane",{staticStyle:{"overflow-y":"scroll",height:"430px",width:"250px"},attrs:{label:"功能权限",name:"first"}},[a("el-tree",{attrs:{props:e.props,data:e.rightTreeDisable,"show-checkbox":"","node-key":"id","default-expanded-keys":e.tenantDetail.rightIdList,"default-checked-keys":e.tenantDetail.rightIdList}})],1),e._e(),e._e()],1)],1)]):e._e(),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.dialogDetail=!1}}},[e._v("返 回")])],1)])],1)},i=[],o=(a("ac6a"),a("cf7e")),r={components:{IconSelect:o["a"]},data:function(){return{title:"租户管理",activeName:"first",dialogAction:"",dialogTitle:"",dialogTableVisible:!1,dialogDetail:!1,tableData:[],sels:[],rightTree:[],rightTreeDisable:[],props:{id:"id",label:"name",children:"children"},tenantDetail:{},searchForm:{name:"",company:"",address:"",page:{number:1,size:10,total:0}},addEditform:{id:"",name:"",company:"",address:"",mobile:"",board:"",position:"",title:"",logoId:"",main:"",rightIdList:[]},rules:{name:[{required:!0,message:"请输入租户名称",trigger:"blur"}],address:[{required:!0,message:"请输入租住地址",trigger:"blur"}],company:[{required:!0,message:"请输入公司名称",trigger:"blur"}],mobile:[{required:!0,message:"请输入公司手机号码",trigger:"blur"},{required:!0,min:5,message:"请输入正确的手机号",trigger:"blur"}]}}},created:function(){this.pageChange(1)},watch:{},methods:{handleClick:function(e,t){console.log(e,t)},handleTenantDetail:function(e){var t=this;this.getRightTree(!0),this.$api.tenant.findTenantDetail(e.id).then((function(e){t.tenantDetail=e.data,t.dialogDetail=!0}))},handleSearch:function(){this.pageChange(1)},handelCancel:function(){this.dialogTableVisible=!1,this.handleSearch()},handleSave:function(){var e=this;this.$refs["ruleForm"].validate((function(t){if(!t)return!1;e.addEditform.rightIdList=e.$refs.elTree.getCheckedKeys(),"edit"==e.dialogAction?e.$api.tenant.updateTenant(e.addEditform).then((function(t){200==t.status&&(e.dialogTableVisible=!1,e.pageChange(1),e.$message({message:"编辑租户成功！",type:"success"}))})):"add"==e.dialogAction&&e.$api.tenant.addTenant(e.addEditform).then((function(t){200==t.status&&(e.dialogTableVisible=!1,e.pageChange(1),e.$message({message:"添加租户成功！",type:"success"}))}))}))},handleAdd:function(){this.dialogTitle="添加租户",this.dialogAction="add",this.addEditform={id:"",name:"",company:"",address:"",mobile:"",board:"",position:"",title:"",logoId:"",main:"",rightIdList:[]},this.getRightTree(!1),this.dialogTableVisible=!0},handleEdit:function(e){var t=this;this.dialogTitle="修改租户信息",this.dialogAction="edit",this.getRightTree(!1),this.$api.tenant.findTenantDetail(e.id).then((function(e){t.addEditform=e.data,t.dialogTableVisible=!0}))},getRightTree:function(e){var t=this;this.$api.tenant.findTenantTree().then((function(a){if(e){var l=function e(a){a.forEach((function(t){t.disabled=!0,t.children&&(t.children.length?e(t.children):t.children=void 0)})),t.rightTreeDisable=a};l(a.data)}else{var i=function e(a){a.forEach((function(t){t.children&&(t.children.length?e(t.children):t.children=void 0)})),t.rightTree=a};i(a.data)}}))},pageChange:function(e){var t=this;e&&(this.searchForm.page.number=e),this.$api.tenant.findTenantPage(this.searchForm).then((function(e){t.tableData=e.data,t.searchForm.page.total=e.page.total}))},handleSizeChange:function(e){e&&(this.searchForm.page.size=e),this.pageChange(1)},batchDelete:function(){for(var e=this,t=[],a=0;a<this.sels.length;a++)t[a]=this.sels[a].id;this.$api.tenant.deleteTenantByIds(t).then((function(){e.$message({message:"批量删除租户成功！",type:"success"}),e.pageChange(1)}))},handleDel:function(e){var t=this;this.$api.tenant.deleteTenant(e.id).then((function(e){200==e.status&&(t.pageChange(1),t.$message({message:e.message,type:"success"}))}))},selsChange:function(e){return this.sels=e,!0},handleMap:function(){window.open("http://lbs.amap.com/console/show/picker")}}},s=r,n=a("2877"),c=Object(n["a"])(s,l,i,!1,null,"4338d75e",null);t["default"]=c.exports},cf7e:function(e,t,a){"use strict";var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticStyle:{display:"inline-block"}},[a("el-input",{attrs:{"prefix-icon":this.text?"el-icon-fa fa-"+this.text:"",readonly:"",placeholder:"请选择图标"},on:{focus:e.handleFocus},model:{value:e.text,callback:function(t){e.text=t},expression:"text"}}),a("el-dialog",{attrs:{visible:e.visible,width:"800px",modal:!1,"close-on-click-modal":!1},on:{"update:visible":function(t){e.visible=t}}},[a("div",{staticStyle:{height:"45vh","overflow-y":"auto","background-color":"#333744",padding:"10px"}},[e._l(e.icons,(function(t,l){return[a("el-col",{key:l,attrs:{span:6}},[a("el-button",{staticStyle:{"font-size":"14px",color:"white"},attrs:{type:"text",icon:"el-icon-fa fa-"+t+" fa-fw"},on:{click:function(a){return e.handleSelect(t)}}},[e._v(e._s(t))])],1)]}))],2),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"danger"},on:{click:e.handleClear}},[e._v("清除")]),a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")])],1)])],1)},i=[],o=(a("a481"),{components:{},name:"icon-select",props:{value:{type:String}},computed:{},created:function(){},mounted:function(){},activated:function(){},beforeDestroy:function(){},watch:{value:function(){this.text=this.value?this.value.replace("fa fa-",""):void 0}},methods:{handleFocus:function(){this.visible=!0},handleSelect:function(e){this.text=e,this.$emit("input","fa fa-"+e),this.visible=!1},handleClear:function(){this.text=void 0,this.$emit("input",void 0),this.visible=!1}},data:function(){return{visible:!1,text:this.value?this.value.replace("fa fa-",""):void 0,icons:["address-book","address-book-o","address-card","address-card-o","adjust","anchor","archive","area-chart","arrows","arrows-h","arrows-v","asl-interpreting","asterisk","at","audio-description","automobile","balance-scale","ban","bank","bar-chart","bar-chart-o","barcode","bars","bath","bathtub","battery","battery-0","battery-1","battery-2","battery-3","battery-4","battery-empty","battery-full","battery-half","battery-quarter","battery-three-quarters","bed","beer","bell","bell-o","bell-slash","bell-slash-o","bicycle","binoculars","birthday-cake","blind","bluetooth","bluetooth-b","bolt","bomb","book","bookmark","bookmark-o","braille","briefcase","bug","building","building-o","bullhorn","bullseye","bus","cab","calculator","calendar","calendar-check-o","calendar-minus-o","calendar-o","calendar-plus-o","calendar-times-o","camera","camera-retro","car","caret-square-o-down","caret-square-o-left","caret-square-o-right","caret-square-o-up","cart-arrow-down","cart-plus","cc","certificate","check","check-circle","check-circle-o","check-square","check-square-o","child","circle","circle-o","circle-o-notch","circle-thin","clock-o","clone","close","cloud","cloud-download","cloud-upload","code","code-fork","coffee","cog","cogs","comment","comment-o","commenting","commenting-o","comments","comments-o","compass","copyright","creative-commons","credit-card","credit-card-alt","crop","crosshairs","cube","cubes","cutlery","dashboard","database","deaf","deafness","desktop","diamond","dot-circle-o","download","drivers-license","drivers-license-o","edit","ellipsis-h","ellipsis-v","envelope","envelope-o","envelope-open","envelope-open-o","envelope-square","eraser","exchange","exclamation","exclamation-circle","exclamation-triangle","external-link","external-link-square","eye","eye-slash","eyedropper","fax","feed","female","fighter-jet","file-archive-o","file-audio-o","file-code-o","file-excel-o","file-image-o","file-movie-o","file-pdf-o","file-photo-o","file-picture-o","file-powerpoint-o","file-sound-o","file-video-o","file-word-o","file-zip-o","film","filter","fire","fire-extinguisher","flag","flag-checkered","flag-o","flash","flask","folder","folder-o","folder-open","folder-open-o","frown-o","futbol-o","gamepad","gavel","gear","gears","gift","glass","globe","graduation-cap","group","hand-grab-o","hand-lizard-o","hand-paper-o","hand-peace-o","hand-pointer-o","hand-rock-o","hand-scissors-o","hand-spock-o","hand-stop-o","handshake-o","hard-of-hearing","hashtag","hdd-o","headphones","heart","heart-o","heartbeat","history","home","hotel","hourglass","hourglass-1","hourglass-2","hourglass-3","hourglass-end","hourglass-half","hourglass-o","hourglass-start","i-cursor","id-badge","id-card","id-card-o","image","inbox","industry","info","info-circle","institution","key","keyboard-o","language","laptop","leaf","legal","lemon-o","level-down","level-up","life-bouy","life-buoy","life-ring","life-saver","lightbulb-o","line-chart","location-arrow","lock","low-vision","magic","magnet","mail-forward","mail-reply","mail-reply-all","male","map","map-marker","map-o","map-pin","map-signs","meh-o","microchip","microphone","microphone-slash","minus","minus-circle","minus-square","minus-square-o","mobile","mobile-phone","money","moon-o","mortar-board","motorcycle","mouse-pointer","music","navicon","newspaper-o","object-group","object-ungroup","paint-brush","paper-plane","paper-plane-o","paw","pencil","pencil-square","pencil-square-o","percent","phone","phone-square","photo","picture-o","pie-chart","plane","plug","plus","plus-circle","plus-square","plus-square-o","podcast","power-off","print","puzzle-piece","qrcode","question","question-circle","question-circle-o","quote-left","quote-right","random","recycle","refresh","registered","remove","reorder","reply","reply-all","retweet","road","rocket","rss","rss-square","s15","search","search-minus","search-plus","send","send-o","server","share","share-alt","share-alt-square","share-square","share-square-o","shield","ship","shopping-bag","shopping-basket","shopping-cart","shower","sign-in","sign-language","sign-out","signal","signing","sitemap","sliders","smile-o","snowflake-o","soccer-ball-o","sort","sort-alpha-asc","sort-alpha-desc","sort-amount-asc","sort-amount-desc","sort-asc","sort-desc","sort-down","sort-numeric-asc","sort-numeric-desc","sort-up","space-shuttle","spinner","spoon","square","square-o","star","star-half","star-half-empty","star-half-full","star-half-o","star-o","sticky-note","sticky-note-o","street-view","suitcase","sun-o","support","tablet","tachometer","tag","tags","tasks","taxi","television","terminal","thermometer","thermometer-0","thermometer-1","thermometer-2","thermometer-3","thermometer-4","thumb-tack","thumbs-down","thumbs-o-down","thumbs-o-up","thumbs-up","ticket","times","times-circle","times-circle-o","times-rectangle","times-rectangle-o","tint","toggle-down","toggle-left","toggle-off","toggle-on","toggle-right","toggle-up","trademark","trash","trash-o","tree","trophy","truck","tty","tv","umbrella","universal-access","university","unlock","unlock-alt","unsorted","upload","user","user-circle","user-circle-o","user-o","user-plus","user-secret","user-times","users","vcard","vcard-o","video-camera","volume-control-phone","volume-down","volume-off","volume-up","warning","wheelchair","wheelchair-alt","wifi","window-close","window-close-o","window-maximize","window-minimize","window-restore","wrench","align-center","align-justify","align-left","align-right","bold","chain","chain-broken","clipboard","columns","copy","cut","dedent","eraser","file","file-o","file-text","file-text-o","files-o","floppy-o","font","header","indent","italic","link","list","list-alt","list-ol","list-ul","outdent","paperclip","paragraph","paste","repeat","rotate-left","rotate-right","save","scissors","strikethrough","subscript","superscript","table","text-height","text-width","th","th-large","th-list","underline","undo","unlink","etsy"]}}}),r=o,s=a("2877"),n=Object(s["a"])(r,l,i,!1,null,null,null);t["a"]=n.exports}}]);
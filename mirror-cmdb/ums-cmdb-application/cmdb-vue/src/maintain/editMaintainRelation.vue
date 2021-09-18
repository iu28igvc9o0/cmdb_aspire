<template>
<div style="width:100%;height:100%;" id="Jtopo_content_all">

 <el-form :model="ruleForm" ref="ruleForm"  label-position="top" label-width="80px"  class="demo-ruleForm" style="white-space:nowrap;">

<el-row >
    <el-col :span="18" style="border-right:2px solid #d1dbe5;height:100%;padding:20px 10px 0px 10px;text-align:left;min-height:300px;">
            <div style="padding:20px 0px 0px 0px;text-align:center;" v-if="instanceName!=''"><h4>{{instanceName}}</h4></div>
	  <div  class="container" style="padding:0px 30px;float:right;">
	               <div style="width:55%;">
		                <div style="text-align:right;position: relative;">
		                    <el-button 	type="primary"    size="small"  @click="stage.zoomOut()"><i class="fa fa-plus" aria-hidden="true"></i></el-button>
		                </div>
		                <div style="text-align:right;padding-top:20px;position: relative;">
		                    <el-button  type="primary"	size="small"  @click="stage.zoomIn()"><i class="fa fa-minus" aria-hidden="true"></i></el-button>
		                </div>
	                </div>
	  </div>
	    <div  id="Jtopo_content" >
	    <canvas class="center-block" width="1050" height="700" id="canvas" style=" solid #8c8c8c;" fit="true">

	    </canvas>
	        <textarea id="jtopo_textfield" style="display:none;width: 60px;position: absolute;" onkeydown="if(event.keyCode==13)this.blur();"></textarea>
	     <ul id="contextmenu"  style="display:none;border:1px solid #1E90FF;font-size:small;">

	        <li  v-if="!isRoot && currentNode != null"><el-button type="primary" @click="deleteOne" size='mini'><i class="el-icon-delete"></i>删除节点</el-button></li>
	    </ul>
	     </div>
	</el-col>
	<el-col :span="5" style="padding:20px 20px 0px 20px;text-align:left; ">
	      <el-form-item label=""    >
		      <el-input style="width:80%;" icon="search" v-model="keyword"  placeholder="请输入内容"></el-input>
			  <el-select  style="width:45%;" v-model="selectModuleId">
					    <el-option-group
					      v-for="group in selectData"
					      :key="group.id"
					      :label="group.name">
					      <el-option
					        v-for="item in group.item"
					        :key="item.id"
					        :label="item.name"
					        :value="item.id">
					      </el-option>
					    </el-option-group>
			 </el-select>
	      </el-form-item>

          <el-row  type="flex" >


		  <el-col :span="23">
          <el-form-item label="配置项名称"    prop="checkboxModules" >

		     <el-checkbox-group v-if="isShowCheckbox  && ruleForm.moduleList.length > 0" v-model="ruleForm.checkboxModules"   >
		         <div v-for="col in ruleForm.moduleList">
		         	<el-row type="flex"  justify="center">
				     <el-col :span="23">

					  <el-checkbox :label="col.id" :key="col.id" >{{col.name}}</el-checkbox>
					 </el-col>
				     <el-col style="margin-left:90px;" :span="1">
					  <el-tag type="primary">{{col.moduleName}}</el-tag>
					 </el-col>
					</el-row>
				</div>
		      </el-checkbox-group>

		    <el-radio-group v-if="!isShowCheckbox && ruleForm.moduleList.length > 0"  v-model="ruleForm.checkRadioModule" >
				 <div v-for="(item, index) in ruleForm.moduleList" >
				 <el-row style="margin-top:10px;" type="flex"  justify="center">
				     <el-col :span="23">
				      <el-radio :label="item.id">{{item.name}}</el-radio>
				     </el-col>
				     <el-col style="margin-left:90px;" :span="1">
				      <el-tag type="primary" >{{item.moduleName}}</el-tag>
				     </el-col>
				 </el-row>
				  </div>
				</el-radio-group>
	      </el-form-item>
	       </el-col>
	       <el-col style="margin-left:40px;" :span="1">
	       	 <el-checkbox  size="mini"  @change="handleCheckAllChange">已选</el-checkbox>
	       </el-col>
	       </el-row>


	        <div class="block"  v-if="selectModuleId != ''  && ruleForm.moduleList.length > 0"  style="padding:0px 0px 0px 120px;"align="center">
	          <el-pagination
			      @size-change="handleSizeChange"
			      @current-change="handleCurrentChange"
			      :current-page.sync="paginationData.currentPage"
			      :page-size="paginationData.pageSize"
			      layout="prev, pager, next"
			      :total="paginationData.total">
			    </el-pagination>


		        <el-form-item align="right" style="margin-top:15px;">
		            <el-button type="primary" @click="onSubmit" >保存</el-button>
		            <el-button @click="closeDialog">取消</el-button>
		        </el-form-item>
            </div>
    </el-col>
</el-row>
</el-form>
  </div>
</template>

<script type="text/javascript">
import '../../static/js/jtopo-0.4.8-min';
import router from '../router'
export
default {
    props: ['instanceId', 'moduleId', 'circleId','instanceName','listType'],
    components: {},
    data() {
        return {
            msg: '',
            layouttype: "circle",
            sourceInstanceId: '',
            sourceInstance: {},
            dataList: [],
            isRoot: false,
            scene: {},
            currentNode: {},
            IntargetModuleId: '',
            InsourceModuleId: '',
            linkTip: '',
            stage: {},
            cloudNode: {},

            keyword: "",
            paginationData: {
                currentPage: 1,
                total: 0,
                pageSize: 9,
                selectPageSizes: [9, 12, 15, 18],
                sort: '',
                order: '',
            },
            selectData: [],
            selectModuleId: "",
            ruleForm: {
                checkedIds: [],
                moduleList: [],
                checkboxModules: [],
                checkRadioModule: "",
                moduleRelationId: "",
            },
            restriction: "",
            direction: "",
            isShowCheckbox: true,
            isSelect: false,
        }
    },
    watch: {
        selectModuleId: "modulePage",
        keyword: "handlemodulePage",
        isSelect: "handlemodulePage",

    },
    mounted: function () {
            this.initData(this.instanceId);
            this.initScene();
            this.draw();

        },

        methods: {
            initScene: function () {

                    var canvas = document.getElementById('canvas');

                    var stage = new JTopo.Stage(canvas);
                    stage.wheelZoom = 1.2;
                    stage.mode = 'select';
                    var scene = new JTopo.Scene();
                    stage.add(scene);
                    this.scene = scene;
                    this.stage = stage;

                },

                initData: function (id) {
                    debugger;
                    //下拉框树
                    $.ajax({
                        url: '/cmdb/circle/getModuleTree',
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: "",
                    }).done(function (data) {
                        debugger;
                        this.selectData = data;
                    }.bind(this));

                    //拓扑图
                    this.sourceInstanceId = id;
                    var query_data = {
                        "sourceInstanceId": id,
                    };
                    $.ajax({
                        url: '/cmdb/relatiomap/getInstanceRelationByCondition',
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.sourceInstance = data.sourceInstance;
                        this.dataList = data.dataList;
                    }.bind(this));


                    //实例列表
                    this.modulePage();


                },

                forDataList: function (dataList, isAdd) {
                    debugger;
                    const self = this;
                    for (var i = 0; i < dataList.length; i++) {

                        //向上关系
                        if (dataList[i].targerInstanceId == this.sourceInstanceId) {
                            var node = this.newNode(dataList[i].sourceInstanceName, dataList[i].sourceInstanceId, dataList[i].id, dataList[i].sourceIconUrl);
                            this.scene.add(node);
                            var link = this.newLink(this.cloudNode, node, dataList[i].relationName, this.dataList[i].restrictionDec, 'up',dataList[i].builtin);
                            this.scene.add(link);
                            link.addEventListener('mouseover', function (event) {
                                self.linkTip = this.lintType;
                                $("#tips").css({
                                    top: event.clientY + 'px',
                                    left: event.clientX + 'px'
                                }).show();
                            });

                            node.addEventListener('mouseup', function (event) {
                                debugger;
                                self.currentNode = this;
                                self.isRoot = false;
                            });
                            //向下关系
                        } else if (dataList[i].sourceInstanceId == this.sourceInstanceId) {
                            var node = this.newNode(dataList[i].targetInstanceName, dataList[i].targerInstanceId, dataList[i].id, dataList[i].targetIconUrl);
                            this.scene.add(node);
                            var link = this.newLink(this.cloudNode, node, dataList[i].relationName, dataList[i].restrictionDec, 'down',dataList[i].builtin);
                            this.scene.add(link);
                            link.addEventListener('mouseover', function (event) {
                                self.linkTip = this.lintType;
                                $("#tips").css({
                                    top: event.clientY + 'px',
                                    left: event.clientX + 'px'
                                }).show();
                            });

                            node.addEventListener('mouseup', function (event) {
                                debugger;
                                self.currentNode = this;
                                self.isRoot = false;
                            });
                        } else {}
                    }
                },

                draw: function () {
                    const self = this;

                    var currentNode = null;
                    var cloudNode = new JTopo.Node(this.sourceInstance.sourceInstanceName);
                    cloudNode.id = this.sourceInstance.sourceInstanceId;
                    cloudNode.fontColor = '30,144,255';
				    cloudNode.font = ' 12px 宋体  italic Arial';
                    cloudNode.setSize(36, 36);
                    cloudNode.textPosition = 'Bottom_Center';
                    cloudNode.textOffsetY = 0;
                    cloudNode.showSelected = false; // 不显示选中矩形
                    cloudNode.layout = {
                        type: 'circle',
                        radius: 220
                    };
                    cloudNode.setLocation(360, 230);


                   var image = new Image(16, 16);
                   image.src = '../..'+this.sourceInstance.sourceIconUrl;
	                cloudNode.percent = 0.8;
	                cloudNode.beginDegree = 0;
	                cloudNode.width = cloudNode.height = 50;
	                cloudNode.paint = function(g){
	                    g.save();

	                    g.beginPath();
	                    g.fillStyle = 'rgba(30,144,255,' + this.alpha + ')';
	                    g.moveTo(0,0);
	                    g.arc(0, 0, this.width/2-1, this.beginDegree, this.beginDegree + 2*Math.PI);
	                    g.fill();
	                    g.closePath();

	                    g.beginPath();
	                    g.drawImage(image, -15, -15,30,30);
	                    g.closePath();

	                    g.restore();
	                    this.paintText(g);
	                };

                    cloudNode.addEventListener('mouseup', function (event) {
                        self.currentNode = this;
                        self.isRoot = true;
                    });
                    this.cloudNode = cloudNode;
                    self.scene.add(cloudNode);

                    this.forDataList(this.dataList, false);


                    JTopo.layout.layoutNode(self.scene, cloudNode, true);



                    self.scene.addEventListener('mouseup', function (e) {
                        debugger;

                        if (e.target && e.target.layout) {
                            JTopo.layout.layoutNode(self.scene, e.target, true);
                        }
                        if (e.target instanceof JTopo.Node) {

                        } else {
                            self.currentNode = null;
                        }
                        if (e.button == 2 && self.currentNode != null && !this.isRoot) { // 右键
                            // 当前位置弹出菜单（div）
                            $("#contextmenu").css({
                                top: event.clientY + 'px',
                                left: event.clientX + 'px'
                            }).show();
                        } else {
                            $("#contextmenu").hide();
                        }
                    });
                    debugger;




                },

                setV: function (value) {
                    this.layouttype = value;
                },
                deleteOne() {
                    if (this.currentNode != null && this.currentNode instanceof JTopo.Node) {
                        var paramData = {
                            "id": this.currentNode.relationId,
                            "sourceInstanceId": this.cloudNode.id,
                            "circleId":this.circleId,
                        }
                        debugger;
                        var submitUrl = "/cmdb/relatiomap/deleteInstanceRelation";
                        jQuery.ajax({
                            url: submitUrl,
                            type: "POST",
                            cache: false,
                            async: false,
                            traditional: true,
                            data: paramData,
                            success: function (json, textStatus) {
                                if (json.success == true) {
                                    this.currentNode = null;
                                    this.scene.clear();
                                    this.initData(this.instanceId);
                                    this.draw();



                                    this.$notify({
                                        title: '提示',
                                        message: '删除成功',
                                        type: 'success',
                                        duration: 3000
                                    });


                                } else {
                                    this.$notify({
                                        title: '提示',
                                        message: json.msg,
                                        type: 'error',
                                        duration: 3000
                                    });
                                }
                            }.bind(this),
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                this.$notify({
                                    title: '提示',
                                    message: '删除失败',
                                    type: 'error',
                                    duration: 3000
                                });
                            }.bind(this),
                        });

                    }
                    JTopo.layout.layoutNode(this.scene, this.cloudNode, true);
                },
                upTo() {
                    if (this.currentNode != null && this.currentNode instanceof JTopo.Node) {
                        debugger;
                        this.IntargetModuleId = this.currentNode.id;

                    }
                },
                downTo() {
                    if (this.currentNode != null && this.currentNode instanceof JTopo.Node) {
                        debugger;
                        this.InsourceModuleId = this.currentNode.id;

                    }
                },
                closeDialog: function () {

                    this.closeWin();
                },
                closeWin: function () {
                    this.$emit("editMointor");
                },

                flushData: function (dataList) {
                    this.restriction = "";
                    this.ruleForm.moduleRelationId = "";
                    this.ruleForm.checkboxModules = [];
                    this.ruleForm.checkRadioModule = "";
                    this.isShowCheckbox = true;
                    this.direction = "";
                    this.scene.clear();
                    this.initData(this.instanceId);
                    this.draw();
                    var nodeList = this.scene.getDisplayedNodes();
                    for (var i = 0; i < dataList.length; i++) {
                        for (var j = 0; j < nodeList.length; j++) {

                            if (nodeList[j].id == dataList[i]) {
                                                   var image = new Image(16, 16);
								                   image.src = '../..'+nodeList[j].path;
									                nodeList[j].percent = 0.8;
									                nodeList[j].beginDegree = 0;
									                nodeList[j].width = nodeList[j].height = 50;
									                nodeList[j].paint = function(g){
									                    g.save();

									                    g.beginPath();
									                    g.fillStyle = 'rgba(0,255,0,' + this.alpha + ')';
									                    g.moveTo(0,0);
									                    g.arc(0, 0, this.width/2-1, this.beginDegree, this.beginDegree + 2*Math.PI);
									                    g.fill();
									                    g.closePath();

									                    g.beginPath();
									                    g.drawImage(image, -15, -15,30,30);
									                    g.closePath();

									                    g.restore();
									                    this.paintText(g);
									                };
                            }
                        }
                    }
                },

                addClose() {
                    this.$refs.addView.child();
                },

                newNode: function (name, id, relationId, path) {
                    var node = new JTopo.Node(name);
                    node.id = id;
                    node.path = path;
                    node.relationId = relationId;
                    node.fontColor = '30,144,255';
				    node.font = ' 12px 宋体  italic Arial';
                    node.setSize(36, 36);
                    node.textPosition = 'Bottom_Center';
                    node.textOffsetY = 0;
                    node.showSelected = false; // 不显示选中矩形

                   var image = new Image(16, 16);
                   image.src = '../..'+path;
	                node.percent = 0.8;
	                node.beginDegree = 0;
	                node.width = node.height = 50;
	                node.paint = function(g){
	                    g.save();

	                    g.beginPath();
	                    g.fillStyle = 'rgba(30,144,255,' + this.alpha + ')';
	                    g.moveTo(0,0);
	                    g.arc(0, 0, this.width/2-1, this.beginDegree, this.beginDegree + 2*Math.PI);
	                    g.fill();
	                    g.closePath();

	                    g.beginPath();
	                    g.drawImage(image, -15, -15,30,30);
	                    g.closePath();

	                    g.restore();
	                    this.paintText(g);
	                };
                    node.layout = {
                        type: this.layouttype,
                        radius: 220
                    };
                    return node;

                },
                newLink: function (nodeA, nodeZ, name, dec, upOrDown,builtin) {
                    var link = new JTopo.Link(nodeA, nodeZ, name);
                    link.arrowsRadius = 10; //箭头大小
                    link.lineWidth = 1; // 线宽
                    link.lintType = dec;
                    link.font = ' 12px 宋体  italic Arial';
	                link.bundleOffset = 60; // 折线拐角处的长度
	                link.bundleGap = 20; // 线条之间的间隔
	                link.textOffsetY = 12; // 文本偏移量（向下3个像素）
                    link.showSelected = false;

                    var color = '30,144,255'
	                if(builtin == 'true'){
	                color='0,255,255';
	                }
                    if (upOrDown == 'up') {
                         link.strokeColor = color;
							     link.getStartPosition = function() {
									var a;
									return null != this.arrowsRadius && (a = (function(thisl){
										var b=thisl.nodeA,c=thisl.nodeZ;
										var d = JTopo.util.lineF(b.cx, b.cy, c.cx, c.cy),
												e = b.getBound(),
												f = JTopo.util.intersectionLineBound(d, e);
										return f
									})(this)),
									null == a && (a = {
										x: this.nodeA.cx,
										y: this.nodeA.cy
									}),
											a
								};
								link.paintPath = function(a, b) {
									if (this.nodeA === this.nodeZ) return void this.paintLoop(a);
									a.beginPath(),

									a.fillStyle = "rgb("+color+")";
									a.fillRect( b[0].x+(b[1].x-b[0].x)/2-20, b[0].y+(b[1].y-b[0].y)/2-10, 40,20);
									a.font = "12px 宋体  italic Arial";
									a.moveTo(b[0].x, b[0].y);
									for (var c = 1; c < b.length; c++) {
										null == this.dashedPattern ? (
												(null==this.PointPathColor?a.lineTo(b[c].x, b[c].y):a.JtopoDrawPointPath(b[c - 1].x, b[c - 1].y, b[c].x, b[c].y, a.strokeStyle,this.PointPathColor))
										) : a.JTopoDashedLineTo(b[c - 1].x, b[c - 1].y, b[c].x, b[c].y, this.dashedPattern)
									};
									if (a.stroke(), a.closePath(), null != this.arrowsRadius) {
										var d = b[b.length - 2],
												e = b[b.length - 1];
										this.paintArrow(a, e, d)
									}
								};
                    } else {
                           link.strokeColor = color;

                           link.paintPath = function(a, b) {
									if (this.nodeA === this.nodeZ) return void this.paintLoop(a);
									a.beginPath(),

									a.fillStyle = "rgb("+color+")";
									a.fillRect( b[0].x+(b[1].x-b[0].x)/2-20, b[0].y+(b[1].y-b[0].y)/2-10, 40,20);
									a.font = "12px 宋体  italic Arial";
									a.moveTo(b[0].x, b[0].y);
									for (var c = 1; c < b.length; c++) {
										null == this.dashedPattern ? (
												(null==this.PointPathColor?a.lineTo(b[c].x, b[c].y):a.JtopoDrawPointPath(b[c - 1].x, b[c - 1].y, b[c].x, b[c].y, a.strokeStyle,this.PointPathColor))
										) : a.JTopoDashedLineTo(b[c - 1].x, b[c - 1].y, b[c].x, b[c].y, this.dashedPattern)
									};
									if (a.stroke(), a.closePath(), null != this.arrowsRadius) {
										var d = b[b.length - 2],
												e = b[b.length - 1];
										this.paintArrow(a, d, e)
									}
								};
                    };
                    return link;

                },

                //选择每页显示记录数
                handleSizeChange(size) {
                    this.paginationData.pageSize = size;
                    var maxPage = Math.ceil(this.paginationData.total / size);
                    if (this.paginationData.currentPage > maxPage) {
                        this.paginationData.currentPage = maxPage;
                    } else {
                        this.handlemodulePage();
                    }
                },
                //分页
                handleCurrentChange(val) {
                    this.paginationData.currentPage = val;
                    this.handlemodulePage();
                },
                modulePage() {
                    if (this.selectModuleId == null || this.selectModuleId == '') {
                        return;
                    }
                    debugger;
                    var query_data = {
                        "pageNumber": this.paginationData.currentPage,
                        "pageSize": this.paginationData.pageSize,
                        "sort": this.paginationData.sort,
                        "orderBy": this.paginationData.order,
                        "queryInstanceName": this.keyword,
                        "instanceId": this.instanceId,
                        "selectModuleId": this.selectModuleId,
                        "instanceModuleId": this.moduleId,
                        "isSelect": this.isSelect,
                        "checkboxModules":this.isShowCheckbox && this.ruleForm.checkboxModules != null && this.ruleForm.checkboxModules.length > 0?this.ruleForm.checkboxModules.join(","):"",
                        "checkRadioModule":this.ruleForm.checkRadioModule,
                        "isInit":'true',

                    };
                    var sysCodeUrl = '/cmdb/relatiomap/getInstanceRetionByModule';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.paginationData.total = data.total;
                        this.ruleForm.moduleList = data.dataList;
                        this.restriction = data.restriction;
                        this.direction = data.direction;
                        this.ruleForm.checkRadioModule = data.checkRadioModule;
                        this.ruleForm.checkboxModules = data.checkboxModules;
                        this.ruleForm.moduleRelationId = data.moduleRelationId;
                        this.isShowCheckbox = data.isShowCheckbox;

                    }.bind(this));
                },

                handlemodulePage() {
                    if (this.selectModuleId == null || this.selectModuleId == '') {
                        return;
                    }
                    debugger;
                    var query_data = {
                        "pageNumber": this.paginationData.currentPage,
                        "pageSize": this.paginationData.pageSize,
                        "sort": this.paginationData.sort,
                        "orderBy": this.paginationData.order,
                        "queryInstanceName": this.keyword,
                        "instanceId": this.instanceId,
                        "selectModuleId": this.selectModuleId,
                        "instanceModuleId": this.moduleId,
                        "isSelect": this.isSelect,
                        "checkboxModules":this.isShowCheckbox && this.ruleForm.checkboxModules != null && this.ruleForm.checkboxModules.length > 0?this.ruleForm.checkboxModules.join(","):"",
                        "checkRadioModule":this.ruleForm.checkRadioModule,
                         "isInit":'false',
                    };
                    var sysCodeUrl = '/cmdb/relatiomap/getInstanceRetionByModule';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.paginationData.total = data.total;
                        this.ruleForm.moduleList = data.dataList;
                        this.ruleForm.checkRadioModule = data.checkRadioModule;
                        this.ruleForm.checkboxModules = data.checkboxModules;

                    }.bind(this));
                },

                resetForm(formName) {
                    this.$refs[formName].resetFields();

                },


                onSubmit() {
                    debugger;
                    if ((this.isShowCheckbox && this.ruleForm.checkboxModules.length <= 0) || (!this.isShowCheckbox && this.ruleForm.checkRadioModule == '')) {
                        this.$notify({
                            title: '提示',
                            message: '没有选择项！',
                            type: 'info',
                            duration: 3000
                        });
                        return false;
                    } else {
                        this.add();
                    }



                },
                add() {
                    debugger;
                    if (!this.checkRelationUse()) {
                    return;
                    }

                        var paramData = {
                            "restriction": this.restriction,
                            "moduleRelationId": this.ruleForm.moduleRelationId,
                            "checkboxModules": this.ruleForm.checkboxModules,
                            "checkRadioModule": this.ruleForm.checkRadioModule,
                            "instanceId": this.instanceId,
                            "isShowCheckbox": this.isShowCheckbox,
                            "circleId": this.circleId,
                            "direction": this.direction,
                        }
                        debugger;
                        var submitUrl = "/cmdb/relatiomap/addInstanceRelation";
                        jQuery.ajax({
                            url: submitUrl,
                            type: "POST",
                            cache: false,
                            async: false,
                            traditional: true,
                            data: paramData,
                            success: function (json, textStatus) {
                                if (json.success == true) {
                                    this.$notify({
                                        title: '提示',
                                        message: '添加成功',
                                        type: 'success',
                                        duration: 3000
                                    });
                                    this.flushData(json.dataList);

                                } else {
                                    this.$notify({
                                        title: '提示',
                                        message: json.msg,
                                        type: 'error',
                                        duration: 3000
                                    });
                                }
                            }.bind(this),
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                this.$notify({
                                    title: '提示',
                                    message: '添加失败',
                                    type: 'error',
                                    duration: 3000
                                });
                            }.bind(this),
                        });

                },
                checkRelationUse() {
                     var result = false;
                    //验证是否存在关联
                    var ids = "";
                    if (this.isShowCheckbox) {
                        for (var i = 0; i < this.ruleForm.checkboxModules.length; i++) {
                            ids += this.ruleForm.checkboxModules[i] + ",";
                        }
                    } else {
                        ids += this.ruleForm.checkRadioModule + ",";
                    }
                    debugger;
                    var goHeadUrl = "/cmdb/relatiomap/checkInstanceRelation";
                    $.ajax({
                        url: goHeadUrl,
                        type: "POST",
                        data: {
                            'instanceIds': ids,
                            'targetModuleId': this.moduleId,
                            'targetInstanceId': this.instanceId,
                            'restriction':this.restriction,
                            "direction": this.direction,
                        },
                        dataType: "json",
                        async: false,
                        success: function (json, textStatus) {
                            debugger;
                            if (json.success == true) {
                                result =  true;
                            } else {
                                this.$notify({
                                    title: '提示',
                                    message: json.erroMsg,
                                    type: 'error',
                                    duration: 3000
                                });
                                result =  false;
                            }
                        }.bind(this),
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            debugger;
                            this.$notify({
                                title: '提示',
                                message: '请求失败',
                                type: 'error',
                                duration: 3000
                            });
                            result =  false;
                        }.bind(this)
                    });

                return result;


                },
                closeBack() {
                    this.$emit("childBindAdd");
                },
                handleCheckAllChange(event) {
                debugger;
                    this.isSelect = !this.isSelect;
                    this.paginationData.currentPage = 1;
                },
        }

}
</script>

<style>
  .text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }

  .box-card {
    width: 180px;
  }

.el-row {
    margin-bottom: 1px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }

    #contextmenu {
        background: #1E90FF;
   position: fixed;
        list-style: none;
        margin: 0;
        padding: 0;
        border-radius:3px;

    }

    #contextmenu li a {
        display: block;
        padding: 1px;
        color:White;

    #contextmenu li a:hover {
        background: #1E90FF;
        color:White;
    }
        cursor: pointer;
    }
</style>

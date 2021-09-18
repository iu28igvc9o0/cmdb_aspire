<template>
<div style="width:100%;height:100%;" id="Jtopo_content_all">
  <div  class="container" style="padding:0px 30px;">
               <div >


                <div style="text-align:right;padding-top:5px;">
                	<div style="display:inline;">         <div id="syscircle" style="float:left">  </div><span style="float:left;font-size:12px;">系统内置关系</span> </div> 
                    <div style="float:right;"> <el-button 	type="primary"    size="small"  @click="stage.zoomOut()"><i class="fa fa-plus" aria-hidden="true"></i></el-button></div>
                <div id="id4" style="clear:both"></div>
                </div>
                <div style="text-align:right;padding-top:5px;">
                	<div style="display:inline;">  		 <div id="norcircle" style="float:left">  </div><span style="float:left;font-size:12px;">非系统内置关系</span> </div> 
                    <div style="float:right;"> <el-button  type="primary"	size="small"  @click="stage.zoomIn()"><i class="fa fa-minus" aria-hidden="true"></i></el-button></div>
                </div>
                </div>
    </div>
    <div  id="Jtopo_content" >             
    <canvas class="center-block" width="1300" height="1050" id="canvas" style=" solid #8c8c8c;" fit="true">  
    </canvas>  
        <textarea id="jtopo_textfield" style="display:none;width: 60px;position: absolute;" onkeydown="if(event.keyCode==13)this.blur();"></textarea>

        <ul id="contextmenu"  style="display:none;border:1px solid #1E90FF;font-size:12px; font-weight: normal;white-space:nowrap;">   
	    	     <li v-if="isRoot"><el-button type="primary" @click="upTo" size='mini'><i class="el-icon-caret-top"></i>上层关系</el-button></li>
	    	      <li v-if="isRoot"><el-button type="primary" @click="downTo" size='mini'><i class="el-icon-caret-bottom"></i>下层关系</el-button></li>
	    	      <li v-if="!isRoot && currentNode != null"><el-button type="primary" @click="deleteOne" size='mini'><i class="el-icon-delete"></i>删除节点</el-button></li>
	    </ul>
	    
	    <ul id="tips"  style="display:none;border:1px solid #1E90FF;font-size:12px; font-weight: normal;">   
        <li><a>{{linkTip}}</a></li>
	    </ul>
        
        
        <el-dialog title="添加上级模型" v-if="addUpViewDialog" v-model="addUpViewDialog"  size="small" @close="addClose">
            <addView :IntargetModuleId="IntargetModuleId" ref="addView" v-on:childBindAdd="closeDialog"></addView>
        </el-dialog>
        <el-dialog title="添加下级模型" v-if="addDownViewDialog" v-model="addDownViewDialog"  size="small" @close="addClose">
            <addView :InsourceModuleId="InsourceModuleId" ref="addView" v-on:childBindAdd="closeDialog"></addView>
        </el-dialog>
     </div>    
  </div>
</template>

<script type="text/javascript">
import '../../static/js/jtopo-0.4.8-min';
import addView from './addModerRelation.vue';
export
default {
    props: ['pid'],
    components: {
        addView,
    },
    data() {
        return {
            msg: '',
            layouttype: "circle",
            sourceModuleId: '',
            sourceModule: {},
            dataList: [],
            addUpViewDialog: false,
            addDownViewDialog: false,
            isRoot: false,
            scene: {},
            currentNode: {},
            IntargetModuleId: '',
            InsourceModuleId: '',
            linkTip: '',
            stage: {},
            cloudNode: {},
            builtin: 'false',
        }
    },
    mounted: function () {

            debugger;
            this.initData(this.pid, this.builtin);
            this.initScene();
         if(this.pid !== null && this.pid !== undefined && this.pid !== ''){
            this.draw();
         }
        },

        watch: {
            builtin: function () {
                this.scene.clear();
             if(this.pid !== null && this.pid !== undefined && this.pid !== ''){
                this.initData(this.pid, this.builtin);
                this.draw();
                }
            },
            pid: function (curVal,oldVal) {
                this.scene.clear();
                if(this.pid !== null && this.pid !== undefined && this.pid !== ''){
                this.initData(this.pid, this.builtin);
                this.draw();
                }
            },
        },

        methods: {
            initScene: function (id) {

                    var canvas = document.getElementById('canvas');

                    var stage = new JTopo.Stage(canvas);
                    stage.wheelZoom = 1.2;
                    stage.mode = 'select';
                    var scene = new JTopo.Scene();
                    stage.add(scene);
                    this.scene = scene;
                    this.stage = stage;

                },

                initData: function (id, builtin) {
                    this.sourceModuleId = id;
                    var query_data = {
                        "sourceModuleId": id,
                        "builtin": builtin
                    };
                    var sysCodeUrl = '/cmdb/relatiomap/getRelationByCondition';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.sourceModule = data.sourceModule;
                        this.dataList = data.dataList;
                    }.bind(this));

                },

                forDataList: function (dataList, isAdd) {
                    debugger;
                    const self = this;
                    for (var i = 0; i < dataList.length; i++) {

                        //向上关系
                        if (dataList[i].targetModuleId == this.sourceModuleId) {
                            var node = this.newNode(dataList[i].sourceModuleName, dataList[i].sourceModuleId, dataList[i].id, dataList[i].sourceIconUrl, dataList[i].insRelationCount, dataList[i].sourceBuiltin);
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
                            link.addEventListener('mouseout', function (event) {
                                $("#tips").hide();
                            });

                            node.addEventListener('mouseup', function (event) {
                                self.currentNode = this;
                                self.isRoot = false;
                            });
                            //向下关系
                        } else if (dataList[i].sourceModuleId == this.sourceModuleId) {
                            var node = this.newNode(dataList[i].targetModuleName, dataList[i].targetModuleId, dataList[i].id, dataList[i].targetIconUrl, dataList[i].insRelationCount,dataList[i].targetBuiltin);
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
                            link.addEventListener('mouseout', function (event) {
                                $("#tips").hide();
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
                    debugger;
                    var currentNode = null;
                    var cloudNode = new JTopo.Node(this.sourceModule.sourceModuleName);
                    cloudNode.id = this.sourceModule.sourceModuleId;
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
                    cloudNode.setLocation(460, 230);
                    
                   var image = new Image(16, 16);  
                   image.src = '../..'+this.sourceModule.sourceIconUrl;
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
                        if (e.button == 2 && self.currentNode != null && ((self.isRoot==false && self.currentNode.insRelationCount==0) || self.isRoot==true)) { // 右键
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
                        }
                        debugger;
                        var submitUrl = "/cmdb/relatiomap/deleteModuleRelation";
                        jQuery.ajax({
                            url: submitUrl,
                            type: "POST",
                            cache: false,
                            async: false,
                            traditional: true,
                            data: paramData,
                            success: function (json, textStatus) {
                                if (json.success == true) {
                                    this.scene.clear();
                                    this.initData(this.pid, this.builtin);
                                    this.draw();
                                    this.currentNode = null;

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
                    $("#contextmenu").hide();
                },
                upTo() {
                    if (this.currentNode != null && this.currentNode instanceof JTopo.Node) {
                        debugger;
                        this.IntargetModuleId = this.currentNode.id;
                        this.addUpViewDialog = true;

                    }
                    $("#contextmenu").hide();
                },
                downTo() {
                    if (this.currentNode != null && this.currentNode instanceof JTopo.Node) {
                        debugger;
                        this.InsourceModuleId = this.currentNode.id;
                        this.addDownViewDialog = true;

                    }
                    $("#contextmenu").hide();
                },
                closeDialog: function (dataList) {
                    this.addUpViewDialog = false;
                    this.addDownViewDialog = false;

                    this.scene.clear();
                    this.initData(this.pid, this.builtin);
                    this.draw();
                    var nodeList = this.scene.getDisplayedNodes();
                    debugger;
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

                newNode: function (name, id, relationId, path, insRelationCount, builtin) {
                    var node = new JTopo.Node(name);
                    node.id = id;
                    node.path = path;
                    node.insRelationCount = insRelationCount;
                    node.fontColor = '30,144,255';
				    node.font = ' 12px 宋体  italic Arial';	
                    node.relationId = relationId;
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
                    debugger;
                    link.arrowsRadius = 10; //箭头大小
                    link.lineWidth = 1; // 线宽
                    link.lintType = dec;
                    link.font = ' 12px 宋体  italic Arial';
	                link.bundleOffset = 60; // 折线拐角处的长度
	                link.bundleGap = 20; // 线条之间的间隔
	                link.textOffsetY = 12; // 文本偏移量（向下3个像素）
	                
	                var color = '30,144,255'
	                if(builtin == 'true'){
	                color='0,255,255';
	                }
                    link.showSelected = false;
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
                    }

                    return link;

                },
        }

}
</script>

<style>  

    
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
    
   
       #tips {  
        background: #1E90FF;  
   position: fixed;
        list-style: none;  
        margin: 0;  
        padding: 0;  
        border-radius:3px;

    }  
   
    #tips li a {  
        display: block;  
        padding: 1px;  
        color:White;
  
    #tips li a:hover {  
        background: #1E90FF;  
        color:White;
    }       
        cursor: pointer;  
    }  
    
    #syscircle {
    width: 20px;
    height: 20px;
    background: 	#00FFFF;
    -moz-border-radius: 50px;
    -webkit-border-radius: 50px;
    border-radius: 50px;
}
    #norcircle {
    width: 20px;
    height: 20px;
    background: #1E90FF;
    -moz-border-radius: 50px;
    -webkit-border-radius: 50px;
    border-radius: 50px;
}

</style>
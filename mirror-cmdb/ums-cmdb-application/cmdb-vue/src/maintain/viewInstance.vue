<template>
<div style="width:100%;height:100%;" id="Jtopo_content_all">
       
<el-row>
    <el-col :span="leftCol" style="border-right:1px solid #d1dbe5;height:100%;padding:10px 10px 0px 10px;text-align:left;">
    	  <div  class="container" style="padding:12px 30px;">
	               <div style="float:left;">
	                <div style="text-align:right">
                                        <el-button style="padding:8px 16px 5px 16px;" v-if="listType=='maintain'" icon="edit" @click="edit">编辑</el-button>
                                        <el-button style="padding:8px 16px 5px 16px;" v-if="listType=='maintain'"  icon="share" @click="handUp">移交</el-button>
                                         <el-button style="padding:8px 16px 5px 16px;" v-if="listType=='repertory'"  icon="share" @click="config">认领</el-button>
                                        <el-button style="padding:8px 16px 5px 16px;"  icon="delete" @click="Delete">删除</el-button>
	
	                </div>
	                </div>
	      </div>
						                
	       <editInstance ref="editInstance"    :instanceId="instanceId"  :instanceName="instanceName"  :moduleId="moduleId" :isOnlyRead="true" v-on:childBindAdd="closeDialog"></editInstance>
	</el-col>
	<el-col :span="rightCol" style="height:100%;padding:10px 20px 0px 20px;">	  
<el-button type="primary" v-if="!isHideLog" icon="arrow-right" @click="hideLog">隐藏日志</el-button>
<el-button type="primary" v-if="isHideLog" icon="arrow-left" @click="openLog">打开日志</el-button>
	      
          <el-row  type="flex" >
		  <el-col :span="23"> 
		  <div v-if="!isHideLog">
		  <section class="content-wrap">
          	<div class="comments-list">
			<div class="lifeline"></div>
			<div class="comments-item" v-for="(item,index) in msgboardList">
				<div class="dateview">{{ item.insertTime.split(" ")[0]}}<br>{{ item.insertTime.split(" ")[1]}}</div>
				<div class="comment">
					<div class="name">{{item.action}}</div>
					<br>
					<div class="words">名称：{{item.name}}</div>
					<div class="words">类型：{{item.moduleName}}</div>
					<div class="words"  v-if="item.desc!=null && item.desc != ''">动态描述：<br>{{item.desc}}</div>
				</div>
			</div>	
			</div>	
		</section>
	    </div>
		 </el-col>
		 </el-row> 
    </el-col>
</el-row>  
        <el-dialog title="移交" v-if="transforInstanceDialog" v-model="transforInstanceDialog"  size="small" @close="addClose">
            <transforInstance :multipleSelection="multipleSelection" ref="transforInstance" v-on:childBindAdd="closeDialog"></transforInstance>
        </el-dialog>
        <el-dialog title=""  v-if="isEdit" v-model="isEdit"  size="large" @close="addClose">
            <editInstance ref="editInstance"    :instanceId="instanceId"  :instanceName="instanceName"  :moduleId="moduleId" :isOnlyRead="false" v-on:childBindAdd="closeDialog"></editInstance>
        </el-dialog> 
</div>          
</template>

<script type="text/javascript">
import router from '../router';
import transforInstance from './transforInstance.vue';
import editInstance from './editInstance.vue';
export
default {
    props: ['instanceId', 'moduleId', 'circleId','instanceName','multipleSelection','listType'],
    components: {transforInstance,editInstance},
    data() {
        return {
            transforInstanceDialog: false,
            isEdit:false,
            msgboardList: [],
            leftCol:18,
            rightCol:5,
            isHideLog:false,
        }
    },
    watch: {


    },
    mounted: function () {
            debugger;
            this.initData();
        },

        methods: {


                initData: function () {
                       var paramData = {
                            "instanceId": this.instanceId,
                            "actionType": 'Instance',
                        }
                    debugger;
                    $.ajax({
                        url: '/cmdb/circle/getHistoryByInstanceId',
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: paramData,
                    }).done(function (data) {
                        debugger;
                        this.msgboardList = data.dataList;
                    }.bind(this));

                },

                Delete() {
                      this.$emit("enterView", "delete");
                  
                },
                
        handUp() {
    

                this.$emit("enterView", "handup");

       },
       edit() {
    
                  this.$emit("enterView", "edit");

       },
      config() {
    

                this.$emit("enterView", "config");

       },
       
           closeDialog: function() {
               this.transforInstanceDialog = false;
               this.isEdit = false;
           },
           
       addClose(){
            this.$refs.transforInstance.child();
            this.$refs.editInstance.child();
         },
         hideLog(){
         this.isHideLog = true;
            this.leftCol = 22;
            this.rightCol = 1;
         },
         openLog(){
         this.isHideLog = false;
            this.leftCol = 18;
            this.rightCol = 5;
         },
        }

}
</script>

<style scoped>  
section.content-wrap{
	position: relative;
}
.comments-wrap{
	display: flex;
	margin-left: 50px;
	margin-top: 30px;
}
.avatar{
	width: 50px;
	height: 50px;
	margin: 0 20px;
	border-radius: 50%;
	background: url('../../static/img/avatar.jpg') no-repeat;
	background-size: cover;
}
.comments-content{
	position: relative;
}
.comments-content textarea{
	border: none;
	border-radius: 10px;
	overflow: hidden;
    resize: none;
    outline: none;
    overflow: auto;
    padding: 10px;
    font-size: 16px;
}
.gv {
	text-decoration: none;
    background: url('../../static/img/nav_gv.png') repeat 0px 0px;
    width: 130px;
    height: 43px;
    display: block;
    text-align: center;
    line-height: 43px;
    cursor: pointer;
    float: left;
    margin: 10px 2px 10px 2px;
    font: 18px/43px 'microsoft yahei';
    color: #066197;
}
.gv:hover { 
	background: url('../../static/img/nav_gv.png') repeat 0px -43px; 
	color:#1d7eb8;
	-webkit-box-shadow: 0 0 6px #1d7eb8;
	transition-duration: 0.5s;
}
.comments-button{
	position: absolute;
	right: 5px;
	top: 120px;
}
.comments-list{
	position: relative;
	border-top: 1px solid #fff;
	width: 70%;
	margin: 10px 0 50px 40px;
	padding-left: 50px;
}
.lifeline{
	position: absolute;
	/* min-height: 300px; */
	height: 100%;
	top: 20px;
	left: 55px;
	width: 4px;
	background: rgba(7,17,27,1);
}
.comments-item{
	margin-left: 50px;
	padding-top: 40px;
	position: relative;
}
.comments-item::before{
	content: '';
    width: 10px;
    height: 10px;
    border-radius: 50%;
    position: absolute;
    background: rgba(7,17,27,1);
    border: 2px solid #fff;
    left: -50px;
    top: 50px;
}
.dateview{
	position: absolute;
    left: -150px;
    top: 50px;
    z-index: 1;
    font-size: 14px;
}
.comment{
	
}
.comment .name {
	line-height: 30px;
	font-size: 18px;
	color:#0000FF;
}
</style>
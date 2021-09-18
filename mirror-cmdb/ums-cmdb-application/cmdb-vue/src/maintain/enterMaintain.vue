<template>
    <div class="container">
    <div class="contendContainer">
		 <el-breadcrumb separator=">" class="el-breadcrumb">
		  <el-breadcrumb-item :to="{ path: '/cmdb/user/getUsers1' }">维护圈</el-breadcrumb-item>
		  <el-breadcrumb-item>{{circleName}}</el-breadcrumb-item>
		</el-breadcrumb>
    </div>
         <div style="height:42px;border-bottom:1px solid #d1dbe5;">
            <div id="tb" style="float:left;width:100%;">
            <el-tabs v-model="activeIndex" @tab-click="handleClick">
                <el-tab-pane label="配置维护" name="配置维护">
                     <configureInstance :circleId="circleId" ref="configureInstance"></configureInstance>                   
                </el-tab-pane>
                <el-tab-pane label="圈子动态" name="圈子动态">
                 <circleHistory :circleId="circleId" ref="circleHistory"></circleHistory>    
                </el-tab-pane>

            </el-tabs>
            </div>
         <div id="eidt" style="float:right;margin-left:-200px;margin-right:20px;margin-top:3px;position:relative;">
                <el-button  @click="edit()">管理圈子</el-button>
            </div>
         </div>    

    </div>   
</template>
<script>
import configureInstance from './configureInstance.vue';
import ModuleRelation from './moduleRelation.vue';
import router from '../router';
import circleHistory from './circleHistory.vue';
  export default {
    name: 'report',
    components: {configureInstance,ModuleRelation,circleHistory},
    data: function () {
      return {          
        activeIndex: '配置维护',//当前tab页
        circleId:'',
        circleName:'',
      }
    },
   created: function(){
           debugger;
        this.circleId = this.$route.query.circleId;
        this.circleName = this.$route.query.circleName;

    },
    mounted: function () {
    debugger;
      //this.$refs.myAlert.dutyFlag = false;
      //this.$refs.myAlert.tableHeight = 360;
    },
    methods:{     
      handleClick(tab, event) {
      console.log(tab, event);
      },
      edit() {
      var id =  this.circleId;
         router.push({ path: '/cmdb/maintain/editMaintain' , query: { circleId: id }});
      },
    }
}
</script>
<style>
.contendContainer {
    height: 100%;
    min-height: 100%;
    background: #ffffff;
}
.el-breadcrumb {
    background-color: #fff;
    padding: 19px 0 19px 30px;
    border-bottom: 4px solid #f0f0f0;
    font-size: 14px;
}
</style>

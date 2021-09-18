<template>
    <div class="container">
        <div class="contendContainer">
		 <el-breadcrumb separator=">" class="el-breadcrumb">
		  <el-breadcrumb-item :to="{ path: '/cmdb/user/getUsers2' }">仓库</el-breadcrumb-item>
		  <el-breadcrumb-item>{{activeIndex}}</el-breadcrumb-item>
		  <el-breadcrumb-item v-if="moduleName !== null && moduleName !== undefined && moduleName !== ''">{{moduleName}}</el-breadcrumb-item>
		</el-breadcrumb>
       </div>
         <div style="height:42px;border-bottom:1px solid #d1dbe5;">
            <div id="tb" style="float:left;width:100%;">
            <el-tabs v-model="activeIndex" @tab-click="handleClick">
                <el-tab-pane label="仓库配置" name="仓库配置">
                     <configureRepertory  ref="configureRepertory" v-on:changeModule="changeModule"></configureRepertory>                   
                </el-tab-pane>
            </el-tabs>
            </div>
         </div>    

    </div>   
</template>
<script>
import configureRepertory from './configureRepertory.vue';
import router from '../router';
  export default {
    name: 'report',
    components: {configureRepertory,},
    data: function () {
      return {          
        activeIndex: '仓库配置',//当前tab页
        moduleName:'',
      }
    },
    mounted: function () {
    },
    methods:{     
      handleClick(tab, event) {
      console.log(tab, event);
      },
      changeModule(moduleName) {
     this.moduleName = moduleName;
      },
    }
}
</script>

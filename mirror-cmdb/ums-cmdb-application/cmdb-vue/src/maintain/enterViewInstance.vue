<template>
    <div class="container">
         <div style="text-align:center;"><h4>{{instanceName}}</h4></div>
         <div style="height:42px;border-bottom:1px solid #d1dbe5;">

            <div id="tb" style="float:left;width:100%;">
            <el-tabs v-model="activeIndex" @tab-click="handleClick">
                <el-tab-pane label="配置信息" name="first">
                     <viewInstance :instanceId="instanceId"  :moduleId="moduleId"  :circleId="circleId"  :instanceName="instanceName"   :multipleSelection="multipleSelection" :listType="listType" ref="viewInstance" v-on:enterView="enterView"></viewInstance>
                </el-tab-pane>
                <el-tab-pane label="关系" v-if="listType=='maintain'" name="second">
                 <editMaintainRelation ref="editMaintainRelation"    :instanceId="instanceId"  :moduleId="moduleId"  :circleId="circleId" :listType="listType" v-on:enterView="enterView"></editMaintainRelation>
                </el-tab-pane>

            </el-tabs>
            </div>
         </div>

    </div>
</template>
<script>
import router from '../router';
import viewInstance from './viewInstance.vue';
import editMaintainRelation from './editMaintainRelation.vue';
  export default {
    props: ['instanceId', 'moduleId', 'circleId','instanceName','multipleSelection','listType'],
    components: {viewInstance,editMaintainRelation},
    data: function () {
      return {
        activeIndex: 'first',//当前tab页
      }
    },
   created: function(){

    },
    mounted: function () {
    debugger;

    },
    methods:{
      handleClick(tab, event) {
      console.log(tab, event);
      },
    enterView : function(type) {
             this.$emit("enterView", type);
           },
        closeWin: function() {
          this.$emit("enterView", "close");
        },
    }
}
</script>

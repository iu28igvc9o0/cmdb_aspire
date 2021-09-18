<template>
    <div id="module">
        <el-row>
             <el-col :span="3" >
                <el-menu :default-active="activeIndex" :model="parentNav" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" theme="light" style="background:#fff;margin-top:20px;"   v-for="(item,index) in parentNav" :index="'\''+index+'\''" :key="item.id">
                        <template slot="title">{{item.name}}</template>
                        <el-menu-item :index="item.id" @click="changeRouter(item.id,item.type)"><i class="el-icon-menu"></i>{{item.name}}
                        </el-menu-item>
                </el-menu>
            </el-col>
            <el-col :span="20"  style="border-left:1px solid #d1dbe5;height:100%;padding:10px 0px 0px 30px;text-align:left;">
            	<div>
                   <circleHistoryList :actionType="actionType" v-on:editEven="editEven"></circleHistoryList>
                </div>
            </el-col>
        </el-row>
         <router-view></router-view>
    </div>

</template>
<style>
</style>
<script>

import circleHistoryList from './circleHistoryList.vue';
export
default {
    props: ['circleId'],
    components: {
    	circleHistoryList,
    },
    data() {
        return {
            parentNav:[
                   	{"id": "1","type":"Relation","name":"关系动态"},
                   	{"id": "2","type":"Instance","name":"配置动态"},

                   ],//父导航
            activeIndex: "1",
            actionType: "Relation",
            defaultOpenNav: [], //默认展开的Nav
            defaultActive: "", //默认选中的菜单,
        }
    },
    mounted: function () {

        },

        methods: {

                handleOpen(key, keyPath) {
                    console.log(key, keyPath);
                },
                handleClose(key, keyPath) {
                    console.log(key, keyPath);
                },
                changeRouter(id,type) {
                    debugger;
                    this.actionType = type;
                    this.activeIndex = id;
                },
                editEven: function () {
                },

        }
}
</script>

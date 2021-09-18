<template>
    <div id="module" style="height:100%;background:#fff;padding-bottom:30px;">
        <el-row>
             <el-col :span="4" style="height:100%;padding:10px 10px 0px 10px;text-align:left;">

                
                <el-menu :default-active="activeIndex" ref="moduleMenu" :default-openeds="defaultOpenNav" :model="parentNav" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" theme="light" style="background:#fff;margin-top:20px;">
                    <el-submenu v-for="(item,index) in parentNav" :index="item.id" :key="item.id">
                        <template slot="title">{{item.name}}</template>
                         <div v-for="(child,ci) in item.childModules" >
	                         <div style='width:150px;text-overflow:ellipsis;overflow:hidden;' >
	                         <el-tooltip :content="child.name" placement="top" effect="dark">
                        <el-menu-item  :index="child.id" :key="child.id" @click="changeRouter(child.id,child.name)"><span class="menuitemName">{{child.name}}</span>
                        </el-menu-item>
	                        </el-tooltip>
	                        </div> 
                         </div> 
                    </el-submenu>
                    <!-- s -->
                </el-menu>
            </el-col>
            <el-col :span="20" style="border-left:1px solid #d1dbe5;height:100%;padding:10px 20px 0px 20px;">
            	<div>
            	                   <InstanseList v-if="moduleId!=''" :moduleId="moduleId" v-on:editEven="editEven"></InstanseList>
                </div>
            </el-col>
        </el-row>
         <router-view></router-view>
    </div>

</template>
<style>
</style>
<script>

import InstanseList from './repertoryInstanseList.vue';
export
default {
    components: {
        InstanseList,
    },
    data() {
        return {
            parentNav: [], //父导航
            activeIndex: null,
            defaultActive: null, //默认选中的菜单，为index属性的值
            moduleId: "",
            defaultOpenNav: [], //默认展开的Nav
            emid:"",
            builtinFlag:false,//模型是否内置
        }
    },
    watch: {
        defaultActive: function(newVal, oldVal) {
            console.log("defaultActive ---- changed");
            let tmp = this.defaultActive;
            console.log(typeof(tmp))
            this.$refs.moduleMenu.activedIndex = tmp;
        }
    },
    mounted: function () {
            this.getModule();


        },

        methods: {

        getModule() {
            $.ajax({
                url: '/cmdb/module/getModule',
                type: 'POST',
                dataType: 'json',
            }).done(function(data) {
                this.parentNav = data;
                if (this.parentNav != null && this.parentNav.length > 0) {
                    for (var i = 0; i < this.parentNav.length; i++) {
                        if (this.parentNav[i].childModules.length <= 0) {
                            continue;
                        } else {
                                this.moduleId = this.parentNav[i].childModules[0].id;
                                this.builtinFlag = this.parentNav[i].childModules[0].builtin;
                                this.defaultActive = this.moduleId;

                            this.defaultOpenNav.push(this.parentNav[0].id);
                            break;
                        }
                    }
                    debugger;
                }

            }.bind(this));
        },
                handleOpen(key, keyPath) {
                    console.log(key, keyPath);
                },
                handleClose(key, keyPath) {
                    console.log(key, keyPath);
                },
                changeRouter(id,name) {
                    debugger;
                    this.moduleId = id;
                    this.activeIndex = id;
                    this.$emit("changeModule",name);
                },
                editEven: function () {
                    this.getModule();
                },

        }
}
</script>

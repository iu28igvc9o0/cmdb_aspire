<template>
    <div id="module">
        <el-row v-if="!isEditInstance">
            <el-col :span="4" style="height:100%;padding:10px 10px -5px 10px;padding-top:30px;">
                <div style="text-align:center;padding-right:55px;">
                <el-button type="primary"  @click="addViewDialog=true"><i class="el-icon-plus">&nbsp</i>创建视图</el-button> 
                </div>
                
                <el-menu :default-active="activeIndex" :model="parentNav" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" theme="light" style="background:#fff;margin-top:20px;"   v-for="(item,index) in parentNav" :index="'\''+index+'\''" :key="item.id">
                       
                        <div style='width:120px;text-overflow:ellipsis;overflow:hidden;float:left;  display:inline;' > 
                        <el-tooltip :content="item.name" placement="top" effect="dark"><el-menu-item :index="item.id" @click="changeRouter(item.id, item.defaultView)"><i class="el-icon-menu"></i>
                       {{item.name}}

                        </el-menu-item>
                        
                       </el-tooltip> 
                       </div>
                        <div  style="right;margin-top:15px;">
                           <el-dropdown v-if="item.defaultView=='false'" @command="handleCommand">
                                <i class="fa fa-cog" aria-hidden="true"></i>
                                <el-dropdown-menu  slot="dropdown">
                                    <el-dropdown-item :command="'1,'+item.id">编辑</el-dropdown-item>
                                    <el-dropdown-item :command="'2,'+item.id">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                            </div>
                </el-menu>
            </el-col>
            <el-col :span="19"  style="border-left:1px solid #d1dbe5;height:100%;padding:10px 0px 0px 30px;text-align:left;min-height:300px; ">
            	<div>
                   <InstanseList v-if="!isEditInstance" :viewId="viewId" :defaultView="defaultView" :circleId="circleId" v-on:editEven="editEven"></InstanseList>
                </div>
            </el-col>
        </el-row>
        <el-row v-if="isEditInstance">
            <el-col>
            	<div>
                   <EditMaintainRelation v-if="isEditInstance" :instanceId="instanceId" :moduleId="moduleId" :circleId="circleId" v-on:editMointor="editMointor"></EditMaintainRelation>
                </div>
            </el-col>
        </el-row>
         <router-view></router-view>
        <el-dialog title="新增视图" v-if="addViewDialog" v-model="addViewDialog"  size="small" @close="addClose">
            <addView :circleId="circleId"  ref="addView" v-on:childBindAdd="closeDialog"></addView>
        </el-dialog>
        <el-dialog title="修改视图" v-if="editViewDialog" v-model="editViewDialog"  size="small" @close="editClose">
            <editView :viewId="editViewId"  ref="editView" v-on:childBindEdit="closeDialog"></editView>
        </el-dialog>
    </div>

</template>
<style>
</style>
<script>
import addView from './addView.vue';
import editView from './editView.vue';
import EditMaintainRelation from './editMaintainRelation.vue';

import InstanseList from './instanseList.vue';
export
default {
    props: ['circleId'],
    components: {
        addView,
        editView,
        InstanseList,
        EditMaintainRelation,
    },
    data() {
        return {
            parentNav: [{
                    "id": "1",
                    "name": "业务应用层"
                }, {
                    "id": "2",
                    "name": "平台资源层"
                }, {
                    "id": "3",
                    "name": "虚拟资源层"
                }, {
                    "id": "4",
                    "name": "基础设施层"
                }

            ], //父导航
            viewId: "",
            defaultView: "",
            addViewDialog: false,
            editViewDialog: false,
            activeIndex: "",
            editViewId: "",
            isEditInstance: false,
            instanceId: "",
            moduleId: "",
        }
    },
    mounted: function () {
            debugger;
            this.initData();

        },

        methods: {

            initData: function () {
                    debugger;
                    var query_data = {
                        "circleId": this.circleId,
                    };
                    var sysCodeUrl = '/cmdb/circle/getMaintainViewMenu';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                        this.parentNav = data.dataList;
                        for (var i = 0; i < data.dataList.length; i++) {
                            if (data.dataList[i].defaultView == 'true') {
                                this.viewId = data.dataList[i].id;
                                this.defaultView = 'true';
                                this.activeIndex = data.dataList[i].id;
                            }
                        }
                    }.bind(this));

                },
                handleOpen(key, keyPath) {
                    console.log(key, keyPath);
                },
                handleClose(key, keyPath) {
                    console.log(key, keyPath);
                },
                changeRouter(id, defaultView) {
                    debugger;
                    this.viewId = id;
                    this.defaultView = defaultView;
                    this.activeIndex = id;
                },
                closeDialog: function () {
                    this.addViewDialog = false;
                    this.editViewDialog = false;
                    this.initData();
                },
                addClose() {
                    this.$refs.addView.child();
                },
                editClose() {
                    this.$refs.editView.child();
                },
                handleCommand(val) {
                    var re = val.split(",")
                    debugger;
                    if (re[0] == '1') {
                        //修改
                        this.editViewDialog = true;
                        this.editViewId = re[1];
                        this.$broadcast('onEditnew', this.editViewId);
                    } else {
                        //删除
                        if (re[1] != "" && re[1] != null) {

                            this.$confirm('确认删除该视图?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                $.ajax({
                                    url: '/cmdb/circle/deleteMaintainView',
                                    type: 'POST',
                                    cache: false,
                                    async: true,
                                    traditional: true,
                                    dataType: 'json',
                                    data: {
                                        "id": re[1]
                                    },
                                    success: function (data) {
                                        if (data.success == true) {
                                            this.$notify({
                                                title: '提示',
                                                message: '删除成功!',
                                                type: 'success',
                                                duration: 3000
                                            });
                                            this.initData();
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
                            }).catch(() => {
                                console.log("已取消删除");
                            });
                        }
                    }
                },
                editEven: function (isEditInstance, instanceId, moduleId, circleId) {
                    this.isEditInstance = isEditInstance;
                    this.instanceId = instanceId;
                    this.moduleId = moduleId;
                    this.initData();
                },

                editMointor() {
                    this.isEditInstance = false;
                },
        }
}
</script>

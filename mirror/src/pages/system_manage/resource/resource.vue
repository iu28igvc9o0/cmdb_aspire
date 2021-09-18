<!--
@author huangzhijie
@date   19.2.27
@description 资源管理页面
-->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-row :gutter="35"
                        type="flex"
                        align="middle">
                    <el-col :span="5">
                        <el-button type="text"
                                   icon="el-icon-plus"
                                   @click="handleAdd">增加资源</el-button>
                    </el-col>
                    <el-col :span="19">
                        <el-tabs v-model="activeName"
                                 @tab-click="handleClick">
                            <el-tab-pane label="设备资源"
                                         name="first"></el-tab-pane>
                            <el-tab-pane label="自动化资源"
                                         name="second"></el-tab-pane>
                        </el-tabs>
                    </el-col>
                </el-row>
            </div>
            <div>
                <el-row :gutter="35">
                    <el-col :span="5">
                        <div class="resoure">
                            <div class="res-manage__main">
                                <comtree class="res-manage__tree"
                                         :ref="treeName1"
                                         :data="resourceData"
                                         :props="resourceTreeDefault"
                                         node-key="id"
                                         ex-title="资源"
                                         :exId="treeName1"
                                         :ex-control="true"
                                         :ex-control-opt="customControl"
                                         ex-show-search
                                         @node-click="handleNodeClick">
                                </comtree>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="18"
                            style="padding-left:0;">
                        <!-- 设备资源 -->
                        <div class="resoure"
                             v-show="activeName === 'first'">
                            <div class="res-manage__main">
                                <el-row :gutter="50">
                                    <el-col :span="18"
                                            class="res-manage__aicon">
                                        <p class="res-manage__title">通用权限</p>
                                        <el-row :gutter="35">
                                            <el-col :span="8">
                                                <comtree :ref="treeName2"
                                                         class="res-manage__tree"
                                                         node-key="uuid"
                                                         :default-expanded-keys="areaDataExpanded"
                                                         :default-checked-keys="areaDataChecked"
                                                         :data="areaData"
                                                         :props="areaDataDefault"
                                                         show-checkbox
                                                         :exId="treeName2"
                                                         ex-show-search></comtree>
                                                <!--@check="treeName1ClickHandler"-->
                                            </el-col>
                                            <el-col :span="8">
                                                <comtree :ref="treeName3"
                                                         class="res-manage__tree"
                                                         node-key="uuid"
                                                         :default-expanded-keys="[0]"
                                                         :default-checked-keys="deviceTypeDataChecked"
                                                         :data="deviceTypeData"
                                                         :props="deviceTypeDataDefault"
                                                         show-checkbox
                                                         :exId="treeName3"
                                                         ex-show-search></comtree>
                                            </el-col>
                                            <el-col :span="8">
                                                <comtree :load="loadBizTree"
                                                         :lazy="true"
                                                         :ref="treeName4"
                                                         class="res-manage__tree"
                                                         node-key="uuid"
                                                         :default-expanded-keys="[0]"
                                                         :default-checked-keys="bizSystemDataChecked"
                                                         :data="bizSystemData"
                                                         :props="bizSystemDataDefault"
                                                         show-checkbox
                                                         :exId="treeName4"
                                                         ex-show-search></comtree>
                                            </el-col>
                                        </el-row>
                                    </el-col>
                                    <el-col :span="6">
                                        <comtree :load="loadHandler"
                                                 :lazy="true"
                                                 :ref="treeName5"
                                                 class="res-manage__tree"
                                                 node-key="uuid"
                                                 :default-expanded-keys="deviceAuthDataExpanded"
                                                 :default-checked-keys="deviceAuthDataChecked"
                                                 :data="deviceAuthData"
                                                 :props="areaDataDefault"
                                                 show-checkbox
                                                 :exId="treeName5"
                                                 ex-title="特殊权限"
                                                 ex-show-search></comtree>
                                    </el-col>
                                </el-row>
                            </div>
                        </div>
                        <!-- 自动化资源 -->
                        <div class="resoure"
                             v-show="activeName === 'second'">
                            <div class="res-manage__main">
                                <el-row :gutter="50">
                                    <el-col :span="6">
                                        <!-- 分组资源 -->
                                        <comtree :load="loadHandler"
                                                 ref="groupSource"
                                                 class="res-manage__tree"
                                                 node-key="group_id"
                                                 :default-expanded-keys="groupSourceDataExpanded"
                                                 :default-checked-keys="groupSourceDataChecked"
                                                 :data="groupSourceData"
                                                 :props="groupSourceDataDefault"
                                                 show-checkbox
                                                 exId="groupSource"
                                                 ex-title="分组资源"
                                                 ex-show-search></comtree>

                                    </el-col>
                                    <el-col :span="18"
                                            class="res-manage__aicon">
                                        <el-row :gutter="35">
                                            <el-col :span="8">
                                                <!-- 作业资源 -->
                                                <comtree ref="workSource"
                                                         class="res-manage__tree"
                                                         node-key="resource_id"
                                                         :default-expanded-keys="workSourceDataExpanded"
                                                         :default-checked-keys="workSourceDataChecked"
                                                         :data="workSourceData"
                                                         :props="otherSourceDataDefault"
                                                         show-checkbox
                                                         exId="workSource"
                                                         :exTitle="treeTitle1"
                                                         ex-show-search></comtree>
                                                <!--@check="treeName1ClickHandler"-->
                                            </el-col>
                                            <el-col :span="8">
                                                <!-- 脚本资源 -->
                                                <comtree ref="scriptSource"
                                                         class="res-manage__tree"
                                                         node-key="resource_id"
                                                         :default-expanded-keys="scriptSourceDataExpanded"
                                                         :default-checked-keys="scriptSourceDataChecked"
                                                         :data="scriptSourceData"
                                                         :props="otherSourceDataDefault"
                                                         show-checkbox
                                                         exId="scriptSource"
                                                         :exTitle="treeTitle2"
                                                         ex-show-search></comtree>
                                            </el-col>
                                            <el-col :span="8">
                                                <!-- yum资源 -->
                                                <comtree ref="yumSource"
                                                         class="res-manage__tree"
                                                         node-key="resource_id"
                                                         :default-expanded-keys="yumSourceDataExpanded"
                                                         :default-checked-keys="yumSourceDataChecked"
                                                         :data="yumSourceData"
                                                         :props="otherSourceDataDefault"
                                                         show-checkbox
                                                         exId="yumSource"
                                                         :exTitle="treeTitle3"
                                                         ex-show-search></comtree>
                                            </el-col>
                                        </el-row>
                                    </el-col>
                                </el-row>
                            </div>
                        </div>
                    </el-col>
                    <el-dialog :visible.sync="dialogTableVisible"
                               :show-close="false"
                               width="450px"
                               top="12%"
                               class="acc-manage__dialog">
                        <span class="dialog__header">已绑定账号</span>
                        <el-table class="mod-table"
                                  :data="accountList"
                                  style="width: 100%"
                                  highlight-current-row>
                            <el-table-column prop="account"
                                             label="账号">
                            </el-table-column>
                            <el-table-column prop="name"
                                             label="用户名">
                            </el-table-column>
                            <el-table-column prop="dept_id"
                                             label="部门">
                            </el-table-column>
                            <el-table-column prop="no"
                                             label="工号">
                            </el-table-column>
                        </el-table>
                        <span slot="footer"
                              class="acc-manage dialog-footer">
                            <el-button class="mod-btn"
                                       @click="dialogTableVisible = false">取 消</el-button>
                        </span>
                    </el-dialog>
                </el-row>
            </div>

        </el-form>
    </div>

</template>
<script>
    import resourceDataService from 'src/services/sys/reource-services.js'
    import resourceDataMixin from 'src/services/sys/reource-mixin.js'
    import comtree from 'src/pages/system_manage/components/tree.vue'
    import systemAccountService from 'src/services/sys/account-services.js'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'
    export default {
        name: 'ResourceListPage',
        components: {
            comtree
        },
        data() {
            const that = this
            return {
                treeName1: 'resourceTree',
                treeName2: 'areaTree',
                treeName3: 'deviceTypeTree',
                treeName4: 'bizSystemTree',
                treeName5: 'deviceAuthTree',
                resourceData: [],
                resourceTreeDefault: {
                    label: 'name',
                    children: 'childList'
                },
                areaData: [],
                bizSystemData: [],
                bizSystemDataDefault: {
                    label: 'nodeName',
                    children: 'nodeList',
                    disabled: function () {
                        return true
                    }
                },
                bizSystemDataChecked: [],
                areaDataExpanded: [],
                areaDataChecked: [],
                areaDataDefault: {
                    label: 'name',
                    children: 'subList',
                    disabled: function () {
                        return true
                    }
                },
                deviceTypeData: [],
                deviceTypeDataChecked: [],
                deviceTypeDataDefault: {
                    label: 'name',
                    children: 'subList',
                    disabled: function () {
                        return true
                    }
                },
                deviceAuthData: [],
                deviceAuthDataExpanded: [],
                deviceAuthCheckName: [],
                deviceAuthDataChecked: [],
                customControl: [
                    {
                        title: '编辑',
                        icon: 'el-icon-edit-outline',
                        callback: that.customEdit
                    },
                    {
                        title: '删除',
                        icon: 'el-icon-delete',
                        callback: that.customDelete
                    }
                ],
                selectedNode: '',
                needAddRoot: false,
                gridData: [],
                dialogTableVisible: false,
                accountList: [],
            }
        },
        watch: {
            deviceAuthData: {
                handler() {
                    this.$forceUpdate()
                },
                deep: true
            }
        },
        mixins: [resourceDataMixin],
        methods: {
            loadHandler(node, resolve) {
                if (node.data && node.data.type && node.data.type !== 'room') {
                    return resolve(node.data.subList)
                } else if (node.data && node.data.type && node.data.type === 'room') {
                    const deviceTypeData = _.map(this.$refs[this.treeName3].getCheckedNodes(true, false), 'uuid').join(',')
                    const bizSystemData = _.map(this.$refs[this.treeName4].getCheckedNodes(true, false), 'id').join(',')
                    let parentId = node.parent.data.uuid
                    let authId = parentId + '_' + node.data.uuid
                    let param = {}
                    param.area = authId
                    param.deviceType = deviceTypeData
                    param.bizSystem = bizSystemData
                    resourceDataService.getAuthDevice(param).then((res) => {
                        res.forEach((item) => {
                            item.isLeaf = true
                        })
                        return resolve(res)
                    })
                } else {
                    return resolve([])
                }
            },
            getParseId(node, authIdArray) {
                if (node.data.id) {
                    authIdArray.push(node.data.id)
                }
                if (node.parent) {
                    this.getParseId(node.parent, authIdArray)
                }
            },
            // 重置
            resetData() {
                let treeArr = [this.treeName2, this.treeName3, this.treeName4, this.treeName5, 'groupSource', 'workSource', 'scriptSource', 'yumSource']
                treeArr.forEach((item, index) => {
                    this.$refs[item] && this.$refs[item].setCheckedKeys([])
                    if (index > 3) {
                        this[item + 'DataChecked'] = []
                    }
                })

                this.deviceTypeDataChecked = []
                this.areaDataChecked = []
                this.deviceAuthDataChecked = []
                this.bizSystemDataChecked = []
            },
            // 点新增
            handleAdd() {
                if (this.selectedNode) {
                    this.goAdd(this.selectedNode)
                } else {
                    this.goAdd(resourceDataService.ROOT_ID)
                }
            },
            goAdd(pid) {
                this.$router.push({
                    path: '/system_manage/resource-add',
                    query: {
                        pid: pid
                    }
                })
            },
            // 点击资源
            handleNodeClick(data) {
                this.selectedNode = data.uuid
                this.getResourceDetail(data.uuid)
            },
            // 获取账户列表
            getAccountList(pageSize, pageNo, searchData) {
                this.accountList = []
                systemAccountService.getAccountList(pageSize, pageNo, searchData).then((res) => {
                    if (res) {
                        res.results.map((item) => {
                            if (item.userInfo !== undefined) {
                                this.accountList.push({
                                    account: item.username,
                                    name: item.userInfo.name,
                                    dept_id: item.userInfo.dept_id,
                                    no: item.userInfo.no
                                })
                            }
                        })
                    }
                })
            },
            // 修改
            customEdit(node, data) {
                this.$router.push({
                    path: '/system_manage/resource-add',
                    query: {
                        id: data.uuid
                    }
                })
            },
            // 删除
            customDelete(node, data) {
                this.$confirm('确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    resourceDataService.deleteResource(data.uuid).then(() => {
                        this.$message({
                            message: '操作成功',
                            type: 'success'
                        })
                        this.$refs[this.treeName1].remove(node)
                    })
                })
            },
            // 显示详细数据
            handleDetailData(data) {
                const constraints = data.permissions[0].constraints[0]
                if (constraints.device_type) {
                    this.deviceTypeDataChecked = constraints.device_type.split(',')
                }
                if (constraints.area) {
                    var areaChecked = constraints.area.split(',')
                    let allIds = []
                    let roomDataChecked = []
                    areaChecked.forEach(item => {
                        let itemArea = item.split('_')
                        if (!allIds.includes(itemArea[0])) {
                            allIds.push(itemArea[0])
                        }
                        if (!roomDataChecked.includes(itemArea[1])) {
                            roomDataChecked.push(itemArea[1])
                        }
                    })
                    this.areaDataChecked = roomDataChecked
                    if (constraints.device) {
                        this.deviceAuthDataChecked = constraints.device.split(',')
                        this.pageLoading = true
                        resourceDataService.getInstance(constraints.device).then((res) => {
                            let roomList = _.map(res, 'roomId')
                            let idcList = _.map(res, 'idcType')
                            this.deviceAuthCheckName = _.uniq(roomList).concat(_.uniq(idcList))
                            allIds = allIds.concat(roomDataChecked)
                            this.parseIds(constraints.area, allIds)
                        })
                    } else {
                        allIds = allIds.concat(roomDataChecked)
                        this.parseIds(constraints.area, allIds)
                    }
                    // this.areaDataChecked =  areaChecked.split('_')
                }
                if (constraints.bizSystem) {
                    this.bizSystemDataChecked = constraints.bizSystem.split(',')
                }

                // 已选中自动化资源
                this.groupSourceDataChecked = constraints.opsGroup ? constraints.opsGroup.split(',') : []
                this.workSourceDataChecked = constraints.opsPipeline ? constraints.opsPipeline.split(',') : []
                this.scriptSourceDataChecked = constraints.opsScript ? constraints.opsScript.split(',') : []
                this.yumSourceDataChecked = constraints.opsYum ? constraints.opsYum.split(',') : []
            },
            async parseIds(param, allIds) {
                if (allIds && allIds.length > 0) {
                    this.deviceAuthData = this.getDeviceAuthData(this.areaData, allIds)
                    let expandedKey = []
                    this.getDeviceAuthDataChecked(this.deviceAuthData, this.deviceAuthCheckName, expandedKey)
                    this.deviceAuthDataExpanded = expandedKey
                    this.$forceUpdate()
                    this.pageLoading = false
                } else {
                    this.deviceAuthData = []
                }
                // }
            },
            getDeviceAuthDataChecked(data, checkedName, checkKey) {
                data.forEach((item) => {
                    if (checkedName.includes(item.name)) {
                        checkKey.push(item.uuid)
                    }
                    if (item.type !== 'room' && item.subList && item.subList.length > 0) {
                        this.getDeviceAuthDataChecked(item.subList, checkedName, checkKey)
                    }
                })
            },
            getDeviceAuthData(data, checkedKey) {
                let deviceData = []
                data.forEach((item) => {
                    if (checkedKey.includes(item.uuid)) {
                        if (item.subList && item.subList.length > 0) {
                            let subDeviceData = this.getDeviceAuthData(item.subList, checkedKey)
                            item.subList = subDeviceData
                        }
                        deviceData.push(item)
                    }
                })
                return deviceData
            },
            getTree(res, authData) {
                res.forEach(item => {
                    if (authData[item.id]) {
                        item.subList = authData[item.id]
                    }
                    if (item.subList) {
                        this.getTree(item.subList, authData)
                    }
                })
            },
            getAuthIds(res, authIds) {
                res.forEach(item => {
                    if (item.type === 'roomId') {
                        let authIdArray = []
                        this.getParseId(this.$refs[this.treeName2].getNode(item.id), authIdArray)
                        let authId = authIdArray.reverse().join('_')
                        authIds.push(authId)
                    }
                    if (item.subList) {
                        this.getAuthIds(item.subList, authIds)
                    }
                })
            },
            // 获取资源
            getResourceList() {
                resourceDataService.getResourceList().then((res) => {
                    if (res) {
                        this.needAddRoot = res.length === 0
                    }
                    this.resourceData = res
                })
            },
            // 获取设备类型
            getDevicetypeList() {
                resourceDataService.getDevicetypeList().then((res) => {
                    this.deviceTypeData = res
                })
            },
            // 获取通用权限
            getAreaData() {
                resourceDataService.getAreaTree().then((res) => {
                    this.areaData = res
                    if (res.length > 0) {
                        this.areaDataExpanded = [res[0].id]
                    }
                })
            },
            loadBizTree(node, resolve) {
                let params = {
                    viewCode: 'business_auth_tree',
                    nodeList: node.data.nodeId ? [node.data] : null,
                }
                rbCmdbService.getViewsTree(params).then((res) => {
                    let nodeList = res.nodeList
                    nodeList.filter((item) => {
                        item.uuid = item.metaData.id
                    })
                    return resolve(nodeList)
                })
            },
            getBizSysData(param = {}, datas = '', rootNode = {}) {
                let params = {
                    viewCode: 'business_auth_tree',
                    nodeList: param.nodeList,
                }
                return rbCmdbService.getViewsTree(params).then((res) => {
                    let nodeList = res.nodeList
                    nodeList.filter((item) => {
                        item.uuid = item.metaData.id
                    })
                    if (datas) {
                        this.$set(datas, 'subNodeList', nodeList)
                        if (!res.nodeList || res.nodeList.length < 1) {
                            datas['is-leaf'] = true
                        }
                    } else {
                        this.$set(rootNode, 'treeDatas', nodeList)
                    }
                    return datas
                }).finally(() => {
                    // this.closeLoading()
                })
            },
            //     resourceDataService.getBizSysList().then((res) => {
            //         this.bizSystemData = res
            //     })
            // },
            // 获取详细数据
            getResourceDetail(id) {
                this.resetData()
                resourceDataService.getResourceDetail(id).then((res) => {
                    this.handleDetailData(res)
                })
            },
        },
        mounted() {
            this.getResourceList()
            this.getAccountList()

            this.initDevice()
        },
        actived() {
            this.getResourceList()
            this.getAccountList()

            this.initDevice()
        }
    }
</script>
<style lang="scss" scoped>
    /deep/.yw-search-box {
        width: 100%;
    }

    .mod-btn {
        border-radius: 8px;
    }
    .res-manage__tree {
        /deep/ .el-tree {
            overflow-y: auto;
            overflow-x: auto;
            border-radius: 8px;
        }
    }
    .resoure {
        // background: #f4f4f4;
        height: 500px;
        padding: 20px;
        border-radius: 8px;
        /deep/ .el-tree {
            padding: 10px;
            height: 400px;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
        }
    }
    .res-manage {
        .el-tree {
            padding: 10px;
            height: 507px;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
        }

        .res-manage__top {
            margin-bottom: 20px;
            padding-left: 17.5px;
            padding-right: 17.5px;
        }

        .res-manage__main {
            margin-bottom: 20px;
            padding: 20px 20px 20px 0;
            border-top: 1px solid #f4f4f4;
        }

        .res-manage__tree {
            .el-tree {
                height: 400px;
            }
        }

        .res-manage__aicon {
            position: relative;
            .icon {
                margin-top: 12px;
                position: absolute;
                top: 50%;
                right: -10px;
                font-size: 24px;
                color: #dddddd;
            }
        }

        .res-manage__title {
            font-size: 12px;
        }
    }
    /deep/ .acc-manage__dialog {
        font-size: 12px;
        overflow: hidden;
        .el-dialog {
            margin: 0 auto;
            margin-bottom: 0;
            border: 1px solid rgba(229, 232, 237, 1);
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.2);
            border-radius: 16px;
        }
        .dialog__header {
            display: block;
            padding: 14px 0 18px;
        }
        .el-dialog__body {
            font-size: 12px;
            height: 240px;
            overflow-y: auto;
        }
    }
</style>

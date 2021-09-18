<!--
@author huangzhijie
@date   19.3.12
@description 资源管理页面 - 新增
-->
<template>
    <div class="res-mange">
        <div class="res-mange__top">
            <el-row style="">
                <el-form ref="resourceform"
                         :rules="resourceRules"
                         :inline="true"
                         :model="resourceForm"
                         class="demo-form-inline">
                    <el-form-item label="资源名称"
                                  prop="name"
                                  style="margin-top: 22px;">
                        <el-input v-model="resourceForm.name"
                                  placeholder="请输入名称"></el-input>
                    </el-form-item>
                    <el-form-item label="资源描述"
                                  prop="describe"
                                  style="margin-top: 22px;">
                        <el-input v-model="resourceForm.describe"
                                  placeholder="请输入描述"></el-input>
                    </el-form-item>
                </el-form>
            </el-row>
        </div>
        <div class="res-mange__main">
            <el-tabs v-model="activeName"
                     @tab-click="handleClick">
                <el-tab-pane label="设备资源"
                             name="first"></el-tab-pane>
                <el-tab-pane label="自动化资源"
                             name="second"></el-tab-pane>
            </el-tabs>
            <!-- 设备资源 -->
            <el-row :gutter="50"
                    v-show="activeName === 'first'">
                <el-col :span="18"
                        class="res-manage__aicon">
                    <p class="res-manage__title">通用权限</p>
                    <el-row :gutter="35">
                        <el-col :span="8">
                            <comtree :ref="treeName1"
                                     class="res-manage__tree"
                                     node-key="uuid"
                                     :default-expanded-keys="areaDataExpanded"
                                     :default-checked-keys="areaDataChecked"
                                     :data="areaData"
                                     :props="areaDataDefault"
                                     show-checkbox
                                     :exId="treeName1"
                                     @check="treeName1ClickHandler"
                                     ex-show-search></comtree>
                        </el-col>
                        <el-col :span="8">
                            <comtree :ref="treeName2"
                                     class="res-manage__tree"
                                     node-key="uuid"
                                     :default-expanded-keys="[0]"
                                     :default-checked-keys="deviceTypeDataChecked"
                                     :data="deviceTypeData"
                                     :props="deviceTypeDataDefault"
                                     show-checkbox
                                     :exId="treeName2"
                                     ex-show-search></comtree>
                        </el-col>
                        <el-col :span="8">
                            <comtree :load="loadBizTree"
                                     :lazy="true"
                                     :ref="treeName3"
                                     class="res-manage__tree"
                                     node-key="uuid"
                                     :default-expanded-keys="[0]"
                                     :default-checked-keys="bizSystemDataChecked"
                                     :data="bizSystemData"
                                     :props="bizSystemDataDefault"
                                     show-checkbox
                                     :exId="treeName3"
                                     ex-show-search
                                     @check="treeName3ClickHandler"></comtree>
                        </el-col>
                    </el-row>
                    <!--<i class="icon el-icon-arrow-right"></i>-->
                </el-col>
                <el-col :span="6">
                    <comtree :ref="treeName4"
                             class="res-manage__tree"
                             node-key="uuid"
                             :default-expanded-keys="deviceAuthDataExpanded"
                             :default-checked-keys="deviceAuthDataChecked"
                             :data="deviceAuthData"
                             :props="deviceDataDefault"
                             show-checkbox
                             :exId="treeName4"
                             ex-title="特殊权限"
                             ex-show-search
                             @check="treeName4ClickHandler"
                             :lazy="true"
                             :load="loadHandler"></comtree>
                </el-col>
            </el-row>
            <!-- 自动化资源 -->
            <el-row :gutter="50"
                    v-show="activeName === 'second'">
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
            <div class="mtop20">
                <el-button type="primary"
                           class="mod-btn"
                           @click="handleSave">保存</el-button>
            </div>
        </div>
    </div>
</template>
<script>
    import resourceDataService from 'src/services/sys/reource-services.js'
    import resourceDataMixin from 'src/services/sys/reource-mixin.js'
    // import configDictService from 'src/services/cmdb/rb-configDict-service.factory'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'
    import comtree from 'src/pages/system_manage/components/tree.vue'

    export default {
        name: 'ResourceAddPage',
        components: {
            comtree
        },
        data() {
            return {
                resourceForm: {
                    pid: '',
                    id: '',
                    name: '',
                    describe: ''
                },
                resourceRules: {
                    name: [
                        { required: true, message: '请输入资源名称', trigger: 'blur' }
                    ]
                },
                treeName1: 'areaTree',
                treeName2: 'deviceTypeTree',
                treeName3: 'bizSystemTree',
                treeName4: 'deviceAuthTree',
                deviceTypeData: [],
                deviceTypeDataDefault: {
                    label: 'name',
                    children: 'subList'
                },
                bizSystemData: [],
                bizSystemDataChecked: [],
                bizSystemDataDefault: {
                    label: 'nodeName',
                    children: 'nodeList'
                },
                deviceTypeDataChecked: [],
                areaData: [],
                areaDataExpanded: [],
                areaDataChecked: [],
                deviceAuthData: [],
                deviceAuthDataExpanded: [],
                deviceAuthDataChecked: [],
                deviceAuthDataCheckedNames: [],
                deviceAuthCheckName: [],
                areaDataDefault: {
                    label: 'name',
                    children: 'subList'
                },
                deviceDataDefault: {
                    label: 'name',
                    children: 'subList',
                    isLeaf: 'isLeaf'
                },
                idcTypeIds: [],
                // 存放大量的机房数据，用作翻页展示，避免卡顿
                originAuthData: {},
            }
        },
        mixins: [resourceDataMixin],
        methods: {
            loadHandler(node, resolve) {
                if (node.data && node.data.type && node.data.type !== 'room') {
                    return resolve(node.data.subList)
                } else if (node.data && node.data.type && node.data.type === 'room') {
                    const deviceTypeData = _.map(this.$refs[this.treeName2].getCheckedNodes(true, false), 'uuid').join(',')
                    const bizSystemData = _.map(this.$refs[this.treeName3].getCheckedNodes(true, false), 'uuid').join(',')
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
                        this.$nextTick(() => {
                            this.$refs[this.treeName4].setCheckedKeys(this.deviceAuthDataChecked)
                        })
                        this.originAuthData[authId] = res
                        return resolve(res)
                    })
                } else {
                    return resolve([])
                }
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
                    this.$nextTick(() => {
                        this.$refs[this.treeName3].setCheckedKeys(this.bizSystemDataChecked)
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
            getParseId(node, authIdArray) {
                if (node.data.uuid) {
                    authIdArray.push(node.data.uuid)
                }
                if (node.parent) {
                    this.getParseId(node.parent, authIdArray)
                }
            },
            // 从已勾选的通用权限获取特殊权限数据
            treeName1ClickHandler(data, data1) {
                let checkedKey = data1.checkedKeys
                let halfCheckedKeys = data1.halfCheckedKeys
                checkedKey = checkedKey.concat(halfCheckedKeys)
                if (checkedKey && checkedKey.length > 0) {
                    this.deviceAuthData = this.getDeviceAuthData(JSON.parse(JSON.stringify(this.areaData)), checkedKey)
                } else {
                    this.deviceAuthData = []
                }
            },
            // 更新业务系统已选择的ids
            treeName3ClickHandler(data, data1) {
                let curDataId = data.uuid
                let checkedKey = data1.checkedKeys
                let checkedNodes = [].concat(data1.checkedNodes, data1.halfCheckedNodes)
                // 先过滤出业务系统节点
                checkedNodes = checkedNodes.filter((item) => {
                    return item.metaData.type === 'bizSystem'
                })
                // 判断当前节点是选中还是未选中状态, 未选择则需要判断移除
                let isChecked = checkedKey.indexOf(curDataId) > -1
                let index = this.bizSystemDataChecked.indexOf(curDataId)
                if (index > -1 && !isChecked) {
                    this.bizSystemDataChecked.splice(index, 1)
                } else {
                    // 选中状态则需要判断追加
                    checkedNodes.forEach((item) => {
                        if (!this.bizSystemDataChecked.includes(item.uuid)) {
                            this.bizSystemDataChecked.push(item.uuid)
                        }
                    })
                }
                // this.bizSystemDataChecked = Array.from(new Set([].concat(this.bizSystemDataChecked, checkedKey)))
            },
            // 更新特殊权限 已选中ids
            treeName4ClickHandler(data, data1) {
                let curDataId = data.uuid
                // 只保存机房id，不保存资源池id
                let checkedKey = data1.checkedKeys
                let checkedNodes = [].concat(data1.checkedNodes, data1.halfCheckedNodes)
                this.deviceAuthDataChecked = Array.from(new Set([].concat(this.deviceAuthDataChecked, checkedKey)))

                // 从已勾选id中剔除反选的id
                let isChecked = checkedKey.indexOf(curDataId) > -1
                let index = this.deviceAuthDataChecked.indexOf(curDataId)
                let nameIndex = this.deviceAuthDataCheckedNames.indexOf(data.name)
                if (index > -1 && !isChecked) {
                    this.deviceAuthDataChecked.splice(index, 1)
                    this.deviceAuthDataCheckedNames.splice(nameIndex, 1)
                }

                // 剔除idcType、room类型的id
                this.deviceAuthDataChecked.forEach((checkedId, i) => {
                    checkedNodes.forEach(item => {
                        if (checkedId === item.uuid && (item.type === 'idcType' || item.type === 'room')) {
                            this.deviceAuthDataChecked.splice(i, 1, '')
                        }
                        if (!item.type && !this.deviceAuthDataCheckedNames.includes(item.name)) {
                            this.deviceAuthDataCheckedNames.push(item.name)
                        }
                    })
                })
            },
            // 获取特殊权限数据
            getDeviceAuthData(data, checkedKey) {
                let deviceData = []
                this.$utils.deepClone(data).forEach((item) => {
                    if (checkedKey.includes(item.uuid)) {
                        if (item.subList && item.subList.length > 0) {
                            let subDeviceData = this.getDeviceAuthData(item.subList, checkedKey)
                            item.subList = subDeviceData
                        }
                        // 特殊权限仅包含有子节点的资源池
                        if ((item.type === 'idcType' && item.subList.length) || item.type === 'room') {
                            deviceData.push(item)
                        }
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
                        this.getParseId(this.$refs[this.treeName1].getNode(item.id), authIdArray)
                        let authId = authIdArray.reverse().join('_')
                        authIds.push(authId)
                    }
                    if (item.subList) {
                        this.getAuthIds(item.subList, authIds)
                    }
                })
            },
            handleSave() {
                this.$refs['resourceform'].validate((valid) => {
                    if (valid) {
                        let data = this.processData()
                        const params = {
                            name: data.name,
                            describe: data.describe,
                            resources: [],
                            permissions: [
                                {
                                    resource: [],
                                    actions: [],
                                    constraints: [data]
                                }
                            ],
                            admin_role: false
                        }
                        const pid = this.$route.query.pid
                        if (pid) {
                            if (pid !== this.ROOT_ID) {
                                params.parents = [{ uuid: pid }]
                            }
                            this.addResource(params)
                        } else {
                            params.role_id = data.id
                            this.updateResource(params)
                        }
                    } else {
                        return false
                    }
                })
            },
            // 处理已选中资源
            processData() {
                // 设备资源
                this.resourceForm.idcType = this.processtAreaData('idc_type')
                this.resourceForm.idcType_name = this.processtAreaData('idc_type_name')
                this.resourceForm.roomId = this.processtAreaData('roomId')
                this.resourceForm.room_name = this.processtAreaData('room_name')
                this.resourceForm.device_type = this._processTreeData(this.treeName2, 'uuid')
                this.resourceForm.device_type_name = this._processTreeData(this.treeName2, 'name')
                this.resourceForm.device = this.deviceAuthDataChecked.toString()
                this.resourceForm.device_name = this.deviceAuthDataCheckedNames.toString()
                this.resourceForm.bizSystem = this.bizSystemDataChecked.toString()
                // ID改造已完成, 取消使用名称权限判断
                // this.resourceForm.bizSystem_name = this._processTreeData(this.treeName3, 'nodeName')

                // 自动化资源
                this.resourceForm.opsGroup = this._processTreeData('groupSource', 'group_id')
                this.resourceForm.opsPipeline = this._processTreeData('workSource', 'resource_id')
                this.resourceForm.opsScript = this._processTreeData('scriptSource', 'resource_id')
                this.resourceForm.opsYum = this._processTreeData('yumSource', 'resource_id')

                return this.resourceForm

            },
            parseIds(device, device_name, idcTypeIds) {
                if (idcTypeIds && idcTypeIds.length > 0) {
                    this.deviceAuthData = this.getDeviceAuthData(this.areaData, idcTypeIds)
                    this.deviceAuthDataExpanded = []    // 首次加载不展开，避免进入页面卡顿
                    this.$nextTick(() => {
                        this.deviceAuthDataChecked = Array.from(new Set(device.split(',')))
                        this.deviceAuthDataCheckedNames = Array.from(new Set(device_name.split(',')))
                    })
                } else {
                    this.deviceAuthData = []
                }
                // }
            },
            processtAreaData(keyType) {
                const data = this.$refs[this.treeName1].getCheckedNodes(false, true)
                let treeData = []
                if (data && data.length > 0) {
                    data.map((item) => {
                        if (keyType === 'roomId' && item.type === 'room') {
                            treeData.push(item.uuid)
                        } else if (keyType === 'room_name' && item.type === 'room') {
                            treeData.push(item.name)
                        } else if (keyType === 'idc_type' && item.type === 'idcType') {
                            treeData.push(item.uuid)
                        } else if (keyType === 'idc_type_name' && item.type === 'idcType') {
                            treeData.push(item.name)
                        }
                    })
                    return treeData.join(',')
                } else {
                    return ''
                }
            },
            // 取出树里的数据
            _processTreeData(treeName, keyName) {
                // 自动化资源-分组资源全选时，把父节点id也上传，其他资源仅上传子节点id
                let leafOnly = treeName === 'groupSource' ? false : true
                let data = this.$refs[treeName].getCheckedNodes(leafOnly, false)

                // 部门树：一级、二级部门节点不提交，只提交type为bizSystem的节点
                if (treeName === this.treeName3 && keyName === 'uuid') {
                    data = data.filter(item => {
                        return item.metaData.type === 'bizSystem'
                    })
                }
                // console.log(data)
                let treeData = []
                if (data && data.length > 0) {
                    data.map((item) => {
                        treeData.push(String(item[keyName]))
                    })
                    return treeData.join(',')
                } else {
                    return ''
                }
            },
            // 设备资源详情
            handleDetailData(data) {
                this.resourceForm.id = data.uuid
                this.resourceForm.name = data.name
                this.resourceForm.describe = data.describe

                const constraints = data.permissions[0].constraints[0]
                const parents = data.parents[0]
                if (parents && parents.uuid) {
                    this.resourceForm.pid = parents.uuid
                } else {
                    this.resourceForm.pid = this.ROOT_ID
                }

                // 已选中设备资源：
                // treeName1：
                let idcTypeIds = []
                if (constraints.idcType) {
                    idcTypeIds = constraints.idcType.split(',')
                }

                // 通过 idcType_id 和 资源池下的 roomId 获取通用权限
                let roomIds = []
                if (constraints.roomId) {
                    roomIds = constraints.roomId.split(',')
                }
                this.areaDataChecked = [].concat(roomIds, idcTypeIds)

                // treeName2：设备类型
                if (constraints.device_type) {
                    this.deviceTypeDataChecked = constraints.device_type.split(',')
                }

                // treeName3：勾选业务系统
                if (constraints.bizSystem) {
                    this.bizSystemDataChecked = constraints.bizSystem.split(',')
                }
                // treeName4：勾选特殊权限：资源池、设备
                idcTypeIds = idcTypeIds.concat(this.areaDataChecked)
                this.parseIds(constraints.device, constraints.device_name, idcTypeIds)

                // 已选中自动化资源
                if (constraints.opsGroup) {
                    this.groupSourceDataChecked = constraints.opsGroup.split(',')
                }
                if (constraints.opsPipeline) {
                    this.workSourceDataChecked = constraints.opsPipeline.split(',')
                }
                if (constraints.opsScript) {
                    this.scriptSourceDataChecked = constraints.opsScript.split(',')
                }
                if (constraints.opsYum) {
                    this.yumSourceDataChecked = constraints.opsYum.split(',')
                }

            },
            handleDeviceAuthDataExpandedKey(data, checkedName, checkKey) {
                data.forEach((item) => {
                    if (checkedName.includes(item.uuid)) {
                        checkKey.push(item.uuid)
                    }
                    if (item.type !== 'room' && item.subList && item.subList.length > 0) {
                        this.handleDeviceAuthDataExpandedKey(item.subList, checkedName, checkKey)
                    }
                })
            },
            addResource(data) {
                resourceDataService.addResource(data).then(() => {
                    this.$message({
                        message: '创建成功',
                        type: 'success'
                    })
                    this.$router.push({
                        path: '/system_manage/resource'
                    })
                })
            },
            updateResource(data) {
                resourceDataService.updateResource(data).then(() => {
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    })
                    this.$router.push({
                        path: '/system_manage/resource'
                    })
                })
            },
            // 获取详细数据
            getResourceDetail(id) {
                resourceDataService.getResourceDetail(id).then((res) => {
                    this.handleDetailData(res)
                })
            },
            // 获取设备类型
            async getDevicetypeList() {
                await resourceDataService.getDevicetypeList().then((res) => {
                    this.deviceTypeData = res
                })
            },
            // async getBizSysData() {
            //     await resourceDataService.getBizSysList().then((res) => {
            //         this.bizSystemData = res
            //     })
            // },
            // 获取通用权限
            async getAreaData() {
                await resourceDataService.getAreaTree().then((res) => {
                    this.areaData = res
                    if (res.length > 0) {
                        this.areaDataExpanded = [res[0].id]
                    }
                })
            },
        },
        mounted() {
            this.initDevice()
            this.initAutoOperation()
        }
    }
</script>
<style lang="scss" scoped>
    .res-manage__tree {
        /deep/ .el-tree {
            height: 350px;
            overflow-y: auto;
        }
    }
    .el-form-item__label {
        font-size: 12px;
    }
    .el-input__inner {
        border-radius: 4px;
    }
    .res-mange {
        background: #f4f4f4;
        font-size: 12px;
        /*.res-mange__top {*/
        /*background: #fff;*/
        /*border: 1px solid #DCDFE6;*/
        /*border-radius: 8px;*/
        /*}*/
        .res-mange__top {
            margin: 10px 20px 0;
            background: #fff;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
            padding-left: 10px;
        }
        .el-tree {
            padding: 10px;
            height: 400px;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
        }
        .res-mange__main {
            padding: 0 20px 50px;
            border-radius: 8px;
            background: #f4f4f4;
            .mtop20 {
                margin-top: 20px;
            }
        }

        .search-box {
            margin-bottom: 14px;
        }
    }
</style>

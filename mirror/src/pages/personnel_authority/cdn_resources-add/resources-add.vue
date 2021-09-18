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
                                  style="margin-top: 2px;">
                        <!-- 22 -->
                        <el-input v-model="resourceForm.name"
                                  placeholder="请输入名称"></el-input>
                    </el-form-item>
                    <el-form-item label="资源描述"
                                  prop="describe"
                                  style="margin-top: 2px;">
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
                <!-- <el-tab-pane label="自动化资源"
                             name="second"></el-tab-pane> -->
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
                areaData: [],
                areaDataExpanded: [],
                areaDataChecked: [],
                deviceAuthDataCheckedNames: [],
                areaDataDefault: {
                    label: 'name',
                    children: 'subList'
                },
                idcTypeIds: [],
            }
        },
        mixins: [resourceDataMixin],
        methods: {
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
                    console.log(checkedKey)
                }
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
                this.resourceForm.device_name = this.deviceAuthDataCheckedNames.toString()
                return this.resourceForm
            },
            parseIds(device, device_name, idcTypeIds) {
                if (idcTypeIds && idcTypeIds.length > 0) {
                    this.$nextTick(() => {
                        this.deviceAuthDataCheckedNames = Array.from(new Set(device_name.split(',')))
                    })
                }
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
                        path: '/personnel_authority/cdn_resources'
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
                        path: '/personnel_authority/cdn_resources'
                    })
                })
            },
            // 获取详细数据
            getResourceDetail(id) {
                resourceDataService.getResourceDetail(id).then((res) => {
                    this.handleDetailData(res)
                })
            },

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
            this.initDevice('cdn')
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

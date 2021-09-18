<!--  -->
<template>
    <div>
        <!-- <el-form class="components-condition yw-form" -->
        <section class="yw-dialog-main">

            <el-form ref="connectForm"
                     :inline="true"
                     label-width="100px">
                <el-form-item label="主机信息：">
                    <el-button type="primary"
                               @click="addList">新增</el-button>
                    <div class="mtop10 yw-el-table-wrap">
                        <el-table ref="serviceTable"
                                  :data="hostList"
                                  class="yw-el-table mtop10"
                                  stripe
                                  border
                                  width="100%">
                            <el-table-column label="操作系统"
                                             prop="pool_name">
                            </el-table-column>
                            <el-table-column label="版本"
                                             prop="pool_name">
                            </el-table-column>
                            <el-table-column label="固件版本"
                                             prop="pool_name">
                            </el-table-column>
                            <el-table-column label="描述"
                                             prop="pool_name">
                            </el-table-column>
                            <el-table-column label="操作">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form-item>
                <el-form-item label="操作系统信息：">
                    <div class="yw-dashboard">
                        <select-servers :dataSelected="addForm.targetMachines"
                                        :targetObjectList="target_exec_object"
                                        @setSelectedKey="setSelectedKey"
                                        @setSelectedService="setSelectedService"></select-servers>
                    </div>
                </el-form-item>

            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="connectSave">保存</el-button>
            <el-button>取消</el-button>
        </section>

    </div>
</template>

<script>
    import selectServers from 'src/pages/auto_operation/components/select-servers.vue'
    import rbAutoOperationBlackList from 'src/services/auto_operation/rb-auto-operation-black-list.js'

    export default {
        components: { selectServers },
        data() {
            return {
                addForm: {
                    targetMachines: []
                },
                target_exec_object: '',

                hostList: [],
                osList: []
            }
        },
        props: ['whitelistId'],
        methods: {
            setSelectedKey(data) {
                console.log('setSelectedService', data)

                this.addForm.targetMachines = []
                this.addForm.targetMachines = data
                // console.log('this.addForm.targetMachines===', this.addForm.targetMachines)
            },
            setSelectedService(data) {
                // this.selectedServiceList = []
                // this.selectedServiceList = data
            },
            connectSave() {
                this.addForm.targetMachines.forEach(item => {
                    let obj = {
                        'whitelist_type': 'cruisecheck',
                        'constraint_type': 'os_type',
                        'whitelist_id': this.whitelistId,
                        'constraint_value': item.idcType + ':' + item.agent_ip
                    }
                    this.osList.push(obj)
                })
                rbAutoOperationBlackList.saveWhitelistLinkConstraints(this.osList).then(res => {
                    if (res.flag == true) {
                        this.$emit('showConnect', false)
                    }
                })

            }
        },
    }
</script>
<style lang='scss' scoped>
    .connect-box {
        width: 50%;
    }
</style>
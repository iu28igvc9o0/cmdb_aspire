<template>
    <el-dialog class="yw-dialog"
               width="500px"
               title="服务数量管理"
               @close="handleClosed"
               :visible.sync="dialogMsg.dialogVisible">
        <div class="yw-el-table-wrap">
            <el-form class="yw-form">
                <div class="table-operate-wrap clearfix">
                    <el-button class="btn-icons-wrap"
                               type="text"
                               icon="el-icon-plus"
                               @click="addRow"
                               v-if="addButtonVisiable">新增</el-button>
                    <el-button class="btn-icons-wrap"
                               type="text"
                               icon="el-icon-minus"
                               @click="removeRow"
                               v-if="removeButtonVisiable">删除最后一行</el-button>
                    <el-button class="btn-icons-wrap"
                               type="text"
                               icon="el-icon-circle-check"
                               @click="submit">提交</el-button>
                </div>
            </el-form>
            <el-table class="yw-el-table"
                      v-loading="loading"
                      border
                      :span-method="rowSpanDeal"
                      :data="firstData"
                      size="mini">
                <el-table-column label="维保项目"
                                 prop="projectName"
                                 align="center"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="服务方式"
                                 align="center"
                                 :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <el-select :disabled="dropButtonVisiable"
                                   v-model="firstData[scope.$index].serviceType"
                                   @change="validateType(firstData[scope.$index].serviceType)"
                                   size="mini"
                                   clearable="validateTypeCanChoose"
                                   placeholder="请选择"
                                   style="width:95%;">
                            <el-option v-for="item in serviceTypeList"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column label="数量"
                                 align="center"
                                 :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <el-input v-model="firstData[scope.$index].serviceNum"></el-input>
                    </template>
                </el-table-column>
            </el-table>
        </div>

    </el-dialog>
</template>

<script>
    export default {
        props: ['dialogMsg'],
        data() {
            return {
                serviceTypeList: [{
                    name: '包月服务',
                    value: '包月服务'
                }, {
                    name: '包年服务',
                    value: '包年服务'
                }, {
                    name: '按次服务',
                    value: '按次服务'
                }, {
                    name: '高级服务',
                    value: '高级服务'
                }],
                templateList: [{
                    name: '包月服务',
                    value: '包月服务'
                }, {
                    name: '包年服务',
                    value: '包年服务'
                }, {
                    name: '按次服务',
                    value: '按次服务'
                }, {
                    name: '高级服务',
                    value: '高级服务'
                }], // 服务类型的所有可选类型
                dropButtonVisiable: false, // 下拉框是否可见
                addButtonVisiable: true,
                removeButtonVisiable: true,
                loading: false,
                firstData: [],
                inputFlag: false  // 是否通过校验
            }
        },
        created() {
        },
        mounted() {
            this.initMethod(this.dialogMsg.data)
        },
        methods: {
            submit() {
                var inputFlag = this.validataBeforeSubmit()
                // 输入验证通过
                if (!inputFlag) {
                    this.$notify({
                        title: '提示',
                        message: '未填写或数字格式错误',
                        type: 'error',
                        duration: 1500
                    })
                    return
                }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: this.firstData,
                    url: '/v1/cmdb/maintenance/project/addServiceNum'
                }).then((res) => {
                    this.$emit('setAddServiceNum', false)
                    return res
                })
            },
            // 验证下拉框中那些服务可以选择
            validateTypeCanChoose() {
                var rsList = []
                var chooseData = this.firstData
                var tempData = this.templateList
                for (var i = 0; i < tempData.length; i++) {
                    var t = tempData[i]
                    for (var j = 0; j < chooseData.length; j++) {
                        if (chooseData[j].serviceType == t.value) {
                            break
                        }
                    }
                    if (j == chooseData.length) {
                        rsList.push(t)
                    }
                }
                this.serviceTypeList = rsList
            },
            // 验证服务方式
            validateType() {
                // 将已经选过的服务类型隐藏掉
                this.validateTypeCanChoose()
            },
            // 验证数字
            // validateNum(str){
            //   var pattern = /^[1-9]\d*$/
            //   if(!pattern.test(str) || str == '') {
            //     this.inputFlag = false
            //   } else {
            //      this.inputFlag = true
            //   }
            // },
            // 提交前进行数据验证
            validataBeforeSubmit() {
                var data = this.firstData
                for (var i = 0; i < data.length; i++) {
                    console.info('验证数据：')
                    console.info(data[i])
                    if (data[i].serviceType == null || data[i].serviceType == '') {
                        return false
                    }
                    if (data[i].serviceNum == null || data[i].serviceNum == '') {
                        return false
                    } else {
                        var str = data[i].serviceNum
                        var pattern = /^[1-9]\d*$/
                        if (!pattern.test(str)) {
                            return false
                        } else {
                            continue
                        }
                    }
                }
                return true
            },
            removeRow() {
                if (this.firstData.length > 1) {
                    this.firstData.pop()
                    var flag = true
                    var data = this.firstData
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].serviceType == '' || data[i].serviceNum == '') {
                            flag = false
                            break
                        }
                    }
                    this.inputFlag = flag
                } else {
                    this.$notify({
                        title: '提示',
                        message: '至少留有一行',
                        type: 'error',
                        duration: 1000
                    })
                }
            },
            addRow() {
                var flag = false
                var data = this.firstData
                for (var i = 0; i < data.length; i++) {
                    if (data[i].serviceType == '' || data[i].serviceNum == '') {
                        flag = true
                        break
                    }
                }
                if (flag) {
                    this.$notify({
                        title: '提示',
                        message: '有空白行需要填写',
                        type: 'error',
                        duration: 1000
                    })
                } else {
                    this.firstData.push({
                        projectName: data[0].projectName,
                        projectId: data[0].projectId,
                        serviceType: '',
                        serviceNum: ''
                    })
                }
            },
            // INIT 初始化
            initMethod(row) {
                var tmpData = []
                if (row.serviceType.indexOf('+') == -1) {
                    this.addButtonVisiable = false
                    this.removeButtonVisiable = false
                    this.dropButtonVisiable = true
                }
                if (row.serviceNums == null || row.serviceNums.length == 0) {
                    if (row.serviceType.indexOf('+') == -1) {
                        tmpData.push({
                            projectName: row.projectName,
                            projectId: row.id,
                            serviceType: row.serviceType,
                            serviceNum: ''
                        })
                    } else {
                        for (var i = 0; i < 2; i++) {
                            tmpData.push({
                                projectName: row.projectName,
                                projectId: row.id,
                                serviceType: '',
                                serviceNum: ''
                            })
                        }
                    }
                } else {
                    row.serviceNums.forEach(item => {
                        tmpData.push({
                            projectName: row.projectName,
                            projectId: row.id,
                            serviceType: item.serviceType,
                            serviceNum: item.serviceNum
                        })
                    })
                }
                this.firstData = tmpData
                this.getServiceType()
            },
            // 获取服务类型列表
            getServiceType() {
                let obj = {
                    'type': 'mainten_service_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    var tmp = []
                    // 去除带有"+"的服务形式
                    res.forEach(element => {
                        if (element.value.indexOf('+') == -1) {
                            tmp.push({
                                name: element.name,
                                value: element.value
                            })
                        }
                    })
                    this.serviceTypeList = tmp
                    this.templateList = tmp
                    this.validateTypeCanChoose()
                    return res
                })
            },
            rowSpanDeal({ rowIndex, columnIndex }) {
                if (columnIndex === 0) {
                    if (rowIndex === 0) {
                        return [this.firstData.length, 1]
                    } else {
                        return [0, 0]
                    }
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>

<!-- 订阅告警管理 -->
<template>
    <div>
        <div class="components-container yw-dashboard">

            <el-form class="yw-form components-condition"
                     :inline="true">
                <el-form-item label="规则名称">
                    <el-select v-model="dataParmas.subscribeRules"
                               placeholder="请选择">
                        <el-option v-for="item in nameOption"
                                   :key="item.id"
                                   :label="item.subscribeRules"
                                   :value="item.subscribeRules">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="设备IP">
                    <el-input v-model="dataParmas.deviceIp"></el-input>
                </el-form-item>
                <el-form-item label="告警等级">
                    <el-select v-model="dataParmas.alertLevel"
                               placeholder="请选择">
                        <el-option v-for="item in option"
                                   :key="item.value"
                                   :label="item.name"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="资源池">
                    <el-select v-model="dataParmas.idcType"
                               placeholder="请选择">
                        <el-option v-for="item in typeOption"
                                   :key="item.value"
                                   :label="item.name"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="goQuery">查询</el-button>
                    <el-button @click="reset">重置</el-button>
                </section>
            </el-form>

            <el-form class="yw-form">
                <div class="table-operate-wrap clearfix">
                    <el-button type="text"
                               icon="el-icon-delete"
                               @click="delBtn">删除</el-button>
                    <el-button type="text"
                               icon="el-icon-upload2"
                               @click="exportBtn">导出</el-button>
                </div>
                <div class="yw-el-table-wrap">

                    <el-table class="yw-el-table"
                              style="cursor:pointer;height: calc(100vh - 276px) !important;"
                              :data="subscribeData"
                              stripe
                              tooltip-effect="dark"
                              border
                              @selection-change="changeCheckbox"
                              size="samll"
                              v-loading="loading">
                        <el-table-column type="selection"
                                         :selectable="checkSelectable"
                                         width="40px"></el-table-column>
                        <el-table-column prop="subscribeRules"
                                         label="规则名称"
                                         sortable></el-table-column>
                        <el-table-column prop="deviceIp"
                                         label="关联设备IP"
                                         sortable></el-table-column>
                        <el-table-column prop="alertLevel"
                                         label="告警等级"
                                         sortable>
                        </el-table-column>
                        <el-table-column prop="idcType"
                                         label="归属资源池"
                                         sortable></el-table-column>
                        <el-table-column prop="itemKey"
                                         label="监控项key"
                                         sortable></el-table-column>
                        <el-table-column prop="moniIndex"
                                         label="告警内容"
                                         sortable></el-table-column>

                    </el-table>
                </div>
                <div class="yw-page-wrap">
                    <!-- <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page.sync="dataParmas.pageNo"
                                   :page-sizes="pageSizes"
                                   :page-size="dataParmas.pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination> -->
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page.sync="dataParmas.pageNo"
                                   :page-sizes="[10, 20, 50]"
                                   :page-size="dataParmas.pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination>

                </div>

            </el-form>
        </div>
    </div>
</template>

<script>
    import subscribeFactory from 'src/services/alert/rb-alert-subscribe-service.factory.js'
    export default {
        data() {
            return {
                subscribeData: [],
                dataParmas: {// 请求列表参数
                    subscribeRules: '',// 告警规则
                    deviceIp: '',// 设备ip
                    alertLevel: '',// 告警等级
                    idcType: '',// 资源池名称
                    pageNo: 1,
                    pageSize: 10
                },
                option: [],
                loading: true,
                currentPage: 1,
                pageSizes: [10, 20, 50], // 每页多少行数组
                pageSize: 10,
                total: 0,
                options: [],
                value: '',
                multipleSelection: [],
                nameOption: [],
                typeOption: []
            }
        },

        methods: {
            // 获取列表数据
            getTableData() {
                let dataParmas = this.dataParmas
                subscribeFactory.getSubscribeData(dataParmas).then(res => {
                    let _this = this
                    _this.total = res.count
                    _this.subscribeData = res.result
                    _this.loading = false
                })
            },
            // 下拉框过滤筛选
            getOption() {
                subscribeFactory.querySubscribeRules().then(res => {
                    const map = new Map()
                    // this.levelOption = res.filter(e => !map.has(e.alertLevel) && map.set(e.alertLevel, 1))
                    this.nameOption = res.filter(e => !map.has(e.subscribeRules) && map.set(e.subscribeRules, 1))
                    // this.typeOption = res.filter(e => !map.has(e.idcType) && map.set(e.idcType, 1))
                })
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'alert_level' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.option = res

                })
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    // let Arrey = []
                    this.typeOption = res
                })


            },
            // 查询
            goQuery() {
                this.getTableData()
            },
            // 重置
            reset() {
                this.dataParmas.subscribeRules = ''
                this.dataParmas.deviceIp = ''
                this.dataParmas.alertLevel = ''
                this.dataParmas.idcType = ''
            },
            // 删除按钮
            delBtn() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择规则名称', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let arr = []
                    this.multipleSelection.forEach((item) => {
                        arr.push(item.id)
                    })
                    let req = {}
                    req.ids = arr.join(',')
                    subscribeFactory.deteleSubscribeService(req).then(res => {
                        if (res === 'success') {
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            })
                            this.getTableData()
                        }
                    })
                }
            },
            // 导出按钮
            exportBtn() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择规则名称', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let arr = []
                    this.multipleSelection.forEach((item) => {
                        arr.push(item.id)
                    })
                    let req = {
                    }
                    req.ids = arr.join(',')
                    this.loading = true
                    subscribeFactory.exportSubscribeRules(req).then(res => {
                        console.log(res)
                        let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                        let objectUrl = URL.createObjectURL(blob)
                        console.log('objectUrl', objectUrl)
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = objectUrl
                        downLoadElement.download = '(服务台)订阅告警数据列表.xls'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(objectUrl)
                    }).finally(() => {
                        this.loading = false
                    })
                }
            },
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.dataParmas.pageSize = val
                this.getTableData()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.dataParmas.pageNo = val
                this.getTableData()
            },
            checkSelectable(row) {
                if (row.defensetor) {
                    return row.defensetor.indexOf(sessionStorage.username) !== -1
                }
            }

        },

        created() {
            this.getTableData()
            this.getOption()
        },
    }

</script>
<style lang='scss' scoped>
</style>
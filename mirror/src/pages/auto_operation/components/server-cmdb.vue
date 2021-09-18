<template>
    <div class="main">
        <el-form ref="form"
                 label-position="right"
                 :model="form">
            <template v-for="(item, i) in settingRelationList">
                <el-form-item :label="item.cmdbCode.filedName"
                              :key="i">
                    <el-select clearable
                               @change="selectChange(item.cmdbCode.filedCode, $event)"
                               v-if="item.cmdbCode.isBindSource === '是'"
                               v-model="form[item.cmdbCode.filedCode]"
                               :placeholder="'请选择' + item.cmdbCode.filedName">
                        <el-option v-for="(v) in formOptions[item.cmdbCode.filedCode]"
                                   :key="v.id"
                                   :label="v.key"
                                   :value="v.value"></el-option>
                    </el-select>
                    <el-input v-else-if="item.cmdbCode.isBindSource === '否'"
                              v-model="inputs[item.cmdbCode.filedCode]"
                              :placeholder="'请输入' + item.cmdbCode.filedName"></el-input>
                </el-form-item>
            </template>
            <el-form-item>
                <el-button @click="search">查询</el-button>
            </el-form-item>
        </el-form>
        <div class="table">
            <section class="yw-dialog-main no-scroll">

                <el-table ref="serverTable"
                          :data="TableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300"
                          v-loading="loading"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column width="100"
                                     prop="device_type_dict_note_name"
                                     show-overflow-tooltip
                                     label="设备类型"></el-table-column>
                    <el-table-column width="100"
                                     prop="device_model"
                                     show-overflow-tooltip
                                     label="设备型号"></el-table-column>
                    <el-table-column width="100"
                                     prop="device_mfrs_value_name"
                                     show-overflow-tooltip
                                     label="制造厂商 "></el-table-column>
                    <el-table-column width="120"
                                     prop="agent_ip"
                                     show-overflow-tooltip
                                     label="agent_ip"></el-table-column>
                    <el-table-column width="120"
                                     prop="ip"
                                     show-overflow-tooltip
                                     label="IP地址"></el-table-column>
                    <el-table-column width="100"
                                     prop="ipmi_ip"
                                     show-overflow-tooltip
                                     label="IPMI地址"></el-table-column>
                    <el-table-column width="120"
                                     prop="idcType_idc_name_name"
                                     show-overflow-tooltip
                                     label="资源池名称"></el-table-column>
                    <el-table-column width="100"
                                     prop="project_name_project_name_name"
                                     show-overflow-tooltip
                                     label="项目名称"></el-table-column>
                    <el-table-column width="100"
                                     prop="pod_name_pod_name_name"
                                     show-overflow-tooltip
                                     label="POD名称"></el-table-column>
                    <el-table-column width="100"
                                     prop="department1_orgName_name"
                                     show-overflow-tooltip
                                     label="一级部门"></el-table-column>
                    <el-table-column width="100"
                                     prop="department2_orgName_name"
                                     show-overflow-tooltip
                                     label="二级部门"></el-table-column>
                    <el-table-column width="100"
                                     prop="bizSystem_bizSystem_name"
                                     show-overflow-tooltip
                                     label="业务系统"></el-table-column>
                    <el-table-column width="100"
                                     prop="os_detail_version"
                                     show-overflow-tooltip
                                     label="os详细版本"></el-table-column>
                    <el-table-column width="100"
                                     prop="ops"
                                     show-overflow-tooltip
                                     label="联系人"></el-table-column>
                    <el-table-column width="100"
                                     prop="device_description"
                                     show-overflow-tooltip
                                     label="备注"></el-table-column>
                </el-table>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToTargetMachines">添加</el-button>
                <el-button @click="serversCmdbBoxShow">取消</el-button>
            </section>
        </div>
    </div>
</template>

<script>
    import CmdbFactory from 'src/services/auto_operation/rb-auto-operation-cmdb.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        components: {
        },
        props: {
            targetMachines: {
                type: Array,
                default: []
            }
        },
        data() {
            return {
                data: [],
                settingRelationList: [],
                form: {},
                formOptions: {},
                searchKeys: {},
                searchKey: [],
                restSQL: {},
                inputs: {},

                TableData: [],
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
            this.handleServerList()
        },
        methods: {
            handleServerList() {
                let serverListRes = this.$store.state.homeStore.serverListRes
                if (!serverListRes || !serverListRes.dataList || !serverListRes.dataList.length) {
                    this.getSerchData()
                } else {
                    this.total = serverListRes.totalCount
                    this.TableData = serverListRes.dataList
                    this.$emit('getServerList', serverListRes)
                }
            },
            getSerchData() {
                CmdbFactory.getCmdbCondition().then(res => {
                    if (!res) {
                        this.$message.warning('没有主机数据！')
                        return
                    }
                    this.data = res
                    this.settingRelationList = []
                    res.settingRelationList.forEach(res => {
                        if (res.cmdbCode.filedCode !== 'is_install_agent') {
                            this.settingRelationList.push(res)
                            this.searchKeys[res.cmdbCode.filedCode] = res.cmdbCode.cascadeList
                            this.searchKey.push(res.cmdbCode.filedCode)
                            this.form[res.cmdbCode.filedCode] = ''
                            if (res.cmdbCode.isBindSource === '是') {
                                this.getSQL(res.cmdbCode.filedCode, res.cmdbCode.codeBindSource)
                                this.restSQL[res.cmdbCode.filedCode] = res.cmdbCode.codeBindSource
                            }
                        } else {
                            this.form[res.cmdbCode.filedCode] = '11462'
                        }
                    })
                    this.form['token'] = '5245ed1b-6345-11e'
                    this.form['condicationCode'] = 'automatic_agent_search'
                    this.form['pageSize'] = '20'
                    this.form['currentPage'] = '1'
                    this.search()
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            getSQL(code, SQL) {
                let obj = {
                    sql: SQL.tableSql
                }
                CmdbFactory.queryTableData(obj).then(res => {
                    this.$set(this.formOptions, code, res)
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            selectChange(code, event) {
                if (!event) {
                    this.searchKeys[code].forEach((v) => {
                        if (v) {
                            if (this.searchKey.indexOf(v.subFiledCode) > -1) {
                                this.form[v.subFiledCode] && (this.form[v.subFiledCode] = '')
                                this.getSQL(v.subFiledCode, this.restSQL[v.subFiledCode])
                            }
                        } else {
                            this.$forceUpdate()
                        }
                    })
                    Object.keys(this.searchKeys).forEach((v) => {
                        let c = this.searchKeys[v]
                        if (this.form[v] && c.length) {
                            c.forEach((item) => {
                                this.form[item.subFiledCode] && (this.form[item.subFiledCode] = '')
                                let newSQL = item.sqlString.split('?').join(this.form[v])
                                this.getSQL(item.subFiledCode, { tableSql: newSQL })
                            })
                        } else {
                            this.$forceUpdate()
                        }
                    })
                } else {
                    this.searchKeys[code].forEach((v) => {
                        if (v) {
                            if (this.searchKey.indexOf(v.subFiledCode) > -1 && v.sqlString) {
                                this.form[v.subFiledCode] && (this.form[v.subFiledCode] = '')
                                let newSQL = v.sqlString.split('?').join(event)
                                this.getSQL(v.subFiledCode, { tableSql: newSQL })
                            }
                        } else {
                            this.$forceUpdate()
                        }
                    })
                }

                this.$forceUpdate()
            },
            search() {
                let obj = {
                    ...this.form,
                    ...this.inputs,
                    pageSize: this.pageSize,
                    currentPage: this.currentPage
                }
                this.loading = true
                CmdbFactory.fetchUserAuthedAgentHostList(obj).then(res => {
                    this.total = res.totalCount
                    this.TableData = res.dataList
                    this.loading = false
                    this.$emit('getServerList', res)
                }).catch(error => {
                    this.loading = false
                    this.showErrorTip(error)
                })
            },
            addToTargetMachines() {
                this.$emit('addCMDB', this.multipleSelection)
            },
            serversCmdbBoxShow() {
                this.$emit('close', true)
            },
        }
    }
</script>

<style lang="scss" scoped>
    .main {
      display: flex;
      flex-direction: column;
      overflow: auto;
      .el-form {
        display: flex;
        flex-wrap: wrap;
        .el-form-item {
          margin-right: 12px;
          display: flex;
          /deep/ .el-form-item__label {
            width: auto !important;
          }
        }
      }
      .table {
        margin-top: 10px;
        .btn-wrap {
          position: absolute;
          bottom: 20px;
          left: 20px;
        }
      }
    }
</style>
<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="modelList"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :beforeTableRowRender="beforeTableRowRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>

        <!-- 弹框 -->
        <el-dialog width="1100px"
                   class="yw-dialog"
                   title="同步信息"
                   v-if="synchronizationShow"
                   :visible.sync="synchronizationShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-form ref="dialogTable"
                                :formJson="dialogJson"
                                v-model="dialogJson.model"
                                :beforeHttpPro="beforeHttpPro"
                                :afterHttpPro="afterHttpPro"
                                @on="onbind">
                    <div class="yw-el-table-wrap"
                         slot="dialog-table">
                        <el-table :data="abnormalList"
                                  class="yw-el-table"
                                  stripe
                                  highlight-current-row
                                  tooltip-effect="dark"
                                  border
                                  show-overflow-tooltip>
                            <el-table-column prop="master_name"
                                             label="主名称"></el-table-column>
                            <el-table-column prop="master_content"
                                             show-overflow-tooltip
                                             label="主内容"></el-table-column>
                            <el-table-column prop="compare_result"
                                             show-overflow-tooltip
                                             label="异常点"></el-table-column>

                        </el-table>
                    </div>
                </asp-smart-form>
            </div>
        </el-dialog>
        <el-dialog width="1100px"
                   class="yw-dialog"
                   title="对比日志"
                   v-if="dailyShow"
                   :visible.sync="dailyShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main ym-table-height">
                <asp-smart-form ref="dailyTable"
                                :formJson="dailyJson"
                                v-model="dailyJson.model"
                                :beforeHttpPro="beforeHttpPro"
                                @on="onbind">
                    <div class="yw-el-table-wrap"
                         slot="daily-table">
                        <el-table :data="dailyList"
                                  class="yw-el-table"
                                  stripe
                                  highlight-current-row
                                  tooltip-effect="dark"
                                  border
                                  show-overflow-tooltip>
                            <el-table-column show-overflow-tooltip
                                             label="主配置文件地址">
                                <template slot-scope="scope">
                                    <a @click="downLoad(scope.row.masterConfigFile)">{{scope.row.masterConfigFile.split('/')[4]}}</a>
                                </template>
                            </el-table-column>
                            <el-table-column show-overflow-tooltip
                                             label="被配置原始文件">
                                <template slot-scope="scope">
                                    <a @click="downLoad(scope.row.backupConfigFile)">{{scope.row.backupConfigFile.split('/')[4]}}</a>
                                </template>

                            </el-table-column>
                            <el-table-column prop="addResult"
                                             show-overflow-tooltip
                                             label="新增未同步"></el-table-column>
                            <el-table-column prop="modifyResult"
                                             show-overflow-tooltip
                                             label="修改未同步"></el-table-column>
                            <el-table-column prop="delResult"
                                             show-overflow-tooltip
                                             label="删除未同步"></el-table-column>
                            <el-table-column prop="compareTime"
                                             show-overflow-tooltip
                                             label="最新比对时间">
                                <template slot-scope="scope">
                                    {{formatDate(scope.row.compareTime)}}
                                </template>
                            </el-table-column>

                        </el-table>
                    </div>

                </asp-smart-form>
            </div>
        </el-dialog>

        <el-dialog width="500px"
                   class="yw-dialog"
                   title="最近7天的配置文件"
                   v-if="indexShow"
                   :visible.sync="indexShow"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form mtop10"
                         ref="addForm"
                         :model="addForm">
                    <el-form-item :label="addForm.masterIp"
                                  prop="masterIndex">
                        <el-select v-model="addForm.masterIndex"
                                   placeholder="请选择">
                            <el-option v-for="item in optionList.masterIndex"
                                       :key="item.index"
                                       :label="item"
                                       :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item :label="addForm.backupIp"
                                  prop="backupIndex">
                        <el-select v-model="addForm.backupIndex"
                                   placeholder="请选择">
                            <el-option v-for="item in optionList.backupIndex"
                                       :key="item.index"
                                       :label="item"
                                       :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>

                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="compareData">确定
                </el-button>
                <el-button @click="closeOption">取消</el-button>
            </section>

        </el-dialog>
        <YwImport v-if="display.isImport"
                  :showImport="display.isImport"
                  @setImportDisplay="closeParent"
                  :importType="importType"></YwImport>

    </div>
</template>

<script>
    import tableJson from './smart_data/contrastList.json'
    import dialogJson from './smart_data/synchronization.json'
    import dailyJson from './smart_data/contrastDaily.json'
    import { createDownloadFile } from 'src/utils/utils.js'
    import rbNetContrastServicesFactory from 'src/services/net/rb-net-contrast-services.factory.js'


    export default {
        name: 'LldpList',
        components: {
            YwImport: () => import('src/components/common/yw-import.vue')
        },
        data() {
            return {
                // 表格
                formSearch: {},
                tableJson: tableJson,
                modelList: tableJson.model,
                subFormSelectData: [],// 选中的表格行
                // 弹窗
                synchronizationShow: false,
                dialogJson: dialogJson,
                dailyShow: false,
                dailyJson: dailyJson,
                display: {
                    isImport: false
                },
                importType: 'contrast',
                abnormalList: [],
                dailyList: [],
                indexShow: false,
                dailyId: '',
                addForm: {
                    masterIndex: '',
                    backupIndex: '',
                    masterIp: '',
                    backupIp: ''
                },
                optionList: {}
            }
        },
        created() {
            let str = '/file_download/config-network/20201230/config-network-huchi-20201229_192.168.8.19_140715.txt'
            console.log(str.split('/')[4])
        },
        watch: {

        },
        methods: {
            // 日期格式转换
            formatDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                let hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
                let mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
                let ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())
                return YY + MM + DD + ' ' + hh + mm + ss
            },
            compareData() {
                rbNetContrastServicesFactory.compareData(this.dailyId, this.addForm.masterIndex, this.addForm.backupIndex).then(res => {
                    if (res.flag === true) {
                        this.$message.success('操作成功')
                        this.indexShow = false
                        this.$refs.addForm.resetFields()
                    }
                })
            },
            closeOption() {
                this.$refs['addForm'].resetFields()
                this.indexShow = false
            },
            downLoad(link) {
                //  let domain = document.domain
                //  console.log(domain)
                //  window.open('http://10.12.70.39:5566' + link)
                this.$message.success('请求成功，下载中…')
                let downLoadElement = document.createElement('a')
                downLoadElement.href = link
                downLoadElement.setAttribute('download', link.split('/')[5])
                document.body.appendChild(downLoadElement)
                downLoadElement.click()
                document.body.removeChild(downLoadElement)
            },
            closeParent(val) {
                this.display.isImport = val
            },
            // 表单及表格 回调事件
            onbind(data) {
                console.log(data)
                this.formSearch = this.$utils.deepClone(data.model)
                // 选择行
                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }
                let temp = []
                temp = this.subFormSelectData.map((item) => {
                    return item.id
                })
                switch (data.item.columnName) {
                    // 导出清单
                    case 'export-list':
                        {
                            if (temp && temp.length > 0) {
                                rbNetContrastServicesFactory.exportList(temp).then(res => {
                                    createDownloadFile(res, '配置比对清单.xls')
                                })
                            } else {
                                this.$message.error('请选择要导出的内容')
                            }
                        }
                        break
                    case 'export-detail':
                        {
                            if (temp && temp.length > 0) {
                                rbNetContrastServicesFactory.exportDetail(temp).then(res => {
                                    createDownloadFile(res, '配置比对明细.xls')
                                })
                            } else {
                                this.$message.error('请选择要导出的内容')
                            }

                        }
                        break
                    case 'import-list':
                        {
                            // rbNetContrastServicesFactory.exportList(temp).then(res => {
                            //     createDownloadFile(res, '配置比对明细.xls')
                            // })
                            this.display.isImport = true
                        }
                        break
                    case 'addCount':
                        {
                            this.dialogJson.model.idcType = data.row.idcType
                            this.dialogJson.model.ip = data.row.masterIp + '(主)~' + data.row.backupIp + '(备)'
                            this.dialogJson.model.type = '新增未同步'
                            this.dialogJson.model.compareTime = this.formatDate(data.row.compareTime)
                            this.dialogJson.model.masterConfigFile = data.row.masterConfigFile.split('/')[4]
                            this.dialogJson.model.backupConfigFile = data.row.backupConfigFile.split('/')[4]
                            this.dialogJson.model.masterConfigFileName = data.row.masterConfigFile
                            this.dialogJson.model.backupConfigFileName = data.row.backupConfigFile
                            this.abnormalList = []
                            this.abnormalList = JSON.parse(data.row.addDatas)
                            this.synchronizationShow = true
                        }
                        break
                    case 'modifyCount':
                        {
                            this.dialogJson.model.idcType = data.row.idcType
                            this.dialogJson.model.ip = data.row.masterIp + '(主)~' + data.row.backupIp + '(备)'
                            this.dialogJson.model.type = '修改未同步'
                            this.dialogJson.model.compareTime = this.formatDate(data.row.compareTime)
                            this.dialogJson.model.masterConfigFile = data.row.masterConfigFile.split('/')[4]
                            this.dialogJson.model.backupConfigFile = data.row.backupConfigFile.split('/')[4]
                            this.dialogJson.model.masterConfigFileName = data.row.masterConfigFile
                            this.dialogJson.model.backupConfigFileName = data.row.backupConfigFile
                            this.abnormalList = []
                            this.abnormalList = JSON.parse(data.row.modifyDatas)
                            this.synchronizationShow = true
                        }
                        break
                    case 'delCount':
                        {
                            this.dialogJson.model.idcType = data.row.idcType
                            this.dialogJson.model.ip = data.row.masterIp + '(主)~' + data.row.backupIp + '(备)'
                            this.dialogJson.model.type = '删除未同步'
                            this.dialogJson.model.compareTime = this.formatDate(data.row.compareTime)
                            this.dialogJson.model.masterConfigFile = data.row.masterConfigFile.split('/')[4]
                            this.dialogJson.model.backupConfigFile = data.row.backupConfigFile.split('/')[4]
                            this.dialogJson.model.masterConfigFileName = data.row.masterConfigFile
                            this.dialogJson.model.backupConfigFileName = data.row.backupConfigFile
                            this.abnormalList = []
                            this.abnormalList = JSON.parse(data.row.delDatas)
                            this.synchronizationShow = true
                        }
                        break
                    case 'row-contrast':
                        {
                            rbNetContrastServicesFactory.indexList(data.row.id).then(res => {
                                this.optionList = {}
                                this.optionList = res
                            })
                            this.dailyId = data.row.id
                            this.addForm.masterIp = data.row.masterIp
                            this.addForm.backupIp = data.row.backupIp
                            this.indexShow = true
                        }
                        break
                    case 'row-recound':
                        {
                            rbNetContrastServicesFactory.logsDataList(data.row.id).then(res => {
                                this.dailyList = []
                                this.dailyList = res
                            })
                            this.dailyId = data.row.id
                            this.dailyJson.model.idcType = data.row.idcType
                            this.dailyJson.model.ip = data.row.masterIp + '(主)~' + data.row.backupIp + '(备)'
                            if (data.row.compareTime) {
                                this.dailyJson.model.compareTime = this.formatDate(data.row.compareTime)
                            } else {
                                this.dailyJson.model.compareTime = ''

                            }
                            this.dailyShow = true
                        }
                        break
                    case 'export-daily':
                        {
                            rbNetContrastServicesFactory.exportLogs(this.dailyId).then(res => {
                                createDownloadFile(res, '比对记录.xls')
                            })
                        }
                        break
                    case 'masterConfigFile':
                        {
                            this.downLoad(data.model.masterConfigFileName)
                        }
                        break
                    case 'backupConfigFile':
                        {
                            this.downLoad(data.model.backupConfigFileName)
                        }
                        break
                }
            },
            /**
             * 智能表格页面所有请求前的前置操作
             * 例如：修改请求参数、修改请求方式、修改请求URL、或者请求条件不满足不给发送请求
             * @param tableItem 组件对象属性集
             * @param params 请求参数body
             * @param httpMethod.url 请求地址URL
             * @param httpMethod.type 请求方式，目前主要三种：'post+json', 'post+form', 'get'
             * @param row 当组件为表格并且是表格操作列触发的请求，此参数返回表格行数据，其它返回undefined
            */
            beforeHttp({ tableItem, params, httpMethod, row }) {

                switch (tableItem.columnName) {
                    case 'table_1607479557915':
                        Object.assign(params, {
                            pageNum: params.page,
                            pageSize: params.rows,
                        })
                        break
                }
            },
            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body
            */
            afterHttp({ tableItem, responseBody }) {

                switch (tableItem.columnName) {
                    // 表格
                    case 'table_1607479557915':
                        responseBody.dataList = responseBody.result
                        responseBody.totalCount = responseBody.count
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                }

            },
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                console.log('after', item)
                console.log('after', model)

                callback(httpObject)
            },
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
                console.log('after', item)
                console.log('after', model)
            },
            /**
            * 智能表格页面上的按钮的前置操作：包括不限于查询区域，表格顶部、表格操作列
            * 例如：对操作按钮进行处理的数据进行预处理，或者对按钮请求进行个性胡逻辑判断等
            * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
            * @param item 组件对象属性集
            * @param rowObj 当组件为表格操作列中的按钮，此参数返回表格行数据，其它返回undefined
            * @param next 回调函数
           */
            beforeButton({ item, rowObj, next }) {
                // switch (item.columnName) {
                //     // 异常日志
                //     case 'btn-operate-errorLogs':
                //         this.$refs.aspSmartTable.asp_setHidden('btn-operate-errorLogs', true)
                //         next(item, rowObj)
                //         break
                //     default:
                //         next(item, rowObj)
                //         break
                // }
                next(item, rowObj)
            },
            /**
             * 表格内容渲染之前的前置动作
             * @param tableName 当前表格名称
             * @param tableData 表格当页的数据
             * @param columnItem 表格当前列的信息
             * @param scope 表格行信息包含属性 $inde row等
             * @param callBack 参数说明如下
             * 参数一：指定修改的表格名称
             * 参数二：指定修改的列名
             * 参数三：指定索引集合，整列生效则传空数组[],指定某几行生效则传索引集合[1,3]
             * 参数四：显示内容{content：可以是文本也可以是html代码片段}
            */
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {


            },

            /**
             * 智能表格监听所有行绘制的前置回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param tableData 表格数据模型
             * @param row:  表格组件当前绘制的行数据
             * @param rowClassName: 子表单组件当前行绘制class name
             * @param callback: 回调api
             * @param           callback回调api参数: rowClassName: 子表单组件当前行绘制class name
            */
            beforeTableRowRender({ item, tableData, row, rowClassName }) {
            },

        }
    }
</script>

<style lang="scss" scoped>
</style>


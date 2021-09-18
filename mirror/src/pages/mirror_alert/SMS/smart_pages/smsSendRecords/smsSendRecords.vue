<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="model"
                         :status="status"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>
        <!-- 弹窗：编辑模板分类 -->
        <DialogSmsTags v-if="dialogTags.dialogVisible"
                       @closeDialog="hideDialogTags"
                       :dialogTags="dialogTags"></DialogSmsTags>
        <!-- 异常日志 -->
        <el-dialog width="750px"
                   class="yw-dialog"
                   title="异常日志"
                   :visible.sync="dialogLogs.dialogVisible"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <div v-html="dialogLogs.data.errorLog">

                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import tableJson from '../smart_data/smsSendRecords.json'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import CommonOption from 'src/utils/commonOption.js'
    import rbAlertSmsServices from 'src/services/alert/rb-alert-sms-service.factory.js'

    export default {
        mixins: [CommonOption],
        name: 'SmsSendRecords',
        components: {
            DialogSmsTags: () => import('../smsTags/smsTags.vue'),
        },
        data() {
            return {
                // 发送短信
                tableJson: tableJson,
                status: 'add',
                model: tableJson.model,
                subFormSelectData: [],
                // dialog
                dialogTags: {
                    dialogVisible: false,
                    title: '短信内容保存到模板分类',
                    status: 'editContentToTags',
                    chooseTagList: [// 分类列表

                    ],
                    chooseTag: {},
                    addTag: {
                        name: ''
                    },
                    templates: ''// 模板内容
                },
                dialogLogs: {
                    dialogVisible: false,
                    data: ''
                }
            }
        },

        mounted() {

        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {

                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }
                switch (data.item.columnName) {

                    // 删除
                    case 'btn-table-delete':
                        {
                            let temp = this.subFormSelectData.map((item) => {
                                return item.id
                            })
                            this.deleteDatas(temp)
                        }

                        break
                    // 导出
                    case 'btn-table-export':
                        {
                            let params = {
                                startTime: formatDate(this.model.startTime),
                                endTime: formatDate(this.model.endTime),
                                receiver: this.model.receiver,
                                content: this.model.content,
                                status: this.model.status,
                            }
                            rbAlertSmsServices.exportSmsRecords(params).then(res => {
                                if (res.message === 'success') {
                                    this.$message.success('请求成功，下载中…')
                                    // window.open(`${location.origin}${res.path}`)
                                    // window.open(`http://10.12.70.40:8080${res.path}`)

                                    this.exportFilesByLink({
                                        path: res.path,
                                        fileName: '短信记录明细.xlsx'
                                    })
                                }

                            })

                        }
                        break
                    // 发送
                    case 'btn-operate-send':
                        this.$router.push({
                            path: '/mirror_alert/SMS/smart_pages',
                            query: {
                                sendTemplates: data.row.content
                            }
                        })
                        break
                    // 删除
                    case 'btn-operate-delete':
                        this.deleteDatas([data.row.id])
                        break
                    // 保存模板
                    case 'btn-operate-saveTemplates':
                        this.dialogTags.templates = data.row.content
                        this.showDialogTags()
                        break
                    // 异常日志
                    case 'btn-operate-errorLogs':
                        this.dialogLogs.dialogVisible = true
                        this.dialogLogs.data = data.row
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
                    // 查询短信发送记录
                    case 'table_smsSendRecords':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,
                            startTime: formatDate(params.startTime),
                            endTime: formatDate(params.endTime),
                            status: params.status
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
                    // 短信发送记录
                    case 'table_smsSendRecords':
                        responseBody.dataList = responseBody.data.result.map((item) => {
                            item.createTime = formatDate(item.createTime)
                            if (item.receiverName && item.receiverName !== 'null') {
                                item.receiverMobile = `${item.receiverName}<${item.receiverMobile}>`
                            }
                            return item
                        })
                        responseBody.totalCount = responseBody.data.count

                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                }
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
                switch (item.columnName) {
                    // 异常日志
                    case 'btn-operate-errorLogs':
                        this.$refs.aspSmartTable.asp_setHidden('btn-operate-errorLogs', true)
                        next(item, rowObj)
                        break
                    default:
                        next(item, rowObj)
                        break
                }

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
                let img = 'email-success.png'
                switch (scope.row.status) {
                    case 0:
                        img = 'email-error.png'
                        break
                    default:
                        img = 'email-success.png'
                        break
                }
                callBack(tableName, 'status', [], { content: `<img src="/static/img/sms/${img}">` })
            },
            deleteDatas(idList = []) {
                if (idList.length < 1) {
                    this.$confirm('请先选择数据?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    }).catch(() => {
                    })
                    return
                }
                let params = {
                    idList: idList,
                    // id: idList.join(','),
                }
                rbAlertSmsServices.deleteSmsRecords(params).then(res => {
                    if (res.state === 'success') {
                        this.$message.success('删除成功!')
                        this.$refs.aspSmartTable.asp_refreshTableList('table_smsSendRecords', false)
                    } else {
                        this.$message.error('删除失败!')
                    }
                })
            },
            showDialogTags() {
                this.dialogTags.dialogVisible = true
            },
            hideDialogTags(data) {
                this.dialogTags.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作

                }

            },

        }
    }
</script>

<style lang="scss" scoped>
    @import "../sms.scss";
</style>

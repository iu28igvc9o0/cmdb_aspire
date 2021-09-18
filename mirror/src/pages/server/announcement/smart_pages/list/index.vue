<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="pageJson"
                         v-model="model"
                         :beforeButton="beforeButton"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         @on="onbind">
            <div slot="customStatus">
                <span>
                    <span>状态：</span>
                    <span class="pointer"
                          :class="{'mleft10' : index !== 0, 'blue' : formSearch.status === item.id}"
                          v-for="(item,index) in statusList"
                          :key="item.id"
                          @click="searchByCondition('status', item.id)">{{item.name}}</span>
                </span>
                <span class="mleft10">
                    <span>公告范围：</span>
                    <span class="pointer"
                          :class="{'mleft10' : index !== 0, 'blue' : formSearch.range === item.id}"
                          v-for="(item,index) in noticeScopeList"
                          :key="item.id"
                          @click="searchByCondition('range', item.id)">{{item.name}}</span>
                </span>
            </div>
        </asp-smart-table>
    </div>
</template>

<script>
    import pageJson from './smart_data/index.json'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbBpmServices from 'src/services/bpm/rb-bpm-services.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    // import templateEditorVue from '../../../../auto_operation/inspection/template/edit/template-editor.vue'

    export default {
        mixins: [rbAutoOperationMixin, CommonOption],
        name: 'AutoOperationSmartBugManage',
        components: {
        },
        data() {
            return {
                formSearch: {
                    status: '',
                    range: ''
                },
                reseting: false,

                pageJson: pageJson,
                model: pageJson.model,

                statusList: [
                    {
                        name: '有效',
                        id: '有效',
                    },
                    {
                        name: '无效',
                        id: '无效',
                    },
                ],
                noticeScopeList: [
                    {
                        name: '全部',
                        id: '全部',
                    },
                    {
                        name: '局部',
                        id: '局部',
                    },
                ],
                // 表格checkbox选中
                subFormSelectData: [],

            }
        },
        watch: {
        },
        computed: {
            smartTable() {
                return this.$refs.aspSmartTable
            },
        },
        created() {
        },
        methods: {
            searchByCondition(key, value) {
                this.formSearch[key] = value
                this.smartTable.asp_sendTableQuery('table_announcement_list')
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
                console.log('beforeButton===', item, rowObj, next)
                switch (item.columnName) {
                    // api重置
                    case 'btn-form-reset':
                        for (const key in this.formSearch) {
                            this.formSearch[key] = ''
                        }
                        this.reseting = true
                        break
                }
                next(item, rowObj)
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
                let querySubject = this.$route.query.subject
                switch (tableItem.columnName) {
                    // 公告列表 
                    case 'table_announcement_list':
                        // 页面初始化，设置url参数条件
                        this.model.subject = params.subject = querySubject || ''
                        // 点击重置按钮时，需要手动重置带入的url参数
                        if (this.reseting) {
                            this.model.subject = params.subject = ''
                        }
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows
                        }, this.formSearch)
                        break

                }
                console.log('beforeHttp====', tableItem, params, httpMethod, row)
            },
            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body
            */
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    // 表格列表数据格式必须统一格式：this.$utils.smartTableDataFormat
                    // 下拉框数据格式{data: [{},{}]}：option字段label、value必须与smartLayout上的设置一致

                    // 通告类型
                    case 'noticeType':
                        {
                            let data = responseBody.data.map(item => {
                                item.label = item.name
                                // item.value = item.value
                                return item
                            })
                            this.$utils.smartTableSelectDataFormat(responseBody, data)
                        }
                        break
                    default:
                        this.$utils.smartTableDataFormat(tableItem, responseBody, 'result', 'count')

                }
                // console.log('afterHttp====', tableItem, responseBody)
            },
            // 表单及表格 回调事件
            onbind(data) {
                if (data.type === 'multipleSelection') {
                    this.subFormSelectData = data.subFormSelectData
                }

                // 清除条件
                if (data.type === 'change' && !data.model[data.item.columnName]) {
                    this.reseting = true
                } else if (data.type === 'change' && data.model[data.item.columnName]) {
                    this.reseting = false
                }

                switch (data.item.columnName) {
                    // 发起公告
                    case 'btn-table-send':
                        {
                            rbBpmServices.getBpmDefId({ defKey: 'ggsplc' }).then(res => {
                                let defId = res.data && res.data.defId || ''
                                this.$router.push({
                                    path: '/resource/flow',
                                    query: { routerHash: `start/${defId}`, currentTitle: '发起公告' }
                                })
                            })
                        }

                        break
                    // 启用
                    case 'btn-table-run':
                        {
                            if (this.subFormSelectData.length < 1) {
                                this.$confirm('请先选择数据?', '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(() => {

                                }).catch(() => {
                                })
                                return
                            }
                            this.showFullScreenLoading({ text: '启用中, 请稍等...' })
                            let temp = this.subFormSelectData.map((item) => {
                                return item.id
                            })
                            let data = {
                                // ids: temp.join(',')
                                ids: temp
                            }
                            let params = {
                                status: 'enable'
                            }
                            rbBpmServices.runNotice(data, params).then(res => {
                                this.smartTable.asp_sendTableQuery('table_announcement_list')
                                if (res.success) {
                                    this.$message.success(res.msg)
                                } else {
                                    this.$message.error(res.msg)
                                }
                            }).finally(() => {
                                this.closeFullScreenLoading()
                            })
                        }
                        break
                    // 停用
                    case 'btn-table-stop':
                        {
                            if (this.subFormSelectData.length < 1) {
                                this.$confirm('请先选择数据?', '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(() => {

                                }).catch(() => {
                                })
                                return
                            }
                            this.showFullScreenLoading({ text: '停用中, 请稍等...' })
                            let temp = this.subFormSelectData.map((item) => {
                                return item.id
                            })
                            let data = {
                                // ids: temp.join(',')
                                ids: temp
                            }
                            let params = {
                                status: 'disable'
                            }
                            rbBpmServices.stopNotice(data, params).then(res => {
                                this.smartTable.asp_sendTableQuery('table_announcement_list')
                                if (res.success) {
                                    this.$message.success(res.msg)
                                } else {
                                    this.$message.error(res.msg)
                                }
                            }).finally(() => {
                                this.closeFullScreenLoading()
                            })
                        }
                        break
                    // 导出
                    case 'btn-table-export':
                        {
                            this.showFullScreenLoading({ text: '导出中, 请稍等...' })
                            let params = Object.assign({
                                startTime: this.model.startTime,
                                endTime: this.model.endTime,
                                subject: this.model.subject,
                                status: this.model.status,
                                range: this.model.range,
                                noticeType: this.model.noticeType,
                                operateStatus: ''
                            }, this.formSearch)
                            rbBpmServices.exportNotice(params).then(res => {
                                this.exportFiles({
                                    data: res,
                                    fileType: 'application/vnd.ms-excel',
                                    fileName: '公告列表.xls',
                                })

                            }).finally(() => {
                                this.closeFullScreenLoading()
                            })

                        }
                        break
                    // 工单号字段
                    case 'instId':
                        this.$router.push({
                            path: '/resource/flow',
                            query: { routerHash: `inst/${data.row.instId}`, currentTitle: `工单-${data.row.instId}` }
                        })

                        break
                    // 通知推送
                    case 'btn-operate-push':
                        this.showFullScreenLoading({ text: '推送中, 请稍等...' })
                        rbBpmServices.pushNotice({ id: data.row.id }).then(res => {
                            this.smartTable.asp_sendTableQuery('table_announcement_list')
                            if (res.success) {
                                this.$message.success(res.msg)
                            } else {
                                this.$message.error(res.msg)
                            }
                        }).finally(() => {
                            this.closeFullScreenLoading()
                        })
                        break
                }

            },

        }
    }
</script>

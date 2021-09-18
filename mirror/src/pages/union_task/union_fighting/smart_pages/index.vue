<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="model"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>
        <!-- 弹窗：新增、编辑 -->
        <DialogEdit v-if="dialogMsg.dialogVisible"
                    @closeDialog="closeDialog"
                    :dialogMsg="dialogMsg"></DialogEdit>
        <!-- 弹窗：标签汇总 -->
        <DialogFightTags v-if="dialogTags.dialogVisible"
                         @closeDialog="closeDialogTags"
                         :dialogMsg="dialogTags"></DialogFightTags>
    </div>
</template>

<script>
    import tableJson from './smart_data/index.json'
    import CommonOption from 'src/utils/commonOption.js'
    import rbUnionTaskServices from 'src/services/unionTask/rb-unionTask-service.factory.js'
    export default {
        mixins: [CommonOption],
        components: {
            DialogEdit: () => import('./fightEdit/dialog-edit.vue'),
            DialogFightTags: () => import('./fightTags/dialog-fightTags.vue'),
        },
        data() {
            return {
                // 发送短信
                tableJson: tableJson,
                model: tableJson.model,
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    status: 'add',// add、edit
                    title: '',
                    data: {} // 数据
                },
                dialogTags: {
                    dialogVisible: false,
                    data: {} // 数据
                }
            }
        },

        mounted() {

        },
        methods: {
            /**
        * 智能表格监听所有组件的交互事件操作：监听、捕捉事件响应
        * @param item 响应组件对象属性集（类型、组件Id，控件内元数属性），columnName每个组件单元的唯一码（组件单元Id）
        * @param type 事件类型（click/blur/onblur等）
        * @param index 当是表格组件时，返回组件的行号
        * @param model 查询区域表单数据模型
        * @param tableModel 表格组件数据模型
        * @param row 表格行数据
        * @param multipleSelection 表格多选数据（当出现列表复选时才有，包括跨页数据，整行数据）
        * @param sortProps 排序属性
        * @returns {Promise<void>}
        */
            async onbind({ item, type, index, model, tableModel, row, subFormSelectData, sortProps }) {
                switch (item.columnName) {
                    // 新增
                    case 'add':
                        this.showDialog({ status: 'add', title: '新增协同作战' })
                        break
                    // 编辑
                    case 'btn-operate-edit':
                        this.showDialog({ status: 'edit', title: '编辑协同作战', data: row })
                        break
                    // 删除
                    case 'btn-operate-delete':
                        {
                            this.$confirm('确定删除数据吗?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                let params = {
                                    id: row.id,
                                }
                                rbUnionTaskServices.deleteUnionFight(params).then(res => {
                                    if (res.success) {
                                        this.$message.success(res.msg)
                                        this.$refs.aspSmartTable.asp_refreshTableList('table', false)
                                    } else {
                                        this.$message.error(res.msg)
                                    }
                                })
                            }).catch(() => {
                            })

                        }
                        break
                    // 进入作战室
                    case 'btn-operate-goRoom':
                        this.$router.push({
                            path: '/union_task/union_fighting/smart_pages/fightRoom',
                            query: {

                            }
                        })
                        break
                    // 重启作战
                    case 'btn-operate-fightRestart':
                        this.$router.push({
                            path: '/union_task/union_fighting/smart_pages/fightRoom',
                            query: {

                            }
                        })
                        break
                    // 标签汇总
                    case 'btn-operate-goTemplates':
                        this.showDialogTags({ data: row })
                        break
                    // 作战历史
                    case 'btn-operate-fightHistory':
                        this.$router.push({
                            path: '/union_task/union_fighting/smart_pages/fightRoom',
                            query: {

                            }
                        })
                        break
                }

            },
            /**
             * 智能表格页面所有请求前的前置操作
             * 例如：修改请求参数、修改请求方式、修改请求URL、或者请求条件不满足不给发送请求
             * @param tableItem 组件对象属性集
             * @param params 请求参数body，数据格式如下(字段格式不一致的需要自行转换)如下:
             *                                         {
             *                                             page：1， // 分页属性(当前页号)，数字类型 （不是分页接口，没有这个字段）
             *                                             rows: 10，// 分页属性(页大小)，数字类型 （不是分页接口，没有这个字段）
             *                                             .......   // 业务属性字段
             *                                          }
             * @param httpMethod.url 请求地址URL
             * @param httpMethod.type 请求方式，目前主要六种：'post+json', 'post+form', 'get'，'put+json'，'delete+json'，'patch+json'
             * @param row 当组件为表格并且是表格操作列触发的请求，此参数返回表格行数据，其它返回undefined
             */
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    // 查询表格
                    case 'table':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                        })
                        break
                }
            },
            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body, 数据包格式(字段格式不一致的需要自行转换)如下：
             *                                              {
             *                                                status: "200", // 业务状态码，字符串类型，成功返回"200"，失败返回其它数据
             *                                                message: "",   // 业务提示语，字符串类型，给业务的提示语属性
             *                                                page：1，      // 分页属性(当前页号)，数字类型 （不是分页接口，没有这个字段）
             *                                                total: 53，    // 分页属性(总记录大小)，数字类型 （不是分页接口，没有这个字段）
             *                                                data: {}或者[] // 业务数据区，对象或数组类型，用于各业务逻辑处理
             *                                               }
             */
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    // 查询表格
                    case 'table':
                        responseBody.dataList = responseBody.result.map((item) => {
                            // item.scenesId = item.twTitle
                            // tagIds= [1, 2]
                            //     user= '张三'
                            //     userIds= [1, 2]
                            return item
                        })

                        responseBody.totalCount = responseBody.count

                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                        break
                    // 下拉框：作战场景
                    // case 'scenes':
                    //     this.$utils.smartTableSelectDataFormat(responseBody, responseBody.dataList)
                    //     break
                    // 下拉框：作战状态
                    case 'twStatus':
                        this.$utils.smartTableSelectDataFormat(responseBody)
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
                next(item, rowObj)
            },
            /**
             * 智能表格页面路由跳转的前置操作
             * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
             * @param item 响应组件对象属性集
             * @param row 当响应组件为表格操作列中的按钮时，此参数返回表格行数据，其它返回undefined
             * @param routerObj.routerType: 路由类型
             * @param routerObj.routerParamType 路由参数类型
             * @param routerObj.routerUrl 路由地址或名称
             * @param routerObj.routerParamValue 路由参数
             * @param next 回调函数
             */
            beforeRouter({ item, row, routerObj, next }) {
                next(routerObj)
            },
            /**
             * 表格内容渲染之前的前置动作，
             * @param tableName 当前表格名称
             * @param tableData 表格当页的数据
             * @param columnItem 表格当前列的信息
             * @param scope 表格行信息包含属性 $index row等
             * @param callback 回调事件，用于改变指定列的显示内容
             * @param callback 参数说明如下
             * 参数一：指定修改的表格名称
             * 参数二：指定修改的列名
             * 参数三：指定索引集合，整列生效则传空数组[],指定某几行生效则传索引集合[1,3]
             * 参数四：显示内容{ content: 可以是文本也可以是html代码片段}
             */
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                let bg = '#aaa'
                let text = scope.row.twStatus
                let content = ''
                switch (scope.row.twStatus) {
                    case '1':
                        bg = '#3A8AD4'
                        text = '协同中'
                        break
                    case '2':
                        bg = '#aaa'
                        text = '协同结束'
                        break
                    case '3':
                        bg = '#aaa'
                        text = '已汇总'
                        break
                    default:
                        break
                }
                content = `<span style="display:inline-block;width:70px;height:22px;line-height:22px;text-align:center;border-radius: 10px;color: white;background:${bg}">${text}</span>`
                callBack(tableName, 'twStatus', [], { content: content })
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
                return rowClassName
            },
            /**
             * 表格页码大小发生变化时触发的前置事件
             * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
             * @param tableItem 表格对象属性集
             * @param pageSize 表格页码大小
             * @param next 回调函数
             */
            sizeChange({ tableItem, pageSize, next }) {
                next(true) // 允许继续运行传true, 否则传false
            },
            /**
             * 表格当前页发生变化时触发的前置事件，包括点翻页、上一页、下一页、刷新页、重置页
             * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
             * @param tableItem 表格对象属性集
             * @param currentPage 当前页码号
             * @param next 回调函数
             */
            currentChange({ tableItem, currentPage, next }) {
                next(true) // 允许继续运行传true, 否则传false
            },
            showDialog(obj = {}) {
                this.dialogMsg.status = obj.status
                this.dialogMsg.title = obj.title
                this.dialogMsg.data = obj.data || {}
                this.dialogMsg.dialogVisible = true
            },
            closeDialog(data) {
                this.dialogMsg.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作

                }

            },
            showDialogTags(obj = {}) {
                this.dialogTags.data = obj.data
                this.dialogTags.dialogVisible = true
            },
            closeDialogTags(data) {
                this.dialogTags.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作

                }

            },


        }
    }
</script>

<style lang="scss" scoped>
</style>

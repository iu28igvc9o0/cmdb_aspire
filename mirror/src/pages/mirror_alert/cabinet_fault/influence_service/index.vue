<template>
    <div class="preview">
        <!-- 条件、列表 -->
        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="modelList"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>

    </div>
</template>

<script>
    import tableJson from '../smart_data/influenceService.json'
    import CommonOption from 'src/utils/commonOption.js'
    import alertCabinet from 'src/services/alert/rb-alert-cabinet-services.factory.js'
    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        mixins: [CommonOption],
        name: 'FaultList',
        components: {

        },
        data() {
            return {
                // 表格
                formSearch: {},
                tableJson: tableJson,
                modelList: tableJson.model,
                subFormSelectData: [],// 选中的表格行

            }
        },
        computed: {
            routeInfo() {
                return this.$route.query
            }
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                switch (data.item.columnName) {

                    // 导出
                    case 'export-system': {
                        if (this.routeInfo.alertStatus === '') {
                            this.routeInfo.status = ''
                        } else if (this.routeInfo.alertStatus === '0') {
                            this.routeInfo.status = 0
                        } else {
                            this.routeInfo.status = 1
                        }
                        alertCabinet.exportBizSystemList(this.routeInfo).then(res => {
                            if (res) {
                                this.$message.success('下载成功')
                                createDownloadFile(res, '影响业务数据列表.xls')
                            }
                        })
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
                Object.assign(this.modelList, this.routeInfo)
                params.bizSystemCount = this.routeInfo.bizSystemCount
                params.cabinetName = this.routeInfo.cabinetName
                params.cabinetColumn = this.routeInfo.cabinetColumn
                params.alertStatus = this.routeInfo.alertStatus

                switch (tableItem.columnName) {
                    case 'table_1605145789293':
                        Object.assign(params, {
                            page_no: params.page,
                            page_size: params.rows,
                            roomId: this.routeInfo.roomId,
                            idcType: this.routeInfo.idcType
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
                    case 'table_1605145789293':
                        responseBody.dataList = responseBody.result
                        responseBody.totalCount = responseBody.count

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
                // let img = 'email-success.png'
                // switch (scope.row.status) {
                //     case 0:
                //         img = 'email-error.png'
                //         break
                //     default:
                //         img = 'email-success.png'
                //         break
                // }
                // callBack(tableName, 'status', [], { content: `<img src="/static/img/sms/${img}">` })
            },


        }
    }
</script>

<style lang="scss" scoped>
</style>

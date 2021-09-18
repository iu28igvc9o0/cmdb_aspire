<template>
    <!-- 服务台： 创建服务事件工单 -->
    <div class="content-chart"
         v-loading="loading">
        <section class="chart-title-wrap clearfix">
            <span class="chart-title">创建服务事件工单</span>
        </section>
        <div class="chart-section">
            <div class="hp100 event-order-form">
                <asp-smart-form ref="aspSmartForm"
                                :formJson="pageJson"
                                :beforeHttpPro="beforeHttpPro"
                                :afterHttpPro="afterHttpPro"
                                v-model="model"
                                @on="onbind">
                </asp-smart-form>
            </div>
        </div>
    </div>
</template>

<script>
    import pageJson from 'src/pages/custom_pages/components/smart_data/event-order.json'
    import orderList from 'src/pages/custom_pages/components/sub_components/bpm-orders-list.vue'

    export default {
        components: {
            orderList
        },
        data() {
            return {
                pageJson: pageJson,
                model: pageJson.model,
                loading: false,
                searchTimer: null,
                departmentOneList: []
            }
        },
        methods: {
            // 表单及表格 回调事件
            onbind({ item, parent, type, index, model, row, fileData, subFormSelectData }) {
                switch (item.columnName) {
                    case 'btn-reset':
                        this.$utils.smartFormReset(model)
                        break
                    case 'yjbm':
                        model.ejbm = ''
                        break
                }
            },
            // 智能表单页面所有请求前置操作 
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                Object.assign(httpObject.httpBody, model)
                // console.log('beforeHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback)
                if (item.columnName === 'btn-add-submit' || item.columnName === 'btn-add-draft') {
                    this.loading = true
                    let curDepartment = this.departmentOneList.find(item => {
                        return item.code === model.yjbm
                    })
                    if (curDepartment) {
                        Object.assign(httpObject.httpBody, {
                            yjbm: curDepartment.name
                        })
                    }
                    callback(httpObject)
                } else if (item.columnName === 'clr') {
                    // 节流：控制搜索频率
                    if (this.searchTimer) {
                        clearTimeout(this.searchTimer)
                    }
                    this.searchTimer = setTimeout(() => {
                        Object.assign(httpObject.httpBody, {
                            pageNo: 1,
                            pageSize: 500
                        })
                        callback(httpObject)
                    }, 600)
                } else {
                    callback(httpObject)
                }
            },
            // 智能表单页面所有请求后置操作
            // 处理返回后的数据格式responseBody，smartLayout 要求格式必须统一为 {data: {}, status: 200}
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {

                if (item.columnName === 'btn-add-submit' || item.columnName === 'btn-add-draft') {
                    this.$utils.handleSmartResponse(this, responseBody.code === 200, responseBody.data.message)
                    this.loading = false
                }

                // 提交表单后重置为初始值
                // this.$refs.aspSmartForm.asp_resetFields()

                switch (item.columnName) {
                    case 'yjbm':
                        this.departmentOneList = responseBody
                        this.$utils.smartFormSelectDataFormat(callback, responseBody, responseBody.rows)
                        break
                    default:
                        this.$utils.smartFormSelectDataFormat(callback, responseBody, responseBody.rows)
                }
            },

        },
        created() {
        }
    }
</script>

<style lang="scss" scoped>
    @import "./assets/server.scss";
</style>


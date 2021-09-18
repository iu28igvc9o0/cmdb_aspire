<template>
    <!-- 弹窗：人员选择 -->
    <el-dialog width="900px"
               class="yw-dialog"
               :title="dialogMsg.title ? dialogMsg.title : '人员'"
               :visible.sync="dialogMsg.dialogVisible"
               :append-to-body="true"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <section class="yw-dialog-main">
            作战人员
        </section>
    </el-dialog>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        props: ['dialogMsg'],
        components: {

        },
        data() {
            return {

            }
        },

        methods: {
            /**
         * 智能表单监听所有组件的交互事件操作：监听、捕捉事件响应
         * @param item 组件对象属性集（类型、组件Id，控件内元数属性），columnName每个组件单元的唯一码（组件单元Id）
         * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
         * @param type 事件类型（click/blur/onblur等）
         * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
         * @param model 表单数据模型
         * @param row 当组件是嵌套在子表单组件内时，返回组件所在行的数据，否则返回undefined
         * @param fileData 当组件是上传附件时，返回附件数据，否则返回undefined
         * @param subFormSelectData 当组件子表单时，返回选中记录集，包括单选与多选
         * @param jsonName 模板模板文件名
        */
            onbind({ item, parent, type, index, model, row, fileData, subFormSelectData, jsonName }) {

            },
            /**
             * 智能表单页面所有请求的前置回调操作
             * 例如：修改请求上行参数、请求方式、请求地址URL、或者请求条件不满足不给发送请求
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param model 表单数据模型
             * @param httpObject.httpUrl 请求地址URL
             * @param httpObject.httpMethod 请求方式，目前主要六种：'post+json', 'post+form', 'get'，'put+json'，'delete+json'，'patch+json'
             * @param httpObject.httpBody 请求上传数据, 数据包格式(字段格式不一致的需要自行转换)如下：
             *                                         {
             *                                             page：1， // 分页属性(当前页号)，数字类型 （不是分页接口，没有这个字段）
             *                                             rows: 10，// 分页属性(页大小)，数字类型 （不是分页接口，没有这个字段）
             *                                             .......   // 业务属性字段
             *                                          }
             * @param callback: 回调api，参数为httpObject
            */
            beforeHttpPro({ item, parent, index, model }, httpObject, callback) {
                callback(httpObject)
            },
            /**
             * 智能表单页面所有请求的后置回调操作
             * 例如：修改请求上行参数、请求方式、请求地址URL、或者请求条件不满足不给发送请求
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param model 表单数据模型
             * @param responseBody 响应数据包,, 数据包格式(字段格式不一致的需要自行转换)如下：
             *                                           {
             *                                                status: "200", // 业务状态码，字符串类型，成功返回"200"，失败返回其它数据
             *                                                message: "",   // 业务提示语，字符串类型，给业务的提示语属性
             *                                                page：1，      // 分页属性(当前页号)，数字类型 （不是分页接口，没有这个字段）
             *                                                total: 53，    // 分页属性(总记录大小)，数字类型 （不是分页接口，没有这个字段）
             *                                                data: {}或者[] // 业务数据区，对象或数组类型，用于各业务逻辑处理
             *                                            }
             * @param callback: 回调api，参数为responseBody(可以是您定制修改后的responseBody)
             */
            afterHttpPro({ item, parent, index, model }, responseBody, callback) {

                callback(responseBody)
            },
            /**
             * 智能表单页面所有请求的后置回调操作
             * 例如：修改请求上行参数、请求方式、请求地址URL、或者请求条件不满足不给发送请求
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param model 表单数据模型
             * @param responseBody 响应数据包,, 数据包格式(字段格式不一致的需要自行转换)如下：
             *                                           {
             *                                                status: "200", // 业务状态码，字符串类型，成功返回"200"，失败返回其它数据
             *                                                message: "",   // 业务提示语，字符串类型，给业务的提示语属性
             *                                                page：1，      // 分页属性(当前页号)，数字类型 （不是分页接口，没有这个字段）
             *                                                total: 53，    // 分页属性(总记录大小)，数字类型 （不是分页接口，没有这个字段）
             *                                                data: {}或者[] // 业务数据区，对象或数组类型，用于各业务逻辑处理
             *                                            }
             * @param callback: 回调api，参数为responseBody(可以是您定制修改后的responseBody)
            */
            afterLoadingHttpPro({ item, parent, index, model }, responseBody, callback) {

                callback(responseBody)
            },

            /**
             * 智能表单监听所有组件的值发生变化的回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param isTable 当组件是嵌套在子表单组件内时，返回true, 否则返回false
             * @param model 表单数据模型
             * @param oldValue: 组件老数据
             * @param newValue: 组件新数据
             * @param changeDirtect 1: 新增选项选中， 2：取消选项选中, 3: 改变组件值
             * @param changeValue: 组件变化的选项值(当组件是checkbox组件, 或者是支持多选的select组件，其它组件无此属性)
             * @param callback: 回调api
             * @param           callback回调api参数: isContinue，false: 撤回数据改变， true: 继续数据改变后继操作
             * 继续数据改变后继操作
            */
            compDataChangePro({ item, parent, index, isTable, model, oldValue, newValue, changeDirtect, changeValue }, callback) {
                const isContinue = true
                callback(isContinue)
            },
            /**
             * 智能表单监听所有组件的值发生变化并且是由用户交互生成的回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param isTable 当组件是嵌套在子表单组件内时，返回true, 否则返回false
             * @param model 表单数据模型
             * @param oldValue: 组件老数据
             * @param newValue: 组件新数据
             * @param changeDirtect 1: 新增选项选中， 2：取消选项选中, 3: 改变组件值
             * @param changeValue: 组件变化的选项值(当组件是checkbox组件, 或者是支持多选的select组件，其它组件无此属性)
             * @param callback: 回调api
             * @param           callback回调api参数: isContinue，false: 撤回数据改变， true: 继续数据改变后继操作
             * 继续数据改变后继操作
            */
            compDataActiveChangePro({ item, parent, index, isTable, model, oldValue, newValue, changeDirtect, changeValue }, callback) {

                const isContinue = true
                callback(isContinue)
            },
            /**
             * 智能表单监听所有按钮组件点击事件的前置回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param isTable 当组件是嵌套在子表单组件内时，返回true, 否则返回false
             * @param model 表单数据模型
             * @param rowData:  当组件是嵌套在子表单组件内时，返回按钮所在行的数据，否则返回undefined
             * @param callback: 回调api
             * @param           callback回调api参数: isContinue，false: 不继续后继操作， true: 继续后继操作
             * @param           callback回调api参数: rowData, 当组件是嵌套在子表单组件内时，传入按钮所在行的数据
            */
            beforeButtonPro({ item, parent, index, isTable, model, rowData }, callback) {
                const isContinue = true
                callback(isContinue, rowData)
            },
            /**
             * 智能表单监听所有按钮组件点击事件的后置回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param parent 当组件是嵌套在子表单组件内时，是组件父组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param index 当组件是嵌套在子表单组件内时，，返回组件在子表单组件的行号
             * @param isTable 当组件是嵌套在子表单组件内时，返回true, 否则返回false
             * @param model 表单数据模型
             * @param rowData:  当组件是嵌套在子表单组件内时，返回按钮所在行的数据，否则返回undefined
             * @param callback: 回调api
             * @param           callback回调api参数: isContinue，false: 不继续后继操作， true: 继续后继操作
             * @param           callback回调api参数: rowData, 当组件是嵌套在子表单组件内时，传入按钮所在行的数据
            */
            afterButtonPro({ item, parent, index, isTable, model, rowData }, callback) {
                const isContinue = true
                callback(isContinue, rowData)
            },
            /**
             * 智能表单监听所有子表单列校验事件的前置回调响应
             * @param tableItem 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param model 表单数据模型
             * @param value: 子表单发起校验单元格的内容值
             * @param type:  1：单列校验，2：组合校验
             * @param callback: 回调api
             * @param           callback回调api参数: isValidateFail，false: 列校验成功(不出现校验错误语)， true: 列校验不成功(出现校验错误语)
             * @param           callback回调api参数: isContinueValidate，false: 不继续组件内部的校验逻辑， true: 继续组件内部的校验逻辑
             * @param           callback回调api参数: failShowTip：列校验不成功的错误提示语
            */
            beforeColumnValidatePro({ tableItem, model, value, type }, callback) {
                const isValidateFail = false
                const isContinueValidate = true
                callback(isContinueValidate, isValidateFail, undefined)
            },
            /**
             * 智能表单监听所有子表单行绘制的前置回调响应
             * @param item 组件对象属性集(类型、组件columnName，组件内元数属性)，columnName是每个组件的唯一标识码
             * @param model 表单数据模型
             * @param row:  子表单组件当前绘制的行数据
             * @param rowClassName: 子表单组件当前行绘制class name
             * @param callback: 回调api
             * @param           callback回调api参数: rowClassName: 子表单组件当前行绘制class name
            */
            beforeDrawTableRowPro({ item, model, row, rowClassName }, callback) {
                callback(rowClassName)
            },

            // 初始化
            init() {


            }

        },
        mounted() {
            this.init()
        }
    }
</script>

<style lang="scss" scoped>
</style>

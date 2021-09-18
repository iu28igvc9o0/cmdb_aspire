<template>
    <!-- 弹窗：协同作战编辑、新增 -->
    <el-dialog width="900px"
               class="yw-dialog"
               :title="dialogMsg.title"
               :visible.sync="dialogMsg.dialogVisible"
               :append-to-body="true"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <section class="yw-dialog-main">
            <!-- model:{{model}} -->
            <asp-smart-form ref="aspSmartForm"
                            v-model="model"
                            :form-json="formJson"
                            :before-http-pro="beforeHttpPro"
                            :after-http-pro="afterHttpPro"
                            :after-loading-http-pro="afterLoadingHttpPro"
                            :comp-data-change-pro="compDataChangePro"
                            :comp-data-active-change-pro="compDataActiveChangePro"
                            :before-button-pro="beforeButtonPro"
                            :after-button-pro="afterButtonPro"
                            :asp-node-page-this="this"
                            :before-column-validate-pro="beforeColumnValidatePro"
                            @on="onbind">
                <!-- 记录标签 -->
                <section slot="tags"
                         class="tags-box">
                    <!-- 下拉框 -->
                    <el-select v-if="model.twSence"
                               :clearable="true"
                               :filterable="true"
                               multiple
                               v-model="model.labelList"
                               placeholder="请选择">
                        <el-option v-for="(selectItem,selectIndex) in selectTags.tagsOptions"
                                   :key="selectIndex"
                                   :label="selectItem.twLabelName"
                                   :value-key="selectItem.id"
                                   :value="selectItem">
                        </el-option>
                    </el-select>
                    <!-- <el-select v-if="model.twSence"
                               :clearable="true"
                               :filterable="true"
                               multiple
                               v-model="model.labelIdsList"
                               placeholder="请选择">
                        <el-option v-for="(selectItem,selectIndex) in selectTags.tagsOptions"
                                   :key="selectIndex"
                                   :label="selectItem.twLabelName"
                                   :value="selectItem.id">
                        </el-option>
                    </el-select> -->
                    <!-- 自定义 -->
                    <div v-else>
                        <el-tag :key="tagIndex"
                                v-for="(tag,tagIndex) in selfTags.tagsOptions"
                                style="margin-right:5px;"
                                closable
                                :disable-transitions="false"
                                @close="deleteTags(tagIndex)">
                            {{tag.twLabelName}}
                        </el-tag>
                        <el-input v-if="selfTags.inputVisible"
                                  v-model="selfTags.inputValue"
                                  ref="saveTagInput"
                                  style="width:100px;"
                                  @keyup.enter.native="addTags"
                                  @blur="addTags">
                        </el-input>
                        <el-button v-else
                                   class="button-new-tag"
                                   size="small"
                                   @click="showInputTags">+ 添加新标签</el-button>
                    </div>

                </section>
                <!-- 记录标签 -->
                <!-- 作战人员 -->
                <section slot="user"
                         class="user-box">
                    <YwUserChoose :userList="model.userList"
                                  :invalid="!model.userList || model.userList.length<1"
                                  :disabled="true"
                                  @deleteUser="deleteUser"></YwUserChoose>
                </section>
            </asp-smart-form>
        </section>
    </el-dialog>
</template>

<script>
    import formJson from '../smart_data/fightEdit.json'
    import CommonOption from 'src/utils/commonOption.js'
    import YwUserChooseOption from 'src/components/common/yw-user-choose/yw-user-choose.js'
    import rbUnionTaskServices from 'src/services/unionTask/rb-unionTask-service.factory.js'
    export default {
        mixins: [CommonOption, YwUserChooseOption],
        props: ['dialogMsg'],
        components: {
            YwUserChoose: () => import('src/components/common/yw-user-choose/yw-user-choose.vue'),
        },
        data() {
            return {
                // form
                formJson: formJson,
                model: formJson.model,
                // 记录标签下拉框
                selectTags: {
                    // 下拉框数据
                    tagsOptions: [
                        //     id: 1,
                        //     twLabelName: '11111111'
                        //     twTeamworkId:'11111'
                    ]
                },
                // 记录标签自定义
                selfTags: {
                    // 当前数据
                    tagsOptions: [
                        // {
                        //     id: 1,
                        //     twLabelName: '11111111'
                        //     twTeamworkId:'11111'
                        // }
                    ],
                    // 是否显示输入框
                    inputVisible: false,
                    // 输入框值
                    inputValue: '',
                },

            }
        },
        computed: {

        },
        watch: {
            // 监听场景，时时更新model的labelList
            'model.twSence': {
                handler(val, oldVal) {
                    let labelIdsList = (this.dialogMsg.data && this.dialogMsg.data.labelList) ? this.dialogMsg.data.labelList.map((item) => {
                        return item.id
                    }) : []
                    let list = []
                    if (val) {
                        this.selectTags.tagsOptions.forEach((item) => {
                            if (labelIdsList.includes(item.id)) {
                                list.push(item)
                            }
                        })
                    } else {
                        list = this.selfTags.tagsOptions
                    }

                    this.$set(this.model, 'labelList', list)

                },
                immediate: true,
                deep: true // true 深度监听
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
                switch (item.columnName) {

                    // 启动
                    case 'submit':
                        {
                            this.$refs.aspSmartForm.$children[0].validate((valid) => {
                                if (valid) {
                                    // if (valid && this.userList && this.userList.length > 0) {
                                    switch (this.dialogMsg.status) {
                                        case 'add':
                                            this.add()
                                            break
                                        case 'edit':
                                            this.edit()
                                            break
                                    }


                                } else {
                                    return false
                                }
                            })


                        }
                        break
                    // 取消
                    case 'cancel':
                        this.$emit('closeDialog', '')
                        break
                }
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
                // switch (item.columnName) {
                //     // 下拉框：作战场景
                //     case 'twSence':
                //         this.$utils.smartTableSelectDataFormat(responseBody, responseBody.dataList)
                //         break
                //     // 下拉框：子场景
                //     case 'twSource':
                //         this.$utils.smartTableSelectDataFormat(responseBody, responseBody.dataList)
                //         break
                // }
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
                switch (item.columnName) {
                    // 下拉框：作战场景
                    case 'twSence':
                        this.setTags(model)
                        break
                    // 下拉框：子场景
                    case 'twSource':
                        this.setTags(model)
                        break
                }
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
            // 删除标签
            deleteTags(tagIndex) {
                this.selfTags.tagsOptions.splice(tagIndex, 1)
            },
            // 添加标签
            addTags() {
                let inputValue = this.selfTags.inputValue
                if (inputValue) {
                    this.selfTags.tagsOptions.push({
                        id: '',
                        twTeamworkId: this.dialogMsg.data.id,
                        twLabelName: inputValue
                    })
                }
                this.selfTags.inputVisible = false
                this.selfTags.inputValue = ''
            },
            // 显示添加框
            showInputTags() {
                this.selfTags.inputVisible = true
                this.$nextTick(_ => {
                    this.$refs.saveTagInput.$refs.input.focus()
                })
            },
            // 设置标签值
            setTags(model) {
                let params = {
                    twSence: model.twSence,
                    twSource: model.twSource,
                }
                console.log(params)
                this.$set(this.model, 'labelList', [])
                // 调用标签接口
                this.selectTags.tagsOptions = [
                    // {
                    //     id: 3,
                    //     twLabelName: '3333333333'
                    // }
                ]
            },
            // 新增
            add() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = this.model
                rbUnionTaskServices.addUnionFight(params).then(res => {
                    if (res.success) {
                        this.$message.success(res.msg)
                        this.$emit('closeDialog', { type: 'update' })
                        this.$router.push({
                            path: '/union_task/union_fighting/smart_pages/fightRoom',
                            query: {

                            }
                        })

                    } else {
                        this.$message.error(res.msg)
                    }
                    this.$emit('closeDialog', { type: 'update' })
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 编辑
            edit() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = this.model
                rbUnionTaskServices.editUnionFight(params).then(res => {
                    if (res.success) {
                        this.$message.success(res.msg)
                        this.$emit('closeDialog', { type: 'update' })
                        this.$router.push({
                            path: '/union_task/union_fighting/smart_pages/fightRoom',
                            query: {

                            }
                        })

                    } else {
                        this.$message.error(res.msg)
                    }
                    this.$emit('closeDialog', { type: 'update' })
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 初始化
            init() {
                this.$set(this.model, 'labelList', [])
                this.$set(this.model, 'userList', [])
                let list = [
                    {
                        id: 'wangjie',
                        twTeamworkId: this.dialogMsg.data.id,
                        twUserName: '王杰',
                        twUserAccount: 'wangjie',
                        twUserRole: 1
                    },
                    {
                        id: 'xiongying',
                        twTeamworkId: this.dialogMsg.data.id,
                        twUserName: '熊鹰',
                        twUserAccount: 'xiongying',
                        twUserRole: 2
                    },
                    {
                        id: 'longfeng',
                        twTeamworkId: this.dialogMsg.data.id,
                        twUserName: '龙凤',
                        twUserAccount: 'longfeng',
                        twUserRole: 2
                    }
                ]


                switch (this.dialogMsg.status) {
                    // 新增
                    case 'add':
                        for (let key in this.model) {
                            this.$set(this.model, key, null)
                        }
                        this.$set(this.model, 'userList', list)
                        break
                    // 编辑
                    case 'edit':
                        for (let key in this.model) {
                            this.$set(this.model, key, this.dialogMsg.data[key] || list)
                        }
                        break

                }


            }

        },
        mounted() {
            this.init()
        }
    }
</script>

<style lang="scss" scoped>
</style>

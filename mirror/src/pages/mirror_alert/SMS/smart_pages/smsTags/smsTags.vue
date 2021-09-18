<template>
    <!-- 弹窗：编辑模板分类 -->
    <el-dialog width="750px"
               class="yw-dialog"
               :title="dialogTags.title"
               :visible.sync="dialogTags.dialogVisible"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <div class="yw-dialog-main">
            <asp-smart-form ref="editTagsFrom"
                            :formJson="editTagsJson"
                            :status="editTagsStatus"
                            :beforeHttp="beforeHttp"
                            :afterHttp="afterHttp"
                            v-model="editTagsModel"
                            @on="onbind">
                <section slot="smsTags"
                         class="smsTags-box">
                    <p>请选择模板分类：</p>
                    <el-radio-group v-model="dialogTags.chooseTag"
                                    @change="(chooseTag)=>{onbind({item:{columnName:'btn-change-tag'},model:{chooseTag:chooseTag}})}">
                        <el-radio :label="tagItem"
                                  class="smsTags-item"
                                  v-for="(tagItem,tagIndex) in dialogTags.chooseTagList"
                                  :key="tagIndex">
                            <span v-if="dialogTags.status === 'editContentToTags'">{{tagItem.name}}</span>
                            <span v-else-if="dialogTags.status === 'editTags' && !tagItem.active">{{tagItem.name}}</span>

                            <el-input v-else-if="dialogTags.status === 'editTags' && tagItem.active"
                                      v-model="tagItem.name"
                                      @change="onbind({item:{columnName:'btn-delete-tag'},model:{deleteTag:tagItem,deleteIndex:tagIndex}})"
                                      placeholder=""></el-input>
                        </el-radio>

                        <el-radio :label="dialogTags.addTag"
                                  v-if="dialogTags.status === 'editTags'"
                                  class="smsTags-item">
                            <el-input v-model="dialogTags.addTag.name"
                                      @focus="onbind({item:{columnName:'btn-change-tag'},model:{chooseTag:dialogTags.addTag}})"
                                      @change="onbind({item:{columnName:'btn-add-tag'},model:{addTag:dialogTags.addTag}})"
                                      placeholder=""></el-input>
                        </el-radio>

                    </el-radio-group>
                </section>
            </asp-smart-form>
        </div>
    </el-dialog>
</template>

<script>
    import editTagsJson from '../smart_data/editTags.json'
    import rbAlertSmsServices from 'src/services/alert/rb-alert-sms-service.factory.js'
    export default {
        name: 'SmsTags',
        props: ['dialogTags'],
        components: {

        },
        data() {
            return {
                // 编辑模板分类
                editTagsJson: editTagsJson,
                editTagsStatus: 'add',
                editTagsModel: {},
                // 待删除分类列表
                deleteList: []
            }
        },

        mounted() {
            this.queryTags()
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 删除标签
                    case 'btn-delete-tag':
                        {

                            // 方式一：为空就删除(后端需要删除接口)
                            // if (!data.model.deleteTag.name.trim()) {
                            //     if (data.model.deleteTag.id) {
                            //         this.deleteList.push(data.model.deleteTag)
                            //     }
                            //     this.dialogTags.chooseTagList.splice(data.model.deleteIndex, 1)

                            // }

                            // 方式二：为空设置为一个备用值(暂时后端无删除接口)
                            if (!data.model.deleteTag.name.trim()) {
                                data.model.deleteTag.name = '模板分类xx'
                                this.dialogTags.chooseTagList[data.model.deleteIndex].name = '模板分类xx'
                            }
                        }

                        break
                    // 添加标签
                    case 'btn-add-tag':
                        if (data.model.addTag.name.trim()) {
                            let obj = JSON.parse(JSON.stringify(data.model.addTag))
                            obj.active = false
                            this.dialogTags.chooseTagList.push(obj)
                            this.dialogTags.addTag.name = ''
                        }
                        break
                    // 选择标签
                    case 'btn-change-tag':
                        this.dialogTags.chooseTagList.forEach((item) => {
                            item.active = false
                        })
                        data.model.chooseTag.active = true
                        break
                    // 保存
                    case 'btn-save-tags':
                        if (this.dialogTags.status === 'editContentToTags') {
                            this.saveContentToTags()
                        } else {
                            if (!this.validateTags()) {
                                return false
                            }
                            this.saveTags()
                        }
                        break
                    // 取消
                    case 'btn-cancel-tags':
                        this.$emit('closeDialog', '')
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
                    // 短信发送记录
                    case 'btn-save-tags':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows
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

            },

            // 保存分类
            saveTags() {
                rbAlertSmsServices.editSmsTemplatesTags(this.dialogTags.chooseTagList).then(res => {
                    if (res.state === 'success') {
                        this.$message.success('保存成功')
                    } else {
                        this.$message.error(res)
                    }
                    this.$emit('closeDialog', { type: 'update' })
                })

            },
            // 保存短信内容到分类
            saveContentToTags() {
                let params = {
                    content: this.dialogTags.templates,
                    name: this.dialogTags.chooseTag.name,
                    id: this.dialogTags.chooseTag.id,
                }
                rbAlertSmsServices.editSmsContentToTags(params).then(res => {
                    if (res.state === 'success') {
                        this.$message.success('保存成功')
                    } else {
                        this.$message.error(res)
                    }
                    this.$emit('closeDialog', { type: 'update' })
                })

            },
            // 查询模板分类
            queryTags() {
                let params = {
                    pageNo: 1,
                    pageSize: 100
                }
                rbAlertSmsServices.querySmsTags(params).then(res => {
                    if (res.data && res.data.result) {
                        this.dialogTags.chooseTagList = res.data.result.map((item) => {
                            item.active = false
                            return item
                        })
                    }


                }).finally(() => {
                    // this.loading = false
                })
            },
            // 校验模板分类
            validateTags(name = '') {

                let validate = true

                // 全量判断
                if (!name || !name.trim()) {
                    let temp = this.dialogTags.chooseTagList.map((item) => {
                        return item.name
                    })
                    if (new Set(temp).size !== temp.length) {
                        validate = false

                    } else {
                        validate = true
                    }

                } else {
                    // 当前name判断
                    validate = !this.dialogTags.chooseTagList.some((item) => {
                        if (item.name.trim() === name.trim()) {
                            return true
                        } else {
                            return false
                        }
                    })
                }

                if (!validate) {
                    this.$confirm('模板分类名称不能重复', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                }
                return validate
            }

        }
    }
</script>

<style lang="scss" scoped>
    @import "../sms.scss";
</style>

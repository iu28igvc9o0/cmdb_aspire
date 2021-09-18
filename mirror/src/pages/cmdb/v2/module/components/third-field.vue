<template>
    <div class="components-container">
        <section class="field-main">
            <YwTransfer :datas="datas"
                        @selectTransfer="selectTransfer"
                        :options="options"></YwTransfer>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="stepTo(3,'prev')">上一步</el-button>
            <el-button type="primary"
                       @click="submit">提交</el-button>
            <el-button @click="cancel">取消</el-button>
        </section>

    </div>
</template>

<script>
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import { mapState, mapGetters } from 'vuex'
    import CommonOption from 'src/utils/commonOption.js'

    export default {
        name: 'ThirdFields',
        mixins: [CommonOption],
        props: ['parentForm'],
        components: {
            YwTransfer: () => import('src/components/common/yw-transfer.vue'),
        },
        data() {
            return {
                // 数据
                datas: [],
                // 配置
                options: {
                    titles: ['不显示字段', '显示字段'],
                    fieldMatch: {// 对应字段匹配
                        title: 'name',// 分类标题
                        list: 'codeList',// 分类列表
                        name: 'filedName',// 显示名称
                        display: 'display'// 是否显示
                    }

                }
            }
        },
        computed: {
            ...mapState({
                // 把 `this.moduleOrigin` 映射为 `this.$store.state.model.moduleField.origin`
                moduleOrigin: state => state.model.moduleField.origin,
                moduleNew: state => state.model.moduleField.new,
                moduleAll: state => state.model.moduleField.origin.concat(state.model.moduleField.new),
                moduleObj: state => state.model.moduleObj,
                commonObj: state => state.model.commonObj,
            }),
            ...mapGetters({
                // 把 `this.getName` 映射为 `this.$store.getters.getName`
                // getName: getName
            })
        },

        methods: {
            // 获得数据
            getDatas() {
                let moduleOrigin = this.moduleOrigin.map((item) => {
                    return {
                        name: item.groupName,
                        groupName: '',// 提交时为空，展示有值
                        codeList: item.codeList
                    }
                })

                let moduleNew = this.moduleNew.map((item) => {
                    item.name = item.groupName
                    item.codeList = item.codeList.map((item2) => {
                        if (!item2.codeSetting) {
                            item2.codeSetting = {
                                'moduleId': '',
                                'ownerModuleId': '',
                                'display': 0
                            }
                        }
                        return item2
                    })
                    return item
                })

                this.datas = moduleOrigin.concat(moduleNew)
            },
            // 上一步、下一步
            stepTo(number, direction) {
                this.$emit('stepTo', { number: number, direction: direction })
            },
            // 取消(回到初始页)
            cancel() {
                this.$emit('setShow', false)
            },
            // 模型是否存在校验
            validModule() {
                let params = {
                    catalogId: this.moduleObj.catalogId,
                    moduleCode: this.moduleObj.code
                }

                return rbCmdbModuleService.validModule(params).then((data) => {
                    return data
                })
            },
            // 添加模型
            addModule() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let data = this.moduleObj
                let params = {
                    topCatalogId: this.commonObj.topCatalogId,
                }

                return rbCmdbModuleService.addModule(params, data).then((data) => {
                    if (data.success) {
                        this.$message.success('提交成功！')
                        this.cancel()
                    } else {
                        this.$message.error(data.message)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 修改模型
            editModule() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let data = this.moduleObj
                let params = {
                    topCatalogId: this.commonObj.topCatalogId,
                }
                return rbCmdbModuleService.editModule(params, data).then((data) => {
                    if (data.success) {
                        this.$message.success('提交成功！')
                        this.cancel()
                    } else {
                        this.$message.error(data.message)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 复制模型(用的是编辑接口)
            copyModule() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let data = this.moduleObj
                let params = {
                    topCatalogId: this.commonObj.topCatalogId,
                }
                return rbCmdbModuleService.addModule(params, data).then((data) => {
                    if (data.success) {
                        this.$message.success('提交成功！')
                        this.cancel()
                    } else {
                        this.$message.error(data.message)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 提交
            submit() {
                this.$confirm('确认提交吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    let datas = this.datas.map((item) => {
                        return {
                            groupName: item.groupName,
                            codeList: item.codeList
                        }
                    })
                    this.$store.commit('setModuleObj', { groupList: datas })
                    let valid = await this.validModule()

                    if (this.commonObj.moduleStatus === 'add') {
                        this.$store.commit('setModuleObj', { id: '' })
                        if (valid.success) {
                            this.addModule()
                        } else {
                            this.$message.error(valid.message)
                        }

                    } else if (this.commonObj.moduleStatus === 'edit') {

                        this.editModule()
                    } else if (this.commonObj.moduleStatus === 'copy') {
                        this.$store.commit('setModuleObj', { id: '' })
                        this.copyModule()
                    }
                })
            },
            // 穿梭框选择操作
            selectTransfer(val) {
                this.datas = val
            }
        },
        mounted() {
            this.getDatas()
        }
    }
</script>

<style lang="scss" scoped>
    .field-main {
        display: flex;
        justify-content: center;
        height: calc(100% - 100px);
    }
    .btn-wrap {
        text-align: center;
        margin-top: 30px;
    }
</style>

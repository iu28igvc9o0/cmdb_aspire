<template>
    <!-- 告警屏蔽规则 -->
    <div>
        <section class="yw-dialog-main">
            <el-form class="yw-form is-required"
                     :inline="true"
                     :model="formData"
                     label-width="80px"
                     ref="formData"
                     :rules="rules">
                <el-form-item label="上级菜单">
                    {{dialogMsg.data.parent? dialogMsg.data.parent.name : formData.systemName}}
                </el-form-item>
                <el-form-item label="菜单名称"
                              prop="name">
                    <el-input v-model="formData.name"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="页面类型">
                    <el-select v-model="formData.menuType"
                               clearable
                               :disabled="this.dialogMsg.type === 'detail'">
                        <el-option v-for="(item, index) in menu_type"
                                   :key="index"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="排序"
                              prop="sort">
                    <el-input type="number"
                              v-model="formData.sort"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="链接"
                              v-if="(dialogMsg.data.parent && formData.menuType === 'children') || (dialogMsg.type !== 'insert' && formData.menuType === 'children') || formData.menuType === 'routers'">
                    <el-input v-model="formData.base"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="链接"
                              v-if="formData.menuType === 'vue' || formData.menuType === 'iframe'">
                    <el-input v-model="formData.path"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="链接"
                              v-if="formData.menuType === 'page'">
                    <el-input v-model="formData.url"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="文件名"
                              v-if="formData.menuType === 'vue' || formData.menuType === 'iframe'">
                    <el-input v-model="formData.component"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="图标"
                              v-if="(!dialogMsg.data.parent && dialogMsg.type === 'insert') || formData.parentId === '-1'">
                    <el-input v-model="formData.icon"
                              clearable
                              :disabled="this.dialogMsg.type === 'detail'"></el-input>
                </el-form-item>
                <el-form-item label="是否显示">
                    <el-select v-model="formData.isShow"
                               clearable
                               :disabled="this.dialogMsg.type === 'detail'">
                        <el-option label="显示"
                                   value="1"></el-option>
                        <el-option label="隐藏"
                                   value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="submit()"
                       v-if="this.dialogMsg.type !== 'detail'">确认
            </el-button>
            <el-button @click="cancel()">取消</el-button>
        </section>
    </div>

</template>

<script>
    import rbSysMenuServiceFactory from '../../../../services/sys/rb-sys-menu-service.factory'
    import { menu_type } from 'src/pages/mirror_alert/alert/config/options.js'
    export default {
        props: ['dialogMsg'],
        mixins: [],
        components: {
        },
        data() {
            return {
                // 表单数据
                formData: {
                    name: '',
                    menuType: '',
                    systemName: '',
                    sort: '',
                    isShow: '1',
                },
                menu_type: [],
                rules: {
                    name: [
                        { required: true, message: '必填项' },
                        { min: 1, max: 128, message: '长度在 1 到 128 个字符' },
                        // {pattern: /^[A-Za-z\u4e00-\u9fa5][A-Za-z0-9\u4e00-\u9fa5\\-]*$/, message: '格式错误'}
                    ],
                    sort: [
                        { required: true, message: '必填项' }
                    ]
                },
            }
        },
        watch: {
            dialogMsg: {
                handler() {
                    this.menu_type = menu_type
                    if (this.dialogMsg.data.id) {
                        this.queryDetail(this.dialogMsg.data.id)
                    } else {
                        if (this.dialogMsg.data.parent) {

                            if (this.dialogMsg.data.parent.menuType === 'children') {
                                if (this.dialogMsg.data.parent.parentId === '-1') {
                                    this.formData.menuType = 'children'
                                } else {
                                    this.formData.menuType = 'routers'
                                }
                            } else {
                                this.formData.menuType = 'vue'
                            }
                        } else {
                            this.formData.menuType = 'children'
                        }
                    }
                },
                deep: true,
                immediate: true
            }
        },
        methods: {
            queryDetail(id) {
                rbSysMenuServiceFactory.getDetail(id).then((date) => {
                    this.formData = date
                })
            },
            // 确认
            submit() {
                this.$refs['formData'].validate((valid) => {
                    if (valid) {
                        if (!(this.dialogMsg.data.parent && this.formData.menuType === 'children')
                            && this.formData.menuType !== 'routers'
                            && !(this.dialogMsg.type !== 'insert' && this.formData.menuType === 'children')) {
                            this.formData.base = ''
                        }
                        if (this.formData.menuType !== 'vue' && this.formData.menuType !== 'iframe') {
                            this.formData.path = ''
                        }
                        if (this.formData.menuType !== 'page') {
                            this.formData.url = ''
                        }
                        if (this.formData.menuType !== 'vue' && this.formData.menuType !== 'iframe') {
                            this.formData.component = ''
                        }
                        if (!(!this.dialogMsg.data.parent && this.dialogMsg.type === 'insert') && this.formData.parentId !== '-1') {
                            this.formData.icon = ''
                        }
                        if (this.dialogMsg.type === 'update') {
                            rbSysMenuServiceFactory.update(this.formData).then(() => {
                                this.$emit('closeDialog', 'update')
                            })
                        } else if (this.dialogMsg.type === 'insert') {
                            this.formData.systemId = this.dialogMsg.data.sysId
                            this.formData.parentId = this.dialogMsg.data.parent ? this.dialogMsg.data.parent.id : '-1'
                            rbSysMenuServiceFactory.insert(this.formData).then(() => {
                                this.$emit('closeDialog', 'update')
                            })
                        }

                    } else {
                        return false
                    }
                })
            },

            // 取消
            cancel() {
                this.$emit('closeDialog')
            },

        }
    }

</script>
<style lang="scss" scoped>
</style>

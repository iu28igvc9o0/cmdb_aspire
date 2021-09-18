<template>
    <div style="height: 100%">
        <el-steps :active="active"
                  simple>
            <el-step title="基本属性"
                     icon="el-icon-upload"></el-step>
            <el-step title="字段设置"
                     icon="el-icon-upload"></el-step>
            <el-step title="设置模型显示字段"
                     icon="el-icon-upload"></el-step>
        </el-steps>
        <keep-alive :include="includeKeepAlive">
            <component :is="currentFields"
                       :key="currentFields"
                       @setShow="setShow"
                       :parentForm="form"
                       @stepTo="stepTo"
                       style="height: calc(100vh - 200px)"></component>
        </keep-alive>
    </div>
</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import ModuleReltaion from './module-relation'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory.js'

    export default {
        name: 'AddModule',
        mixins: [updateComponent],
        components: {
            ModuleReltaion,
            baseFields: () => import('./base-field-new'),
            otherFields: () => import('./other-field-new'),
            thirdFields: () => import('./third-field'),
        },
        props: ['activeModuleDatas', 'parentId', 'state', 'moduleId'],// activeModuleDatas编辑状态下选中的值
        data () {
            return {

                // 缓存步骤
                includeKeepAlive: ['baseFields'],
                // 当前步骤
                currentFields: 'baseFields',
                active: 1,
                loading: false,
                testForm: '',
                moduleInfo: {},
                form: {
                    state: this.state,
                    baseFields: {
                        id: this.state === 'edit' || this.state === 'copy' ? this.moduleId : '',
                        parentId: this.state === 'edit' || this.state === 'copy' ? this.parentId : null,
                        name: this.state === 'edit' || this.state === 'copy' ? this.name : null,
                        code: '',
                        iconUrl: '',
                        tags: [],
                        auths: ['edit', 'copy'].indexOf(this.state) > -1 ? this.activeModuleDatas.auths : [],
                        catalogId: ['edit', 'copy'].indexOf(this.state) > -1 ? this.activeModuleDatas.catalogId : null,
                        refModules: ['edit', 'copy'].indexOf(this.state) > -1 ? this.activeModuleDatas.refModules : null,
                    },
                    otherFields: {
                        module: { id: this.state === 'edit' || this.state === 'copy' ? this.moduleId : '' },
                        groupList: []
                    }
                }
            }
        },
        watch: {
        },
        methods: {
            // 上一步、下一步
            stepTo (obj = {}) {

                let fields = ['', 'baseFields', 'otherFields', 'thirdFields']

                if (obj.direction === 'prev') {// 上一步

                    // let index = this.includeKeepAlive.indexOf(fields[obj.number]);
                    // if (obj.number === 2 && index > -1) {
                    //   this.includeKeepAlive.splice(index, 1);
                    // }
                    this.currentFields = fields[obj.number - 1]

                } else if (obj.direction === 'next') {// 下一步

                    // if (obj.number === 2) {
                    //   this.includeKeepAlive.push(fields[obj.number])
                    //   this.includeKeepAlive = [...new Set(this.includeKeepAlive)];
                    // }
                    this.currentFields = fields[obj.number + 1]
                }

            },
            // 取消
            setShow (val) {
                this.$emit('setShow', val)
            },


        },
        mounted: function () {
            if (this.state === 'edit' || this.state === 'copy') {
                rbCmdbModuleServiceFactory.getModuleDetail(this.moduleId).then((data) => {
                    this.moduleInfo = data
                    let module = this.moduleInfo.module
                    if (this.state === 'copy') {
                        this.form.baseFields.tags = []
                        this.form.baseFields.id = module.id
                        this.form.baseFields.parentId = module.parentId
                        this.form.baseFields.auths = module.auths// 权限
                        this.form.baseFields.catalogId = module.catalogId// 分组
                        this.form.baseFields.refModules = module.refModules// 引用模型
                    } else {
                        this.form.baseFields.tags = []
                        this.form.baseFields.id = module.id
                        this.form.baseFields.code = module.code
                        this.form.baseFields.iconUrl = module.iconUrl
                        this.form.baseFields.parentId = module.parentId
                        this.form.baseFields.name = module.name
                        this.form.otherFields.module.id = this.moduleInfo.module.id
                        this.form.otherFields.groupList = this.moduleInfo.groupList
                        this.form.baseFields.auths = module.auths
                        this.form.baseFields.catalogId = module.catalogId
                        this.form.baseFields.refModules = module.refModules
                    }
                })
            } else { // 添加
                this.form.baseFields.auths = []
            }
        }
    }
</script>

<style scoped>
</style>

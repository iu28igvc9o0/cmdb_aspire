<template>
    <div>
        <el-dialog
            class="yw-dialog"
            title="添加关系模型"
            :visible.sync="moduleRlObj.visiable"
        >
            <section>
                <el-form
                    class="components-condition yw-form"
                    :model="transData"
                    label-width="75px"
                >
                    <el-form-item label="当前模型">
                        <el-input
                            v-model="transData.name"
                            style="width: 130px"
                            disabled
                        ></el-input>
                    </el-form-item>
                </el-form>
            </section>

            <!-- 列表 -->
            <section>
                <div class="border-bottom pbottom10">
                    当前关系列表
                </div>
                <div class="table-operate-wrap clearfix mtop10">
                    <el-button
                        class="btn-icons-wrap"
                        type="text"
                        icon="el-icon-plus"
                        @click="create"
                        >新增</el-button
                    >
                </div>
                <div class="yw-el-table-wrap">
                    <el-table
                        class="yw-el-table"
                        :data="result"
                        :element-loading-text="loading_text"
                        border
                        size="mini"
                    >
                        <el-table-column
                            label="关联关系"
                            prop="relationDict.dictNote"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            label="关联类型"
                            prop="relationTypeDict.dictNote"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            label="关联模型"
                            prop="relationModule.name"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            label="关联资源名称"
                            prop="resourceName"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            label="关联sql"
                            prop="relationSql"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            label="字段映射"
                            :formatter="codeRelationFomatter"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                    @click="updateModuleRelation(scope.row)"
                                    title="编辑"
                                    type="text"
                                    icon="el-icon-edit"
                                ></el-button>
                                <el-button
                                    @click="deleteModuleRelation(scope.row)"
                                    type="text"
                                    title="删除"
                                    icon="el-icon-delete"
                                ></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </section>
        </el-dialog>
        <ModuleRelationModify
            v-if="modifyObj.visiable"
            :modifyObj="modifyObj"
            @setCloseDialog="closeDialog"
        ></ModuleRelationModify>
    </div>
</template>

<script>
import rbCmdbModuleServiceFactory from '../../../../../services/cmdb/rb-cmdb-module-service.factory'
export default {
    name: 'ModuleReltaion',
    components: {
        ModuleRelationModify: () => import('./module-relation-modify.vue')
    },
    props: ['moduleRlObj'],
    data() {
        return {
            loading_text: '空数据',
            result: [],
            modifyObj: {
                visiable: false,
                type: '', // 新增或者编辑
                data: {}
            }
        }
    },
    computed: {
        transData() {
            return this.moduleRlObj.data
        }
    },
    methods: {
        // 格式化字段映射
        codeRelationFomatter(row) {
            var rs = ''
            var codeRelations = row.codeRelationList
            for (var i = 0; i < codeRelations.length; i++) {
                var key = codeRelations[i].currCode.filedName
                if (codeRelations[i].relationCode != null) {
                    var value = codeRelations[i].relationCode.filedName
                    rs = rs + key + '->' + value + ','
                } else {
                    rs = rs + key + ','
                }
            }
            return rs.substring(0, rs.length - 1)
        },
        // 跳转到模型关系的新增页面
        create() {
            this.modifyObj.visiable = true
            this.modifyObj.type = 'ADD'
            this.modifyObj.data = this.transData
        },
        // 查询列表
        list() {
            var param = {
                moduleId: this.transData.id
            }
            rbCmdbModuleServiceFactory
                .getModuleRelationList(param)
                .then(res => {
                    this.result = res
                })
        },
        // 编辑关联模型关系
        updateModuleRelation(row) {
            this.modifyObj.visiable = true
            this.modifyObj.type = 'UPDATE'
            this.modifyObj.data = row
        },
        // 删除关联模型关系
        deleteModuleRelation(row) {
            this.$confirm('确定删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    this.deleteMethod(row.id)
                })
                .catch(() => {})
        },
        // deleteMethod
        deleteMethod(id) {
            var param = {
                id: id
            }
            rbCmdbModuleServiceFactory.deleteModuleRelation(param).then(res => {
                if (res.success) {
                    this.$message.success('删除成功！')
                    this.list()
                } else {
                    this.$message.error(res.message)
                }
            })
        },
        // 关闭顶层弹框
        closeDialog() {
            this.modifyObj.visiable = false
            this.list()
        }
    },
    mounted: function() {
        this.list()
    }
}
</script>

<style scoped></style>

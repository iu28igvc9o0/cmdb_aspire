<template>
    <div>
        <el-collapse class="yw-dashboard-section"
                     v-model="activeCollapseNames"
                     v-loading="dynamicLoading.fullLoading">
            <el-collapse-item :name="item.id"
                              v-for="(item, index) in relationList"
                              :key="index">
                <template slot="title">
                    {{item.relationDict.dictNote + '-' + item.resourceName}}
                </template>
                <div style="min-height: 100px">
                    <el-form class="yw-form">
                        <el-form class="yw-form">
                            <div class="yw-el-table-wrap"
                                 v-loading="dynamicLoading[item.id]">
                                <el-table v-if="relationDetailMap[item.id]"
                                          :data="relationDetailMap[item.id].relationCiList"
                                          style="min-height: 100px"
                                          class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          border>
                                    <template v-for="(header, index) in relationDetailMap[item.id].headerList">
                                        <el-table-column v-if="item.relationTypeDict.dictNote == '模型'"
                                                         :key="index"
                                                         show-overflow-tooltip
                                                         :width="header.filedName.length * 20"
                                                         :prop="header.filedCode"
                                                         :label="header.filedName">
                                        </el-table-column>
                                    </template>
                                    <template v-for="(header, index) in relationDetailMap[item.id].headerList">
                                        <el-table-column v-if="item.relationTypeDict.dictNote == '数据表'"
                                                         :key="index"
                                                         show-overflow-tooltip
                                                         :width="header.length * 20"
                                                         :prop="header"
                                                         :label="header">
                                        </el-table-column>
                                    </template>
                                </el-table>
                            </div>
                        </el-form>
                    </el-form>
                </div>

            </el-collapse-item>
        </el-collapse>
    </div>

</template>

<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        components: {},
        data() {
            return {
                queryParams: JSON.parse(this.$route.query.queryParams),
                relationList: [],
                relationDetailMap: {},
                activeCollapseNames: [],
                dynamicLoading: {}
            }
        },
        mounted() {
            this.loading = true
            let _this = this
            _this.$set(_this.dynamicLoading, 'fullLoading', true)
            rbCmdbServiceFactory.listRDetailModuleRelation({ moduleId: this.queryParams.moduleId }).then((res) => {
                _this.$set(_this.dynamicLoading, 'fullLoading', false)
                this.relationList = res
                console.log(this.relationList)
                res.forEach(item => {
                    _this.$set(_this.dynamicLoading, item.id, true)
                    this.activeCollapseNames.push(item.id)
                    let params = {
                        relationId: item.id,
                        instanceId: this.queryParams.instanceId
                    }
                    rbCmdbServiceFactory.getModuleRelationDetail(params).then((res) => {
                        this.relationDetailMap[item.id] = res
                        _this.$set(_this.dynamicLoading, item.id, false)
                    }).finally(() => {
                        _this.$set(_this.dynamicLoading, item.id, false)
                    })
                })
            })
        },
        methods: {}
    }
</script>

<style scoped>
</style>

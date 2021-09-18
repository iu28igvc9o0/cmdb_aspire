<!--  -->
<template>
    <div>
        <section class="yw-dialog-main">
            <YwSearch :searchParams="searchParams"
                      style="width:130px;margin-bottom:10px"
                      @changeSearch="changeSearch"></YwSearch>
            <el-table class="yw-el-table"
                      ref="scriptData"
                      height="290"
                      stripe
                      :data="scriptList"
                      @selection-change="handleSelectionChange"
                      style="width: 100%">
                <el-table-column type="selection"
                                 align="center">
                </el-table-column>
                <el-table-column prop="template_name"
                                 show-overflow-tooltip
                                 label="监控模板名称"
                                 align="center">
                </el-table-column>
                <el-table-column prop="name"
                                 show-overflow-tooltip
                                 label="监控项名称"
                                 align="center">
                </el-table-column>
                <el-table-column prop="key"
                                 show-overflow-tooltip
                                 label="监控项key"
                                 align="center">
                </el-table-column>
                <el-table-column prop="value_type"
                                 label="数据类型"
                                 align="center"
                                 :formatter="formatterValueType">
                </el-table-column>
                <el-table-column prop="units"
                                 label="数据单位"
                                 align="center">
                </el-table-column>
            </el-table>
            <el-pagination @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-size="pageSize"
                           layout="total, prev, pager, next, jumper"
                           :total="total">
            </el-pagination>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="saveTemplate()">保存</el-button>
            <el-button @click="addCancel()">取消</el-button>
        </section>

    </div>
</template>

<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'

    export default {
        data() {
            return {
                scriptList: [],
                multipleSelection: [],
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入巡检名称',
                        bgcolor: ''
                    }
                },
                currentPage: 1,
                pageSize: 10,
                total: 0

            }
        },
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
        },
        created() {
            this.searchScriptList()
        },
        methods: {
            saveTemplate() {
                this.$emit('selectInspectionData', this.multipleSelection)
            },
            addCancel() {
                this.$emit('inspectionShow', false)
            },
            changeSearch(val) {
                this.searchParams.keyword = val
                this.searchScriptList()
            },
            searchScriptList() {
                let obj = {
                    // 'name': '主机巡检',
                    'type': 'SCRIPT',
                    'template_name': this.searchParams.keyword,
                    'page_no': this.currentPage,
                    'page_size': this.pageSize
                }
                rbProjectDataServiceFactory.getItemList(obj).then((res) => {
                    this.total = res.count
                    // res.dataList.forEach((item) => {
                    //     item.content_type_desc = rbMirrorCommonService.common('CONTENT_TYPE', '1', item.content_type)
                    //     if (item.group_relation_list) {
                    //         item.group_name = _.map(item.group_relation_list, 'group_name').join(',')
                    //     }
                    // })
                    this.scriptList = res.result
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
                if (val.length > 1) {
                    this.$refs.scriptData.clearSelection()
                    this.$refs.scriptData.toggleRowSelection(val.pop())
                }
            },
            formatterValueType(row) {
                return rbMirrorCommonService.getZbxType(row.value_type)
            },
            selectScript() {
                this.searchScriptList()
                this.selectScriptShow = true
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.searchScriptList()
            }
        }
    }

</script>
<style lang='scss' scoped>
</style>
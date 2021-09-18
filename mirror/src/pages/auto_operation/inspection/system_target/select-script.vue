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
                                 width="30">
                </el-table-column>
                <el-table-column label="序号"
                                 width="50"
                                 type="index"
                                 align="left">
                </el-table-column>
                <el-table-column label="分组"
                                 width="100"
                                 show-overflow-tooltip
                                 align="left">
                    <template slot-scope="scope">
                        {{ scope.row.group_name }}
                    </template>
                </el-table-column>
                <el-table-column label="脚本名称"
                                 width="180"
                                 show-overflow-tooltip
                                 align="left">
                    <template slot-scope="scope">
                        {{ scope.row.script_name }}
                    </template>
                </el-table-column>
                <el-table-column label="脚本类型"
                                 width="100"
                                 show-overflow-tooltip
                                 align="left">
                    <template slot-scope="scope">
                        {{ scope.row.content_type_desc }}
                    </template>
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
                        placeholder: '请输入脚本名称',
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
                this.$emit('selectScriptData', this.multipleSelection)
            },
            addCancel() {
                this.$emit('scriptShow', false)
            },
            changeSearch(val) {
                this.searchParams.keyword = val
                this.searchScriptList()
            },
            searchScriptList() {
                let obj = {
                    scriptNameLike: this.searchParams.keyword,
                    groupNameLike: '',
                    group_ids: '',
                    label_id: 'cruisecheck',
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                rbProjectDataServiceFactory.getScriptList(obj).then((res) => {
                    this.total = res.totalCount
                    res.dataList.forEach((item) => {
                        item.content_type_desc = rbMirrorCommonService.common('CONTENT_TYPE', '1', item.content_type)
                        if (item.group_relation_list) {
                            item.group_name = _.map(item.group_relation_list, 'group_name').join(',')
                        }
                    })
                    this.scriptList = res.dataList
                })
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
                if (val.length > 1) {
                    this.$refs.scriptData.clearSelection()
                    this.$refs.scriptData.toggleRowSelection(val.pop())
                }
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
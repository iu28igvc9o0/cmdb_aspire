<template>
    <!-- 短信模板 -->
    <div>
        <YwSearch :searchParams="searchSmsTemplates"
                  style="width:180px;margin: 20px 0 20px 8px;width:80%"
                  @changeSearch="(keyword)=>{onbind({item:{columnName:'btn-search-templates'},model:{keyword:keyword}})}"></YwSearch>
        <div class="smsTemplates-tag">
            <span v-for="(tag,tagIndex) in tagDatas"
                  class="smsTemplates-tag-item"
                  :class="{active:tag.id === activeTag.id}"
                  @click="onbind({item:{columnName:'btn-change-tag'},model:{activeTag:tag}})"
                  :key="tagIndex">{{tag.name}}</span>
            <el-button @click="onbind({item:{columnName:'btn-edit-tags'}})">编辑模板分类</el-button>
        </div>
        <div class="smsTemplates-content">
            <div class="smsTemplates-content-item"
                 v-for="(templateItem,contentIndex) in templateDatas"
                 @click="onbind({item:{columnName:'btn-choose-template'},model:{sendTemplates:templateItem}})"
                 :key="contentIndex">
                <section class="operator-wrap">
                    <!-- <i class="icon-btn el-icon-plus"
                       title="录入"
                       @click.stop="onbind({item:{columnName:'btn-choose-template'},model:{sendTemplates:templateItem}})"></i> -->
                    <i class="icon-btn el-icon-edit"
                       title="编辑"
                       @click.stop="onbind({item:{columnName:'btn-edit-template'},model:{sendTemplates:templateItem}})"></i>
                    <i class="icon-btn el-icon-delete"
                       title="删除"
                       @click.stop="onbind({item:{columnName:'btn-delete-template'},model:{sendTemplates:templateItem}})"></i>
                </section>
                <el-tooltip class="item"
                            effect="dark"
                            popper-class="tooltip-width-400"
                            :content="templateItem.content"
                            placement="bottom">
                    <section class="main-wrap">{{templateItem.content && templateItem.content.length > 300 ? templateItem.content.substring(0, 300) + '...' : templateItem.content}}</section>
                </el-tooltip>

            </div>
        </div>
        <YwPagination @handleSizeChange="handleSizeChange"
                      class="pagination"
                      @handleCurrentChange="handleCurrentChange"
                      :current-page="currentPage"
                      :page-sizes="pageSizes"
                      :page-size="pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="total"></YwPagination>
        <!-- dialog -->
        <DialogEdit v-if="dialogMsg.dialogVisible"
                    @closeDialog="closeDialog"
                    :dialogMsg="dialogMsg"></DialogEdit>
    </div>
</template>

<script>
    import rbAlertSmsServices from 'src/services/alert/rb-alert-sms-service.factory.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        name: 'SmsTags',
        mixins: [YwPaginationOption, CommonOption],
        props: ['dialogTags'],
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
            DialogEdit: () => import('./dialog-edit.vue'),
        },
        data() {
            return {
                // 模板分类
                tagDatas: [],
                // 当前选中的tag
                activeTag: {},
                // 模板内容
                templateDatas: [],
                // 查询条件
                queryParams: {},
                // 短信模板
                searchSmsTemplates: {
                    keyword: '',
                    desc: {
                        placeholder: '关键字',
                        // bgcolor: '',
                        // borderRadius: '20px',
                        // borderColor: '',
                        // color: '#88AAB4',
                        // iconColor: '#BCC0C6',
                        width: '70%'
                    },
                    event: {// 事件
                        input: true
                    }
                },
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    data: ''
                },
            }
        },

        mounted() {
            this.queryTags()
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 选择模板分类
                    case 'btn-change-tag':
                        this.changeTag(data.model.activeTag)
                        break
                    // 编辑模板分类
                    case 'btn-edit-tags':
                        this.$emit('editTags')
                        break
                    // 关键字搜索
                    case 'btn-search-templates':
                        this.searchSmsTemplates.keyword = data.model.keyword
                        this.query()
                        break
                    // 选择模板内容
                    case 'btn-choose-template':
                        this.$emit('chooseTemplates', data.model.sendTemplates)
                        break
                    // 编辑模板内容
                    case 'btn-edit-template':
                        this.dialogMsg.data = data.model.sendTemplates
                        this.dialogMsg.dialogVisible = true

                        break
                    // 删除模板内容
                    case 'btn-delete-template':
                        {
                            this.$confirm('确定删除吗?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                this.showFullScreenLoading({ text: '正在删除数据, 请稍等...' })
                                let params = {
                                    id: data.model.sendTemplates.id
                                }
                                rbAlertSmsServices.deleteSmsTemplates(params).then(res => {
                                    if (res.state === 'success') {
                                        this.query()
                                        this.$message.success('删除成功')
                                    } else {
                                        this.$message.error(res)
                                    }

                                }).finally(() => {
                                    this.closeFullScreenLoading()
                                })
                            })

                        }

                        break
                }
            },
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNo'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'templateId': this.activeTag.id,
                        'content': this.searchSmsTemplates.keyword,
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,
                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                // this.loading = true

                this.setParams(activePagination)

                rbAlertSmsServices.querySmsTemplatesByTag(this.queryParams).then(res => {
                    this.templateDatas = res.data.result
                    this.total = res.data.count

                }).finally(() => {
                    // this.loading = false
                })

            },
            // 查询模板分类
            queryTags() {
                let params = {
                    pageNo: 1,
                    pageSize: 100
                }
                rbAlertSmsServices.querySmsTags(params).then(res => {
                    this.tagDatas = res.data && res.data.result || []
                    this.changeTag(this.tagDatas[0] || {})

                }).finally(() => {
                    // this.loading = false
                })
            },
            changeTag(data = {}) {
                this.activeTag = data
                this.query()
            },
            closeDialog(data) {
                this.dialogMsg.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作
                    this.query()
                }

            },

        }
    }
</script>

<style lang="scss" scoped>
    @import "../sms.scss";
    .pagination {
        background: white;
        position: fixed;
        bottom: 0;
    }
</style>

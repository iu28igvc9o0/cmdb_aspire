<template>
    <!-- 服务台-深圳：公告 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>公告</div>
                </div>
            </div>
            <!-- <div class="chart-filter">
                <el-button type="text"
                           class="mleft20"
                           @click="onbind({item:{columnName:'btn-more'}})">更多</el-button>
            </div> -->
        </section>
        <div class="chart-section">
            <div class="chart-section-main table-box-wrap"
                 v-loading="loading">
                <div class="yw-el-table-wrap"
                     style="height: calc(100% - 32px)">
                    <el-table class="yw-el-table"
                              :data="result"
                              :show-header="false"
                              height="100%"
                              stripe
                              border>
                        <el-table-column label="公告内容">
                            <template slot-scope="scope">
                                <el-tooltip class="item"
                                            effect="dark"
                                            :content="scope.row.notice"
                                            placement="top">
                                    <span class="text-ellipse text-link dark-blue"
                                          style="max-width:calc(100% - 10px);width:auto;"
                                          @click="onbind({item:{columnName:'btn-goto-details'},dataList:scope.row})">
                                        {{ scope.row.notice }}
                                    </span>
                                </el-tooltip>
                                <span class="flag"
                                      v-if="scope.row.status === 'todo'"></span>
                            </template>
                        </el-table-column>
                        <el-table-column label="时间"
                                         width="140"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.createTime }}
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </div>
        </div>
    </div>
</template>

<script>
    import commonUtils from '../mixin/commonUtils'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [commonUtils, YwPaginationOption, CommonOption],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),
        },
        data() {
            return {
                // 查询条件
                queryParams: {

                },
                // 查询结果
                result: []
            }
        },

        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 更多
                    case 'btn-more':
                        this.linkUrl('/resource/flow', { routerHash: 'flow/todoList', currentTitle: '待办事项', curTreeNode: 'OfficialFilesDown' })
                        break
                    // 详情
                    case 'btn-goto-details':
                        {

                            if (data.dataList.link && data.dataList.link.includes('#')) {
                                let routerHashList = data.dataList.link.split('#/')
                                this.linkUrl('/resource/flow', { routerHash: routerHashList[1], currentTitle: data.dataList.status === 'todo' ? `待办事项-${data.dataList.instId}` : `已办事项-${data.dataList.instId}` })
                            } else {
                                window.open(data.dataList.link, '_blank')
                            }


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
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,
                    }
                }
                return this.queryParams
            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {

                this.showLoading()

                this.setParams(activePagination)
                rbBpmHomeServices.getAnnouncement(this.queryParams).then(res => {
                    this.total = res.total || 0
                    this.result = res.data
                }).finally(() => {
                    this.closeLoading()
                })
            },
            init() {
                this.pageSize = 7
                this.pageSizes = [7]
                this.query()
            }

        },
        created() {
            this.init()

        },


    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
    .table-box-wrap {
        height: 100%;
        .flag {
            display: inline-block;
            width: 5px;
            height: 5px;
            border-radius: 50%;
            background: #f5222d;
            vertical-align: text-top;
            margin-left: 2px;
        }
    }
</style>


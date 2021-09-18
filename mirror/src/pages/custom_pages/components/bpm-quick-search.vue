<template>
    <!-- 服务台： 快速搜索 -->
    <div class="content-chart"
         v-loading="loading">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>快速搜索</div>
                </div>
            </div>
        </section>
        <div class="chart-section p1020 displaybox flex-column quick-search-box">
            <div class="chart-filter">
                <el-row :gutter="10">
                    <el-col :span="8">
                        <el-select v-model="curSearchType">
                            <el-option label="搜工单"
                                       value="order"></el-option>
                            <el-option label="搜知识"
                                       value="knowledge"></el-option>
                            <el-option label="搜人"
                                       value="user"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="16">
                        <el-input v-model="searchWord"
                                  clearable
                                  :placeholder=" moduleData[curSearchType] && moduleData[curSearchType].placeholders ? moduleData[curSearchType].placeholders : '请输入' + placeholders[curSearchType] + '搜索'">
                            <i slot="suffix"
                               class="el-input__icon el-icon-search"
                               @click="search(currentPage=1)"></i>
                        </el-input>
                    </el-col>
                </el-row>
                <div class="ptb10 search-text"
                     v-if="curSearchType==='knowledge'">
                    <span v-for="item in searchTextList"
                          :key="item"
                          @click="changeText(item)">{{item.name}}</span>
                </div>
                <div class="border-bottom ptb10">{{total}}个搜索结果</div>
            </div>
            <div class="boxflex01 wp100 scroll">
                <div class="t-center"
                     v-if="!dataList.length">
                    <img src="../../../assets/img/custom_modules/search-no-contents.png">
                    <div v-show="hasSearched">没有搜索到相关内容</div>
                    <div v-show="!hasSearched">请输入<span class="bold">{{placeholders[curSearchType]}}</span>搜索</div>
                </div>
                <div v-else>
                    <template v-for="(item, index) in dataList">
                        <search-item :key="index"
                                     :searchWord="searchWord"
                                     :itemData="item"
                                     :itemType="curSearchType"
                                     :isLastOne="index+1 === 10"></search-item>
                    </template>
                </div>
            </div>
            <div class="yw-page-wrap wp100 border-top">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               :pager-count="5"
                               small
                               layout="prev, pager, next, jumper"></el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        props: {
            orderType: {
                type: String,
                default: 'all'
            },
            // 其他模块传参
            moduleData: {
                type: Object,
                default: () => {
                    return {}
                }
            }
        },
        components: {
            searchItem: () => import('./sub_components/bpm-search-item.vue')
        },
        data() {
            return {
                curSearchType: 'order',
                placeholders: {
                    order: '工单号',
                    knowledge: '知识关键词',
                    user: '用户名称、真实姓名',
                },
                searchWord: '',
                hasSearched: false,

                dataList: [], // 表格数据

                searchTextList: [{ name: '网络' }, { name: '4A' }, { name: 'VPN' }, { name: '5G' }]
            }
        },
        watch: {
            curSearchType: {
                handler(val) {
                    // 切换搜索类型，重置数据
                    this.searchWord = ''
                    this.total = 0
                    this.currentPage = 1
                    this.hasSearched = false
                    this.dataList = []

                    if (val === 'knowledge') {
                        // 默认搜索 网络
                        this.searchWord = '网络'
                        this.search()
                    } else if (val === 'order') {
                        this.search()
                    }
                },
                immediate: true
            }
        },
        computed: {
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        methods: {
            selectable(row) {
                return row.status === 'running'
            },
            changeText(item) {
                this.searchWord = item.name
                this.search()
            },
            // 获得其他服务和参数
            getOrderService(req) {
                let obj = {
                    req: req,// 查询参数
                    requestType: 'getOrders'// 服务接口
                }
                if (this.moduleData.order && this.moduleData.order.getListService) {
                    obj.requestType = this.moduleData.order.getListService
                }
                if (this.moduleData.order && this.moduleData.order.queryParamsKeys) {
                    for (let key in this.moduleData.order.queryParamsKeys) {
                        obj.req[this.moduleData.order.queryParamsKeys[key]] = req[key]
                    }
                }


                return obj
            },
            // 获取列表数据 
            search() {
                let req = {
                    pageNo: this.currentPage,   // 获取总数的时候不传
                    pageSize: this.pageSize,    // 获取总数的时候不传
                }
                console.log(req)
                let requestType
                if (this.curSearchType === 'order') {
                    req.subject = this.searchWord

                    req = this.getOrderService(req).req
                    requestType = this.getOrderService(req).requestType

                } else if (this.curSearchType === 'user') {
                    req.search = this.searchWord
                    requestType = 'getUsers'

                    // 搜知识改版本暂无接口
                } else {
                    if (!this.searchWord) {
                        this.$message.error('请输入关键字')
                        return
                    }
                    req.search = this.searchWord
                    requestType = 'knowledge'

                    // this.hasSearched = true
                    // return
                }
                this.loading = true
                rbBpmHomeServices[requestType](req)
                    .then(res => {
                        this.hasSearched = true
                        this.loading = false
                        // 设置待办/处理中工单的数量
                        this.total = res.total || res.count || res.data.total
                        this.dataList = res.rows || res.result || []
                        if (res.data.rows) {
                            this.dataList = res.data.rows.map((item) => {
                                item.id = item.instId
                                return item
                            })
                        }
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
        }

    }
</script>

<style lang="scss" scoped>
    .search-text {
        display: flex;
        span {
            display: block;
            width: 54px;
            height: 26px;
            background-color: #e9ecef;
            margin-right: 18px;
            border-radius: 13px;
            text-align: center;
            line-height: 26px;
        }
    }
</style>


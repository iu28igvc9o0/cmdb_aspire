<template>
    <div class="choose-wrap">
        <section>
            <el-select v-model="hostOsType"
                       style="width:140px;"
                       placeholder="请选择操作系统类型"
                       clearable
                       @change="query(false)">
                <el-option :label="p.name"
                           :value="p.value"
                           v-for="(p, index) in classArray"
                           :key="index"></el-option>
            </el-select>
            <section class="yw-search-wrap">
                <YwSearch :searchParams="searchParams"
                          style="width:140px;"
                          @changeSearch="changeSearch"></YwSearch>
            </section>
        </section>
        <div class="clearfix">

            <!-- 结果 -->
            <section class="search-right fl">

                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              height="290"
                              :data="result"
                              @selection-change="changeCheckbox"
                              style="width: 100%">
                        <el-table-column type="selection"
                                         width="30">
                        </el-table-column>
                        <el-table-column label="proxy标识"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.proxy_id }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备IP"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.agent_ip }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作系统类型"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.host_os_type }}
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              layout="total, prev, pager, next, jumper"
                              :total="total"></YwPagination>

            </section>
            <!-- 结果 -->

            <section class="search-arrow fl"
                     @click="add()">
                <i class="el-icon-right"></i>
            </section>

            <!-- 确认结果 -->
            <section class="search-right  search-result fl">
                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              height="290"
                              :data="submitResult"
                              style="width: 100%">
                        <el-table-column label="选择项"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row}}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作"
                                         align="left">
                            <template slot-scope="scope">
                                <a class="yw-table-link"
                                   @click="del(scope.row,scope.$index)">删除</a>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

            </section>
        </div>
    </div>
</template>

<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import { classArray } from '../../config/options.js'
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        name: 'AgentDeviceChoose',
        mixins: [QueryObject],
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        props: ['deviceList'],
        watch: {
            deviceList: {
                handler(newValue) {
                    // this.deviceList = this.dialogData.deviceList ? JSON.parse(this.dialogData.deviceList) : []
                    this.submitResult = newValue
                    this.$forceUpdate()
                },
                deep: true
            }
        },
        data() {
            return {
                // 搜索框参数
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: 'Agent设备IP',
                        bgcolor: ''
                    }
                },
                queryParams: {},
                hostOsType: '',
                deviceType: '',
                classArray: classArray,
                typeArray: [],
                result: [],
                addResult: [],
                submitResult: this.deviceList
            }
        },
        mounted() {
            this.query()
        },
        methods: {
            // 设置参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                        'host_os_type': this.hostOsType,
                        'agent_ip': this.searchParams.keyword
                    }
                }

            },
            // 查询数据
            query(activePagination = false) {
                this.setParams(activePagination)
                rbProjectDataServiceFactory.getAgentList({}).then((res) => {
                    this.result = res.dataList
                    this.total = res.totalCount
                }).catch(() => {
                    this.$message.error('查询失败')
                })
            },

            // 删除
            del(row, index) {
                this.submitResult.splice(index, 1)
                this.$emit('changeDeviceList', this.submitResult)
            },

            // 确认
            submit() {
                let obj = {
                    mulFlag: true,
                    result: this.submitResult,
                    type: this.currentType.id
                }
                this.$emit('setData', obj)
            },

            // 取消
            cancel() {
                this.$emit('closeDialog')
            },

            // checkbox
            changeCheckbox(val) {
                this.addResult = val
            },

            // 添加
            add() {
                if (this.addResult.length === 0) {
                    return
                }
                this.addResult.forEach(item => {
                    if (!this.submitResult.includes(item.proxy_id + ':' + item.agent_ip)) {
                        this.submitResult.push(item.proxy_id + ':' + item.agent_ip)
                    }
                })
                this.$emit('changeDeviceList', this.submitResult)
            },

            // 搜索框查询
            changeSearch(val) {
                this.searchParams.keyword = val
                this.query()
            },

        }
    }
</script>

<style scoped lang="scss">
    .choose-wrap {
        display: inline-block;
    }
    .search-right {
        width: 364px;
    }
    .search-arrow {
        width: 30px;
        height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        margin: 170px 15px 0 15px;
        text-align: center;
        line-height: 30px;
        cursor: pointer;
        .el-icon-right {
            font-size: 18px;
        }
        &:hover {
            border: 1px solid #46bafe;
            .el-icon-right {
                color: #46bafe;
            }
        }
    }

    .yw-el-table-wrap {
        border: 1px solid rgba(220, 223, 230, 1);
        border-radius: 8px;
        margin-top: 15px;
        height: 300px;
        padding: 2px;
    }

    .yw-search-wrap {
        display: inline-block;
    }
</style>

<template>
    <div class="clearfix">
        <!-- 可选择 -->
        <section class="search-right fl"
                 style="width: 50%">
            <div class="search-right-top">
                <div class="name">
                    <h4>请选择设备</h4>
                </div>
                <div class="selectConfig">
                    <YwSearch :searchParams="searchParams"
                              style="width:160px;"
                              @changeSearch="changeSearch"></YwSearch>
                    <el-select v-model="hostOsType"
                               style="width:140px;"
                               placeholder="操作系统"
                               clearable
                               @change="query(false)">
                        <el-option :label="p.name"
                                   :value="p.value"
                                   v-for="(p, index) in classArray"
                                   :key="index"></el-option>
                    </el-select>
                </div>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          height="290"
                          :data="result"
                          @selection-change="changeCheckbox"
                          style="width: 100%">
                    <el-table-column type="selection"
                                     width="30">
                    </el-table-column>
                    <el-table-column label="序号"
                                     type="index"
                                     align="left">
                    </el-table-column>
                    <el-table-column label="所在区域"
                                     width="80"
                                     align="left">
                        <template slot-scope="scope">
                            {{ scope.row.area}}
                        </template>
                    </el-table-column>
                    <el-table-column label="设备IP"
                                     width="120"
                                     align="left">
                        <template slot-scope="scope">
                            {{ scope.row.agent_ip }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作系统"
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
        <!-- 选择按钮 -->

        <section class="search-arrow fl"
                 @click="add()">
            <i class="el-icon-right"></i>
        </section>

        <!-- 选择结果 -->
        <section class="search-right  search-result fl"
                 style="width: 40%">
            <div class="search-right-top">
                <div class="name">
                    <h4>已选</h4>
                    <a class="yw-table-link"
                       @click="empty()">清空已选</a>
                </div>
                <div class="selectConfig">
                    <YwSearch :searchParams="searchParams2"
                              style="width:160px;"
                              @changeSearch="changeSearch2"></YwSearch>
                    <!--<YwSearch :searchParams="searchParams" style="width:160px;"-->
                    <!--@changeSearch="changeSearch"></YwSearch>-->
                </div>

            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          height="290"
                          :data="submitResult"
                          style="width: 100%">
                    <el-table-column label="序号"
                                     type="index"
                                     align="left">
                    </el-table-column>
                    <el-table-column label="设备IP"
                                     width="180"
                                     align="left">
                        <template slot-scope="scope">
                            {{scope.row.substring(scope.row.indexOf(':')+1)}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     align="left"
                                     width="100">
                        <template slot-scope="scope">
                            <a class="yw-table-link"
                               @click="del(scope.row,scope.$index)"><i class="el-icon-delete"></i></a>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

        </section>
        <!-- 确认结果 -->
    </div>
</template>

<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    // import {classArray} from '../../config/options.js'
    import QueryObject from 'src/utils/queryObject.js'
    import _ from 'lodash'
    const classArray = [
        {
            label: '全部',
            value: ''
        },
        {
            label: 'linux',
            value: 'linux'
        }, {
            label: 'windows',
            value: 'windows'
        }
    ]
    export default {
        name: 'AgentDeviceChoose',
        mixins: [QueryObject],
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        props: ['currentTemplateObj'],
        watch: {
            currentTemplateObj: {
                handler(newValue) {
                    // this.deviceList = this.dialogData.deviceList ? JSON.parse(this.dialogData.deviceList) : []
                    this.submitResult = newValue.object_ids ? newValue.object_ids.split(',') : []
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
                        placeholder: '请输入设备IP查询',
                        bgcolor: ''
                    }
                },
                searchParams2: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入设备IP查询',
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
                tempList: [],
                submitResult: this.currentTemplateObj.object_ids ? this.currentTemplateObj.object_ids.split(',') : []
            }
        },
        mounted() {
            this.query()
        },
        methods: {
            changeSearch2(val) {
                this.searchParams2.keyword = val
                this.searchFilter()
            },
            searchFilter() {
                if (this.tempList.length == 0) {
                    this.tempList = JSON.parse(JSON.stringify(this.submitResult))
                }
                if (this.searchParams2.keyword != '') {
                    this.submitResult = _.filter(this.submitResult, (item) => {
                        return item.includes(this.searchParams2.keyword)
                    })
                } else {
                    this.submitResult = JSON.parse(JSON.stringify(this.tempList))
                }
            },
            // 设置参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                        'agent_ip': this.searchParams.keyword,
                        'host_os_type': this.hostOsType
                    }
                }

            },
            // 查询数据
            query(activePagination = false) {
                this.setParams(activePagination)
                rbProjectDataServiceFactory.getAgentList(this.queryParams).then((res) => {
                    this.result = res.dataList
                    this.total = res.totalCount
                }).catch(() => {
                    this.$message.error('查询失败')
                })
            },

            // 删除
            del(row, index) {
                this.submitResult.splice(index, 1)
                this.currentTemplateObj.object_ids = this.submitResult.join(',')
                // this.$emit('changeDeviceList', this.submitResult)
            },

            // //确认
            // submit() {
            //     let obj = {
            //         mulFlag: true,
            //         result: this.submitResult,
            //         type: this.currentType.id
            //     }
            //     this.$emit('setData', obj);
            // },
            //
            // //取消
            // cancel() {
            //     this.$emit('closeDialog');
            // },

            // checkbox
            changeCheckbox(val) {
                this.addResult = val
            },
            empty() {
                this.submitResult = []
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
                this.currentTemplateObj.object_ids = this.submitResult.join(',')
                // this.$emit('changeDeviceList', this.submitResult)
            },
            // 搜索框查询
            changeSearch(val) {
                this.searchParams.keyword = val
                this.query()
            },

        }
    }
</script>

<style lang="scss" scoped>
    .search-right {
        width: 360px;
        border: 1px solid #dcdfe6;
        &.search-result {
            // margin-top: 30px;
        }
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

    .search-right-top {
        padding-top: 10px;
        .name {
            float: left;
            padding-right: 10px;
            padding-left: 10px;
            h4 {
                color: #606266cc;
                float: left;
                padding-right: 10px;
            }
        }
        .selectConfig {
            text-align: right;
            padding-right: 10px;
        }
    }
    .clearfix {
        padding-top: 10px;
    }
    /deep/ .el-input__inner {
        border: 1px solid #dcdfe6 !important;
        background-color: #fff !important;
        border-radius: 6px !important;
    }
    /deep/ .el-icon-search {
        top: 6px !important;
    }

    /deep/ .search-box-wrap {
        /*border: 1px solid #DCDFE6 !important;*/
        line-height: 26px;
    }
    .yw-el-table-wrap {
        /*border: 1px solid rgba(220, 223, 230, 1);*/
        /*border-radius: 8px;*/
        margin-top: 15px;
        height: 300px;
        padding: 2px;
    }
    .yw-search-wrap {
        display: inline-block;
    }
</style>
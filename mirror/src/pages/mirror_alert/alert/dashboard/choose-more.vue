<template>
    <div class="choose-wrap">
        <section class="clearfix">
            <YwSelectResource :pool="pool"
                              placeholder="请选择资源池"
                              style="width:140px;float: left;margin-right: 5px;"
                              @changePool="changePool"
                              @cleanPool="cleanPool"></YwSelectResource>
            <!-- 搜索框 -->
            <section class="yw-search-wrap"
                     style="width:160px;float: left">
                <YwSearch :searchParams="searchParams"
                          style="width:140px;margin-right: 5px;"
                          @changeSearch="changeSearch"></YwSearch>
            </section>

            <el-select v-model="deviceClass"
                       style="width:130px;float: left;margin-right: 5px;"
                       placeholder="请选择设备分类"
                       clearable
                       @change="classChange">
                <el-option :label="p.name"
                           :value="p.value"
                           v-for="(p, index) in classArray"
                           :key="index"></el-option>
            </el-select>
            <el-select v-model="deviceType"
                       style="width:130px;float: left;margin-right: 5px;"
                       placeholder="请选择设备类型"
                       clearable
                       filterable
                       @change="query(false)">
                <el-option :label="p.name"
                           :value="p.value"
                           v-for="(p, index) in typeArray"
                           :key="index"></el-option>
            </el-select>
            <!-- 懒加载子节点：children设置为null，并且需要设置:load-options="loadOptions"方法，否则控制台会报错；非懒加载，没有children属性 -->
            <!-- <treeselect class="yw-treeselect"
                        style="width:200px;float: left;font-color: #606266"
                        v-model="bizSysDepChecked"
                        :options="bizDepTreeSelOpts"
                        :multiple="true"
                        :limit="1"
                        @input="query(false)"
                        placeholder="请选择业务系统"></treeselect> -->
            <YwSelectTree @changeTree="changeTree"></YwSelectTree>
            <!-- 搜索框 -->

        </section>
        <div class="clearfix">

            <!-- 结果 -->
            <section class="search-right fl">

                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              height="290"
                              :data="result"
                              v-loading="loading"
                              @selection-change="changeCheckbox"
                              style="width: 100%">
                        <el-table-column type="selection"
                                         width="45">
                        </el-table-column>
                        <el-table-column label="资源池"
                                         width="110"
                                         show-overflow-tooltip
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.idcType }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备分类"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.device_class }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备类型"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.device_type }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备IP"
                                         width="130"
                                         show-overflow-tooltip
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.ip }}
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
                        <el-table-column label="资源池"
                                         width="110"
                                         show-overflow-tooltip
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.idcType }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备分类"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.deviceClass }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备类型"
                                         width="80"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.deviceType }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备IP"
                                         width="130"
                                         show-overflow-tooltip
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.ip }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作"
                                         align="left"
                                         width="50">
                            <template slot-scope="scope">
                                <a class="yw-table-link"
                                   @click="del(scope.row,scope.$index)">删除</a>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

            </section>
            <!-- 确认结果 -->
        </div>

        <!-- 按钮 -->
        <section class="btn-wrap">
            <el-button type="primary"
                       size="small"
                       @click="submit()">确定</el-button>
            <el-button size="small"
                       @click="cancel()">取消</el-button>
        </section>
        <!-- 按钮 -->
    </div>

</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    export default {
        props: ['currentType', 'dialogMsg'],
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            YwSelectResource: () => import('src/components/common/yw-select-resource.vue'),
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwSelectTree: () => import('src/components/common/yw-selectTree-bizSystem.vue'),
        },

        data() {
            return {
                bizSysTree: null,
                bizDepTreeSelOpts: [],
                bizSysDepChecked: null,
                // 搜索框参数
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '设备IP',
                        bgcolor: ''
                    }
                },

                // 查询参数
                queryParams: {

                },
                deviceClass: '',
                deviceType: '',
                classArray: [],
                typeArray: [],
                // 结果
                result: [],
                addResult: [],// 临时数据
                submitResult: []


            }
        },
        mounted() {
            this.query()
        },
        methods: {
            changeTree(val = '') {
                this.bizSysDepChecked = val
                this.query(false)
            },
            // 设置参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryParams['pageNo'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {
                    this.queryParams = {
                        'ip': this.searchParams.keyword,
                        'idcType': this.pool,
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,
                        'deviceType': this.deviceType,
                        'deviceClass': this.deviceClass,
                        // 'bizSystem': (this.bizSysDepChecked && this.bizSysDepChecked.length > 0) ? this.getBizSysByChecked(this.bizSysDepChecked) : '',
                        'bizSystem': this.bizSysDepChecked
                    }
                }

            },
            getBizSysByChecked(sysDepChecked) {
                if (sysDepChecked && sysDepChecked.length == 1 && !sysDepChecked[0]) {
                    return ''
                }
                let target = []
                let list = this.bizSysTree[0].children
                let addEle = function (eles) {
                    if (!eles.children || eles.children.length == 0) {
                        target.push(eles.id)
                    } else {
                        _.forEach(eles.children, ele => {
                            addEle(ele)
                        })
                    }
                }
                let findEle = function (_array, _origin) {
                    _.forEach(_array, child => {
                        if (child.id == _origin) {
                            addEle(child)
                        } else if (child.children && child.children.length > 0) {
                            findEle(child.children, _origin)
                        }
                    })
                }
                _(sysDepChecked).forEach(ele => {
                    findEle(list, ele)
                })
                return _(target).uniq().join(',')
            },
            initial() {
                // 已选中ip
                let ipArr = []
                if (this.dialogMsg.data && this.dialogMsg.data.rs) {
                    ipArr = this.dialogMsg.data.rs.split(',')
                }
                if (ipArr.length) {
                    this.result.find(item => {
                        ipArr.forEach(ip => {
                            if (item.ip == ip) {
                                this.submitResult.push(item)
                            }
                        })
                    })
                }

                let obj = {
                    'type': 'device_class',
                    'pid': ''
                }
                rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.classArray = res
                    }
                })
            },

            // 查询数据
            query(activePagination = false) {
                this.setParams(activePagination)
                this.queryParams['currentPage'] = this.currentPage
                this.queryParams['token'] = '5245ed1b-6345-11e'
                this.loading = true

                rbCmdbServiceFactory.getCmdbPageList(this.queryParams).then((res) => {
                    this.loading = false
                    this.result = res.result
                    this.total = res.count
                    this.submitResult = []
                    this.initial()
                })
            },

            classChange(val) {
                this.typeArray = []
                this.deviceType = ''

                this.query(false)
                let obj1 = {
                    'type': 'device_type',
                    'pValue': val
                }
                rbProjectDataServiceFactory.getConfigDictByType(obj1).then((res) => {
                    if (res) {
                        this.typeArray = res
                    }
                })

            },
            // 删除
            del(row, index) {
                this.submitResult.splice(index, 1)
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
                if ((this.submitResult.length + this.addResult.length) > 100) {
                    this.$message.warning('所选数据不能超过100条！')
                    return
                }
                if (this.addResult.length == 0) {
                    return
                }
                let temp = this.submitResult.concat(this.addResult)
                let flag = this.checkData(temp)
                if (!flag) {
                    return
                }
                // 去重
                this.submitResult = this.$utils.reduceArrObj(this.submitResult.concat(this.addResult))
            },
            checkData(temp) {
                let deviceClass = temp[0].deviceClass
                let deviceType = temp[0].deviceType
                for (let i = 1; i < temp.length; i++) {
                    if (deviceClass != temp[i].deviceClass || deviceType != temp[i].deviceType) {
                        this.$message.warning('请选择设备分类和设备类型一致的ip设备')
                        return false
                    }
                }
                return true
            },
            // 筛选资源池
            changePool(val) {
                this.pool = val
                this.query()
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
    .choose-wrap {
        display: inline-block;
    }
    /deep/.yw-treeselect.vue-treeselect
        .vue-treeselect__control
        .vue-treeselect__placeholder {
        color: #c0c4cc;
    }
    .search-right {
        width: 450px;
        //   &.search-result {
        // margin-top: 30px;
        //   }
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

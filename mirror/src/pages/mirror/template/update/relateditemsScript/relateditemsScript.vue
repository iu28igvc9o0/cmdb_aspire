<template>
    <div class="component-container">
        <header>
            <div class="he-wrapper">
                <div class="item-tit"
                     style="margin-left:-3%">脚本名称：</div>
                <el-input v-model="scriptNameLike"
                          placeholder="请输入内容"
                          class="item-input input-tem-name1"></el-input>

                <div class="item-tit"
                     style="margin-left:3%">脚本内容：</div>
                <el-input v-model="scriptContentLike"
                          placeholder="请输入内容"
                          class="item-input input-tem-name1"></el-input>
                <div class="item-tit"
                     style="margin-left:3%">脚本类型：</div>
                <el-select v-model="content_type"
                           placeholder="请选择"
                           class="list-sel"
                           ref="temp">
                    <el-option v-for="item in contentTypeList"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </div>
            <div class="he-wrapper1">
                <el-button class="search item-button"
                           type="primary"
                           @click="search">查询</el-button>
                <el-button class="reset"
                           type="primary"
                           @click="reset">重置</el-button>
            </div>
            <!-- </div> -->
        </header>
        <div class="body-container"
             ref="container">
            <el-table :data="tableData"
                      border
                      style="width: 100%;margin-top:15px"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection"
                                 align="center">
                </el-table-column>
                <el-table-column prop="content_type"
                                 label="脚本类型"
                                 align="center">
                </el-table-column>
                <el-table-column prop="script_name"
                                 label="脚本名称"
                                 align="center">
                </el-table-column>
                <el-table-column prop="script_content"
                                 label="脚本内容"
                                 align="center">
                </el-table-column>
            </el-table>
            <div class="block">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="total">
                </el-pagination>
            </div>
        </div>
        <section>
            <div class="selected-head">
                <!-- <i class="el-icon-d-arrow-right"></i> -->
                <el-button type="primary"
                           icon="el-icon-d-arrow-right"
                           class="tem-search"
                           @click="addItems">添加</el-button>
                <el-button type="primary"
                           icon="el-icon-delete"
                           class="tem-search"
                           @click="clearAll">清空</el-button>
            </div>
            <div class="selected-container"
                 ref="container1">
                <ul>
                    <li><span class="moni-name">监控项名称</span></li>
                    <li v-for="(item, index) in monitor"
                        :key="index">
                        <span class="moni-name">{{item.name}}</span>
                        <i class="el-icon-delete"
                           @click="clear(index)"></i></li>
                </ul>
            </div>
        </section>
        <footer>
            <el-button class=""
                       type="primary"
                       @click="selected"
                       icon="el-icon-success">选择</el-button>
            <el-button class=""
                       type="primary"
                       @click="empty"
                       icon="el-icon-delete">清空</el-button>
            <el-button class=""
                       type="primary"
                       @click="cancel"
                       icon="el-icon-error">取消</el-button>
        </footer>
    </div>
</template>

<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import rbMirrorCommonService from 'src/services/mirror/rb-mirror-common-services.factory.js'
    import { contentTypeList } from '../../config/options.js'
    export default {
        data() {
            return {
                contentTypeList: '',
                scriptNameLike: '',
                scriptContentLike: '',
                content_type: '',
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                // 当前页
                currentPage: 1,
                // 分页每页多少行数据
                pageSize: 5,
                // 每页多少行数组
                pageSizes: [5, 10, 20],
                // 总共多少行数据
                total: 0,
                // 已选择添加的监控项
                monitor: [],
                // 保存当前的监控项列表以作分页
                // list: []
            }
        },
        methods: {
            // 多选项选中
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变每页个数
            handleSizeChange(val) {
                this.pageSize = val
                this.search()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search()
            },
            // 添加到已选栏中
            addItems() {
                var id = this.$store.state.homeStore.template_id
                var arr = [].concat(this.multipleSelection)
                arr.forEach(item => {
                    if (this.monitor.indexOf(item) < 0) {
                        let obj = {}
                        obj.template_id = id
                        obj.value_type = '0'
                        obj.sys_type = 'SCRIPT'
                        obj.key = item.script_id
                        obj.name = item.script_name
                        this.monitor.push(obj)
                    }
                })
            },
            // 已选栏全清
            clearAll() {
                this.monitor = []
            },
            // 已选栏单个清
            clear(index) {
                this.monitor.splice(index, 1)
            },
            // 选择监控项
            selected() {
                // 由于封装的方法中数组仍返回的是原数组而不是副本所以monitor数组和vuex的数组相关联
                var id = this.$store.state.homeStore.template_id
                if (this.monitor.length <= 0) {
                    this.$alert('请选择至少一个监控项进行添加', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    // 判断添加进的模板是否重复
                    rbProjectDataServiceFactory.detail(id).then((res) => {
                        let bool = true
                        let arr = []
                        this.monitor.forEach((item) => {
                            res.item_list.forEach((item1) => {
                                if (item1.key === item.key) {
                                    bool = false
                                    arr.push(item1.name)
                                }
                            })
                        })
                        // 如果不重复
                        if (bool) {
                            let monitor = []
                            this.monitor.forEach((item) => {
                                // let obj = JSON.parse(JSON.stringify(item))
                                let obj = Object.assign({}, item)
                                obj.template_id = id
                                obj.value_type = '0'
                                obj.sys_type = 'SCRIPT'
                                monitor.push(obj)
                            })
                            let obj = {
                                'list_item': monitor
                            }
                            rbProjectDataServiceFactory.batchCreatItem(obj).then(() => {
                                this.$store.commit('changeBatch', true)
                                this.$router.go(-1)
                            }).catch(() => {
                                this.$alert('内部服务器出错', '警告', {
                                    confirmButtonText: '确定'
                                })
                            })
                        } else {
                            // 如果添加的模板中有重复的
                            let str = arr.join(',')
                            this.$alert(`模板${str}已存在`, '警告', {
                                confirmButtonText: '确定'
                            })
                        }
                    })
                }
            },
            // 取消选择
            cancel() {
                this.$router.go(-1)
            },
            // 清空监控项
            empty() {
                this.tableData = []
            },
            reset() {
                this.template_name = ''
                this.name = ''
                this.key = ''
                this.bizTem = ''
            },
            search() {
                let obj = {
                    content_type: this.content_type,
                    scriptNameLike: this.scriptNameLike,
                    scriptContentLike: this.scriptContentLike,
                    label_id: 'cruisecheck',
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.getItemList(obj)
            },
            // 巡检项增删改查
            getItemList(obj) {
                if (obj) {
                    rbProjectDataServiceFactory.getScriptList(obj).then((res) => {
                        this.total = res.totalCount
                        res.dataList.forEach((item) => {
                            item.content_type = rbMirrorCommonService.common('CONTENT_TYPE', '1', item.content_type)
                        })
                        this.tableData = res.dataList
                    })
                }
            },
            // 分页封装数据
            // packData (arr) {
            //   var arr1 = []
            //   var i = this.currentPage
            //   var j = this.pageSize
            //   for (let k = (i - 1) * j; k < i * j; k++) {
            //     if (arr[k]) {
            //       arr1.push(arr[k])
            //     }
            //   }
            //   return arr1
            // }
        },
        mounted() {
            this.contentTypeList = contentTypeList
            // this.initBizTemList()
            this.search()
        },

    }
</script>

<style lang="scss" scoped>
    .component-container {
        width: 100%;
        height: calc(100% - 58px - 48px);
        padding: 14px 14px 0px;
        overflow-y: auto;
        overflow-x: hidden;
        background-color: #f4f4f4;
        position: relative;
        header {
            height: 140px;
            width: 74%;
            padding: 10px;
            border: 2px solid #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;
            div.he-wrapper {
                text-align: center;
                width: 100%;
                .item-tit {
                    display: inline-block;
                }
                .item-input {
                    width: 18%;
                }
                /deep/ .el-select {
                    .el-input {
                        width: 75%;
                    }
                }
            }
            div.he-wrapper1 {
                text-align: center;
                width: 100%;
                .he-wrapper1-tit {
                    display: inline-block;
                }
                /deep/ .el-select {
                    .el-input {
                        width: 75%;
                    }
                }
                .el-button {
                    height: 30px;
                    width: 80px;
                    padding: 8px 15px;
                }
            }
        }
        .body-container {
            border: 2px solid #f0f0f0;
            margin-top: 20px;
            padding: 10px 5px;
            width: 74%;
            .block {
                margin-top: 30px;
                height: 50px;
                display: flex;
                align-items: center;
                justify-content: center;
            }
        }
        section {
            position: absolute;
            right: 2%;
            top: 15px;
            width: 22%;
            .selected-head {
                height: 40px;
                display: flex;
                align-items: center;
            }
            .selected-container {
                height: 559px;
                border: 2px solid #f0f0f0;
                margin-top: 15px;
                padding: 10px;
                overflow-y: scroll;
                ul {
                    list-style: none;
                    li {
                        color: #878d99;
                        line-height: 24px;
                        margin-top: 8px;
                        padding-left: 10px;
                        border: 1px solid #f0f0f0;
                        display: flex;
                        align-items: center;
                        .moni-name {
                            display: inline-block;
                            width: 100%;
                            text-align: center;
                            text-align: center;
                        }
                        i {
                            cursor: pointer;
                            margin-left: 10px;
                        }
                    }
                    li:first-of-type {
                        border: none !important;
                    }
                }
            }
        }
        footer {
            width: 74%;
            margin-top: 35px;
            text-align: center;
        }
    }
</style>

<template>
    <div>
        <el-row>
            <el-col :span="6">
                <div class="com-tree">
                    <div>
                        <el-input placeholder="输入关键字进行过滤"
                                  class="search-box"
                                  v-model="filterText"></el-input>
                    </div>
                    <div class="yw-el-tree">
                        <el-tree ref="departmentTree"
                                 :data="data"
                                 :filter-node-method="filterNode"
                                 node-key="uuid"
                                 :props="defaultProps"
                                 :lazy="true"
                                 :load="loadHandler"
                                 :default-expanded-keys="[0]"
                                 :default-checked-keys="defaultCheckedKeys"
                                 @node-click="handleNodeClick">
                        </el-tree>
                    </div>
                </div>
            </el-col>
            <el-col :span="18">
                <div class="com-tree">
                    <el-row>
                        <el-col :span="18">
                            <el-input class="search-box"
                                      style="width: 300px;"
                                      v-model="searchText"
                                      placeholder="请输入用户名/真实姓名/手机号/邮箱">
                                <i slot="suffix"
                                   class="el-input__icon el-icon-search"
                                   @click="searchPersionList"></i>
                            </el-input>
                        </el-col>
                        <!--<el-col :span="6"><el-button style="font-size: 16px;padding-left:50px;" type="text" icon="el-icon-plus" @click="customizeUserDialog = true">自定义用户</el-button></el-col>-->
                    </el-row>
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-el-table"
                                  :data="userData"
                                  stripe
                                  border
                                  height="270px"
                                  tooltip-effect="dark"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection"
                                             align="center"></el-table-column>
                            <el-table-column prop="name"
                                             label="姓名"
                                             align="center"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column prop="mobile"
                                             label="手机号"
                                             width="100"
                                             align="center"
                                             show-overflow-tooltip></el-table-column>
                            <el-table-column show-overflow-tooltip
                                             prop="mail"
                                             label="邮箱"
                                             align="center"></el-table-column>
                            <el-table-column prop="post"
                                             label="职责"
                                             align="center"
                                             show-overflow-tooltip></el-table-column>
                        </el-table>
                        <el-pagination @current-change="handleCurrentChange"
                                       :current-page="userPageNo"
                                       :page-size="userPageSize"
                                       layout="total, prev, pager, next, jumper"
                                       :total="userTotal">
                        </el-pagination>

                    </div>

                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import personDataService from 'src/services/sys/person-services.js'
    import personDataMixin from 'src/services/sys/person-mixin.js'
    export default {
        components: {
        },
        props: [
        ],
        mixins: [personDataMixin],
        data() {
            return {
                userResult: [],
                userList: [],
                data: [],
                defaultProps: {
                    children: 'childList',
                    label: 'name'
                },
                defaultCheckedKeys: [],
                deviceAuthDataExpanded: [],
                searchText: '',
                selectedDepartmentId: '',
                userData: [],
                userTotal: '',
                userPageNo: 1,
                userPageSize: 50,
                multipleSelection: [],
                filterText: ''
            }
        },
        mounted: function () {
            this.getDepartment()
        },
        beforeDestroy: function () {
            this.setUserList()
        },
        methods: {
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleNodeClick(data) {
                this.selectedDepartmentId = data.uuid
                this.getPersionList(data.uuid)
            },
            getDepartment() {
                personDataService.getDepartmentTree().then((res) => {
                    // 返回部门id用作获取默认用户列表
                    if (res.length > 0) {
                        this.deviceAuthDataExpanded = [res[0].uuid]
                        this.selectedDepartmentId = res[0].uuid
                        this.getPersionList(res[0].uuid)
                    }
                })
            },
            getPersionList(deptId) {
                this.userPageNo = 1
                personDataService.getPersonList(deptId, this.userPageSize, this.userPageNo, '').then((res) => {
                    if (res) {
                        this.userData = res.result
                        this.userTotal = res.count

                    }
                })
                //        let obj = {
                //          'userType': 1,
                //          'deptId': deptId
                //        }
                //        personDataService.getPersonLists(obj).then((res) => {
                //          if (res) {
                //            let arr = []
                //            for (let i = 0; i < res.length; i++) {
                //              let obj = {
                //                'label': res[i].name,
                //                'key': res[i].ldapId
                //              }
                //              arr.push(obj)
                //            }
                //            this.userList = arr
                //          }
                //        })
            },
            setUserList() {
                this.userList = []
                this.userResult = []
            },
            searchPersionList() {
                if (!this.selectedDepartmentId) {
                    this.$alert('请先选择部门!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }

                this.userPageNo = 1
                personDataService.getPersonList('', this.userPageSize, this.userPageNo, this.searchText).then((res) => {
                    if (res) {
                        this.userData = res.result
                        this.userTotal = res.count
                    }
                })
            },
            filterNode(value, data, node) {
                if (!value) return true
                return node.label.indexOf(value) !== -1
            },
            handleCurrentChange(val) {
                this.userPageNo = val
                personDataService.getPersonList(this.selectedDepartmentId, this.userPageSize, this.userPageNo, this.searchText).then((res) => {
                    if (res) {
                        this.userData = res.result
                        this.userTotal = res.count
                    }
                })
            }

        },
        watch: {
            filterText(val) {
                this.$refs['departmentTree'].filter(val)
            }
        }
    }
</script>

<style scoped lang="scss">
    .com-tree {
      padding-right: 20px;
      .search-box {
        margin-bottom: 14px;
        font-size: 12px;
      }
      .yw-el-tree {
        height: 270px;
      }
      .el-tree {
        /deep/ .el-tree-node__content {
          .buss {
            display: none;
          }
        }
        /deep/ .el-tree-node__content:hover {
          .buss {
            display: inline-block;
          }
        }
      }
      .el-table {
        border-radius: 8px;
        font-size: 12px;
        height: 270px;
      }
    }
</style>

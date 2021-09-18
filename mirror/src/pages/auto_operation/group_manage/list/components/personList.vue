<template>
    <div class="yw-el-table-wrap"
         style="height:calc(100% - 40px)">
        <el-table class="yw-el-table"
                  :data="personList"
                  stripe
                  tooltip-effect="dark"
                  border
                  height="calc(100% - 40px)"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection"
                             width="40">
            </el-table-column>
            <el-table-column prop="group_name"
                             label="分组名称">
            </el-table-column>
            <el-table-column prop="object_type_name"
                             label="对象类型">
            </el-table-column>
            <el-table-column prop="object_name"
                             show-overflow-tooltip
                             label="对象名称">
            </el-table-column>
            <el-table-column prop="creater"
                             show-overflow-tooltip
                             label="创建人">
            </el-table-column>
            <el-table-column show-overflow-tooltip
                             prop="create_time"
                             label="创建时间">
            </el-table-column>
            <el-table-column prop="updater"
                             label="最新修改人">
            </el-table-column>
            <el-table-column show-overflow-tooltip
                             prop="update_time"
                             label="最新修改时间">
            </el-table-column>
            <!-- <el-table-column show-overflow-tooltip
                       prop="mail"
                       label="邮箱">
      </el-table-column>
      <el-table-column prop="no"
                       label="工号">
      </el-table-column>
      <el-table-column show-overflow-tooltip
                       prop="phone"
                       label="办公电话">
      </el-table-column> -->
            <!-- <el-table-column label="操作"
                       width="80">
        <template slot-scope="scope">
          <div class="yw-table-operator">
            <el-button type="text"
                       icon="el-icon-tickets"
                       @click="handleDetail(scope.row)">
            </el-button>
            <el-button type="text"
                       icon="el-icon-delete"
                       @click="handleDelete(scope.row)">
            </el-button>
          </div>

        </template>
      </el-table-column> -->
        </el-table>
        <div class="yw-page-wrap">
            <el-pagination class="person-manage__pager"
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-size="pagesize"
                           :page-sizes="[5, 10, 50]"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="total">
            </el-pagination>
        </div>

    </div>
</template>
<script>
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'

    export default {
        data() {
            return {
                personList: [],
                currentPage: 1,
                total: 1,
                pagesize: 10,
                multipleSelection: []
            }
        },
        props: {
            // dictionary: {
            //   type: Object,
            //   default: null
            // },
            searchText: {
                type: String,
                default: ''
            },
            searchTextGroup: {
                type: String,
                default: ''
            },
            valueObject: {
                type: String,
                default: ''
            },
            selectedGroupId: {
                type: String,
                default: ''
            }
        },
        watch: {
            // dictionary (val) {
            //   // console.log('watch dictionary', val)
            //   this.refreshTableDictionary(val)
            // },
            searchText() {
                this.searchData()
            },
            searchTextGroup() {
                this.searchData()
            },
            valueObject() {
                this.searchData()
            },
        },
        methods: {
            // 选择的列表
            handleSelectionChange(val) {
                this.multipleSelection = val
                this.$emit('selectGroupObject', val)
            },
            handleSizeChange(val) {
                this.pagesize = val
                // console.log(this.pagesize)
                let groupid = this.selectedGroupId
                this.getPersonList(groupid, val)
            },
            handleCurrentChange(val) {
                // console.log(this.pagesize)
                let groupid = this.selectedGroupId
                // if (this.searchText || this.searchTextGroup || this.valueObject ) {
                this.getPersonList(null, this.pagesize, val, this.searchText, this.searchTextGroup, this.valueObject, groupid)
                // } else {
                //   this.getPersonList(null, this.pagesize, val, null)
                // }
            },
            // handleDetail (data) {
            //   this.$emit('detial-click', data.uuid)
            // },
            // handleDelete (data) {
            //   this.$confirm('确定要删除吗?', '提示', {
            //     confirmButtonText: '确定',
            //     cancelButtonText: '取消',
            //     type: 'warning'
            //   }).then(() => {
            //     groupDataService.deletePerson(data.uuid).then((res) => {
            //       this.$message({
            //         message: '操作成功',
            //         type: 'success'
            //       })
            //       this.getPersonList()
            //     }).catch((res) => {
            //       var message
            //       if (res.data.errors[0].code === 'User does\'nt have the permission') {
            //         message = res.data.errors[0].message
            //       }
            //       this.$message({
            //         message: message ? message.toString() : '操作失败',
            //         type: 'error'
            //       })
            //     })
            //   })
            // },
            // 搜索过滤
            // searchFilter (data) {
            //   return !this.searchText || data.code.toLowerCase().includes(this.searchText.toLowerCase())
            // },
            // refreshTableDictionary (dictionary) {
            //   if (dictionary && Object.keys(dictionary).length > 0) {
            //     if (this.personList && this.personList.length > 0) {
            //       this.personList.map((item) => {
            //         item.sex = dictionary.sexal[item.sex]
            //         item.user_type = dictionary.userType[item.user_type]
            //         // item.dept_id = dictionary.department[item.dept_id]
            //       })
            //     }
            //   }
            // },
            searchData(id) {
                let groupid = null
                if (id) {
                    groupid = id
                } else {
                    groupid = this.selectedGroupId
                }
                // alert("s")
                this.currentPage = 1
                this.getPersonList(null, this.pagesize, this.currentPage, this.searchText, this.searchTextGroup, this.valueObject, groupid)
            },
            getPersonList(id, page, val, search, search1, valueObject, groupParentId) {
                // console.log(id, page, val, code)
                groupDataService.querGroupRelationList(id, page, val, search, search1, valueObject, groupParentId).then((res) => {
                    if (res) {
                        this.currentPage = res.curPage
                        this.total = res.totalCount
                        this.personList = res.dataList
                        this.personList.forEach((item) => {
                            switch (item.object_type) {
                                case 'pipeline':
                                    item.object_type_name = '作业'
                                    break
                                case 'script':
                                    item.object_type_name = '脚本'
                                    break
                                case 'yum':
                                    item.object_type_name = 'yum文件'
                                    break
                                case 'scenes':
                                    item.object_type_name = '场景'
                                    break
                                case 'ap_scheme':
                                    item.object_type_name = '自愈规则'
                                    break
                                case 'file':
                                    item.object_type_name = '文件'
                                    break
                            }
                        })
                        this.$emit('getGroupListData', res.dataList)
                    }
                })
            }

        },
        mounted() {

        }
    }
</script>
<style lang="scss" scoped>
    .el-table th {
        color: #333333;
    }
    .person-manage__ex-table {
        margin-top: 14px;
        padding: 12px;
        background: #fff;
        border: 1px solid #dcdfe6;
        border-radius: 8px;
        height: 480px;
    }

    .person-manage__table {
        .el-table__body-wrapper {
            height: 450px;
            overflow: auto;
            .el-table__body tr > td {
                padding: 4px 0;
            }
        }
    }

    .person-manage__pager {
        text-align: right;
        padding-top: 8px;
        // padding-bottom: 14px;
    }
</style>

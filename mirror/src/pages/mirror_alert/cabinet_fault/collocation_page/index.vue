<!--  -->
<template>
    <div class="components-container"
         style="padding:0;">
        <el-container>
            <el-aside width="260px"
                      style="overflow:hidden"
                      class="aside-tree">
                <!-- 左侧树 -->
                <comtree :ref="treeName2"
                         class="res-manage__tree"
                         node-key="uuid"
                         :data="areaData"
                         :props="areaDataDefault"
                         show-checkbox
                         :exId="treeName2"
                         @check="check"
                         ex-show-search></comtree>

            </el-aside>
            <el-main class="tree-main"
                     style="padding:0 0 0 10px;">
                <collocationView :parentParams="parentParams"
                                 :childParams="childParams"></collocationView>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import collocationView from './collocationView.vue'
    import comtree from 'src/pages/system_manage/components/tree.vue'
    import resourceDataService from 'src/services/sys/reource-services.js'

    export default {
        data() {
            return {
                treeName2: 'areaTree',
                parentParams: {},// 选中树参数
                childParams: {},// 选中树参数
                areaData: [],
                areaDataDefault: {
                    label: 'name',
                    children: 'subList',
                    disabled: function () {
                        return false
                    }
                },

            }
        },
        components: {
            collocationView,
            comtree
        },
        created() {
            this.getAreaData()
        },
        methods: {
            // 获取通用权限
            getAreaData() {
                resourceDataService.getAreaTree().then((res) => {
                    this.areaData = res
                })
            },
            // getDeviceAuthData(data, checkedKey) {
            //     let deviceData = []
            //     data.forEach((item) => {
            //         if (checkedKey.includes(item.uuid)) {
            //             if (item.subList && item.subList.length > 0) {
            //                 let subDeviceData = this.getDeviceAuthData(item.subList, checkedKey)
            //                 item.subList = subDeviceData
            //             }
            //             deviceData.push(item)
            //         }
            //     })
            //     return deviceData
            // },
            check(data) {
                if (data.type === 'idcType') {
                    this.parentParams = data
                }
                if (data.type === 'room') {
                    this.childParams = data

                    console.log('data===', this.childParams)

                }
            }
        }
    }

</script>
<style lang='scss' scoped>
</style>
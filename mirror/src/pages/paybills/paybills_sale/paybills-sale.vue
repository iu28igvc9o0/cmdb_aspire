<template>
    <div class="components-container">
        <!-- tab -->
        <section class=" tab-section">
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane
                    :label="item.label"
                    :name="item.name"
                    v-for="(item, index) in tabData"
                    :key="index"
                >
                </el-tab-pane>
            </el-tabs>
            <!-- <keep-alive> -->
            <component
                :is="currentTabComponent"
                exclude="paybillsSaleUser"
                v-if="resetComponent"
            ></component>
            <!-- </keep-alive> -->
        </section>
        <!-- tab -->
    </div>
</template>

<script>
import updateComponent from 'src/utils/updateComponent.js'
export default {
    // name: 'paybillsSale',
    mixins: [updateComponent],
    components: {
        paybillsSaleUser: () => import('src/pages/paybills/main/paybills-sale-user.vue'),
        paybillsSaleRes: () => import('src/pages/paybills/main/paybills-sale-res.vue'),
        paybillsSalePool: () => import('src/pages/paybills/main/paybills-sale-pool.vue'),
    },
    data() {
        return {
            // tab
            activeName: 'paybillsSaleUser',
            activeIndex: 0,
            tabData: [
                { label: '租户折扣', name: 'paybillsSaleUser', index: 0 },
                { label: '资源池折扣', name: 'paybillsSalePool', index: 1 },
            ],
            // { label: '资源折扣', name: 'paybillsSaleRes', index: 2 },//  资源折扣暂时屏蔽
            currentTabComponent: 'paybillsSaleUser',
        }
    },
    created() {},
    methods: {
        handleClick(tab) {
            this.currentTabComponent = tab.name
            this.activeIndex = tab.index
            this.updateComponent()
        },
    },
}
</script>

<style lang="scss" scoped>
.step-wrap {
    position: absolute;
    top: 15px;
    right: 20px;
    .step-item {
        display: inline-block;
        margin-right: 20px;
        cursor: pointer;
        &:hover {
            color: #409eff;
        }
    }
}
</style>

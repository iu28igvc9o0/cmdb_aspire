<template>
    <!-- 表格tab组件 -->
    <div class="tab-group mleft20">
        <template v-for="(item, index) in tabList">
            <span class="tab-item pointer"
                  :key="item.name"
                  @click="handleTabClick(index)"
                  :class="{'active' : activeIndex === index}"
                  :label="item.name"
                  :name="item.name">

                <!-- 下拉类型tab -->
                <el-dropdown v-if="item.tabType === 'dropdown'"
                             @command="(dropIndex) => handleTabClick(index, true, dropIndex)">
                    <span class="el-dropdown-link f12">
                        {{item.name}}
                        <span v-if="item.count !== undefined && showNum">（{{item.count}}）</span>
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="(dropItem, dropIndex) in item.dropList"
                                          :command="dropIndex"
                                          class="f12"
                                          :key="dropItem.name">
                            {{dropItem.name}}
                            <span v-if="dropItem.count !== undefined && showNum">（{{dropItem.count}}）</span>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>

                <!-- 平铺类型tab -->
                <span v-else>
                    <span v-html="item.html ? item.html : item.name"></span>
                    <span v-if="item.count !== undefined && showNum">（{{item.count}}）</span>
                </span>
            </span>
        </template>
    </div>
</template>

<script>

    export default {
        props: {
            tabList: {
                type: Array,
                default() {
                    return []
                }
            },
            activeIndex: {
                type: Number,
                default: 0
            },
            showNum: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {}
        },
        mounted() {
        },
        methods: {
            handleTabClick(index, isDropList, dropIndex) {
                this.$emit('handleTabClick', index, isDropList, dropIndex)
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>


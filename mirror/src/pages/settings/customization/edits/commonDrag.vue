<template>
    <!-- 定制化：可编辑页 -->
    <div class="edit-wrap">
        <draggable class="component-list"
                   :class="componentsDir"
                   @end="dragEnd"
                   @add="dragAdd"
                   element="section"
                   :value="componentsRequire"
                   :options="dragOptionsMain">
            <component :is="item.componentName.default"
                       class="component-item"
                       v-for="(item,index) in componentsRequire"
                       :key="index"></component>
        </draggable>

    </div>
</template>

<script>
    import draggable from 'vuedraggable'

    export default {
        // mixins: [DrawChart],
        components: {
            draggable,
        },
        data () {
            return {
                // 组件菜单
                componentsDir: '',
                // 组件数据
                componentsData: [{ componentName: 'resource-chart-overview' }],
            }
        },
        computed: {
            // 拖拽配置
            dragOptionsMain () {
                return {
                    animation: 0,
                    sort: true,
                    group: 'chartGroup',
                    scroll: true
                }
            },

            // 动态引入组件
            componentsRequire () {
                let componentsRequire = []
                this.componentsData.forEach(item => {
                    componentsRequire.push({ componentName: require(`../components/${item.componentName}.vue`) })
                })
                return componentsRequire
            }
        },
        methods: {
            // 获得模块数据
            getComponents () {
                // 获得模块数据
                this.componentsDir = this.$store.state.customization.componentsDir
                this.componentsData = this.$store.state.customization.componentsData

            },

            // 拖拽排序完成
            dragEnd (val) {
                let activeData = this.componentsData[val.oldIndex]
                let positionData = this.componentsData[val.newIndex]
                this.componentsData.splice(val.newIndex, 1, activeData)
                this.componentsData.splice(val.oldIndex, 1, positionData)

            },
            // 拖拽添加
            dragAdd (val) {
                this.$bus.$on('getDragSource', (source) => {
                    let componentName = source.componentName

                    let isExist = this.componentsData.some((item) => {
                        return item.componentName === componentName
                    })

                    if (!isExist) {
                        this.componentsData.splice(val.newIndex, 0, { componentName: source.componentName })
                    }

                })
            }

        },
        created () {
            this.getComponents()
        },
        mounted () {


        }
    }
</script>

<style lang="scss" scoped>
.edit-wrap {
  .component-list {
    .component-item {
      cursor: move;
    }
  }
}

/deep/.alert.component-list {
  display: grid;
  grid-template-columns: 24.2% 50% 24.2%;
  justify-content: space-between;
  // grid-gap: 20px;
  align-content: start;
  width: 100%;
  .component-item {
    width: 100% !important;
    &:nth-child(2) {
      grid-row: span 3;
    }
    &:nth-child(8),
    &:nth-child(9) {
      grid-column: span 3;
    }
  }
}
</style>

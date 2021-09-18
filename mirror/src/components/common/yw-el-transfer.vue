<template>
    <!-- 穿梭框(elementui的穿梭框封装) -->
    <div class="yw-transfer">
        <el-transfer filterable
                     :filter-method="filterMethod"
                     filter-placeholder="请输入搜索内容"
                     :titles="activeOptions.titles"
                     v-model="value"
                     :props="activeOptions.props"
                     :data="datas">
            <span slot-scope="{ datas }">{{datas}}</span>
            <span slot-scope="{ option }">{{ option.id }} - {{ option.name }}</span>
        </el-transfer>
    </div>
</template>

<script>

    export default {
        props: ['datas', 'options'],
        components: {

        },
        data () {
            return {
                // 选中的值
                value: [],
                // 配置项
                defaultOptions: {
                    // 数据对应字段
                    props: {
                        key: 'key',
                        label: 'label',
                    },
                    // 标题
                    titles: []
                },
            }
        },
        computed: {
            activeOptions () {
                let temp = this.defaultOptions
                temp.props = this.options && this.options.props ? this.options.props : temp.props
                temp.titles = this.options && this.options.titles ? this.options.titles : temp.titles
                return temp

            }
        },
        methods: {
            // 搜索
            filterMethod (query, item) {
                return item[this.activeOptions.props.label].indexOf(query) > -1
            }
        },
        mounted () {
            this.data = []
        }

    }
</script>
<style lang="scss" scoped>
.yw-transfer {
  display: inline-block;
}
</style>

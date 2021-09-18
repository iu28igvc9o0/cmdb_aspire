<template>
    <!-- 搜索框 -->
    <div class="search-box-wrap"
         :style="activeStyle">
        <el-input v-model="defaultParams.keyword"
                  :style="activeStyle"
                  :placeholder="(searchParams.desc && searchParams.desc.placeholder) ? searchParams.desc.placeholder : defaultParams.desc.placeholder"
                  @keyup.enter.native="query()"
                  @clear="query()"
                  :clearable="false"></el-input>
        <i class="el-icon-search"
           :style="{color:searchParams.desc.iconColor ? searchParams.desc.iconColor : defaultParams.desc.iconColor}"
           @click="query()"></i>
    </div>

</template>
<script>
    export default {
        props: ['searchParams'],
        data() {
            return {
                // 默认参数
                defaultParams: {
                    keyword: '',
                    desc: {
                        placeholder: '请输入关键字',
                        bgcolor: 'transparent',
                        borderRadius: '6px',
                        borderColor: '#fff',
                        color: '#606266',
                        iconColor: '#46bafe'
                    }
                },
            }
        },
        computed: {
            activeStyle() {
                let style = {
                    backgroundColor: this.searchParams.desc.bgcolor ? this.searchParams.desc.bgcolor : this.defaultParams.desc.bgcolor,
                    borderRadius: this.searchParams.desc.borderRadius ? this.searchParams.desc.borderRadius : this.defaultParams.desc.borderRadius,
                    borderColor: this.searchParams.desc.borderColor ? this.searchParams.desc.borderColor : this.defaultParams.desc.borderColor,
                    color: this.searchParams.desc.color ? this.searchParams.desc.borderColor : this.defaultParams.desc.color,
                }
                return style
            }
        },
        created() {
            this.defaultParams.keyword = this.searchParams.keyword

        },
        methods: {
            // 查询
            query() {
                this.$emit('changeSearch', this.defaultParams.keyword)
            },

        }

    }
</script>
<style lang="scss" scoped>
    .search-box-wrap {
        position: relative;
        display: inline-block;
        width: 100%;
        border: 1px solid transparent;
        .el-icon-search {
            position: absolute;
            top: 5px;
            right: 8px;
            font-size: 16px;
            color: #46bafe;
            cursor: pointer;
            &:hover {
                color: #086297;
            }
        }
        .el-input {
            width: 100%;
            .el-input__inner {
                padding-right: 30px;
                background: inherit;
                // border-radius: inherit;
                // border-color: inherit;
                border-radius: 0;
                border-color: transparent;
                color: inherit;
            }
        }
    }
</style>

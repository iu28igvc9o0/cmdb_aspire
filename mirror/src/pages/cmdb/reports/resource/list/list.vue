<template>
    <div>
        <ReportComponent :params="params"
                         @changeDate="changeDate"></ReportComponent>
    </div>

</template>

<script>
    import { xMonth } from 'src/assets/js/utility/rb-filters.js'
    export default {
        components: {
            ReportComponent: () => import('../../reportComponent.vue'),
        },
        data() {
            return {
                params: {
                    updatePerson: '',// 更新人
                    province: '',// 省份(省份名称)
                    source: 'resource',// 数据来源(资源:resource,运维:yunwei)
                    date: '',// 月份(如：2020-03)
                    type: 'query',// 保存页面update、查询页面query
                    link: '',// 外部链接external、内部链接其他值
                }
            }
        },

        mounted() {
            this.getDatas()
        },
        methods: {
            getDatas() {
                let lastMonth = xMonth(-1, '-')
                // this.params.updatePerson = getUrlKey('updatePerson') || sessionStorage.getItem('username');
                // this.params.province = getUrlKey('province') || '';
                // this.params.date = getUrlKey('date') || lastMonth;
                this.params.updatePerson = sessionStorage.getItem('updatePersonStorage') || sessionStorage.getItem('username')
                this.params.province = sessionStorage.getItem('provinceStorage') ||
                    JSON.parse(sessionStorage.getItem('loginUserInfo'))['dept_list'][0].name
                this.params.date = sessionStorage.getItem('dateStorage') || lastMonth
                // 外部链接
                if (sessionStorage.getItem('mirrorTokenStorage')) {
                    this.params.link = 'external'
                } else {
                    this.params.link = ''
                }
            },
            changeDate(val) {
                this.params.date = val
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>

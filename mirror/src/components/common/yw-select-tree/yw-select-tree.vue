<template>
    <!-- 下拉框树-->
    <div class="yw-select-tree">
        <treeselect :options="dataOptions"
                    :value="selectValues"
                    :normalizer="normalizer" />
    </div>

</template>

<script>
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    export default {
        props: ['options'],
        components: {
            Treeselect,
        },
        data() {
            return {

            }
        },


        methods: {


        },
        created() {

        },

    }
</script>
<style lang="scss" scoped>

<template>
    <div class="SolveQuestionsConfirm">
        <Solve :selectInfo="selectInfo" />
    </div>
</template>
<script>
    import Solve from '../view'
    export default {
        name: 'SolveQuestionsConfirm',
        data() {
            return {
                selectInfo: 'confirm'
            }
        },
        components: {
            Solve
        }
    }
</script>
<style lang="scss" scoped>
    .SolveQuestionsConfirm {
        height: 100%;
        width: 100%;
    }
</style>
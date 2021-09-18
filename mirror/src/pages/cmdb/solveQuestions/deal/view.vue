<template>
    <div class="SolveQuestionsDeal">
        <Solve :selectInfo="selectInfo" />
    </div>
</template>
<script>
    import Solve from '../view'
    export default {
        name: 'SolveQuestionsDeal',
        data() {
            return {
                selectInfo: 'deal'
            }
        },
        components: {
            Solve
        }
    }
</script>
<style lang="scss" scoped>
    .SolveQuestionsDeal {
        height: 100%;
        width: 100%;
    }
</style>
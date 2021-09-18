export default {
    data() {
        return {
            Contentoptions: {
                animation: 150,
                ghostClass: 'sortable-ghost',
                chosenClass: 'chosenClass',
                dragClass: 'highLight',
                scroll: true,
                scrollSensitivity: 200,
                forceFallback: false
            },
            ComArr: [],
            Menu: [],
            Menuoptions: [],
            Actived: {},
            hover: '',
            flag: false,
            dragover1: {},
            dragindex: '',
            currentModuleIndex: '',
            event: {},
        }
    },
    computed: {
    },
    methods: {
        Itemhover(item) {
            this.hover = item
        },
        itemClick(item) {
            // 在根据类型反馈控制高亮和列表
            let type = item.type
            this.Menuoptions = this.Menu.filter(item => item.type === type)
        },
        ConDrag(evt) {
            this.flag = true
            this.currentModuleIndex = evt.oldIndex
        },
        dragover(e, item, index) {
            this.event = e.target
            this.dragover1 = JSON.parse(JSON.stringify(item))
            this.dragindex = index
        },
        dragleave(e) {
            // 拖拽事件子节点也会多次触发，所以要加判断是初次进入的同一个父级才做清除操作
            if (e.target === this.event) {
                this.dragover1 = {}
                this.dragindex = ''
            }
        },
        start(evt) {
            // 拖拽开始操作
            console.log(evt)
        },
        // eslint-disable-next-line no-unused-vars
        end(evt) {
            // 拖拽结束操作
            console.log(evt)
        },
        filterItem(item) {
            // 初始化过滤画板已有的组件
            item.forEach(item => {
                item.children.forEach(item => {
                    this.ComArr.push(item.name)
                })
            })
            this.Menu = this.Menu.filter(item => {
                return !this.ComArr.includes(item.name)
            })
            this.Menuoptions = this.Menu
        },
    },
    mounted() {
    }
}

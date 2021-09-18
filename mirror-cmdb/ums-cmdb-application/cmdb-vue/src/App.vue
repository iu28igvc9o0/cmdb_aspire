<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>
<style>
    @import "../static/css/main.css";
	@import "assets/css/public/font.css";
</style>

<script>
export default {
    data() {
            return {
 
           
            }
        },
        mounted: function() {
			
			var explorer = window.navigator.userAgent;
			
			
			if ((!$.support.style) && (!$.support.opacity)) {
				//alert('IE');
				//IE������������ �б��ѡ�����������
				function f(){
					//$('.el-table-column--selection div.cell').css('width','70px'); 
					//$('.el-table td, .el-table th').css('text-overflow','clip');
					//$('.el-table .cell, .el-table th>div').css('text-overflow','clip');
					$('.el-table-column--selection div.cell').css('text-overflow','clip'); 
					setTimeout(f,1000);
				}
				
				setTimeout(f,1000);
			}
			
		


        },
        methods: {

        },
        computed: {

        }
}
</script>
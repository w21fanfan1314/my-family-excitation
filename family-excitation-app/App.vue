<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import '@/uni_modules/uni-scss/index.scss';
	@import '@/uni_modules/uv-ui-tools/index.scss';
	
	@font-face {
		font-family: 'hanyizhuziguozhiruantang';
		src: url('~@/static/fonts/hanyizhuziguozhiruantang.ttf') format('truetype');
	}
</style>

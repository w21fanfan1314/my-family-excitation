<template>
	<uv-navbar :fixed="true" :title="title" bg-color="transparent" :title-style="{color: '#ffffff'}"
		left-icon-color="#ffffff" @leftClick="handleBackClick"></uv-navbar>
	<view class="video-detail" :style="{height: windowHeight}" v-html="html">
	</view>
</template>

<script setup>
	import { computed, getCurrentInstance, ref } from 'vue';
	import { onLoad } from '@dcloudio/uni-app'

	const videoData = ref()
	const title = ref()
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const html = computed(() => videoData.value?.url?.replace(/style=".*?"|style=".*?"/, ''))
	
	onLoad(() => {
		const instance = getCurrentInstance()
		const eventChannel = instance.proxy.getOpenerEventChannel()
		eventChannel.on('videoDetail', ({detail}) => {
			videoData.value = detail
			// uni.setNavigationBarTitle({
			// 	title: detail?.name
			// })
			title.value = detail?.name;
			
			console.log(html.value)
		})
	})
	
	function handleBackClick() {
		uni.navigateBack()
	}
</script>

<style scoped lang="scss">
	.video-detail {
		display: flex;
		flex-direction: column;
		background-color: black;
		
		:first-child {
			flex: 1;
		}
	}
</style>

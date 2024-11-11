<template>
	<view class="train-container" :style="{height: windowHeight}">
		<start></start>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { onLoad, onUnload } from '@dcloudio/uni-app'
	import Start from './scene/start.vue';
	
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	
	
	function onStart() {
		
	}
</script>

<style scoped lang="scss">
	
	.train-container {
		display: flex;
		flex-direction: column;
		background-color: orange;
		box-sizing: border-box;
		font-family: hanyizhuziguozhiruantang;
	}
</style>

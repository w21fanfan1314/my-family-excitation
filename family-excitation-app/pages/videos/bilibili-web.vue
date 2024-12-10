<template>
	<view class="bilibili-web" :style="pageStyle">
		<web-view :src="url" class="web-view"></web-view>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';

	const url = ref('https://m.bilibili.com/channel/v/life/funny/?spm_id_from=333.1007.0.0')
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const pageStyle = computed(() => ({
		height: windowHeight
	}))
</script>

<style scoped lang="scss">
	.bilibili-web {
		display: flex;
		flex-direction: column;
		
		.web-view {
			flex: 1;
		}
	}
</style>

<template>
	<uni-popup type="center" background-color="#ffffff" border-radius="30rpx" ref="popup" :is-mask-click="false">
		<view class="win-modal">
			<text class="title">恭喜中奖</text>
			<image class="image" :src="winData.image"></image>
			<uv-gap height="60rpx"></uv-gap>
			<tui-button type="danger" style="width: 400rpx;" shape="circle" @click="handleGetItNowClick">立即领取</tui-button>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose } from 'vue';

	const winData = ref({
		image: '',
		name: ''
	})
	const popup = ref()
	
	function handleGetItNowClick() {
		popup.value?.close()
	}
	
	defineExpose({
		open(data) {
			winData.value = data
			popup.value.open()
		}
	})
</script>

<style scoped lang="scss">
	.win-modal {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx;
		
		.title {
			font-size: 32rpx;
			color: $uni-color-error;
			font-weight: bold;
			margin-bottom: 30rpx;
		}
		
		.image {
			width: 300rpx;
			height: 300rpx;
			border-radius: 20rpx;
		}
	}
</style>
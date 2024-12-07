<template>
	<uni-popup type="center" background-color="#ffffff" border-radius="30rpx" ref="popup" :is-mask-click="false">
		<view class="win-modal">
			<text class="title">恭喜中奖</text>
			<image class="image" :src="winData.image"></image>
			<uv-gap height="20rpx"></uv-gap>
			<uv-text :size="18" :bold="true" :text="winData.name" color="#000" align="center"></uv-text>
			<uv-gap height="60rpx"></uv-gap>
			<tui-button type="danger" style="flex: 1" shape="circle" @click="handleGetItNowClick">立即领取</tui-button>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose } from 'vue';
	import { saveLotteryRecords } from '../../../api/Lottery';
	import { useUserStore } from '../../../store/user';

	const winData = ref({
		image: '',
		name: '',
		id: 0
	})
	const popup = ref()
	const user = useUserStore()
	
	async function handleGetItNowClick() {
		uni.showToast({
			icon: 'success',
			title: '领取成功'
		})
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
		width: 500rpx;
		
		.title {
			font-size: 32rpx;
			color: $uni-color-error;
			font-weight: bold;
			margin-bottom: 30rpx;
		}
		
		.image {
			width: 350rpx;
			height: 350rpx;
			border-radius: 20rpx;
		}
	}
</style>
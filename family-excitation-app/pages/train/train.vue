<template>
	<view class="train-container" :style="{height: windowHeight}">
		<text class="title uni-mb-10">一起来挑战吧</text>
		
		<train-button class="train-item" v-for="(item, index) in trainsData" :class="{'top0': index === 0}" :key="'train-item-' + item.id">
			{{item.name}}
		</train-button>
		<train-button class="back-item" @onClick="onBack">
			退出挑战
		</train-button>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { onLoad, onUnload } from '@dcloudio/uni-app'
	import TrainButton from './components/train-button.vue';
	import { trains } from '../../api/Train';
	
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const trainsData = ref([])
	
	onLoad(async() => {
		await loadTrains()
	})
	
	function onBack() {
		uni.navigateBack()
	}
	
	async function loadTrains() {
		uni.showLoading({
			title: '查询中...'
		})
		try {
			const res = await trains()
			if (res.code === 200) {
				trainsData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询订单列表错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
</script>

<style scoped lang="scss">
	
	@mixin train-item-btn($border-image-srouce: url('../../static/panel_brown_corners_a.png'), $broder-image-slice: 40 60 fill) {
		border-width: 10px;
		border-style: solid;
		border-image-source: $border-image-srouce;
		border-image-width: auto;
		border-image-slice: $broder-image-slice;
		font-size: 55rpx;
		color: $uni-text-color;
		padding: 0 40rpx;
		box-sizing: border-box;
		height: 150rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		min-width: 450rpx;
		
		&:active {
			transform: scale(0.9)
		}
	};
	.train-container {
		display: flex;
		flex-direction: column;
		background-color: lightyellow;
		padding: 30rpx 30rpx 120rpx 30rpx;
		align-items: center;
		justify-content: flex-end;
		box-sizing: border-box;
		font-family: hanyizhuziguozhiruantang;
		
		.title {
			color: $uni-color-warning;
			font-size: 60rpx;
		}
		
		.train-item {
			@include train-item-btn;
			margin-top: 50rpx;
			&.top0 {
				margin-top: 0;
			}
		}
		
		.back-item {
			@include train-item-btn(url('../../static/panel_grey_bolts_red.png'), 40 fill);
			margin-top: 100rpx;
		}
	}
</style>

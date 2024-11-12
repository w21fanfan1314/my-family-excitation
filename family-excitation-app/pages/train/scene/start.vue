<template>
	<view class="train-start">
		<text class="title uni-mb-10">一起来挑战吧</text>
		
		<train-button class="train-item" v-for="(item, index) in trainsData" 
			:class="{'top0': index === 0}" :key="'train-item-' + item.id" @click="onItemClick(item)">
			{{item.name}}
		</train-button>
		<train-button class="back-item" @onClick="onBack">
			退出挑战
		</train-button>
	</view>
</template>

<script setup>
	import { onMounted, ref, defineEmits } from 'vue';
	import { trains } from '../../../api/Train';
	import TrainButton from '../components/train-button.vue';
	
	const emit = defineEmits(['onStart'])
	const trainsData = ref([])
	
	onMounted(async() => {
		await loadTrains()
	})
	
	function onBack() {
		uni.navigateBack()
	}
	
	function onItemClick(item) {
		emit('onStart', {item})
	}
	
	
</script>

<style scoped lang="scss">
	@mixin train-item-btn($border-image-srouce: url('~@/static/panel_brown_corners_a.png'), $broder-image-slice: 40 60 fill) {
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
		min-width: 450rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		
		&:active {
			transform: scale(0.9)
		}
	};
	
	.train-start {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: flex-end;
		padding: 30rpx 30rpx 120rpx 30rpx;
		height: 100%;
		
		
		.title {
			color: $uni-color-primary;
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
			@include train-item-btn(url('~@/static/panel_grey_bolts_red.png'), 40 fill);
			margin-top: 100rpx;
		}
	}
</style>
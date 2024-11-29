<template>
	<view class="lottery-container">
		<view class="lottery">
			<view class="lottery-header">
				幸运大抽奖
			</view>
			<view class="lottery-grid">
				<view v-for="(item, index) in prizeData" :key="'prize-item-'+ index" 
					class="lottery-item" :class="[index % 3 === 0 ? 'first' : '', index < 3 ? 'top' : '', selectedPrizeIndex === index ? 'selected' : '']">
					<image :src="item.image" class="prize-image" mode="aspectFill"></image>
					{{item.name}}
				</view>
			</view>
			<view class="lottery-bottom">
				<tui-button type="danger" shape="circle" :loading="loading" @click="handlestartClick">开始抽奖</tui-button>
				<uv-gap height="30rpx"></uv-gap>
				<tui-button type="green" shape="circle" :plain="false" >我的奖品</tui-button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref } from 'vue';

	const prizeData = ref([
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'}
	])
	const selectedPrizeIndex = ref(-1)
	const loading = ref(false)
	
	async function handlestartClick() {
		selectedPrizeIndex.value = 0
		await startLottery()
	}
	
	function startLottery() {
		return new Promise(reslove => {
			loading.value = true
			let index = 0
			const win = Math.floor((Math.random() * 9) + 27)
			const timer = setInterval(() => {
				if (index === win) {
					clearInterval(timer)
					reslove(win)
					loading.value = false
					return
				}
				if (selectedPrizeIndex.value + 1 === prizeData.value.length) {
					selectedPrizeIndex.value = 0
				} else {
					selectedPrizeIndex.value ++
				}
				index ++
			}, 100)
		})
	}
</script>

<style scoped lang="scss">
	.lottery-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		.lottery {
			$grid-width: 600rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			
			$bg-color: cornsilk;
			.lottery-header {
				border-radius: 16rpx 16rpx 0 0;
				background-color: $bg-color;
				display: flex;
				flex-direction: column;
				align-items: center;
				padding: 10rpx 20rpx;
				width: $grid-width - 200rpx;
				font-size: 38rpx;
				box-sizing: border-box;
				color: red;
				font-weight: bold;
			}
			.lottery-grid {
				$item-margin: 20rpx;
				$item-size: ($grid-width - (40rpx + ($item-margin * 2))) / 3;
				display: flex;
				flex-direction: row;
				flex-wrap: wrap;
				width: $grid-width;
				height: $grid-width;
				border-radius: 16rpx;
				background-color: $bg-color;
				padding: 20rpx;
				box-sizing: border-box;
				
				.lottery-item {
					display: flex;
					flex-direction: column;
					width: $item-size;
					height: $item-size;
					border-radius: 12rpx;
					align-items: center;
					justify-content: space-between;
					font-size: 22rpx;
					margin-left: $item-margin;
					margin-top: $item-margin;
					background-color: white;
					box-sizing: border-box;
					padding: 10rpx;
					border: 6rpx solid coral;
					color: black;
					// box-shadow: 5rpx 5rpx coral;
					
					&.first {
						margin-left: 0;
					}
					
					&.top {
						margin-top: 0;
					}
					
					$prize-image-size: $item-size - 70rpx;
					.prize-image {
						width: $prize-image-size;
						height: $prize-image-size;
					}
					
					&.selected {
						background-color: coral;
						color: white;
					}
				}
			}
			.lottery-bottom {
				display: flex;
				flex-direction: column;
				margin-top: 40rpx;
				width: $grid-width;
			}
		}
	}
</style>

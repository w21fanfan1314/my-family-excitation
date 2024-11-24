<template>
	<view class="lottery-container" :style="{height: windowHeight}">
		<view class="lottery">
			<view class="lottery-header">
				幸运大抽奖
			</view>
			<view class="lottery-grid">
				<view v-for="(item, index) in prizeData" :key="'prize-item-'+ item.index" 
					class="lottery-item" :class="[index % 3 === 0 ? 'first' : '', index < 3 ? 'top' : '', 
						selectedPrizeIndex === item.index ? 'selected' : '', item.index === -2 ? 'show' : '']">
					<template v-if="item.index === -2">
						<image class="user-image" mode="aspectFill"></image>
					</template>
					<template v-else class="prize-item">
						<image :src="item.image" class="prize-image" mode="aspectFill"></image>
						{{item.name}}
					</template>
				</view>
			</view>
			<view class="lottery-bottom">
				<tui-button type="primary" shape="circle" :loading="loading" :prevent-click="true"
					@click="handlestartClick">开始抽奖</tui-button>
				<uv-gap height="30rpx"></uv-gap>
				<tui-button type="green" shape="circle" :plain="false" :prevent-click="true" @click="handleMyWinClick">我的奖品</tui-button>
			</view>
		</view>
	</view>
	<tui-modal :show="noWinning" content="未中将,再接再厉" title="提示" :button="[{text: '确定', type: 'danger'}]" 
		@cancel="noWinning = false" @click="noWinning = false"></tui-modal>
	<win-modal ref="winModal"></win-modal>
	<winning-records-modal ref="winningRecordModal"></winning-records-modal>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import WinModal from './components/win-modal.vue';
	import WinningRecordsModal from './components/winning-records-modal.vue';
	
	

	const prizeData = ref([
		{ index: 0, win: true, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 1, win: true, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 2, win: true, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 7, win: true, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: -2, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 3, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 6, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 5, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'},
		{ index: 4, name: '谢谢参与', image: 'https://pic.chaopx.com/chao_origin_pic/23/06/02/fec76841a2f0cd2d71647ae458f6a3df.jpg!/fw/452/quality/90/unsharp/true/compress/true'}
	])
	const selectedPrizeIndex = ref(-1)
	const loading = ref(false)
	// ready starting end
	const status = ref("ready")
	const noWinning = ref(false)
	const winModal = ref()
	const winningRecordModal = ref()
	
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const winData = computed(() => {
		if (selectedPrizeIndex.value < prizeData.value.length && selectedPrizeIndex.value >= 0) {
			return prizeData.value[selectedPrizeIndex.value]
		}
		return null
	})
	
	function handleMyWinClick() {
		winningRecordModal.value?.open()
	}
	
	async function handlestartClick() {
		if (status.value === 'ready') {
			selectedPrizeIndex.value = 0
			status.value = "start"
			await startLottery()
			if (winData.value.win) {
				winModal.value?.open(winData.value)
			} else {
				noWinning.value = true
			}
			status.value = 'ready'
		}
	}
	
	function startLottery() {
		return new Promise(reslove => {
			loading.value = true
			let index = 0
			const winIndex = Math.floor((Math.random() * 8) + 24)
			const timer = setInterval(() => {
				if (index === winIndex) {
					clearInterval(timer)
					reslove(winIndex)
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
			margin-top: 50rpx;
			
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
				color: $uni-color-error;
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
					width: $item-size;
					height: $item-size;
					border-radius: 12rpx;
					font-size: 22rpx;
					margin-left: $item-margin;
					margin-top: $item-margin;
					background-color: white;
					box-sizing: border-box;
					padding: 10rpx;
					border: 6rpx solid $uni-color-error;
					color: black;
					// box-shadow: 5rpx 5rpx coral;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: space-between;
					
					&.show {
						border-width: 0;
					}
					
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
					
					.user-image {
						width: $prize-image-size;
						height: $prize-image-size;
						border-radius: $prize-image-size / 2;
					}
					
					&.selected {
						background-color: $uni-color-error;
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

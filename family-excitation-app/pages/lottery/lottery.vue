<template>
	<view class="lottery-container" :style="{minHeight: windowHeight}">
		<view class="lottery">
			<view class="lottery-header">
				<view style="display: flex; flex-direction: row; align-items: center;">
					幸运大抽奖
					<view class="uni-ml-2">
						<uv-icon size="40rpx" color="uv-color-error" name="reload" @click="handleReloadLottery"></uv-icon>
					</view>
				</view>
			</view>
			<view class="lottery-grid">
				<view v-for="(item, index) in randomPrizeData" :key="'prize-item-'+ item.index" 
					class="lottery-item" :class="[index % 3 === 0 ? 'first' : '', index < 3 ? 'top' : '', 
						selectedPrizeIndex === item.index ? 'selected' : '', item.index === -2 ? 'show' : '']">
					<template v-if="item.index === -2">
						<image class="user-image" mode="aspectFill" :src="user.userInfo?.avatar || defaultAvatar"></image>
						<uv-gap height="20rpx"></uv-gap>
						<uv-text :size="16" color="#000" :text="user.userInfo?.name" align="center"></uv-text>
					</template>
					<template v-else class="prize-item">
						<image :src="item.image" class="prize-image" mode="aspectFill"></image>
						{{item.name}}
					</template>
				</view>
			</view>
			<view class="lottery-bottom">
				<uv-text :text="'还剩抽次数: ' + lotteryChance + '次'" color="white" :bold="true" align="center"></uv-text>
				<uv-gap height="30rpx"></uv-gap>
				<tui-button type="primary" shape="circle" :loading="loading" :prevent-click="true"
					@click="handleStartClick" :disabled="lotteryChance === 0">开始抽奖</tui-button>
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
	import {giveLotteries, saveLotteryRecords} from '@/api/Lottery.js'
	import _ from 'lodash'
	import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { useUserStore } from '../../store/user';
	import { defaultAvatar } from '../../common/data';
	
	
	const user = useUserStore()
	const indexs = [0, 1, 2, 7, -2, 3, 6, 5, 4]
	const prizeData = ref()
	const selectedPrizeIndex = ref(-1)
	const loading = ref(false)
	// ready starting end
	const status = ref("ready")
	const noWinning = ref(false)
	const winModal = ref()
	const winningRecordModal = ref()
	const queryFormData = ref({
		count: 5
	})
	// 中奖的概率 1-100
	const probabilityWinning = ref(0)
	// 抽中的奖品
	const winData = ref()
	// 所有参与抽奖的数据
	const randomPrizeData = computed(() => {
		const result = prizeData.value?.map((item) => ({
					name: item.name,
					image: item.image,
					win: true, 
					index: 0,
					id: item.id,
					type: item.type?.name
				})) || []
		const count = result.length
		for (let i = count; i < 9; i ++) {
			result.push({
				name: '谢谢参与', 
				image: '/static/lottery-default.jpg',
				win: false, 
				index: 0,
				id: 0,
			})
		}
		const rand = _.shuffle(result)		
		for (let i = 0; i < rand.length; i ++) {
			rand[i].index = indexs[i]
		}
		return rand
	})
	// 所有的奖品
	const allWinningData = computed(() => {
		return randomPrizeData.value?.filter(item => Boolean(item.win))
	})
	// 谢谢参与的商品
	const allSubstituteData = computed(() => {
		return randomPrizeData.value?.filter(item => !Boolean(item.win))
	})
	// 抽奖机会
	const lotteryChance = computed(() => user.userInfo?.lotteryChance || 0)
	
	
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	
	onLoad(async() => {
	    await user.userDetail({userId: user.userInfo.id})
		await loadPrizeData()
	})
	
	async function handleReloadLottery() {
		await loadPrizeData()
	}
	
	function handleMyWinClick() {
		winningRecordModal.value?.open()
	}
	
	async function handleStartClick() {
		if (status.value === 'ready') {
			probabilityWinning.value = _.random(20, 50, false)
			selectedPrizeIndex.value = 0
			status.value = "start"
			const result = await startLottery()
			winData.value = result.winData
			await getYourPrize()
			if (winData.value?.win) {
				winModal.value?.open(winData.value)
			} else {
				noWinning.value = true
			}
			status.value = 'ready'
		}
	}
	
	function startLottery() {
		return new Promise(async (reslove) => {
			loading.value = true
			let index = 0
			let {winIndex, randomIndex} = await randomWinIndex()
			const timer = setInterval(() => {
				index ++
				if (selectedPrizeIndex.value + 2 === randomPrizeData.value.length) {
					selectedPrizeIndex.value = 0
				} else {
					selectedPrizeIndex.value ++
				}
				if (index === randomIndex) {
					clearInterval(timer)
					reslove({winData: randomPrizeData.value.find(item => item.index === selectedPrizeIndex.value), index: selectedPrizeIndex.value})
					loading.value = false
					return
				}
			}, 100)
		})
	}
	
	async function randomWinIndex() {
		const probability = _.random(1, 100, false)
		// 本次是否中奖
		const isWinning = probability <= probabilityWinning.value
		let winIndex = 0
		const val = 24
		if (isWinning) {
			winIndex = _.sample(allWinningData.value).index
		} else {
			winIndex = _.sample(allSubstituteData.value).index
		}
		return {winIndex, randomIndex: winIndex + val}
	}
	
	async function loadPrizeData() {
	    uni.showLoading({
	        title: '获取奖品中'
	    });
	    try {
	        const res = await giveLotteries(queryFormData.value)
	        if (res.code === 200) {
	            prizeData.value = res?.data || []
	        } else {
	            uni.showToast({
	                title: res.msg,
	                icon: 'none'
	            });
	        }
	    } catch(err) {
			console.log("获取奖品异常", err)
	        uni.showToast({
	            title: '获取奖品失败',
	            icon: 'none'
	        });
	    } finally {
	        uni.hideLoading();
	    }
	}
	
	async function getYourPrize() {
	    uni.showLoading({
	        title: '领取中'
	    });
	    try {
	        const res = await saveLotteryRecords({
				"lottery.id": winData.value.id
			})
	        if (res.code === 200) {
	            await user.userDetail({userId: user.userInfo.id})
				if (winData.value.type === 'AMOUNT') {
					await user.loadUserBalance()
				}
	        } else {
	            uni.showToast({
	                title: res.msg,
	                icon: 'none'
	            });
	        }
			return res
	    } catch(err) {
			console.log("领取奖品异常", err)
	        uni.showToast({
	            title: '领取奖品失败',
	            icon: 'none'
	        });
	    } finally {
	        uni.hideLoading();
	    }
	}
</script>

<style scoped lang="scss">
	.lottery-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: $uni-color-error;
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
						border: 1px solid $uni-color-error;
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

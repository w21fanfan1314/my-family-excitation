<template>
	<uni-popup type="bottom" background-color="#fff" border-radius="30rpx 30rpx 0 0" ref="popup">
		<view class="winning-records-modal" >
			<text class="title">我的奖品</text>
			<scroll-view :scroll-y="true" class="win-list" :style="{maxHeight}" 
				:refresher-enabled="true"  >
				<tui-list-view >
					<tui-list-cell v-for="(item, index) in winningRecordsData" :key="'winning-record-item-' + index">
						<view class="win-item">
							<image class="image"></image>
							<view class="info">
								<text>奖品</text>
								<text class="time">奖品</text>
							</view>
						</view>
					</tui-list-cell>
					<tui-list-cell>
						<tui-nomore></tui-nomore>
					</tui-list-cell>
				</tui-list-view>
			</scroll-view>
			<view class="close-container">
				<tui-icon name="close-fill" :size="48" unit="rpx"  @click="handleCloseClick"></tui-icon>
			</view>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose, computed } from 'vue';

	const popup = ref()
	const winningRecordsData = ref([{},{},{},{},{},{},{},{},{},{},{},{}])
	const maxHeight = computed(() => Math.floor(uni.getWindowInfo().windowHeight * 0.7) + 'px')
	
	function handleCloseClick() {
		popup.value?.close()
	}
	
	defineExpose({
		open() {
			popup.value?.open()
		}
	})
</script>

<style scoped lang="scss">
	.winning-records-modal {
		display: flex;
		flex-direction: column;
		padding: 30rpx 0;
		
		.title {
			font-size: 1.2em;
			color: $uni-text-color;
			font-weight: bold;
			text-align: center;
		}
		
		.close-container {
			position: absolute;
			top: 30rpx;
			right: 30rpx;
		}
		
		.win-list {
			flex: 1;
			display: flex;
			flex-direction: column;
			margin-top: 30rpx;
			
			.win-item {
				display: flex;
				flex-direction: row;
				
				.image {
					width: 80rpx;
					height: 80rpx;
					border-radius: 10rpx;
				}
				.info {
					display: flex;
					flex-direction: column;
					font-size: 1em;
					color: $uni-text-color;
					margin-left: 20rpx;
					justify-content: space-between;
					height: 80rpx;
					
					.time {
						font-size: 0.8em;
						color: $uni-text-color-grey;
					}
				}
			}
		}
		
	}
</style>
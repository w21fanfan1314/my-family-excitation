<template>
	<uni-popup ref="popup" :is-mask-click="false">
		<view class="settlement-rate-modal">
			<view class="container">
				<uv-text v-if="starCount === 3" type="success" :size="30" text="全部正确" align="center"></uv-text>
				<uv-text v-else type="error" :size="30" text="再接再厉" align="center"></uv-text>
				<view  class="uni-mt-8" style="display: flex; flex-direction: column; align-items: center;">
					<uv-icon v-if="props.transcript.hasAward" name="empty-coupon" color="error" :size="36" 
						:label="'奖励' + level?.currency?.name +': ' + level?.currency?.symbol + level?.award"></uv-icon>
					<view  v-else-if="props.transcript.starCount === 3">
						<uv-icon v-if="level.awardType.name === 'SINGLE'"  name="empty-coupon" color="error" :size="36"
							label="奖励已全部发放完毕"></uv-icon>
						<uv-icon v-else-if="level.awardType.name === 'DAILY'"  name="empty-coupon" color="error" :size="30"
								label="今日奖励已发放完毕,明天再来试试吧!"></uv-icon>
					</view>
				</view>
				<view class="uni-mt-15">
					<tui-button type="primary" @click="onBackClick" :prevent-click="true">继续闯关</tui-button>
					<uv-gap :height="20"></uv-gap>
					<tui-button v-if="props.transcript?.wrongs?.length > 0"  type="danger"
						@click="onCheckWrongClick" :prevent-click="true">查看错题</tui-button>
				</view>
			</view>
			<view class="rate">
				<uv-rate :count="3" readonly :size="80" :gutter="12" :value="starCount" active-color="warning" inactive-icon="star-fill" ></uv-rate>
			</view>
		</view>
	</uni-popup>
</template>

<script setup>
	import { computed, ref, defineProps, defineExpose } from 'vue';
	
	const props = defineProps({
		transcript: {
			type: Object,
			default: {
			}
		}
	})
	const popup = ref()
	const starCount = computed(() => props.transcript?.starCount || 0)
	const level = computed(() => props.transcript?.level)
	
	function onBackClick() {
		uni.navigateBack()
	}
	
	function onCheckWrongClick() {
		uni.navigateTo({
			url: '/pages/train/wrongs?transcriptId=' + props.transcript.id
		})
	}
	
	defineExpose({
		open() {
			popup.value?.open()
		}
	})
</script>

<style scoped lang="scss">
	.settlement-rate-modal {
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.container {
			box-sizing: border-box;
			padding: 60rpx 30rpx 30rpx 30rpx;
			background-color: $uni-bg-color;
			border-radius: 10px;
			display: flex;
			flex-direction: column;
			margin-top: 50px;
			width: 500rpx;
		}
		
		.rate {
			position: absolute;
			top: 0;
		}
	}
</style>
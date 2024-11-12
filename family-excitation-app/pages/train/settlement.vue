<template>
	<view class="train-settlement">
		<view class="header">
			<uv-rate :count="3" readonly size="36" :gutter="12" :value="starCount"></uv-rate>
			<view class="uni-mt-15">
				<uv-text v-if="starCount === 3" type="success" :size="30" text="全部正确"></uv-text>
				<uv-text v-else type="error" :size="30" text="再接再厉"></uv-text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import { computed, ref } from 'vue';
	import { useUserStore } from '@/store/user';
	import { defaultAvatar } from '../../common/data';
	const user = useUserStore()
	const settlementData = ref({
		score: 0
	})
	const avatar = computed(() => user.userInfo?.avatar || defaultAvatar )
	const starCount = computed(() => {
		const score = settlementData.value.score
		if (score == 0) {
			return 0
		}
		if (score > 0 && score <= 30) {
			return 1
		}
		
		if (score > 30 && score >= 90) {
			return 2
		} 
		
		return 3
	})
	
	onLoad(async (options) => {
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		eventChannel.on('settlementData',  ({settlement}) => {
			settlementData.value = settlement
		})
	})
</script>

<style scoped lang="scss">
	.train-settlement {
		display: flex;
		flex-direction: column;
		
		.header {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 60rpx 30rpx;
		}
	}
</style>

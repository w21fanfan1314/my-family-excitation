<template>
	<uv-popup ref="popup" bg-color="#ffffff" close-icon-pos="top-right" 
		round="20rpx" :closeable="true" :close-on-click-overlay="false">
		<view class="pay-modal">
			<view style="display: flex; flex-direction: column; align-items: center;">
				<text class="title">您需要支付</text>
				<text class="pay-amount">{{props.amount}}</text>
			</view>
			
			<view class="uni-mt-15" >
				<uni-easyinput :focus="true" :style="{fontSize: '30rpx'}" type="text" v-model="payPassword" placeholder="请输入密码"></uni-easyinput>
			</view>
			<view class="uni-mt-10">
				<tui-button shape="circle" type="primary" @click="checkPay">立即支付</tui-button>
			</view>
		</view>
	</uv-popup>
</template>

<script setup>
	import { ref, defineExpose, defineProps, defineEmits } from 'vue';
	import { useUserStore } from '../../../store/user';
	import { checkPayPassword } from '../../../api/Order';
	
	const props = defineProps({
		amount: {
			type: String,
			default: ''
		}
	})
	const emit = defineEmits(['correct'])
	const user = useUserStore()
	const popup = ref()
	const payPassword = ref('')
	
	function close() {
		popup.value?.close()
	}
	
	async function checkPay() {
		uni.showLoading({
			title: '验证中...'
		})
		try {
			const res = await checkPayPassword({"user.id": user.userInfo.id, password: payPassword.value})
			if (res.code === 200) {
				emit("correct")
				close()
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '支付密码验证错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
	
	defineExpose({
		open() {
			popup.value?.open()
		}
	})
</script>

<style scoped lang="scss">
	.pay-modal {
		display: flex;
		flex-direction: column;
		min-width: 550rpx;
		max-width: 700rpx;
		padding: 30rpx;
		box-sizing: border-box;
		
		.title {
			font-size: 28rpx;
			color: $uni-text-color-grey;
		}
		
		.pay-amount {
			margin-top: 12rpx;
			font-size: 40rpx;
			font-weight: bold;
			color: $uni-color-error;
		}
	}
</style>
<template>
	<uni-popup border-radius="20rpx" type="center" ref="popup" :is-mask-click="false" background-color="#ffffff">
		<view class="update-pwd-modal">
			<view class="header">
				<text class="title">修改密码</text>
				<uni-icons type="clear" :size="30" @click="close"></uni-icons>
			</view>
			<uni-forms class="uni-mt-10" v-model="formData">
				<uni-forms-item label="原密码">
					<uni-easyinput type="text" v-model="formData.password"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="新密码">
					<uni-easyinput type="text" v-model="formData.newPassword" ></uni-easyinput>
				</uni-forms-item>
				<view class="uni-mt-10">
					<tui-button type="danger" @click="onPasswordChange">确认修改</tui-button>
				</view>
			</uni-forms>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose } from 'vue';
	import { useUserStore } from '../../../store/user';
	
	const user = useUserStore()
	const popup = ref()
	const formData = ref({
		password: '',
		newPassword: '',
		userId: ''
	})
	
	function close() {
		popup.value?.close()
	}
	
	async function onPasswordChange() {
		uni.showLoading({
			title: '修改中...'
		})
		try {
			const res = await user.userUpdatePwd(formData.value)
			if (res.code === 200) {
				uni.showToast({
					icon: 'success',
					title: '修改成功'
				})
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
				title: '修改密码错误'
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
	.update-pwd-modal {
		width: 500rpx;
		padding: 30rpx;
		display: flex;
		flex-direction: column;
		
		.header {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			
			.title {
				flex: 1;
				color: $uni-text-color;
				font-size: 1.2em;
				font-weight: bold;
			}
		}
	}
</style>
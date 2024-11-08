<template>
	<uni-popup border-radius="20rpx" type="center" ref="popup" :is-mask-click="false" background-color="#ffffff">
		<view class="update-pwd-modal">
			<view class="header">
				<text class="title">修改密码</text>
				<uni-icons type="clear" :size="30" @click="close"></uni-icons>
			</view>
			<uni-forms class="uni-mt-10">
				<uni-forms-item label="原密码">
					<uni-easyinput type="text"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="新密码">
					<uni-easyinput type="text"></uni-easyinput>
				</uni-forms-item>
				<view class="uni-mt-10">
					<tui-button type="danger">确认修改</tui-button>
				</view>
			</uni-forms>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose } from 'vue';
	const popup = ref()
	
	function close() {
		popup.value?.close()
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
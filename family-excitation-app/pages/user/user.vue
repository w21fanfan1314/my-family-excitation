<template>
	<view class="user-container">
		<tui-list-view >
			<tui-list-cell :arrow="true">
				<view class="user-info">
					<uv-avatar :src="avatar" size="60" @click="onAvatarClick"></uv-avatar>
					<view class="info"  @click="onEdit">
						<text>{{userInfo?.name}}<text class="user-id">(@{{userInfo?.userName}})</text></text>
						<view class="uni-mt-4" style="display: flex; flex-direction: row;">
							<uv-icon v-if="userInfo?.gender?.name === 'MALE'" color="primary" name="man" size="20px" label="男"></uv-icon>
							<uv-icon v-else-if="userInfo?.gender?.name === 'FEMALE'" color="error" name="woman" size="20px" label="女"></uv-icon>
							<uv-icon v-if="age" class="uni-ml-10" color="warning" name="level" size="20px" :label="age"></uv-icon>
						</view>
					</view>
				</view>
			</tui-list-cell>
			<tui-list-cell :arrow="true" @click="onGoOrder">
				<uv-icon name="order" color="primary" size="20px" label="我的订单"></uv-icon>
			</tui-list-cell>
			<tui-list-cell :arrow="true" @click="onUpdatePwdClick">
				<uv-icon name="setting-fill" color="primary" size="20px" label="设置密码"></uv-icon>
			</tui-list-cell>
			<tui-list-cell :arrow="true" @click="onQuit">
				<uv-icon name="close-circle-fill" color="error" size="20px" label="退出登录"></uv-icon>
			</tui-list-cell>
		</tui-list-view>
	</view>
	<update-pwd-modal ref="updatePwdModal"></update-pwd-modal>
</template>

<script setup>
	import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
	import { useUserStore } from '../../store/user';
	import {storeToRefs} from 'pinia'
	import { computed, ref } from 'vue';
	import { defaultAvatar } from '../../common/data';
	import moment from 'moment'
	import UpdatePwdModal from './components/update-pwd-modal.vue';
	
	const user = useUserStore()
	const {userInfo} = storeToRefs(user)
	const updatePwdModal = ref()
	const avatar = computed(() => userInfo.value?.avatar || defaultAvatar)
	const age = computed(() => {
		if (userInfo.value?.birthday) {
			const birthday =  moment.utc(userInfo.value.birthday)
			const today = moment()
			return today.diff(birthday, 'years') + '岁'
		}
		return null
	})
	
	onLoad(async () => {
		await loadData()
	})
	
	onPullDownRefresh(async () => {
		try {
			await loadData(false)
		} finally {
			uni.stopPullDownRefresh()
		}
	})
	
	function onUpdatePwdClick() {
		updatePwdModal.value?.open()
	}
	
	function onEdit() {
		uni.navigateTo({
			url: '/pages/user/edit'
		})
	}
	
	function onAvatarClick() {
		uni.previewImage({
			urls: [avatar.value]
		})
	}
	
	function onGoOrder() {
		uni.navigateTo({
			url: '/pages/order/order'
		})
	}
	
	async function loadData(showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '加载中...'
			})
		}
		try {
			const res = await user.userDetail({userId: user.userInfo.id})
			if (!(res?.code === 200)) {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '加载用户数据错误'
			})
			console.log('加载用户数据错误', err)
		} finally {
			if (showLoading) {
				uni.hideLoading()
			}
		}
	}
	
	async function onQuit() {
		uni.showModal({
			title: '提示',
			content: '是否退出登录',
			confirmText: '退出登录',
			showCancel:true,
			async success(res) {
				if (res.confirm) {
					uni.showLoading({
						title: '退出中...'
					})
					try {
						const res = await user.userQuit()
						if (res.code === 200) {
							uni.redirectTo({
								url: '/pages/login/login'
							})
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
					} catch(err) {
						uni.showToast({
							icon: 'none',
							title: '退出登录错误'
						})
					} finally {
						uni.hideLoading()
					}
				}
			}
		})
	}
	
</script>

<style scoped lang="scss">
	.user-container {
		display: flex;
		flex-direction: column;
		
		.user-info {
			display: flex;
			flex-direction: row;
			
			.info {
				flex: 1;
				display: flex;
				flex-direction: column;
				font-size: 1em;
				color: $uni-text-color;
				margin-left: 20rpx;
				justify-content: center;
				
				.user-id {
					font-size: 0.8em;
					color: $uni-text-color-grey;
					margin-left: 10rpx;
				}
			}
		}
	}
</style>

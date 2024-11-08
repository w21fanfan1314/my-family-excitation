<template>
	<view class="edit-container">
		<uni-section title="用户信息" type="line">
			<tui-wing-blank :gap="30" :margin-top="0">
				<uni-forms v-model="formData">
					<uni-forms-item label="头像">
						<uv-upload name="avatar" accept="image" max-count="1" @afterRead="onAfterRead" :file-list="fileList" @delete="onDelete"></uv-upload>
					</uni-forms-item>
					<uni-forms-item label="名字">
						<uni-easyinput type="text" v-model="formData.name" :placeholder="userInfo?.name"></uni-easyinput>
					</uni-forms-item>
					<uni-forms-item label="性别">
						<uni-data-select v-model="formData.gender" :localdata="genderData" :clear="false"></uni-data-select>
					</uni-forms-item>
					<uni-forms-item label="生日">
						<uni-datetime-picker type="date" :value="formData.birthday"  return-type="timestamp" @change="onBirthdayChange"></uni-datetime-picker>
					</uni-forms-item>
					<view class="uni-mt-10">
						<tui-button type="primary" @click="saveUserInfo">保存修改</tui-button>
					</view>
				</uni-forms>
			</tui-wing-blank>
		</uni-section>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { useUserStore } from '../../store/user';
	import { onLoad } from '@dcloudio/uni-app'
	import {storeToRefs} from 'pinia'
	import { uploadFile } from '../../api/Request';
	import moment from 'moment';
	
	const user = useUserStore()
	const fileList = ref([])
	const {userInfo} = storeToRefs(user)
	const formData = ref({
		name: '',
		gender: '',
		birthday: 0,
		avatar: ''
	})
	const genderData = computed(() => {
		return [
			{ value: 'MALE', text: '男'},
			{ value: 'FEMALE', text: '女'},
		]
	})
	
	onLoad(() => {
		if (userInfo.value?.avatar) {
			fileList.value = [
				{
					url: userInfo.value?.avatar
				}
			]
		}
		if (userInfo.value?.gender) {
			formData.value.gender = userInfo.value.gender.name
		}
		
		if (userInfo.value?.birthday) {
			formData.value.birthday = moment.utc(userInfo.value.birthday).valueOf()
		}
	})
	
	function onBirthdayChange(time) {
		formData.value.birthday = time
	}
	
	function onDelete({file, index, name}) {
		fileList.value = []
		formData.value.avatar = ''
	}
	
	async function onAfterRead({file, list, name}) {
		if (!fileList.value || fileList.value.length === 0) {
			fileList.value = [
				{
					url: '',
					status: 'uploading',
					message: ''
				}
			]
		}
		fileList.value[0].url = file.thumb
		uni.showLoading({
			title: '头像上传中'
		})
		try {
			const res = await uploadFile({url: file.url})
			if (res.code === 200) {
				fileList.value[0].status = 'success'
				formData.value.avatar = res.data.url
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
				fileList.value[0].status = 'failed'
				fileList.value[0].message = res.msg
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '上传头像失败'
			})
			console.log('上传头像失败', err)
			fileList.value[0].status = 'failed'
			fileList.value[0].message = '上传错误'
		} finally {
			uni.hideLoading()
		}
	}
	
	async function saveUserInfo() {
		uni.showLoading({
			title: '修改中...'
		})
		try {
			const res = await user.userUpdate({userId: user.userInfo.id, ...formData.value})
			if (res?.code === 200) {
				uni.showToast({
					icon: 'success',
					title: '修改成功'
				})
				uni.navigateBack()
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '修改用户数据错误'
			})
			console.log('修改用户数据错误', err)
		} finally {
			uni.hideLoading()
		}
	}
</script>

<style scoped lang="scss">
	.edit-container {
		display: flex;
		flex-direction: column;
	}
</style>

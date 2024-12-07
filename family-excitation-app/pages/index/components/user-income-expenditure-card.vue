<template>
	<view class="user-income-expenditure-card">
		<uni-section :loading="loading" title="最近7天收支" type="line">
			<swiper :indicator-dots="false" :autoplay="true" >
				<swiper-item>
					<uni-card  title="人民币">
						<uv-row :gutter="12">
							<uv-col :span="6">
								<view class="item">
									<uv-icon label="收入" :size="20" name="arrow-down-fill" color="error"></uv-icon>
									<uv-gap :height="10"></uv-gap>
									<uv-text align="center" :bold="true" :size="25" text="0" type="error"></uv-text>
								</view>
							</uv-col>
							<uv-col :span="6">
								<view class="item">
									<uv-icon label="支出" :size="20" name="arrow-up-fill" color="success"></uv-icon>
									<uv-gap :height="10"></uv-gap>
									<uv-text align="center" :bold="true" :size="25" text="0" type="success"></uv-text>
								</view>
							</uv-col>
						</uv-row>
					</uni-card>
				</swiper-item>
			</swiper>
		</uni-section>
	</view>
</template>

<script setup>
	import { computed, onMounted, ref } from 'vue';
	import { useUserStore } from '../../../store/user';
	import moment from 'moment';
	import { userRecords } from '../../../api/UserApi';
	
	const user = useUserStore()
	const fromData = ref({
		beginTime: 0,
		endTime: 0,
		withPage: false,
		"user.id": 0
	})
	const loading = ref(false)
	const userRecordRes = ref()
	const userRecordData = computed(() => userRecordRes.value?.data)
	const income = computed(() => {
		
	})
	
	onMounted(async () => {
		fromData.value['user.id'] = user.userInfo?.id
		fromData.value.beginTime = moment().subtract(7, 'days').hours(0).minutes(0).seconds(0).milliseconds()
		fromData.value.endTime = moment().hours(23).minutes(59).seconds(59).milliseconds()
		await loadData()
	})
	
	async function loadData() {
		try {
			loading.value = true
			const res = await userRecords(formData.value)
			userRecordRes.value = res
			return res
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '修改试卷数据错误'
			})
		} finally {
			loading.value = false
		}
	}
</script>

<style scoped lang="scss">
	.user-income-expenditure-card {
		display: flex;
		flex-direction: column;
		
		.item {
			display: flex;
			flex-direction: column;
			align-items: center;
		}
	}
</style>
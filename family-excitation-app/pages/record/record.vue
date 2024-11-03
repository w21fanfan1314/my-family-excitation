<template>
	<tui-no-data v-if="recordsData.records?.length === 0"></tui-no-data>
	<uni-list v-else :border="false">
		<uni-list-item v-for="item in recordsData.records" :key="'user-record-' + item.id"
			:title="'['+recordTypeToString(item.recordType.name) +'] '+item.currency.name+': ' + item.currency.symbol + item.amount"
			:note="item.content" :right-text="moment(item.dateCreated).format('yyyy-MM-DD HH:mm')">
		</uni-list-item>
		<uni-list-item v-if="recordsData.records?.length > 0">
			<uni-load-more :status="loadMoreStatus"></uni-load-more>
		</uni-list-item>
	</uni-list>
</template>

<script setup>
	import {
		onReady,
		onPullDownRefresh,
		onLoad,
		onReachBottom
	} from '@dcloudio/uni-app'
	import {
		userRecords
	} from '../../api/UserApi';
	import {
		computed,
		ref
	} from 'vue';
	import moment from 'moment'

	const formData = ref({
		userId: 0,
		page: 0,
		size: 30
	})
	const recordsData = ref({
		records: [],
		total: 0
	})
	const loadingMore = ref(false)
	const loadMoreStatus = computed(() => loadingMore.value ? 'loading' : isMore.value ? 'more' : 'noMore')
	const isMore = computed(() => recordsData.records?.length < recordsData.value.total)

	onLoad(async (options) => {
		// console.log(options)
		formData.value.userId = options.userId || 0
		if (formData.value.userId) {
			await loadData()
		}
	})

	onPullDownRefresh(async () => {
		try {
			await loadData(0, false)
		} finally {
			uni.stopPullDownRefresh()
		}
	})

	onReachBottom(async () => {
		if (isMore.value) {
			try {
				loadingMore.value = true
				await loadData(formData.value.page + 1, true)
			} finally {
				loadingMore.value = false
			}
		}
	})

	function recordTypeToString(recordType) {
		switch (recordType) {
			case 'CONSUME':
				return "消费"
			case 'AWARD':
				return "奖励"
			case 'RECHARGE':
				return "充值"
			case 'PUNISH':
				return "处罚"
			case 'TRANSFER':
				return "转入"
			case 'TRANSFER_OUT':
				return "转出"
			default:
				return "未知"
		}
	}

	async function loadData(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '加载中'
			})
		}
		try {
			const res = await userRecords({
				...formData.value,
				page
			})
			if (res.code === 200) {
				if (page === 0) {
					recordsData.value = res.data
				} else {
					recordsData.value = {
						...res.data,
						records: [...recordsData.value.records, ...res.data.records]
					}
				}
				formData.value.page = page
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch (err) {
			uni.showToast({
				icon: 'none',
				title: '查询存折数据错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
</script>

<style lang="scss" scoped>

</style>
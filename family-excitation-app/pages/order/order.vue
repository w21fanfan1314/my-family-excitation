<template>
	<view class="order-container">
		<tui-tabs :current-tab="selectedTab" :tabs="tabsData" @change="onTabChange"></tui-tabs>
		<tui-no-data v-if="listOrdersData?.orders?.length === 0"></tui-no-data>
		<tui-list-view v-else>
			<tui-list-cell v-if="commoditiesData?.commodities?.length > 0" :hover="false">
				<uni-load-more :status="loadMoreStatus" @clickLoadMore="loadMore"></uni-load-more>
			</tui-list-cell>
		</tui-list-view>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { listOrders } from '../../api/Order';
	import { onReady, onPageScroll, onLoad, onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
	
	const tabsData = ref([
		{ name: '全部', value: 'ALL'},
		{ name: '待发货', value: 'PAID'},
		{ name: '待收货', value: 'SHIPPED'},
		{ name: '已收货', value: 'COMPLETED'},
		{ name: '已取消', value: 'CANCELLED'}
	])
	const selectedTab = ref(0)
	const formData = ref({
		status: '',
		page: 0,
		size: 30
	})
	const listOrdersData = ref({
		orders: [],
		total: 0
	})
	const loadingMoreData = ref(false)
	const loadMoreStatus = computed(() => loadingMoreData.value ? 'loading' : isMore.value ? 'more' : 'noMore')
	const isMore = computed(() => listOrdersData.orders?.length < listOrdersData.value.total)
	
	onLoad( async (options) => {
		await loadOrders()
	})
	
	onPullDownRefresh(async () => {
		try {
			await loadOrders(0, false)
		} finally {
			uni.stopPullDownRefresh()
		}
	})
	
	onReachBottom(async () => {
		await loadMore()
	})
	
	async function loadMore() {
		if (!isMore.value) {
			return;
		}
		loadingMoreData.value = true
		try {
			await loadOrders(formData.value.page + 1, false)
		} finally {
			loadingMoreData.value = false
		}
	}
	
	async function onTabChange(e) {
		selectedTab.value = e.index
		formData.value.status = (e.index === 0 ? '' : e.item.value)
		await loadOrders()
	}
	
	
	async function loadOrders(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await listOrders({...formData.value, page})
			if (res.code === 200) {
				if (page === 0) {
					listOrdersData.value = res.data
				} else {
					listOrdersData.value = {
						...res.data,
						orders: [...listOrdersData.value.orders, ...res.data.orders]
					}
				}
				
				formData.value.page = page
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询订单列表错误'
			})
		} finally {
			if (showLoading) {
				uni.hideLoading()
			}
		}
	}
</script>

<style scoped lang="scss">
	.order-container {
		display: flex;
		flex-direction: column;
	}
</style>

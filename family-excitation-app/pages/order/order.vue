<template>
	<view class="order-container">
		<tui-tabs :current-tab="selectedTab" :tabs="tabsData" @change="onTabChange"></tui-tabs>
		<tui-no-data v-if="listOrdersData?.orders?.length === 0"></tui-no-data>
		<tui-list-view v-else>
			<tui-list-cell v-for="item in listOrdersData?.orders" :key="'order-item-' + item.orderNo">
				<view class="order-item">
					<view class="header">
						<text>{{moment(item.dateCreated).format('yyyy-MM-DD HH:mm:ss') }}</text>
						<text class="status">{{statusToDesc(item.status.name)}}</text>
					</view>
					<view class="commidty" v-for="commodity in item.items" :key="'order-commodity-' + commodity.id">
						<image class="image" :src="commodity.imageUrl || defaultCommodity" mode="aspectFill"></image>
						<view style="display: flex; flex-direction: column; flex: 1;">
							<text>{{commodity.name}}</text>
							<view class="info-item">
								<text class="label">数量:</text>
								<text>x{{commodity.buyCount}}</text>
							</view>
							<view class="info-item">
								<text class="label">规格:</text>
								<text>{{commodity.specification }}</text>
							</view>
						</view>
					</view>
					<view class="footer">
						<view class="pay-item" v-for="pay in item.payments" :key="'order-pay-' + pay.id">
							<text class="label">{{pay.currency.name}}</text>
							<text class="amount">{{pay.currency.symbol}}{{pay.amount}}</text>
						</view>
						<tui-button style="margin-top: 20rpx;" type="danger" height="80rpx"  >取消订单</tui-button>
					</view>
				</view>
			</tui-list-cell>
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
	import { defaultCommodity } from '../../common/data';
	import moment from 'moment';
	
	const tabsData = ref([
		{ name: '全部', value: 'ALL'},
		{ name: '已付款', value: 'PAID'},
		{ name: '已发货', value: 'SHIPPED'},
		{ name: '已完成', value: 'COMPLETED'},
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
	
	function statusToDesc(status) {
		switch(status) {
			case 'WAIT_PAY':
				return '待支付'
			case 'PAID':
				return '已付款'	
			case 'SHIPPED':
				return '已发货'
			case 'COMPLETED':
				return '已完成'
			case 'CANCELLED':
				return '已取消'
		}
		return ''
	}
	
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
		
		.order-item {
			display: flex;
			flex-direction: column;
			
			.header {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				
				.status {
					font-size: 1em;
					color: $uni-color-error;
				}
			}
			.commidty {
				display: flex;
				flex-direction: row;
				font-size: 1em;
				color: $uni-text-color;
				font-weight: bold;
				margin-top: 16rpx;
				
				.image {
					width: 100rpx;
					height: 100rpx;
					margin-right: 12rpx;
					border-radius: 10rpx;
				}
				
				.info-item {
					display: flex;
					flex-direction: row;
					align-items: center;
					font-size: 1em;
					color: $uni-text-color;
					margin-top: 12rpx;
					
					.label {
						color: $uni-text-color-grey;
						margin-right: 12rpx;
					}
				}
			}
			.footer {
				display: flex;
				flex-direction: column;
				
				.pay-item {
					display: flex;
					flex-direction: row;
					justify-content: flex-end;
					align-items: center;
					font-size: 1em;
					margin-top: 12rpx;
					
					.label {
						color: $uni-text-color-grey;
						margin-right: 8rpx;
						font-size: .8em;
						line-height: 1em;
					}
					
					.amount {
						color: $uni-color-error;
						font-weight: bold;
					}
				}
				
			}
		}
	}
</style>

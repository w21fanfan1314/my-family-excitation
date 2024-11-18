<template>
	<view class="shopping-container">
		<uv-sticky bg-color="#ffffff">
			<tui-tabs :tabs="tabsData" :current-tab="tabSelectedIndex" @change="onTabChange"></tui-tabs>
			<uni-search-bar is-full v-model="queryFormData.searchKey" placeholder="请输入你要的东西"
			 @confirm="onSearch" @cancel="onSearchCancel"></uni-search-bar>
		</uv-sticky>
		<tui-no-data v-if="isEmpty" btn-text="重新加载" @click="onReloadData">暂无商品</tui-no-data>
		<tui-list-view :title="listTitle">
			<tui-list-cell v-for="item in commoditiesData.commodities"
				:key="'commodity-' + item.id" :hover="false" @click="onCellClick">
				<view class="commodity-item">
					<view class="item-image-container">
						<image class="item-img" :src="item.imageUrl || defaultCommodity" mode="aspectFill" @click="onShowCommmodityImage(item.imageUrl)"></image>
						<uni-link v-if="item.referenceUrl" class="uni-mt-4" :href="item.referenceUrl" text="查看详情"></uni-link>
					</view>
					<view class="item-container">
						<text class="name">{{item.name}}</text>
						<view class="spec" v-if="item.specification">
							<tui-tag type="green" padding="8rpx 18rpx" size="24rpx" :plain="true">规格: {{item.specification}}</tui-tag>
						</view>
						<view class="item-price" v-if="!currenciesData">
							<text class="label">人民币</text>¥{{item.price}}
						</view>
						<view class="item-price" v-else v-for="currency in currenciesData" 
						:key="'commodity-' + item.id + '-currency-' + currency.id">
							<text class="label">{{currency.name}}</text>{{currency.symbol}}{{currency.rate * item.price}}
						</view>
						<view class="item-bottom">
							<tui-button type="warning"  btn-size="mini" :size="24" @click="onBuy(item)" :prevent-click="true">立即购买</tui-button>
						</view>
					</view>
				</view>
			</tui-list-cell>
			<tui-list-cell v-if="commoditiesData?.commodities?.length > 0" :hover="false">
				<uni-load-more :status="loadMoreStatus"></uni-load-more>
			</tui-list-cell>
		</tui-list-view>
		<tui-scroll-top :scroll-top="scrollTop"></tui-scroll-top>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { onReady, onPageScroll, onLoad, onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
	import { listCategories, listCommodities, listCurrencies } from '../../api/Commodity';
	import { defaultCommodity } from '../../common/data';
	
	const scrollTop = ref(0)
	const queryFormData = ref({
		page: 0,
		size: 30,
		"category.id": 0,
		searchKey: ''
	})
	const commoditiesData = ref({
		commodities: [],
		total: 0
	})
	const categoriesData = ref([])
	const currenciesData = ref([])
	const tabSelectedIndex = ref(0)
	const loadingMoreData = ref(false)
	const tabsData = computed(() => [
		{ name: "全部" },
		...(categoriesData.value?.map(item => ({
		name: item.name
	})) || [])
	])
	const isEmpty = computed(() => (!commoditiesData.value?.commodities || commoditiesData.value.commodities?.length === 0))
	const listTitle = computed(() => queryFormData.value.searchKey ? '搜索结果' : '')
	const loadMoreStatus = computed(() => loadingMoreData.value ? 'loading' : isMore.value ? 'more' : 'noMore')
	const isMore = computed(() => commoditiesData.commodities?.length < commoditiesData.value.total)
	
	onLoad( async (options) => {
		await Promise.all([loadCategories(), loadCurrencies()])
	})
	
	onReady(async () => {
		await loadCommodities()
	})
	
	onPageScroll(e => {
		scrollTop.value = e.scrollTop
	})
	
	onPullDownRefresh(async () => {
		try {
			await onReloadData(false)
		} finally {
			uni.stopPullDownRefresh()
		}
	})
	onReachBottom(async () => {
		if (!isMore.value) {
			return;
		}
		loadingMoreData.value = true
		try {
			await loadCommodities(queryFormData.value.page + 1, false)
		} finally {
			loadingMoreData.value = false
		}
	})
	
	function onShowCommmodityImage(url) {
		if (!url) {
			return;
		}
		uni.previewImage({
			urls: [url]
		})
	}
	
	async function onBuy(commodity) {
		uni.navigateTo({
			url: '/pages/shopping/statement',
			success(res) {
				res.eventChannel.emit('commodities', {commodities: [commodity], currencies: currenciesData.value})
			}
		})
	}
	
	async function onCellClick() {
		console.log("onCellClick")
	}
	
	async function onSearch() {
		queryFormData.value['category.id'] = ''
		tabSelectedIndex.value = 0
		await onReloadData()
	}
	
	async function onSearchCancel() {
		queryFormData.value.searchKey = ''
		await onReloadData()
	}
	
	async function onReloadData(showLoading = true) {
		await loadCommodities(0, showLoading)
	}
	
	async function onTabChange(e) {
		tabSelectedIndex.value = e.index
		if (e.index === 0) {
			queryFormData.value['category.id'] = ''
		} else {
			queryFormData.value['category.id'] = categoriesData.value[e.index - 1].id
		}
		await onReloadData()
	}
	
	async function loadCommodities(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await listCommodities({...queryFormData.value, page})
			if (res.code === 200) {
				if (page === 0) {
					commoditiesData.value = res.data
				} else {
					commoditiesData.value = {
						...res.data,
						commodities: [...commoditiesData.value.commodities, ...res.data.commodities]
					}
				}
				
				queryFormData.value.page = page
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询商品错误'
			})
		} finally {
			if (showLoading) {
				uni.hideLoading()
			}
		}
	}
	
	async function loadCategories() {
		try {
			const res = await listCategories({})
			if (res.code === 200) {
				categoriesData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询商品分类错误'
			})
		} 
	}
	
	async function loadCurrencies() {
		try {
			const res = await listCurrencies({})
			if (res.code === 200) {
				currenciesData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询货币列表错误'
			})
		} 
	}
	
</script>

<style scoped lang="scss">
	@import 'common/common.scss';
	.shopping-container {
		@include commodity-item;
		
		.item-image-container {
			display: flex;
			flex-direction: column;
			align-items: center;
		}
	}
</style>

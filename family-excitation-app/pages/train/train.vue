<template>
	<view class="train-container">
		<uv-sticky bg-color="#ffffff">
			<uv-tabs :list="tabList" @change="onTabChange"></uv-tabs>
		</uv-sticky>
		<tui-list-view>
			<tui-list-cell v-for="item in trainsData?.trains" :key="'train-item-' + item.id" 
				@click="onItemClick(item)">
				<view class="train-item">
					<uv-image :src="item.image" height="300rpx" width="100%" radius="10"></uv-image>
					<text class="uni-mt-2">{{item.name}}</text>
					<text v-if="item.description" class="desc uni-mt-1">{{item.description}}</text>
					<view class="bottom uni-mt-3">
						<uv-avatar-group></uv-avatar-group>
						<text>发布于：{{moment(item.dateCreated).format('YYYY年MM月DD日')}}</text>
					</view>
				</view>
			</tui-list-cell>
			<tui-list-cell>
				<uni-load-more :status="loadMoreStatus"></uni-load-more>
			</tui-list-cell>
		</tui-list-view>
	</view>
</template>

<script setup>
	import { computed, ref } from 'vue';
	import { onLoad } from '@dcloudio/uni-app'
	import { categories, trains } from '../../api/Train';
	import { useRefreshData } from '../../common/useRefreshData';
	import moment from 'moment'
	
	const trainsData = ref({
		trains: [],
		total: 0
	})
	const formData = ref({
		page: 0,
		size: 20,
		category: ''
	})
	const categoriesData = ref([])
	const isMore = computed(() => trainsData.value.trains?.length < trainsData.value.total)
	const tabList = computed(() => [{name: '全部'}, ...(categoriesData.value?.map(item => ({ name: item })) || [])])
	const {loadMoreStatus, loadMore} = useRefreshData(loadTrains, isMore, formData)
	
	onLoad(async () => {
		await loadTrainCategories()
		await loadTrains()
	})
	
	async function onTabChange({index, name}) {
		formData.value.category = (index === 0 ? '' : name)
		await loadTrains()
	}
	
	function onItemClick(item) {
		uni.navigateTo({
			url: '/pages/train/levels?trainId='+ item.id,
			success(res) {
				res.eventChannel.emit('trainData', {train: item})
			}
		})
	}
	
	async function loadTrainCategories(showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await categories()
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
				title: '查询闯关分类列表错误'
			})
		} finally {
			if (showLoading) {
				uni.hideLoading()
			}
		}
	}
	
	async function loadTrains(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await trains(formData.value)
			if (res.code === 200) {
				if (page === 0) {
					trainsData.value = res.data
				} else {
					trainsData.value = {
						trains: [...trainsData.value.trains, ...res.data.trains],
						total: res.data.total
					}
				}
				formData.value.page = page;
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询闯关列表错误'
			})
		} finally {
			if (showLoading) {
				uni.hideLoading()
			}
		}
	}

</script>

<style scoped lang="scss">
	.train-container {
		display: flex;
		flex-direction: column;
		
		.train-item {
			display: flex;
			flex-direction: column;
			width: 100%;
			
			font-size: 1.2em;
			color: $uni-text-color;
			
			.desc {
				font-size: 26rpx;
				color: $uni-text-color-grey;
			}
			
			.bottom {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				font-size: 26rpx;
				color: $uni-text-color-grey;
			}
		}
	}
</style>

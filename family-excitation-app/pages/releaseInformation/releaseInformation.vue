<template>
	<view class="release-information-container">
		<uv-sticky bg-color="#ffffff">
			<uv-tabs :list="categories" @change="handleTabChange"></uv-tabs>
		</uv-sticky>
		<tui-list-view v-if="listData.list?.length > 0">
			<tui-list-cell v-for="item in listData.list" :key="'release-information-item-' + item.id" @click="onItemClick(item)" >
				<view class="media-item">
					<view class="thumbnail-container">
						<uv-image :src="item.thumbnail?.url" width="100%" height="100%" :radius="6"></uv-image>
						<view class="play-btn">
							<uv-icon name="play-circle-fill" color="primary" :size="80"></uv-icon>
						</view>
					</view>
					<!-- <view v-for="media in item.mediaDataList" :key="'media-item-'+ media.id">
					
					</view> -->
					<text class="uni-mt-4">
						<tui-tag type="green" padding="10rpx 20rpx" size="26rpx" shape="circleLeft" v-if="item.category">{{item.category}}</tui-tag>
						{{item.title}}
					</text>
					<view class="info uni-mt-4">
						<text class="text">发布人: {{item.user.name}}</text>
						<uv-line direction="col" length="30rpx" margin="0 30rpx"></uv-line>
						<text class="text">{{moment(item.dateCreated).format("YYYY年MM月DD HH:mm")}}</text>
					</view>
				</view>
			</tui-list-cell>
			<tui-list-cell :hover="false">
				<uni-load-more :status="loadMoreStatus" @clickLoadMore="loadMore"></uni-load-more>
			</tui-list-cell>
		</tui-list-view>
		<tui-no-data v-else></tui-no-data>
	</view>
</template>

<script setup>
	import { onReady, onPageScroll, onLoad, onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
	import { listReleaseInformations, releaseInformationCategories } from '../../api/ReleaseInformation';
	import { computed, ref } from 'vue';
	import { useRefreshData } from '../../common/useRefreshData';
	import moment from 'moment'
	
	const categoriesData = ref([])
	const formData = ref({
		category: '',
		title: '',
		page: 0,
		size: 10
	})
	const listData = ref({
		list: [],
		total: 0
	})
	const isMore = computed(() => listData.value.list?.length < listData.value.total)
	const categories = computed(() => [{name: '全部'}, ...(categoriesData.value?.map(item => ({ name: item })) || [])])
	const { loadMoreStatus, loadMore } = useRefreshData(loadData, isMore, formData)
	
	onLoad(async() => {
		await loadCategories()
		await loadData()
	})
	
	async function handleTabChange(e) {
		formData.value.category = e.index == 0 ? '' : categoriesData.value[e.index - 1]
		await loadData()
	}
	
	function onItemClick(item) {
		console.log(item.mediaDataList)
		if (item.mediaDataList?.length > 0) {
			const videoMediaData = item.mediaDataList.find(media => media.type?.name === 'VIDEO')
			if (videoMediaData) {
				uni.navigateTo({
					url: '/pages/releaseInformation/videoPlay',
					success(res) {
						res.eventChannel.emit("mediaData", {data: videoMediaData, thumbnail: item.thumbnail?.url || ''})
					}
				})
			}
		}
	}
	
	
	async function loadData(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await listReleaseInformations({...formData.value, page})
			if (res.code === 200) {
				if (page === 0) {
					listData.value = res.data
				} else {
					listData.value = {
						...res.data,
						list: [...listData.value.list, ...res.data.list]
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
	
	async function loadCategories() {
		uni.showLoading({
			title: '加载中...'
		})
		try {
			const res = await releaseInformationCategories({})
			if (res.code === 200) {
				categoriesData.value = res.data
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '初始化页面数据错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
</script>

<style scoped lang="scss">
	.release-information-container {
		display: flex;
		flex-direction: column;
		
		.media-item {
			display: flex;
			flex-direction: column;
			font-size: 26rpx;
			color: $uni-text-color;
			
			.thumbnail-container {
				$thumbnail-height: 350rpx;
				display: flex;
				flex-direction: column;
				width: 100%;
				height: $thumbnail-height;
				
				.play-btn {
					position: absolute;
					width: 690rpx;
					height: $thumbnail-height;
					display: flex;
					flex-direction: row;
					align-items: center;
					justify-content: center;
					top: 0;
				}
			}
			
			.tags {
				display: flex;
				flex-direction: row;
				flex-wrap:  wrap;
			}
						
			.info {
				display: flex;
				flex-direction: row;
				justify-content: flex-end;
				
				.text {
					font-size: 22rpx;
					color: $uni-text-color-grey;
				}
			}
		}
	}
</style>

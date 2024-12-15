<template>
	<view class="bilibili" >
		<uv-sticky bg-color="#ffffff">
			<uv-tabs :list="categories" @change="handleTabChange"></uv-tabs>
		</uv-sticky>
		<tui-list-view>
			<tui-list-cell v-for="item in videoData.items" :key="'video-item-' + item.id" 
				@click="handleItemClick(item)">
				<view class="video-item">
					<uv-image v-if="item.category === '直播'" width="100%" height="180px" src="https://yx-web-nosdn.netease.im/quickhtml/assets/yunxin/node-website/live/%E5%A8%B1%E4%B9%90%E7%9B%B4%E6%92%AD.png"></uv-image>
					<view v-else style="height: 200px; display: flex; flex-direction: column;">
						<view class="thumbnail" v-html="html(item.url)"></view>
						<view style="position: absolute; top: 0; width: 100%; height: 100%;"></view>
					</view>
					<uv-gap height="20rpx"></uv-gap>
					<uv-text :size="16" :bold="true" color="uni-text-color" :text="item.name"></uv-text>
				</view>
			</tui-list-cell>
			<tui-list-cell >
				<uni-load-more :status="loadMoreStatus"></uni-load-more>
			</tui-list-cell>
		</tui-list-view>
	</view>
</template>

<script setup>
	import { computed, defineComponent, ref } from 'vue';
	import { onLoad, onPullDownRefresh, onReachBottom} from '@dcloudio/uni-app'
	import { useRefreshData } from '../../common/useRefreshData';
	import { cateList, list } from '../../api/VideoItemApi';
	
	const pageStyle = computed(() => ({
		height: uni.getWindowInfo().windowHeight + 'px'
	}))
	const videoData = ref({
		items: [],
		total: 0
	})
	const formData = ref({
		page: 0,
		size: 20,
		category: ''
	})
	const categoriesData = ref([])
	const loadingMoreData = ref(false)
	const isMore = computed(() => videoData.value.items?.length < videoData.value.total)
	const { loadMoreStatus, loadMore } = useRefreshData(loadData, isMore, formData)
	const categories = computed(() => [{name: '全部'}, ...(categoriesData.value?.map(item => ({ name: item })) || [])])
	
	onLoad(async () => {
		await loadCategories()
		await loadData()
	})
	
	function html(url) {
		return url?.replace(/style=".*?"|style=".*?"/, '')
			?.replace("autoplay;", '')
			?.replace('allowfullscreen="true"','')
	}
	
	function handleItemClick(item) {
		uni.navigateTo({
			url: '/pages/videos/detail',
			success(res) {
				res.eventChannel.emit('videoDetail', {detail: item})
			}
		})
	}
	
	async function handleTabChange(e) {
		formData.value.category = e.index == 0 ? '' : categoriesData.value[e.index - 1]
		await loadData()
	}
	
	
	async function loadData(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '加载中...'
			})
		}
		try {
	        const res = await list(formData.value)
			if (res?.code === 200) {
				if (page === 0) {
					videoData.value = res?.data
				} else {
					videoData.value = {
						total: res.data.total,
						items: [...videoData.value.items, ...res.data.items]
					}
				}
				formData.page = page
			} else {
	            uni.showToast({
	                title: res.msg,
	                icon: 'none'
	            });
	        }
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '初始化页面数据错误'
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
				const res = await cateList({})
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
	.bilibili {
		display: flex;
		flex-direction: column;
		
		.video-item {
			display: flex;
			flex-direction: column;
			
			.thumbnail {
				display: flex;
				flex-direction: column;
				overflow: hidden;
				height: 100%;
				
				:first-child {
					flex: 1;
				}
			}
		}
	}
</style>

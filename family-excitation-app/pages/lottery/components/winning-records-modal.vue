<template>
	<uni-popup type="bottom" background-color="#fff" border-radius="30rpx 30rpx 0 0" ref="popup">
		<view class="winning-records-modal" >
			<text class="title">我的奖品</text>
			<scroll-view :scroll-y="true" class="win-list" :style="{height: maxHeight}" 
				:refresher-enabled="true"  @refresherrefresh="handleRefresh" @scrolltolower="handleMore" :refresher-triggered="loading">
				<tui-list-view >
					<tui-list-cell v-for="(item, index) in winningRecordsData.records" :key="'winning-record-item-' + index">
						<view class="win-item">
							<view style="display: flex; flex-direction: column;">
								<image class="image" :src="item.lottery.image"></image>
								<uv-gap height="10rpx"></uv-gap>
								<tui-tag v-if="item.exchanged" type="green" padding="2px 5px">已兑换</tui-tag>
								<tui-tag v-else type="danger"  padding="2px 5px">未兑换</tui-tag>
							</view>
							<view class="info">
								<view>
									<uv-text :size="16" :text="item.lottery.name" :bold="true" color="uni-text-color"></uv-text>
								</view>
								<view>
									<uv-text :size="14" :text="moment(item.lottery.dateCreated).format('YYYY-MM-DD HH:mm:ss')"
										color="uni-text-color-grey" ></uv-text>
								</view>
							</view>
						</view>
					</tui-list-cell>
					<tui-list-cell v-if="winningRecordsData.records?.length > formData.size">
						<uni-load-more :status="loadMoreStatus"></uni-load-more>
					</tui-list-cell>
				</tui-list-view>
			</scroll-view>
			<view class="close-container">
				<tui-icon name="close-fill" :size="48" unit="rpx"  @click="handleCloseClick"></tui-icon>
			</view>
		</view>
	</uni-popup>
</template>

<script setup>
	import { ref, defineExpose, computed, onMounted } from 'vue';
	import { useRefreshData } from '../../../common/useRefreshData';
	import { queryLotteryRecords } from '../../../api/Lottery';
	import { useUserStore } from '../../../store/user';
	import moment from 'moment';

	const user = useUserStore()
	const popup = ref()
	const winningRecordsData = ref({
		records: [],
		total: 0
	})
	const loadingMoreData = ref(false)
	const loading = ref(false)
	const scrollView = ref()
	const maxHeight = computed(() => Math.floor(uni.getWindowInfo().windowHeight * 0.7) + 'px')
	const formData = ref({
		page: 0,
		size: 20,
		"user.id": 0
	})
	const isMore = computed(() => winningRecordsData.value.records?.length < winningRecordsData.value.total)
	const loadMoreStatus = computed(() => loadingMoreData.value ? 'loading' : isMore.value ? 'more' : 'noMore')
	
	onMounted(async () => {
	})
	
	async function handleRefresh() {
		try {
			loading.value = true
			await loadLotteryRecords(0, false)
		} finally {
			loading.value = false
		}
	}
	
	async function handleMore() {
		if (!isMore.value) {
			return 
		}
		loadingMoreData.value = true
		try {
			await loadLotteryRecords(formData.value.page + 1, false)
		} finally {
			loadingMoreData.value = false
		}
	}
	
	function handleCloseClick() {
		popup.value?.close()
	}
	
	async function loadLotteryRecords(page = 0, showLoading = true) {
	    if (showLoading) {
			uni.showLoading({
			    title: '获取奖品中'
			});
		}
	    try {
			formData.value['user.id'] = user.userInfo?.id
	        const res = await queryLotteryRecords(formData.value)
	        if (res.code === 200) {
				if (page === 0) {
					winningRecordsData.value = res?.data
				} else {
					winningRecordsData.value = {
						total: res.data.total,
						records: [...winningRecordsData.value.records, ...res.data.records]
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
			console.log("获取奖品列表异常", err)
	        uni.showToast({
	            title: '获取奖品列表失败',
	            icon: 'none'
	        });
	    } finally {
	        if (showLoading) {
				uni.hideLoading();
			}
	    }
	}
	
	defineExpose({
		async open() {
			popup.value?.open()
			await loadLotteryRecords(0, false)
		}
	})
</script>

<style scoped lang="scss">
	.winning-records-modal {
		display: flex;
		flex-direction: column;
		padding: 30rpx 0;
		
		.title {
			font-size: 1.2em;
			color: $uni-text-color;
			font-weight: bold;
			text-align: center;
		}
		
		.close-container {
			position: absolute;
			top: 30rpx;
			right: 30rpx;
		}
		
		.win-list {
			display: flex;
			flex-direction: column;
			margin-top: 30rpx;
			
			.win-item {
				display: flex;
				flex-direction: row;
				
				.image {
					width: 100rpx;
					height: 100rpx;
					border-radius: 10rpx;
				}
				.info {
					display: flex;
					flex-direction: column;
					font-size: 1em;
					color: $uni-text-color;
					margin-left: 20rpx;
					justify-content: space-between;
					flex: 1;
					
					.time {
						font-size: 0.8em;
						color: $uni-text-color-grey;
					}
				}
			}
		}
		
	}
</style>
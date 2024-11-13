<template>
	<view class="train-levels">
		<tui-list-view>
			<tui-list-cell  v-for="item in levelsData?.levels" :key="'level-item-' + item.id" :arrow="true" 
				@click="onItemClick(item)">
				<view class="level-item">
					<view class="level-title">
						<text>第{{item.level}}关</text>
						<text class="uni-mt-2 desc">
							共{{item.questionCount}}道题
						</text>
					</view>
					<view class="uni-mr-10">
						<uv-rate :size="24" count="3" active-color="warning" :readonly="true" :touchable="false" :value="item.starCount"></uv-rate>
					</view>
				</view>
			</tui-list-cell>
		</tui-list-view>
		<tui-list-cell>
			<uni-load-more :status="loadMoreStatus"></uni-load-more>
		</tui-list-cell>
	</view>
</template>

<script setup>
	import { computed, getCurrentInstance, ref } from 'vue';
	import { onLoad, onBackPress } from '@dcloudio/uni-app'
	import { levels } from '../../api/Train';
	import { useRefreshData } from '../../common/useRefreshData';
	import moment from 'moment'
	
	const levelsData = ref({
		levels: [],
		total: 0
	})
	const formData = ref({
		page: 0,
		size: 20,
		category: '',
		"train.id": 0
	})
	const trainData = ref()
	const isMore = computed(() => levelsData.value.levels?.length < levelsData.value.total)
	const {loadMoreStatus, loadMore} = useRefreshData(loadLevels, isMore, formData)
	
	onLoad(async (options) => {
		formData.value['train.id'] = options.trainId
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		eventChannel.on('trainData',  ({train}) => {
			trainData.value = train
			uni.setNavigationBarTitle({
				title: train.name
			})
		})
		await loadLevels()
	})
	
	
	
	function onItemClick(item) {
		uni.navigateTo({
			url: '/pages/train/start?levelId='+ item.id,
			success(res) {
				res.eventChannel.emit('levelData', {level: item})
			}
		})
	}
	

	async function loadLevels(page = 0, showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await levels(formData.value)
			if (res.code === 200) {
				if (page === 0) {
					levelsData.value = res.data
				} else {
					levelsData.value = {
						levels: [...levelsData.value.levels, ...res.data.levels],
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
	.train-levels {
		display: flex;
		flex-direction: column;
		
		.level-item {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			.level-title {
				color: $uni-text-color;
				font-size: 1.2em;
				display: flex;
				flex-direction: column;
				flex: 1;
				.desc {
					color: $uni-text-color-grey;
					font-size: 0.9em;
				}
			}
			
		}
	}
</style>

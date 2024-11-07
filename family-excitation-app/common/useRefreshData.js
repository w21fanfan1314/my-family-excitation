import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
import { ref, computed } from 'vue'

export  function useRefreshData(loadData, isMore, formData) {
	const loadingMoreData = ref(false)
	const loadMoreStatus = computed(() => loadingMoreData.value ? 'loading' : isMore.value ? 'more' : 'noMore')
	
	onPullDownRefresh(async () => {
		try {
			await loadData(0, false)
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
			await loadData(formData.value.page + 1, false)
		} finally {
			loadingMoreData.value = false
		}
	}
	
	return {
		loadMoreStatus,
		loadMore
	}
}
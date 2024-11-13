<template>
	<view class="train-settlement">
		<tui-list-view>
			<tui-list-cell v-for="(item, index) in transcriptData?.wrongs" :key="'wrong-item-' + item.id" :hover="false">
				<question-item  :question="item.question" :num="index + 1" :total-count="transcriptData.wrongs.length" :user-answer="item.userSelectedOptions"  :show-correct-answer="true" readOnly></question-item>
			</tui-list-cell>
		</tui-list-view>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	import { useUserStore } from '@/store/user';
	import { defaultAvatar } from '../../common/data';
	import QuestionItem from './components/question-item.vue';
	import { transcriptDetail } from '../../api/Train';
	
	const user = useUserStore()
	const transcriptData = ref({
		score: 0,
		wrongs: []
	})
	const awardModal = ref()
	const transcriptId = ref('')
	const avatar = computed(() => user.userInfo?.avatar || defaultAvatar )
	
	onLoad(async (options) => {
		transcriptId.value = options.transcriptId
		await loadTranscript()
	})
	
	async function loadTranscript() {
		uni.showLoading({
			title: '查询中...'
		})
		try {
			const res = await transcriptDetail({"transcript.id": transcriptId.value})
			if (res.code === 200) {
				transcriptData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询成绩单错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
	
</script>

<style scoped lang="scss">
	.train-settlement {
		display: flex;
		flex-direction: column;
		
		.header {
			
		}
	}
</style>

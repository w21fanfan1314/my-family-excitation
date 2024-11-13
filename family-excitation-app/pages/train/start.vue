<template>
	<view class="train-start">
		<tui-no-data v-if="noQuestionsData">暂无题目</tui-no-data>
		<swiper v-else :indicator-dots="true" :style="{height: windowHeight}" @change="onSwiperChange" :current-item-id="currentSelectedQuestionId">
			<swiper-item v-for="(item, index) in questionsData" :key="'question-item-' + item.id" :item-id="String(item.id)">
				<view class="question-item">
					<question-item :question="item" :num="(index + 1)" :total-count="questionsData?.length" v-model="formData.commitData[index].answer"></question-item>
					<view style="padding: 60rpx 30rpx 0 30rpx;">
						<tui-button v-if="index + 1 < questionsData.length" type="primary" @click="onNextClick(index + 1)" :prevent-click="true">下一题</tui-button>
						<tui-button v-else type="green" :prevent-click="true" @click="onCommit">提交</tui-button>
					</view>
				</view>
			</swiper-item>
		</swiper>
	</view>
	<settlement-rate-modal ref="settlementRateModal" :transcript="transcriptData"></settlement-rate-modal>
</template>

<script setup>
	import { onLoad, onBackPress } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	import { commit, questions } from '../../api/Train';
	import QuestionItem from './components/question-item.vue';
	import SettlementRateModal from './components/settlement-rate-modal.vue';
	
	const levelData = ref()
	const transcriptData = ref()
	const questionsData = ref([])
	const settlementRateModal = ref([])
	const noQuestionsData = ref(false)
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const currentSelectedQuestionId = ref()
	const commitConfirmDialog = ref(false)
	const formData = ref({
		"level.id": 0,
		commitData: []
	})
	
	onLoad(async (options) => {
		formData.value['level.id'] = options.levelId
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		eventChannel.on('levelData',  ({level}) => {
			levelData.value = level
			uni.setNavigationBarTitle({
				title: '第' +level.level+ '关'
			})
		})
		await loadQuestions()
	})
	
	onBackPress((res) => {
		if (res.from === 'backbutton' && questionsData.value?.length > 0) {
			uni.showModal({
				title: '提示',
				content: '正在闯关，是否退出？',
				confirmText: '退出',
				success(e) {
					if (e.confirm) {
						uni.navigateBack()
					}
				}
			})
			return true
		}
	})
	
	function onSwiperChange(e) {
		const {currentItemId} = e.detail
		currentSelectedQuestionId.value = currentItemId
	}
	
	function onNextClick(nextIndex) {
		currentSelectedQuestionId.value = String(questionsData.value[nextIndex].id)
	}
	
	async function onCommit() {
		uni.showModal({
			title: '提示',
			content: '是否提交？',
			confirmText: '提交',
			async success(res) {
				if (res.confirm) {
					await commitData()
				}
			}
		})
	}
	
	async function commitData() {
		uni.showLoading({
			title: '查询中...'
		})
		try {
			const res = await commit(formData.value)
			if (res.code === 200) {
				transcriptData.value = res.data			 
				 if (levelData.value && levelData.value.starCount < res.data.starCount) {
					 levelData.value.starCount = res.data.starCount
				 }
				settlementRateModal.value.open()
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
			uni.hideLoading()
		}
	}
	
	async function loadQuestions(showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await questions({"level.id": formData.value['level.id']})
			if (res.code === 200) {
				questionsData.value = res.data
				noQuestionsData.value = Boolean(!res.data || res.data.length === 0)
				if (!noQuestionsData.value) {
					currentSelectedQuestionId.value = String(res.data[0].id)
					formData.value.commitData = questionsData.value.map(item => ({id: item.id, answer: undefined}))
				} 
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
	.train-start {
		display: flex;
		flex-direction: column;
		
		
	}
</style>

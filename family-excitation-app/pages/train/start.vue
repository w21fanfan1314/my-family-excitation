<template>
	<view class="train-start">
		<tui-no-data v-if="noQuestionsData">暂无题目</tui-no-data>
		<swiper v-else :indicator-dots="true" :style="{height: windowHeight}" @change="onSwiperChange" :current-item-id="currentSelectedQuestionId">
			<swiper-item v-for="(item, index) in questionsData" :key="'question-item-' + item.id" :item-id="String(item.id)">
				<view class="question-item">
					<uni-section type="circle" :title="'[' + (index + 1) +'/'+ questionsData.length +']'+'问题'" :padding="true">
						<view style="display: flex; flex-direction: column;">
							<uv-text :text="item.content" color="#000000" size="1.2em"></uv-text>
							<view v-for="media in item.meidaDataList" :key="'question-' + item.id +'-media-' + media.id"  class="uni-mt-10">
								<uv-image v-if="media.type.name === 'IMAGE'" width="100%" height="350rpx" :radius="6" :src="media.url" mode="aspectFit" @click="onImageClick(item)"></uv-image>
								<video v-if="media.type.name === 'VIDEO'" style="width: 100%; height: 350rpx;" :src="media.url"></video>
							</view>
						</view>
					</uni-section>
					<uni-section v-if="item.type.name === 'SINGLE'" type="line" title="选项" :padding="true">
						<uv-radio-group v-model="formData.commitData[index].answer"  placement="column">
							<uv-radio v-for="opt in item.options" :name="opt.id" :label="opt.option" :custom-style="{marginBottom: '30rpx'}"></uv-radio>
						</uv-radio-group>
					</uni-section>
					<uni-section  v-else-if="item.type.name === 'MULTIPLE'" type="line" title="选项" :padding="true">
						<uv-checkbox-group v-model="formData.commitData[index].answer"  placement="column">
							<uv-checkbox v-for="opt in item.options" :name="opt.id" :label="opt.option" :custom-style="{marginBottom: '30rpx'}"></uv-checkbox>
						</uv-checkbox-group>
					</uni-section>
					<uni-section v-model="item.answer"  v-else-if="item.type.name === 'JUDGE'" type="line" title="判断" :padding="true">
						<uv-radio-group v-model="formData.commitData[index].answer" >
							<uv-radio v-for="opt in item.options" :name="opt.id" :label="opt.option" :custom-style="{marginRight: '30rpx', marginBottom: '60rpx'}"></uv-radio>
						</uv-radio-group>
					</uni-section>
					<uni-section v-else type="line" title="请输入" padding>
						<uv-textarea v-model="formData.commitData[index].answer" :custom-style="{marginBottom: '60rpx'}" placeholder="请输入"></uv-textarea>
					</uni-section>
					<view style="padding: 0 30rpx;">
						<tui-button v-if="index + 1 < questionsData.length" type="primary" @click="onNextClick(index + 1)" :prevent-click="true">下一题</tui-button>
						<tui-button v-else type="green" :prevent-click="true" @click="onCommit">提交</tui-button>
					</view>
				</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	import { commit, questions } from '../../api/Train';
	
	const levelData = ref()
	const questionsData = ref([])
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
	
	function onImageClick(item) {
		const urls = item.meidaDataList.filter(media => media.type.name === 'IMAGE').map(media => media.url)
		uni.previewImage({
			urls,
		})
	}
	
	async function commitData() {
		uni.showLoading({
			title: '查询中...'
		})
		try {
			const res = await commit(formData.value)
			if (res.code === 200) {
				 uni.showToast({
				 	icon: 'success',
					title: '提交成功'
				 })
				 uni.navigateTo({
				 	url: '/pages/train/settlement',
					success(res) {
						res.eventChannel.emit('settlementData', {info: res.data})
					}
				 })
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
		
		.question-item {
			display: flex;
			flex-direction: column;
		}
	}
</style>

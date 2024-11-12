<template>
	<view class="train-start">
		<tui-no-data v-if="noQuestionsData">暂无题目</tui-no-data>
		<swiper v-else :indicator-dots="true" :style="{height: windowHeight}">
			<swiper-item v-for="item in questionsData" :key="'question-item-' + item.id" :item-id="item.id">
				<view class="question-item">
					<uni-section type="circle" title="问题" :padding="true">
						<uv-text :text="item.content" color="#000000" size="1.2em"></uv-text>
					</uni-section>
					<uni-section type="line" title="选项" :padding="true">
						<uv-radio-group v-if="item.type.name === 'SINGLE'" placement="column">
							<uv-radio v-for="opt in item.options" :name="opt.id" :label="opt.option" :custom-style="{marginBottom: '30rpx'}"></uv-radio>
						</uv-radio-group>
					</uni-section>
					<view style="padding: 0 30rpx;">
						<tui-button type="primary">下一题</tui-button>
					</view>
				</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	import { questions } from '../../api/Train';
	
	const levelData = ref()
	const levelId = ref(0)
	const questionsData = ref([])
	const noQuestionsData = ref(false)
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	
	onLoad(async (options) => {
		levelId.value = options.levelId
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
	
	async function loadQuestions(showLoading = true) {
		if (showLoading) {
			uni.showLoading({
				title: '查询中...'
			})
		}
		try {
			const res = await questions({"level.id": levelId.value})
			if (res.code === 200) {
				questionsData.value = res.data
				noQuestionsData.value = Boolean(!res.data || res.data.length === 0)
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

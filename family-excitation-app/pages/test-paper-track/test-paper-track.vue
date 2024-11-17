<template>
	<view class="test-paper-track" :style="{height: windowHeight}">
		<view v-if="status === 'UNSTARTED'">
			<uni-card :title="testPaperTrackData.name">
				<view style="margin-bottom: 30rpx;">
					<uv-text v-if="hasAward" :bold="true" :size="20" :text="award" style="margin-bottom: 30rpx;" type="error" 
						prefix-icon="gift-fill" :icon-style="{fontSize: '30px', color: 'orange'}"></uv-text>
					<uv-text v-else size="20" text="暂无奖励" type="warning"></uv-text>
				</view>
				<uv-text :text="desc"></uv-text>
				<view class="uni-mt-10">
					<tui-button type="primary" shape="circle" @click="onStartTestPaper">开始作答</tui-button>
				</view>
			</uni-card>
		</view>
		<view v-else-if="status === 'ANSWERING'">
			<uni-card :title="testPaperTrackData.name">
				<view style="display: flex; flex-direction: column; align-items: center;">
					<uv-image :src="`/static/test-paper-status/${status}.gif`" width="200" mode="widthFix"></uv-image>
					<uv-gap height="20rpx"></uv-gap>
					<tui-tag type="black">{{startTime}}</tui-tag>
				</view>
				<view class="uni-mt-10">
					<tui-button type="danger" shape="circle" @click="onCompleteTestPaper">完成作答</tui-button>
				</view>
			</uni-card>
		</view>
		<view v-else-if="status === 'ENDED'">
			<uni-card :title="testPaperTrackData.name">
				<view style="display: flex; flex-direction: column; align-items: center;">
					<uv-image :src="`/static/test-paper-status/${status}.gif`" width="300" mode="widthFix"></uv-image>
					<uv-gap height="20rpx"></uv-gap>
					<uv-text size="20" type="primary" text="审核中..." bold="true" align="center"></uv-text>
					<uv-gap height="30rpx"></uv-gap>
					<tui-tag type="black">{{startTime}}</tui-tag>
					<uv-gap height="10rpx"></uv-gap>
					<tui-tag type="primary">{{endTime}}</tui-tag>
				</view>
			</uni-card>
		</view>
		<view v-else-if="status === 'UNQUALIFIED' || status === 'TIMEOUT'">
			<uni-card :title="testPaperTrackData.name">
				<view style="display: flex; flex-direction: column; align-items: center;">
					<uv-image :src="`/static/test-paper-status/${status}.gif`" width="300" mode="widthFix"></uv-image>
					<uv-gap height="20rpx"></uv-gap>
					<view v-if="status === 'UNQUALIFIED'" style="display: flex; flex-direction: column;">
						<uv-text  size="20" type="error" text="不合格" bold="true" align="center"></uv-text>
						<uv-gap height="10rpx"></uv-gap>
						<uv-text :text="`本次成绩${testPaperTrackData.score}分`" bold="true" align="center"></uv-text>
					</view>
					<view v-else-if="status === 'TIMEOUT'" style="display: flex; flex-direction: column;">
						<uv-text  size="20" type="error" text="作答超时" bold="true" align="center"></uv-text>
						<uv-gap height="10rpx"></uv-gap>
						<uv-text :text="duration" bold="true" align="center"></uv-text>
					</view>
				</view>
			</uni-card>
		</view>
		<view v-else-if="status === 'QUALIFIED'">
			<uni-card :title="testPaperTrackData.name">
				<view style="display: flex; flex-direction: column; align-items: center;">
					<uv-image :src="`/static/test-paper-status/${status}.gif`" width="300" mode="widthFix"></uv-image>
					<uv-gap height="30rpx"></uv-gap>
					<view style="display: flex; flex-direction: column;">
						<uv-text size="40" type="success" text="合格" bold="true" align="center"></uv-text>
						<uv-gap height="30rpx"></uv-gap>
						<uv-text :text="`本次成绩${testPaperTrackData.score}分`" bold="true" align="center"></uv-text>
					</view>
				</view>
				<view class="uni-mt-10" v-if="hasAward">
					<tui-button type="danger" shape="circle" @click="onReciveTestPaper">领取奖励</tui-button>
				</view>
			</uni-card>
		</view>
		<view v-else-if="status === 'RECEIVED'">
			<uni-card :title="testPaperTrackData.name">
				<view style="display: flex; flex-direction: column; align-items: center;">
					<uv-image :src="`/static/test-paper-status/${status}.gif`" width="300" mode="widthFix"></uv-image>
					<uv-gap height="30rpx"></uv-gap>
					<view style="display: flex; flex-direction: column;">
						<uv-text size="40" type="success" text="合格" bold="true" align="center"></uv-text>
						<uv-gap height="30rpx"></uv-gap>
						<uv-text :text="`本次成绩${testPaperTrackData.score}分`" bold="true" align="center"></uv-text>
					</view>
				</view>
				<view class="uni-mt-10">
					<tui-button type="danger" shape="circle" :disabled="true">已领取{{testPaperTrackData.currency.name}}:{{testPaperTrackData.currency.symbol}}{{testPaperTrackData.answerLimitTime}}元</tui-button>
				</view>
			</uni-card>
		</view>
	</view>
	<tui-toast ref="toast"></tui-toast>
</template>

<script setup>
	import {onLoad} from '@dcloudio/uni-app'
	import { showTestPaperTrack, updateTestPaperTrackStatus } from '../../api/TestPaperTrack';
	import { computed, ref } from 'vue';
	import moment from 'moment';
	
	const testPaperTrackData = ref({
		name: '',
		totalScore: 0,
		qualifiedScore: 0,
		award: 0,
		currency: {
			name: '',
			symbol: ''
		},
		answerLimitTime: 0,
		status: {
			name: 'UNSTARTED'
		},
		startTime: '',
		endTime: ''
	})
	const formData = ref({
		"track.id": '',
		status: ''
	})
	const toast = ref()
	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const desc = computed(() => {
		if (testPaperTrackData.value) {
			let result = `试卷共${testPaperTrackData.value.totalScore}分`
			if (hasAward.value) {
				result += `，达到${testPaperTrackData.value.qualifiedScore}分后可以获得奖励`
			}
			result += '\n'
			if (testPaperTrackData.value.answerLimitTime) {
				result += `试卷作答限时${testPaperTrackData.value.answerLimitTime}分钟`
			}
			return result
		}
		return ''
	})
	const award = computed(() => `${testPaperTrackData.value.currency.name}: ${testPaperTrackData.value.currency.symbol}${testPaperTrackData.value.award}元`)
	const hasAward = computed(() => Boolean(testPaperTrackData.value.award && testPaperTrackData.value.currency))
	const startTime = computed(() => '开始时间:' + moment(testPaperTrackData.value.startTime).format('YYYY年MM月DD日 HH:mm'))
	const endTime = computed(() => '完成时间:' + moment(testPaperTrackData.value.endTime).format('YYYY年MM月DD日 HH:mm'))
	const duration = computed(() => {
		const _startTime = moment(testPaperTrackData.value.startTime)
		const _endTime = moment(testPaperTrackData.value.endTime)
		const mDiff = _endTime.diff(_startTime, "minutes")
		return `本次考试用时：${String(mDiff).padStart(2, '0')}分`
	})
	const status = computed(() => testPaperTrackData.value.status.name)
	
	onLoad(async (options) => {
		formData.value['track.id'] = options.trackId
		if (formData.value['track.id']) {
			await loadData()
		}
	})
	
	async function onStartTestPaper() {
		formData.value.status = 'ANSWERING'
		await updateStatus()
	}
	function onCompleteTestPaper() {
		uni.showModal({
			title: '提示',
			content: '确定已全部作答完毕?',
			confirmText: '交卷',
			async success(res) {
				if (res.confirm) {	
					formData.value.status = 'ENDED'
					const res = await updateStatus()
					if (res?.code === 200) {
						toast.value.show({
							title: '提交成功'
						})
					}
				}
			}
		})
	}
	async function onReciveTestPaper() {
		formData.value.status = 'RECEIVED'
		const res = await updateStatus()
		if (res?.code === 200) {
			toast.value.show({
				title: '领取成功'
			})
		}
	}
	
	async function loadData() {
		uni.showLoading({
			title: '查询中...'
		})
		try {
			const res = await showTestPaperTrack(formData.value)
			if (res.code === 200) {
				testPaperTrackData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
			return res
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '查询试卷数据错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
	
	async function updateStatus() {
		uni.showLoading({
			title: '提交中...'
		})
		try {
			const res = await updateTestPaperTrackStatus(formData.value)
			if (res.code === 200) {
				testPaperTrackData.value = res.data
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '修改试卷数据错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
</script>

<style scoped lang="scss">
	.test-paper-track {
		display: flex;
		flex-direction: column;
		font-size: 1em;
		color: $uni-text-color;
	}
</style>

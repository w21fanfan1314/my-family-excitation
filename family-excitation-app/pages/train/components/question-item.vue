<template>
	<view class="question-item">
		<uni-section type="circle" :title="'[' + (props.num) +'/'+ props.totalCount +']'+'问题'" :padding="true">
			<view style="display: flex; flex-direction: column;">
				<uv-text :text="props.question.content" color="#000000" size="1.2em"></uv-text>
				<view v-for="media in props.question.meidaDataList" :key="'question-' + props.question.id +'-media-' + media.id"  class="uni-mt-10">
					<uv-image v-if="media.type.name === 'IMAGE'" width="100%" height="350rpx" :radius="6" :src="media.url" mode="aspectFit" @click="onImageClick()"></uv-image>
					<video v-if="media.type.name === 'VIDEO'" style="width: 100%; height: 350rpx;" :src="media.url"></video>
				</view>
			</view>
		</uni-section>
		<view  v-if="!props.showCorrectAnswer">
			<uni-section v-if="props.question.type.name === 'SINGLE'" type="line" title="选项" :padding="true">
				<uv-radio-group v-model="model"  placement="column" :disabled="props.readOnly">
					<uv-radio v-for="(opt, index) in props.question.options" :name="opt.id" :label="opt.option" :custom-style="{marginTop: index === 0 ? 0 : '30rpx'}"></uv-radio>
				</uv-radio-group>
			</uni-section>
			<uni-section  v-else-if="props.question.type.name === 'MULTIPLE'" type="line" title="选项" :padding="true">
				<uv-checkbox-group v-model="model"  placement="column" :disabled="props.readOnly">
					<uv-checkbox v-for="(opt, index) in props.question.options" :name="opt.id" :label="opt.option" :custom-style="{marginTop: index === 0 ? 0 : '30rpx'}"></uv-checkbox>
				</uv-checkbox-group>
			</uni-section>
			<uni-section   v-else-if="props.question.type.name === 'JUDGE'" type="line" title="判断" :padding="true">
				<uv-radio-group v-model="model" :disabled="props.readOnly">
					<uv-radio v-for="opt in props.question.options" :name="opt.id" :label="opt.option" :custom-style="{marginRight: '30rpx'}"></uv-radio>
				</uv-radio-group>
			</uni-section>
			<uni-section v-else type="line" title="请输入" padding>
				<uv-textarea v-model="model" placeholder="请输入" :disabled="props.readOnly"></uv-textarea>
			</uni-section>
		</view>
		<uni-section v-else type="square" title="正确答案" padding>
			<uv-text type="success" :size="20" :text="props.question.rightOption?.map(item => item.option)?.join('\n')"></uv-text>
		</uni-section>
		<uni-section v-if="props.userAnswer?.length > 0" type="line" title="你的答案" padding>
			<uv-text  type="error" :size="20" :text="props.userAnswer?.map(item => item.option)?.join('\n')"></uv-text>
		</uni-section>
	</view>
</template>

<script setup>
	import { computed, getCurrentInstance, ref, defineProps, defineModel } from 'vue';
	
	const props = defineProps({
		question: {
			type: Object,
			default: ({
				id: 0,
				content: '',
				type: {
					name: ''
				},
				meidaDataList: []
			})
		},
		num: {
			type: Number,
			default: 1
		},
		totalCount: {
			type: Number,
			default: 1
		},
		readOnly: {
			type: Boolean,
			default: false
		},
		showCorrectAnswer: {
			type: Boolean,
			default: false
		},
		userAnswer: {
			type: Array,
			default: []
		}
	})
	const model = defineModel()
	
	function onImageClick(item) {
		const urls = props.question.meidaDataList.filter(media => media.type.name === 'IMAGE').map(media => media.url)
		uni.previewImage({
			urls,
		})
	}
</script>

<style scoped lang="scss">
	.question-item {
		display: flex;
		flex-direction: column;
	}
</style>
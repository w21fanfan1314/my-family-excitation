<template>
	<view class="train-button" @click="onClick">
		<slot></slot>
	</view>
</template>

<script setup>
	import { defineEmits } from 'vue'
	import throttle from '@/uni_modules/uv-ui-tools/libs/function/throttle.js';
	
	const emit = defineEmits(['onClick'])
	
	function onClick() {
		throttle(() => {
			setTimeout(() => {
				emit('onClick')
			}, 300)
		}, 500)
	}
</script>

<style scoped lang="scss">
	.train-button {
		display: flex;
		flex-direction: column;
		
		&:active {
			transform: scale(0.9);
		}
	}
</style>
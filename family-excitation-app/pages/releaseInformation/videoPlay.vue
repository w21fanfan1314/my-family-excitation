<template>
	<video style="width: 100%;" :style="{height: windowHeight}" :src="mediaData.url" :poster="poster" :title="mediaData.name" ></video>
</template>

<script setup>
	import { onLoad } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	
	const mediaData = ref({
		url: '',
		name: ''
	})
	const poster = ref('')
	const windowHeight = computed(() => (uni.getWindowInfo().windowHeight) + 'px')
	
	onLoad((options) => {
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		
		eventChannel.on('mediaData', ({data, thumbnail}) => {
			mediaData.value = data
			poster.value = thumbnail
			
			uni.setNavigationBarTitle({
				title: data?.name
			})
		})
	})
</script>

<style scoped lang="scss">
</style>

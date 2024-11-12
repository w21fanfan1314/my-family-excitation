<template>
	<view class="train-settlement">
		<tui-card :image-url="avatar" :title="user.userInfo?.name"></tui-card>
	</view>
</template>

<script setup>
	import { computed } from 'vue';
	import { useUserStore } from '@/store/user';
	import { defaultAvatar } from '../../common/data';
	const user = useUserStore()
	const avatar = computed(() => user.userInfo?.avatar || defaultAvatar )
</script>

<style scoped lang="scss">
	.train-settlement {
		display: flex;
		flex-direction: column;
	}
</style>

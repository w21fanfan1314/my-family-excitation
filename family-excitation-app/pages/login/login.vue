<template>
    <view class="login-container">
		<uni-forms :modelValue="formData">
			<uni-forms-item label="登录名" name="loginName">
				 <uni-easyinput  v-model="formData.loginName" placeholder="请输入" />
			</uni-forms-item>
			<uni-forms-item label="密码" name="password">
				 <uni-easyinput type="password"  v-model="formData.password" placeholder="请输入" />
			</uni-forms-item>
			<button type="primary" @click="onLogin">登陆</button>
		</uni-forms>
    </view>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/store/user';

const user = useUserStore()
const formData = ref({
    loginName: '',
    password: '',
    type: 'WEB'
});

async function onLogin() {
    uni.showLoading({
        title: '登陆中...'
    });
    try {
        const res = await user.userAuth(formData.value)
        if (res.code === 200) {
            uni.redirectTo({
                url: '/pages/index/index'
            })
        } else {
            uni.showToast({
                title: res.msg,
                icon: 'none'
            });
        }
    } catch(err) {
		console.log("登录异常", err)
        uni.showToast({
            title: '登陆失败',
            icon: 'none'
        });
    } finally {
        uni.hideLoading();
    }
}
</script>

<style lang="scss" scoped>
    .login-container {
        display: flex;
        flex-direction: column;
        padding: 30rpx;
        box-sizing: border-box;
    }
</style>
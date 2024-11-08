import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { checkAuthValid, login, queryBalance, queryUserInfo, quit, updateUserInfo } from "../api/UserApi";

const LOGIN_INFO_KEY = "LOGIN_INFO_KEY"
	
export const useUserStore = defineStore('user', () => {
    const loginInfoData = ref(uni.getStorageSync(LOGIN_INFO_KEY))
	const balanceData = ref()
    const isAuth = computed(() => Boolean(loginInfo.value?.token))
    const token = computed(() => loginInfo.value?.token)
    const userInfo = computed(() => loginInfo.value?.user)
    const loginInfo = computed({
        get() {
            return loginInfoData.value
        },
        set(val) {
			loginInfoData.value = val
			if (val) {
				uni.setStorageSync(LOGIN_INFO_KEY, val)
			} else {
				uni.removeStorageSync(LOGIN_INFO_KEY)
			}
        }
    })
	
	async function userUpdate(params) {
	    const res = await updateUserInfo(params)
	    if (res?.code === 200) {
			const userInfo = res.data
	        const info = {...loginInfo.value, user: userInfo}
			loginInfo.value = info
	    }
		return res
	}
	
	async function userDetail(params) {
	    const res = await queryUserInfo(params)
	    if (res?.code === 200) {
			const userInfo = res.data
	        const info = {...loginInfo.value, user: userInfo}
			loginInfo.value = info
	    }
		return res
	}

    async function userAuth(params) {
        const res = await login(params)
        if (res?.code === 200) {
            loginInfo.value = res.data
        }
		return res
    }
	
	async function userQuit() {
		const res = await quit()
		if (res?.code === 200) {
		    loginInfo.value = undefined
		}
		return res
	}
	
	async function loginValidCheck() {
		const res = await checkAuthValid()
		if (res?.code === 401) {
		    loginInfo.value = undefined
		}
		return res
	}
	
	async function loadUserBalance() {
		const res = await queryBalance({userId: userInfo.value?.id});
		if (res.code === 200) {
			balanceData.value = res.data
		}
		return res
	}
	
    
    return {
        isAuth,
        token,
        userInfo,
        userAuth,
		loginValidCheck,
		userQuit,
		loadUserBalance,
		balanceData,
		userDetail,
		userUpdate
    }
})
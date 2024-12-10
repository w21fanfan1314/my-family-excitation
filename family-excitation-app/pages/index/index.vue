<template>
  <view class="index-container">
    <uv-notice-bar v-if="noticeText?.length > 0" icon="volume-fill" :text="noticeText" direction="column"></uv-notice-bar>
    <uni-card>
     <navigator url="/pages/user/user">
		 <view class="user-info">
		   <uv-avatar :size="60" :src="user?.userInfo?.avatar || defaultAvatar"></uv-avatar>
		   <view class="info-container">
		     <text>{{user.userInfo?.name}}</text>
		     <uni-rate :max="starCount" :value="starCount" :readonly="true"></uni-rate>
		   </view>
		 </view>
	 </navigator>
	  <uni-grid :column="3" :show-border="false" :square="false" class="menu-list">
		  <uni-grid-item>
			  <navigator url="/pages/shopping/shopping">
				  <view class="menu-item">
				  	<uni-icons type="shop-filled" color="#007aff" :size="24"></uni-icons>
				  	商城
				  </view>
			  </navigator>
		  </uni-grid-item>
		  <uni-grid-item>
		  	<navigator url="/pages/videos/bilibili-web">
		  		<view class="menu-item">
		  			<uni-icons type="pyq" color="#007aff" :size="24"></uni-icons>
		  			视频
		  		</view>	  				  
		  	</navigator>		  
		  </uni-grid-item>
		 <!-- <uni-grid-item>
			<navigator url="/pages/train/train">
				<view class="menu-item">
					<uni-icons type="hand-up-filled" color="#007aff" :size="24"></uni-icons>
					闯关
				</view>
			</navigator>	 
		  </uni-grid-item> -->
		 <!-- <uni-grid-item @click="onScanQr">
		  		<view class="menu-item">
		  			<uni-icons type="scan" color="#007aff" :size="24"></uni-icons>
		  			扫码
		  		</view>		 
		   </uni-grid-item> -->
		   <uni-grid-item>
		   		<navigator url="/pages/lottery/lottery">
					<view class="menu-item">
						<uni-icons type="vip-filled" color="#007aff" :size="24"></uni-icons>
						抽奖
					</view>		
				</navigator> 
		    </uni-grid-item>
	  </uni-grid>
    </uni-card>
	<!-- <user-income-expenditure-card></user-income-expenditure-card> -->
    <uni-section type="line" title="我的资产">
		<template v-slot:right>
			<navigator :url="'/pages/record/record?userId=' + user.userInfo?.id">
				<text>查看记录</text>
			</navigator>
		</template>
     <uni-row>
        <uni-card v-for="(item, index) in balanceData" 
			:key="'user-balance-' + item.currency.id" :is-shadow="false" :title="item.currency.name" :class="{'top0': index === 0}">
            <view class="amount">
              <text class="currency">{{item.currency.symbol}}</text>
              {{item.balance}}
            </view>
        </uni-card>
     </uni-row>
    </uni-section>
    <uni-section v-if="scoreData?.scores?.length > 0" type="line" title="今日成绩">
      <uni-grid :column="2" :show-border="false" :square="false">
        <uni-grid-item v-for="(item, index) in scoreData?.scores" :key="'score-' + item.user.id+'-' + item.score.id">
          <uni-card :is-shadow="false" :title="item.discipline.name" :class="{'top0': (index > 3)}">
            <h1 style="text-align: center;">{{item.level}}</h1>
          </uni-card>
		</uni-grid-item>
      </uni-grid>
    </uni-section>

    <uni-section type="line" title="规则介绍">
      <uni-list :border="false">
		  <uni-list-item title="1 作业成绩拿到A或者A+获得2元奖励"></uni-list-item>
		  <uni-list-item title="2 如果每天可以集齐3颗星(🌟🌟🌟)额外获得2元奖励"></uni-list-item>
		  <uni-list-item title="3 作业不认真对待,写字不公正等需要处罚" note="处罚1: 订正内容每个部分写一篇"></uni-list-item>
	  </uni-list>
    </uni-section>
  </view>
</template>

<script setup>
import { computed, onMounted, readonly, ref } from 'vue';
import { useUserStore } from '../../store/user';
import { queryBalance, queryScore, queryTopAScore } from '../../api/UserApi';
import { onLoad, onPullDownRefresh } from '@dcloudio/uni-app'
import { defaultAvatar } from '../../common/data';
import { storeToRefs } from 'pinia'
import { uploadFile } from '../../api/Request';
import { useDecodeQRCode } from '../../common/useDecodeQRCode';
import UserIncomeExpenditureCard from './components/user-income-expenditure-card.vue';

const user = useUserStore()
const scoreData = ref({starCount: 0, scores: []})
const noticeData = ref([])
const starCount = computed(() =>  scoreData.value?.starCount || 0)
const noticeText = computed(() => noticeData.value?.map(item => (`🎉恭喜${item.user.name}同学的${item.discipline.name}获得${item.level}🎉`)))
const {loadUserBalance} = user
const {balanceData} = storeToRefs(user)
const {chooseImage} = useDecodeQRCode()

onLoad(async () => {
	await refreshData()
})

onPullDownRefresh(async() => {
	try {
		await refreshData(false)
	} finally {
		uni.stopPullDownRefresh()
	}
})

async function onScanQr() {
	const text = await chooseImage()
	if (text) {
		uni.navigateTo({
			url: '/pages/test-paper-track/test-paper-track?trackId=' + text
		})
	} else {
		uni.showToast({
			icon: 'none',
			title: '未识别到二维码'
		})
	}
}

async function refreshData(showLoading = true) {
	await user.loginValidCheck()
	if (!user.isAuth) {
		uni.redirectTo({
			url: '/pages/login/login'
		})
		return;
	} 
	await loadData(showLoading)
}


async function loadData(showLoading = true) {
	if (showLoading) {
		uni.showLoading({
			title: '加载中...'
		})
	}
	try {
		await Promise.all([loadUserBalance(), loadUserScore(), loadNotice()])
	} catch(err) {
		uni.showToast({
			icon: 'none',
			title: '初始化页面数据错误'
		})
	} finally {
		if (showLoading) {
			uni.hideLoading()
		}
	}
}



async function loadUserScore() {
	const res = await queryScore({userId: user.userInfo.id, time: Date.now()});
	if (res.code === 200) {
		scoreData.value = res.data
	}
}

async function loadNotice() {
	const res = await queryTopAScore({time: Date.now()});
	if (res.code === 200) {
		noticeData.value = res.data
	}
}

</script>

<style scoped lang="scss">
  .index-container {
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    width: 100%;
	
	.top0 {
		margin-top: 0;
	}

    .user-info {
      display: flex;
      flex-direction: row;
      align-items: center;
      width: 100%;
      
      .user-avatar {
        width: 100rpx;
        height: 100rpx;
        border-radius: 10rpx;
        background-color: $uni-bg-color-grey;
      }

      .info-container {
        display: flex;
        flex-direction: column;
        flex: 1;
        margin-left: 12rpx;
        height: 90%;
        justify-content: space-between;

        font-size: 1.2em;
        color: black;
        font-weight: bold;
      } 
    }
	
	.menu-list {
		margin-top: 30rpx;
		color: $uni-color-primary;
		font-size: 28rpx;
		font-weight: bold;
		
		.menu-item {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: center;
			padding: 10rpx 0;
		}
	}

    .amount {
      color: black;
      font-weight: bold;
      font-size: 1.5em;
      display: flex;
      flex-direction: row;
      align-items: flex-end;
      .currency {
        font-size: 0.8em;
        margin-right: 8rpx;
        color: grey;
      }
    }
    
  }
</style>

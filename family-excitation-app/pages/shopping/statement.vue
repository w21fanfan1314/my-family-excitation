<template>
	<view class="statement-container">
		<uni-section title="商品信息" type="line">
			<tui-list-view>
				<tui-list-cell v-for="item in commoditiesData" :key="'order-item-' + item.commodity.id" :hover="false">
					<view class="commodity-item">
						<image class="item-img" :src="item.imageUrl" mode="aspectFill"></image>
						<view class="item-container">
							<text class="name">{{item.name}}</text>
							<view class="spec" v-if="item.specification">
								<tui-tag type="green" padding="8rpx 18rpx" size="24rpx" :plain="true">规格: {{item.specification}}</tui-tag>
							</view>
							<view class="item-price" v-if="!currenciesData">
								<text class="label">人民币</text>¥{{item.price}}
							</view>
							<view class="item-price" v-else v-for="currency in currenciesData" 
							:key="'commodity-' + item.id + '-currency-' + currency.id">
								<text class="label">{{currency.name}}</text>{{currency.symbol}}{{currency.rate * item.price}}
							</view>
							<view class="item-bottom">
								<tui-numberbox :min="1" :value="item.buyCount" @change="onBuyCountChange($event, item)"></tui-numberbox>
							</view>
						</view>
					</view>
				</tui-list-cell>
				<tui-list-cell>
					<view class="order-item">
						<text class="label">总价</text>
						<text class="amount">{{totalDesc}}</text>
					</view>
				</tui-list-cell>
			</tui-list-view>
		</uni-section>
		<uni-section title="支付信息" type="line">
			<tui-list-view>
				<tui-list-cell :hover="false">
					<uni-data-checkbox v-model="selectedPayCurrencyId" :localdata="payItems" mode="list" ></uni-data-checkbox>
				</tui-list-cell>
			</tui-list-view>
		</uni-section>
		<view class="order-bottom">
			<tui-button type="primary" :disabled="!isCanPay" @click="createNewOrder" :prevent-click="true">立即下单</tui-button>
		</view>
	</view>
	<uni-popup ref="successModal" type="dialog">
		<uni-popup-dialog mode="base" type="success" 
			title="提示" content="下单成功" :show-close="true" 
			confirm-text="查看详情" cancel-text="返回" @confirm="onConfirmGoOrder" @close="onCloseGoShopping"></uni-popup-dialog>
	</uni-popup>
</template>

<script setup>
	import { onReady, onPageScroll, onLoad } from '@dcloudio/uni-app'
	import { computed, getCurrentInstance, ref } from 'vue';
	import { createOrder } from '../../api/Order';
	import { queryBalance } from '../../api/UserApi';
	import { useUserStore } from '../../store/user';
	import { storeToRefs } from 'pinia'
	
	const successModal = ref()
	const commoditiesData = ref([])
	const currenciesData = ref([])
	const user = useUserStore()
	const selectedPayCurrencyId = ref(0)
	const orderData = ref()
	const payItems = computed(() => balanceData.value?.map(item => ({
		text: `${item.currency.name} 余额: ${item.currency.symbol}${item.balance}`,
		value: item.currency.id,
		disable: calTotal(item.currency.rate) > item.balance
	})) || [])
	const totalDesc = computed(() => {
		const current = currency.value
		const rate = current?.rate || 1
		const symbol = current?.symbol || '¥'
		const name = current?.name || '人民币'
		const pay = total.value.toFixed(2)
		
		console.log('onPayChange', currenciesData.value)
		return `${name}: ${symbol}${pay}`
	})
	const total = computed(() => calTotal(currency.value?.rate))
	const currency = computed(() => currenciesData.value?.find(item => item?.id === selectedPayCurrencyId.value))
	const isCanPay = computed(() => total.value < balanceData.value?.find(item => item.currency.id === currency.value?.id)?.balance)
	const {loadUserBalance} = user
	const {balanceData} = storeToRefs(user)
	
	onLoad(async (options) => {
		const instance = getCurrentInstance().proxy
		const eventChannel = instance.getOpenerEventChannel()
		
		eventChannel.on('commodities', ({commodities, currencies}) => {
			commoditiesData.value = commodities?.map(item => ({name: item.name, 
				imageUrl: item.imageUrl, 
				price: item.price, 
				specification: item.specification, 
				buyCount: 1, 
				commodity: item})) || [];
			currenciesData.value = currencies
			if (commodities?.length > 0) {
				selectedPayCurrencyId.value = currencies[0].id
			}
		})
		
		await Promise.all([loadUserBalance()])
	})
	
	function calTotal(rate = 1) {
		 return Number(commoditiesData.value?.reduce((total, cur) => total + ((cur.price * rate) * cur.buyCount), 0))
	}
	
	function onConfirmGoOrder() {
		uni.redirectTo({
			url: '/pages/order/detail',
			success(res) {
				res.eventChannel.emit("orderData", orderData.value)
			}
		})
	}
	
	function onCloseGoShopping() {
		uni.navigateBack()
	}
	
	function onBuyCountChange(e, commodity) {
		commodity.buyCount = e.value
	}
	
	
	async function createNewOrder() {
		uni.showLoading({
			title: '创建中...'
		})
		try {
			const res = await createOrder({
				items: commoditiesData.value,
				payments: [
					{
						currency: currency.value,
						amount: total.value.toFixed(2)
					}
				]
			})
			if (res.code === 200) {
				// 刷新余额
				await loadUserBalance()
				orderData.value = res.data
				successModal.value?.open()
			} else {
				uni.showToast({
					icon: 'none',
					title: res.msg
				})
			}
		} catch(err) {
			uni.showToast({
				icon: 'none',
				title: '创建订单错误'
			})
		} finally {
			uni.hideLoading()
		}
	}
	
	
</script>

<style scoped lang="scss">
	@import 'common/common.scss';
	.statement-container {
		display: flex;
		flex-direction: column;
		
		@include commodity-item(80rpx, 0.9em);
		
		.order-item {
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			
			.label {
				font-size: 1.1em;
				color: $uni-text-color-grey;
			}
			
			.amount {
				font-size: 1.2em;
				font-weight: bold;
				color: $uni-color-error;
			}
		}
		
		.order-bottom {
			display: flex;
			flex-direction: column;
			padding: 30rpx;
			margin-top: 60rpx;
		}
	}
</style>

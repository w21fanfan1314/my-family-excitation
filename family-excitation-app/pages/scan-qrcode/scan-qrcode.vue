<template>
	<view class="scan-qrcode">
		<view id="scan-qrcode-canvas"></view>
		<view style="display: flex; flex-direction: column; padding-top: 30rpx;">
			<uv-row justify="center">
				<uv-col :span="8" id="choose-image-canvas" >
					<!-- <image style="width: 100%;" mode="widthFix" :src="chooseImagePath"></image> -->
				</uv-col>
			</uv-row>
			<uv-row style="margin-top: 30rpx;" justify="center">
				<uv-col :span="10">
					<uv-button type="primary" text="选择图片" @click="onChooseImage"></uv-button>
					<view v-if="codeData" style="margin-top: 40rpx;">
						<uv-button type="success" text="确认打开" @click="onOpen" size="large"></uv-button>
					</view>
				</uv-col>
			</uv-row>
		</view>
	</view>
</template>

<script setup>
	import {
		computed, getCurrentInstance, nextTick, ref
	} from 'vue';
	import {
		onReady,
		onUnload
	} from '@dcloudio/uni-app'
	import jsQR from 'jsqr'

	const windowHeight = computed(() => uni.getWindowInfo().windowHeight + 'px')
	const windowWidth = computed(() => uni.getWindowInfo().windowWidth + 'px')
	let video;
	let canvasElement;
	let canvas;
	let eventChannel;
	let ticking = false
	const codeData = ref('')

	onReady(() => {
		const instance = getCurrentInstance().proxy
		eventChannel = instance.getOpenerEventChannel()
		// const container = document.querySelector("#scan-qrcode-canvas")
		// canvasElement = document.createElement("canvas")
		// canvasElement.style.position = 'absolute'
		// canvasElement.style.top = 0
		// canvasElement.style.left = 0
		// canvasElement.height = uni.getWindowInfo().windowHeight
		// canvasElement.width = uni.getWindowInfo().windowWidth
		// container.appendChild(canvasElement)
		// // canvas = uni.createCanvasContext("scan-qrcode-canvas", getCurrentInstance().proxy)
		// canvas = canvasElement.getContext('2d')
		// if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
		// 	navigator.mediaDevices.getUserMedia({
		// 		video: true
		// 	}).then(stream => {
		// 		video = document.createElement('video')
		// 		video.srcObject = stream
		// 		video.setAttribute("autoplay", true)
		// 		video.setAttribute("muted", true)
		// 		video.setAttribute("playsinline", true)
		// 		ticking = true
		// 		requestAnimationFrame(tick)
		// 	})
		// }
		
		canvasElement = document.createElement("canvas")
		document.querySelector("#choose-image-canvas").appendChild(canvasElement)
		canvas = canvasElement.getContext('2d')
	})
	

	onUnload(() => {
		// ticking = false
		// const tracks = video.srcObject.getTracks()
		// for (const item of tracks) {
		// 	item.stop()
		// }
		// video.srcObject = null
	})
	
	function onChooseImage() {
		uni.chooseImage({
			count: 1,
			success(res) {
				const imagePath = res.tempFilePaths[0]
				const img = new Image()
				img.onload = function() {
					canvasElement.width =  this.width
					canvasElement.height = this.height
					canvas.drawImage(this, 0, 0, this.width, this.height)
					const imageData = canvas.getImageData(0, 0,  this.width, this.height)
					const code = jsQR(imageData.data, imageData.width, imageData.height)
					console.log(code)
					if (code) {
						drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#FF3B58");
						drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#FF3B58");
						drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#FF3B58");
						drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#FF3B58");
					}
					codeData.value = code
				}
				img.src = imagePath
			}
		})
	}
	
	function onOpen() {
		eventChannel.emit('qrCodeData', {text: codeData.value.data})
		uni.navigateBack()
	}
	

	function tick() {
		if (!ticking) {
			return
		}
		if (video.readyState === video.HAVE_ENOUGH_DATA) {
			const height = video.videoHeight * (canvasElement.width / video.videoWidth)
			canvas.drawImage(video, 0, 0, canvasElement.width, height);
			const imageData = canvas.getImageData(0, 0, canvasElement.width, height)
			const code = jsQR(imageData.data, imageData.width, imageData.height, {
				inversionAttempts: "dontInvert",
			});
			if (code) {
				drawLine(code.location.topLeftCorner, code.location.topRightCorner, "#FF3B58");
				drawLine(code.location.topRightCorner, code.location.bottomRightCorner, "#FF3B58");
				drawLine(code.location.bottomRightCorner, code.location.bottomLeftCorner, "#FF3B58");
				drawLine(code.location.bottomLeftCorner, code.location.topLeftCorner, "#FF3B58");
				
				setTimeout(() => {
					eventChannel.emit('qrCodeData', {text: code.data})
					uni.navigateBack()
				}, 1000)
				return
			}
		}
		requestAnimationFrame(tick);
	}

	function drawLine(begin, end, color) {
		canvas.beginPath();
		canvas.moveTo(begin.x, begin.y);
		canvas.lineTo(end.x, end.y);
		canvas.lineWidth = 4;
		canvas.strokeStyle = color;
		canvas.stroke();
	}
</script>

<style scoped lang="scss">
	.scan-qrcode {
		display: flex;
		flex-direction: column;
		width: 750rpx;
	}
</style>
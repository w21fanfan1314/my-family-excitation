<template>
	<view class="scan-qrcode" :style="{height: windowHeight}">
		<view id="scan-qrcode-canvas" ></view>
	</view>
</template>

<script setup>
	import {
		computed, getCurrentInstance, nextTick
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

	onReady(() => {
		const instance = getCurrentInstance().proxy
		eventChannel = instance.getOpenerEventChannel()
		const container = document.querySelector("#scan-qrcode-canvas")
		canvasElement = document.createElement("canvas")
		canvasElement.style.position = 'absolute'
		canvasElement.style.top = 0
		canvasElement.style.left = 0
		canvasElement.height = uni.getWindowInfo().windowHeight
		canvasElement.width = uni.getWindowInfo().windowWidth
		container.appendChild(canvasElement)
		// canvas = uni.createCanvasContext("scan-qrcode-canvas", getCurrentInstance().proxy)
		canvas = canvasElement.getContext('2d')
		if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
			navigator.mediaDevices.getUserMedia({
				video: true
			}).then(stream => {
				video = document.createElement('video')
				video.srcObject = stream
				video.setAttribute("autoplay", true)
				video.setAttribute("muted", true)
				video.setAttribute("playsinline", true)
				ticking = true
				requestAnimationFrame(tick)
			})
		}
	})

	onUnload(() => {
		ticking = false
		const tracks = video.srcObject.getTracks()
		for (const item of tracks) {
			item.stop()
		}
		video.srcObject = null
	})
	

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
import { onMounted, onUnmounted } from "vue";
import jsQR from 'jsqr'

export function useDecodeQRCode() {
	let canvasElement;
	let canvas;
	
	onMounted(() => {
		canvasElement = document.createElement("canvas")
		canvas = canvasElement.getContext('2d')
	})
	
	onUnmounted(() => {
		document.removeChild(canvasElement)
	})
	
	function onChooseImage() {
		return new Promise((resolve) => {
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
						resolve(code?.data)
					}
					img.onerror = function() {
						resolve()
					}
					img.src = imagePath
				}
			})
		})
	}
	
	return {
		chooseImage: onChooseImage
	}
}
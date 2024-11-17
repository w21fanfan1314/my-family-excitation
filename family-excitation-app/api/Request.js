import { useUserStore } from "../store/user"

export const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

export async function request(options) {
    return new Promise((resolve, reject) => {
        const url = apiBaseUrl + options.url
        const user = useUserStore()
        uni.request({
            ...options,
            url,
            header: {
                ...options.header,
                'app-token': user.token || ''
            },
            success: (res) => {
                console.log("请求" +options.url+ "返回:", res)
                resolve(res.data)
            },
            fail: (err) => {
                console.log("请求" +options.url+ "错误:", err)
                reject({code: 500, msg: '请求错误', data: err})
            }
        })
    })
}

export async function uploadFile({url, name, fileType}) {
	return new Promise(resolve => {
        const user = useUserStore()
		uni.uploadFile({
			url: apiBaseUrl + '/mediaDataApi/upload.json',
			name: name || 'file',
			fileType: fileType || 'image',
			filePath: url,
			header: {
				 'app-token': user.token || ''
			},
			success(res) {
				console.log('上传文件返回', res)
				resolve(JSON.parse(res.data))
			},
			fail(err) {
				console.log('上传文件错误', res)
				resolve({code: 500, msg: '请求错误', data: err})
			}
		})
	})
}
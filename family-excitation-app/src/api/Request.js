const apiBaseUrl = import.meta.env.VITE_API_BASE_URL

export async function request(options) {
    return new Promise((resolve, reject) => {
        const url = apiBaseUrl + options.url
        uni.request({
            ...options,
            url,
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
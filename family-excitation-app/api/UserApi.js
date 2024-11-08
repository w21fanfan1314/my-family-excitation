import {request} from './Request'

export async function login(data) {
    return request({
        url: '/userApi/login.json',
        method: 'POST',
        data
    })
}

export async function quit() {
    return request({
        url: '/userApi/quit.json'
    })
}

export async function checkAuthValid() {
	return request({
		url: '/userApi/checkAuthValid.json'
	})
}
export async function queryBalance(data) {
	return request({
		url: '/userApi/queryBalance.json?user.id=' + data.userId
	})
}

export async function queryScore(data) {
	return request({
		url: '/userApi/queryScore.json?user.id=' + data.userId +"&time=" + data.time
	})
}

export async function queryTopAScore(data) {
	return request({
		url: '/userApi/queryTopAScore.json?time=' + data.time
	})
}

export async function userRecords(data) {
	return request({
		url: '/userApi/userRecords.json?user.id='+ data.userId,
		data
	})
}

export async function queryUserInfo(data) {
	return request({
		url: '/userApi/show.json?user.id=' + data.userId
	})
}

export async function updateUserInfo(data) {
	return request({
		url: '/userApi/update.json',
		method: 'POST',
		data
	})
}

export async function updatePassword(data) {
	return request({
		url: '/userApi/updatePwd.json',
		method: 'POST',
		data
	})
}
import {request} from './Request'

export async function createOrder(data) {
	return request({
		url: '/orderApi/createOrder.json',
		data,
		method: 'post'
	})
}

export async function listOrders(data) {
	return request({
		url: '/orderApi/listOrders.json',
		data
	})
}

export async function checkPayPassword(data) {
	return request({
		url: '/orderApi/checkPayPassword.json',
		data
	})
}
import {request} from './Request'

export async function listCommodities(data) {
	return request({
		url: '/commodityApi/list.json',
		data
	})
}

export async function listCategories(data) {
	return request({
		url: '/commodityApi/listCategories.json',
		data
	})
}

export async function listCurrencies(data) {
	return request({
		url: '/commodityApi/listCurrencies.json',
		data
	})
}
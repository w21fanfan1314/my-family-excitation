import {request} from './Request'

export async function trains() {
	return request({
		url: '/trainApi/list.json'
	})
}

export async function categories(data) {
	return request({
		url: '/trainApi/categories.json',
		data
	})
}

export async function levels(data) {
	return request({
		url: '/trainApi/levels.json',
		data
	})
}

export async function questions(data) {
	return request({
		url: '/trainApi/questions.json',
		data
	})
}
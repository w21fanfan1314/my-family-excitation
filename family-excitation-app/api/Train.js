import {request} from './Request'

export async function trains(data) {
	return request({
		url: '/trainApi/list.json',
		data
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

export async function commit(data) {
	return request({
		url: '/trainApi/commit.json',
		method: 'POST',
		data
	})
}

export async function transcriptDetail(data) {
	return request({
		url: '/trainApi/transcriptDetail.json',
		data
	})
}
import {request} from './Request'

export async function listReleaseInformations(data) {
	return request({
		url: '/releaseInformationApi/list.json',
		data
	})
}

export async function releaseInformationCategories(data) {
	return request({
		url: '/releaseInformationApi/categories.json',
		data
	})
}
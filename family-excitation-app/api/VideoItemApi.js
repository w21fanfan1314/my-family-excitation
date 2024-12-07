import { request } from "./Request"

export async function list(data) {
	return request({
	    url: '/videoItemApi/list.json',
	    method: 'GET',
	    data
	})
}

export async function cateList(data) {
	return request({
	    url: '/videoItemApi/categories.json',
	    method: 'GET',
	    data
	})
}
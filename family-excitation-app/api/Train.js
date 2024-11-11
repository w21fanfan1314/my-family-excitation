import {request} from './Request'

export async function trains() {
	return request({
		url: '/trainApi/list.json'
	})
}
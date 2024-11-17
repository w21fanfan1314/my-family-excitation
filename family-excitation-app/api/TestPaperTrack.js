import {request, apiBaseUrl} from './Request'

export async function showTestPaperTrack(data) {
	return request({
	    url: '/testPaperTrackApi/show.json',
		data
	})
}

export async function updateTestPaperTrackStatus(data) {
	return request({
	    url: '/testPaperTrackApi/updateStatus.json',
		data
	})
}
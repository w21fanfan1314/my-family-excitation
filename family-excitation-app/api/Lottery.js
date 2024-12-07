import { request } from "./Request"

export async function  giveLotteries(data) {
	return request({
	    url: '/lotteryApi/giveLotteries.json',
	    method: 'GET',
	    data
	})
}

export async function  queryLotteryRecords(data) {
	return request({
	    url: '/lotteryApi/queryLotteryRecords.json',
	    method: 'GET',
	    data
	})
}

export async function  saveLotteryRecords(data) {
	return request({
	    url: '/lotteryApi/saveLotteryRecords.json',
	    method: 'GET',
	    data
	})
}
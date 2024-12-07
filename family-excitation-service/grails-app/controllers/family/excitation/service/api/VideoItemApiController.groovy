package family.excitation.service.api

import family.excitation.service.VideoItem

class VideoItemApiController {

    def list(Integer page, Integer size, String category) {
        def result = VideoItem.createCriteria().list {
            if (category) {
                eq('category', category)
            }
            order('dateCreated', 'desc')
            maxResults(size)
            firstResult(page * size)
        }

        def total = VideoItem.createCriteria().get {
            if (category) {
                eq('category', category)
            }
            projections {
                rowCount()
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [items: result, total: total])
    }

    def categories() {
        def result = VideoItem.createCriteria().list {
            projections {
                groupProperty('category')
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: result)
    }
}

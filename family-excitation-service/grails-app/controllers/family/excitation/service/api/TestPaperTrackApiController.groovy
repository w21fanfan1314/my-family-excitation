package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.TestPaperStatus
import family.excitation.service.TestPaperTrack
import family.excitation.service.TestPaperTrackService
import family.excitation.service.User

class TestPaperTrackApiController {
    static allowedMethods = [scanQr: "POST"]
    TestPaperTrackService testPaperTrackService

    def show(TestPaperTrack track) {
        if (!checkTestPaperTrackForLogin(track)) {
            return
        }
        respond new ApiResult(code: 200, data: track, msg: '查询成功')
    }

    def updateStatus(TestPaperTrack track) {
        if (!checkTestPaperTrackForLogin(track)) {
            return
        }
        TestPaperStatus status = TestPaperStatus.valueOf(params.status)
        if (!status) {
            respond new ApiResult(code: 400, msg: '参数错误')
            return
        }
        track.status = status
        def result = testPaperTrackService.save(track)
        if (result) {
            respond new ApiResult(code: 200, data: track, msg: '更新成功')
        } else {
            respond new ApiResult(code: 500, msg: '更新失败')
        }
    }


    private def checkTestPaperTrackForLogin(TestPaperTrack track) {
        if (!track) {
            respond new ApiResult(code: 404, msg: '未找到试卷')
            return false
        }
        def token = request.getHeader('app-token')
        Login loginInfo = Login.findByToken(token)
        User user = loginInfo?.user
        if (!user) {
            respond new ApiResult(code: 403, msg: '请先登录')
            return false
        }
        if (track.user?.id != user.id) {
            respond new ApiResult(code: 403, msg: '这不是你的试卷')
            return false
        }
        return true
    }
}

package family.excitation.service.api

import family.excitation.service.MediaData

class MediaDataApiController {
    UploadService uploadService
    def show(MediaData mediaData) {
        if (mediaData.localPath) {
            render file: new File(mediaData.localPath), fileName: mediaData.name, contentType: mediaData.contentType
        } else if (mediaData.url) {
            redirect url: mediaData.url
        } else {
            // 返回资源不存在的状态码
            render status: 404
        }
    }

    def upload() {
        def result = uploadService.upload(request.getFile('file'))
        respond result
    }

    private String getCurrentUrl() {
        "${request.scheme}://${request.serverName}:${request.serverPort}${request.contextPath}"
    }
}

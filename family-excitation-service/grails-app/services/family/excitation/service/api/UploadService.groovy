package family.excitation.service.api

import family.excitation.service.MediaData
import family.excitation.service.MediaDataService
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import grails.util.Environment
import grails.web.mapping.LinkGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile

import javax.servlet.ServletContext

@Transactional
class UploadService {
    @Value('${familyExcitation.upload.path}')
    String uploadPath

    MediaDataService mediaDataService
    LinkGenerator grailsLinkGenerator

    def upload(MultipartFile file) {
        if (file?.empty) {
            return new ApiResult(code: 400, msg: '文件为空')
        }

        def uploadDir = new File(uploadPath)
        if (!uploadDir.exists()) {
            def result = uploadDir.mkdirs()
            if (!result) {
                return new ApiResult(code: 500, msg: '创建上传目录失败')
            }
        }
        def fileName = file.originalFilename
        def suffix = fileName.substring(fileName.lastIndexOf('.'))
        def filePath = "${uploadPath}/${fileName.md5()}${suffix}"
        try {
            file.transferTo(new File(filePath))
        } catch (Exception e) {
            e.printStackTrace()
            return new ApiResult(code: 500, msg: '上传文件失败', data: e.getLocalizedMessage())
        }
        def mediaData = new MediaData(name: fileName, localPath: filePath)
        def result = mediaDataService.save(mediaData)
        if (result) {
            result.url = grailsLinkGenerator.link(controller: 'mediaDataApi', action: 'show', params: [id: result.id], absolute: true)
            result = mediaDataService.save(mediaData)
        }
        return new ApiResult(code: 200, msg: '上传成功', data: result)
    }
}

package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.MediaData
import family.excitation.service.MediaType
import family.excitation.service.ReleaseInformation
import family.excitation.service.ReleaseInformationService
import family.excitation.service.User
import grails.util.Environment

class ReleaseInformationApiController {
    ReleaseInformationService releaseInformationService
    def generateAll() {
        User user
        def rootPath = ''
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                rootPath = 'D:\\Downloads\\media_data';
                user = User.findByUserName('admin')
                break
            case Environment.PRODUCTION:
                rootPath = '/www/wwwroot/47.120.23.110/media_data'
                user = session.login?.user
                break
        }
        def rootFile = new File(rootPath)
        rootFile.eachFile { File file ->
            if (file.isDirectory()) {
                file.eachFile {
                    if (it.name.endsWith('.mp4')) {
                        def releaseInformation = ReleaseInformation.findByTitleAndCategory(it.name, file.name)
                        if (releaseInformation) {
                            releaseInformationService.delete(releaseInformation.id)
                        }
                        releaseInformationService.save(new ReleaseInformation(title: it.name, category: file.name, user: user , mediaDataList: [
                                new MediaData(url: "http://47.120.23.110/media_data/${file.name}/${it.name}", type: MediaType.VIDEO)
                        ]))
                    }
                }
            }
        }
        respond new ApiResult(code: 200, msg: '生成成功')
    }

    def list(String category, String title, Integer page, Integer size) {
        if (!page) {
            page = 0
        }
        if (!size) {
            size = 20
        }
        def result = ReleaseInformation.createCriteria().list {
            if (category) {
                eq('category', category)
            }
            if (title) {
                like('title', "%${title}%")
            }
            order('dateCreated', 'desc')
            maxResults(size)
            firstResult(page * size)
        }

        def total = ReleaseInformation.createCriteria().get {
            if (category) {
                eq('category', category)
            }
            if (title) {
                like('title', "%${title}%")
            }
            projections {
                rowCount()
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [list: result, total: total])
    }

    def categories() {
        def result = ReleaseInformation.createCriteria().listDistinct {
            projections {
                property('category')
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: result)
    }
}

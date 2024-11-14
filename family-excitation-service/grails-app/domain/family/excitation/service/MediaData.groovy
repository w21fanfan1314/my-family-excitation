package family.excitation.service

import family.excitation.service.train.Question

class MediaData {
    String name
    String url
    String localPath
    MediaType type = MediaType.IMAGE
    String contentType
    Date dateCreated
    Date lastUpdated

    static belongsTo = [releaseInformation:ReleaseInformation, user:User, question :Question]
    static constraints = {
        name maxSize: 255, nullable: true
        releaseInformation nullable: true
        localPath nullable: true, maxSize: 255
        url nullable: true, maxSize: 255
        user nullable: true
        contentType nullable: true
        question nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
        url sqlType: 'text'
    }
    def beforeInsert() {
        if (!localPath && !url) {
            return false
        }

        if (!name) {
            def lastName = url.split('/').last()
            name = lastName.length() > 80 ? lastName.substring(0, 80) : lastName
        }

        if (!contentType) {
            updateContentType()
        }
        if (!type) {
            updateType()
        }
    }

    def beforeUpdate() {
        if (!contentType) {
            updateContentType()
        }
        if (!type) {
            updateType()
        }
    }

    def afterDelete() {
        if (localPath) {
            new File(localPath).delete()
        }
    }

    private void updateContentType() {
        if (localPath) {
            def contentType = URLConnection.guessContentTypeFromName(localPath)
            if (contentType) {
                this.contentType = contentType
            }
        }
    }

    private void updateType() {
        if (localPath) {
            def contentType = URLConnection.guessContentTypeFromName(localPath)
            if (contentType?.startsWith('image')) {
                this.type = MediaType.IMAGE
            } else if (contentType?.startsWith('video')) {
                this.type = MediaType.VIDEO
            }
        }
    }

    @Override
    String toString() {
        return name
    }
}

enum MediaType {
    // 图片
    IMAGE,
    // 视频
    VIDEO,
    // 封面
    COVER

    @Override
    String toString() {
        switch (this) {
            case IMAGE:
                return '图片'
            case VIDEO:
                return '视频'
            case COVER:
                return '封面'
            default:
                return '未知'
        }
    }
}


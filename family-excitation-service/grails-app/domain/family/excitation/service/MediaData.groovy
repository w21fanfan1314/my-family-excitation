package family.excitation.service

class MediaData {
    String name
    String url
    String localPath
    MediaType type = MediaType.IMAGE
    String contentType
    Date dateCreated
    Date lastUpdated

    static belongsTo = [releaseInformation:ReleaseInformation, user:User]
    static constraints = {
        name maxSize: 100, nullable: true
        releaseInformation nullable: true
        localPath nullable: true
        url nullable: true
        user nullable: true
        contentType nullable: true
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
    VIDEO

    @Override
    String toString() {
        switch (this) {
            case IMAGE:
                return '图片'
            case VIDEO:
                return '视频'
            default:
                return '未知'
        }
    }
}


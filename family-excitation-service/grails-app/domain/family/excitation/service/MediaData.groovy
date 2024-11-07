package family.excitation.service

class MediaData {
    String name
    String url
    MediaType type = MediaType.IMAGE
    Date dateCreated
    Date lastUpdated

    static belongsTo = [releaseInformation:ReleaseInformation]
    static constraints = {
        name maxSize: 100, nullable: true
        releaseInformation nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
        url sqlType: 'text'
    }
    def beforeInsert() {
        if (!name) {
            if (releaseInformation) {
                name = releaseInformation.title
            } else {
                def lastName = url.split('/').last()
                name = lastName.length() > 80 ? lastName.substring(0, 80) : lastName
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

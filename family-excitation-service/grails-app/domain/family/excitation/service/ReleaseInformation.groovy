package family.excitation.service

class ReleaseInformation {
    static transients = ['getThumbnail']
    String title
    String content
    String category
    Date dateCreated
    Date lastUpdated

    static belongsTo = [user: User]
    static hasMany = [mediaDataList: MediaData]
    static constraints = {
        title maxSize: 200
        category nullable: true
        content nullable: true, blank: true, maxSize: 500, widget: 'textarea'
        mediaDataList nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    def getThumbnail() {
        mediaDataList?.find { it.type == MediaType.COVER }
    }

    String toString() {
        return "(${category})${title}"
    }
}
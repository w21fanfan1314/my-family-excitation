package family.excitation.service

class VideoItem {
    String name
    String url
    String thumbnail
    String category
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name maxSize: 500
        url widget: 'textarea'
        thumbnail nullable: true, blank: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
        url type: 'text'
        thumbnail type: 'text'
    }
    String toString() {
        return name
    }
}

package family.excitation.service

class VideoItem {
    String name
    String url
    String category
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name maxSize: 500
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
        url type: 'text'
    }
    String toString() {
        return name
    }
}

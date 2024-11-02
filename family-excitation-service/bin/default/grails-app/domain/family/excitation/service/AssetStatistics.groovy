package family.excitation.service

class AssetStatistics {
    Double total = 0
    Date dateCreated
    Date lastUpdated
    
    static belongsTo = [user:User, currency: Currency]
    static constraints = {
        total min: 0D
    }

    @Override
    String toString() {
        return "${user} ${currency}: ${total}"
    }
}

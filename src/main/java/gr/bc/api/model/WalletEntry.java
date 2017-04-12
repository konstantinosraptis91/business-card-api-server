package gr.bc.api.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "id",
            "businessCardId",
            "userId",
            "createdAt",
            "lastUpdated"
        })
public class WalletEntry extends DBEntity {
    
    private final long businessCardId;
    private final long userId;
    private final String stamp;
    
    public WalletEntry(long userId, long businessCardId) {
        super();
        this.userId = userId;
        this.businessCardId = businessCardId;
        this.stamp = userId + "_" + businessCardId;
    }
       
    public long getBusinessCardId() {
        return businessCardId;
    }

    public long getUserId() {
        return userId;
    }

    public String getStamp() {
        return stamp;
    }
    
    @Override
    public String toString() {
        return "WalletEntry{"
                + "id=" + id
                + ", businessCardId=" + businessCardId 
                + ", userId=" + userId
                + ", stamp=" + stamp
                + ", lastUpdated=" + lastUpdated
                + ", createdAt=" + createdAt
                + '}';
    }
    
}
